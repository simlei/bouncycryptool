package org.jcryptool.bouncycastle.core.util;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class FileUtils {

	private static File lastSelection = null;
	
	public static File selectFile() {
		FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        fd.setText("Open");
        fd.setFilterPath(lastSelection==null?null:lastSelection.getAbsolutePath());
        String selected = fd.open();
        if(selected != null) {
        	File result = new File(selected);
        	File path = result.getParentFile();
        	lastSelection = path;
        	return result;
        }
        return null;
	}
	
	
}
