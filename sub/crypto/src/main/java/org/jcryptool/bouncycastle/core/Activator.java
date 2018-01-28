package org.jcryptool.bouncycastle.core;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jcryptool.bouncycastle.core"; //$NON-NLS-1$
	// The shared instance
	private static Activator plugin;
	/**
	 * The constructor
	 */
	public Activator() {
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	public static final String IMG_STAR = "star";
	public static final String IMG_SAVE = "save";
	public static final String IMG_OPEN = "open";
	public static final String IMG_GEAR = "gear";
	public static final String IMG_STATUS_WARNING = "status_warning";
	public static final String IMG_STATUS_COMPLETE = "status_complete";
	public static final String IMG_SAMPLE = "sample";
	public static final String IMG_RUN = "run";
	public static final String IMG_KEY = "key";
	public static final String IMG_FOLDER = "folder";
	public static final String IMG_EDIT = "edit";
	public static final String IMG_BC = "bc";
	public static final String IMG_BC_DIGEST = "bc_digest";

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	protected void initializeImageRegistry(ImageRegistry registry) {
        Bundle bundle = Platform.getBundle(PLUGIN_ID);
        
		registerImg(registry, bundle, IMG_EDIT, "icons/edit.gif");
		registerImg(registry, bundle, IMG_FOLDER, "icons/folder.gif");
		registerImg(registry, bundle, IMG_KEY, "icons/key.gif");
		registerImg(registry, bundle, IMG_RUN, "icons/run.gif");
		registerImg(registry, bundle, IMG_SAMPLE, "icons/sample.gif");
		registerImg(registry, bundle, IMG_STATUS_COMPLETE, "icons/complete_status.gif");
		registerImg(registry, bundle, IMG_STATUS_WARNING, "icons/warning_status.gif");
		registerImg(registry, bundle, IMG_GEAR, "icons/gear.png");
		registerImg(registry, bundle, IMG_OPEN, "icons/open.png");
		registerImg(registry, bundle, IMG_SAVE, "icons/save.png");
		registerImg(registry, bundle, IMG_STAR, "icons/star.png");
		registerImg(registry, bundle, IMG_BC, "icons/bc.png");
		registerImg(registry, bundle, IMG_BC_DIGEST, "icons/bc_digest.png");
     }

	private void registerImg(ImageRegistry registry, Bundle bundle,
			String IMAGE_ID, String fullPath) {
		IPath path = new Path(fullPath);
        URL url = FileLocator.find(bundle, path, null);
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		registry.put(IMAGE_ID, desc);
	}
	
	public static Image getImage(String id) {
		AbstractUIPlugin plugin = Activator.getDefault();
		ImageRegistry imageRegistry = plugin.getImageRegistry();
		Image myImage = imageRegistry.get(id);
		return myImage;
	}
	public static ImageDescriptor getImageD(String id) {
		AbstractUIPlugin plugin = Activator.getDefault();
		ImageRegistry imageRegistry = plugin.getImageRegistry();
		ImageDescriptor myImage = imageRegistry.getDescriptor(id);
		return myImage;
	}
}
