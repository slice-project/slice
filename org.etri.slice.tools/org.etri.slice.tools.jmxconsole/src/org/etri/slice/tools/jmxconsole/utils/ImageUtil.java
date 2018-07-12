package org.etri.slice.tools.jmxconsole.utils;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;
import org.etri.slice.tools.jmxconsole.Activator;
import org.osgi.framework.Bundle;

/**
 *  이미지 관련 유틸
 *
 */ 
public class ImageUtil {

	public static ImageDescriptor getImageDescriptor(String imgName) {
        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
        URL url = FileLocator.find(bundle, new Path(imgName), null);
        ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
        
        return imageDescriptor; 
	}
	
	public static Image getImage(String imgName) {
		 ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());
		 
		 Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID); 
		 
         URL url = FileLocator.find(bundle, new Path(imgName), null);
         ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
         
		 return resourceManager.createImage(imageDescriptor);
	}
}
