package org.jcryptool.bouncycastle.core.operation.data;

import java.nio.charset.Charset;
import java.util.Observer;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.jcryptool.bouncycastle.core.Activator;
import org.jcryptool.bouncycastle.core.operation.Output;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.editors.AbstractEditorService;

public class UTF8EditorOutput implements Output {
	
	public static final String HEXEDITOR = IOperationsConstants.ID_HEX_EDITOR;
	public static final String TEXTEDITOR = IOperationsConstants.ID_TEXT_EDITOR;
	
	private byte[] payload;
	private boolean hex;
	
	public UTF8EditorOutput(byte[] payload, boolean hex) {
		this.payload = payload;
		this.hex = hex;
	}
	
	public static IEditorInput makeEditorInputFor(byte[] data) {
		return AbstractEditorService.createOutputFile(data);
	}
	
	public static void performOpenEditor(final IEditorInput input,
			final String editorId, final Observer o) {
//		if (o == null) {
//			throw new NullPointerException(
//					"provided observer for performOpenEditor method was null.");
//		}
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				
				IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				try {
					IEditorPart editorRef = activeWorkbenchWindow
							.getActivePage().openEditor(input, editorId);
//					o.update(null, editorRef);
				} catch (PartInitException ex) {
					try {
						IEditorPart editorRef = activeWorkbenchWindow
								.getActivePage().openEditor(input,
										IOperationsConstants.ID_HEX_EDITOR);
//						o.update(null, editorRef);
					} catch (PartInitException e) {
//						o.update(null, null);
						LogUtil.logError(Activator.PLUGIN_ID, e);

						MessageDialog.openError(
								activeWorkbenchWindow.getShell(), "Error",
								"Error opening the editor");
					} catch (Exception e) {
//						o.update(null, null);
						LogUtil.logError(Activator.PLUGIN_ID, e);

						MessageDialog.openError(
								activeWorkbenchWindow.getShell(), "Error",
								"Error opening the editor");
					}
				}
			}
		});
	}
	
	@Override
	public byte[] readComplete() {
		IEditorInput editorInput = makeEditorInputFor(payload);
		performOpenEditor(editorInput, hex?HEXEDITOR:TEXTEDITOR, null);
		return payload;
	}

	public String getPayloadString() {
		return new String(payload, Charset.forName("UTF-8"));
	}
	

}
