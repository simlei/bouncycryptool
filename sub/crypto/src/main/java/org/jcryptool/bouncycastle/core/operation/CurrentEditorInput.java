package org.jcryptool.bouncycastle.core.operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.operations.editors.EditorsManager;


public class CurrentEditorInput implements Input {

	private byte[] readContent;
	
	private static byte[] bytesFromEHEPEditor(IEditorInput input) {
		//
		// Input file is outside the Eclipse Workspace
		//
		IPathEditorInput pathEditorInput = (IPathEditorInput) input;
		IPath path = pathEditorInput.getPath();
		File ioFile = path.toFile();

		try {
			FileInputStream in = new FileInputStream(ioFile);
			int c;

			List<Byte> bytes = new LinkedList<Byte>();
			while ((c = in.read()) != -1) {
				bytes.add((byte) c);
			}
			byte[] result = new byte[bytes.size()];
			for (int i = 0; i < bytes.size(); i++) {
				result[i] = bytes.get(i);
			}
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO: get rid of all this shit,
											// its ehep-editors problem
		}
	}
	
	public CurrentEditorInput() {
		
		try{
			InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
			
			if(stream == null) {
				throw new RuntimeException("No editor is opened");
			}
			
			int read = 0;
			List<Byte> content = new LinkedList<Byte>();
			
			try {
				while((read = stream.read()) > -1) {
					content.add((byte) read);
				}
			} catch (IOException e) {
				// should not happen because the editors do not usually corrupt their content stream
				e.printStackTrace();
			}
			
			readContent = new byte[content.size()];
			for (int i = 0; i < content.size(); i++) {
				Byte b = content.get(i);
				readContent[i] = b;
			}
		} catch(Exception e) {
			IEditorPart ed = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().getActiveEditor();
			IEditorInput input = ed.getEditorInput();
			if(input instanceof IPathEditorInput) {
				this.readContent = bytesFromEHEPEditor(input);
			}
		}
		
	}
	
	@Override
	public byte[] read() {
		return readContent;
	}

}
