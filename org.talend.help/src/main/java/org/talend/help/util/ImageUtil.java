// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.help.util;

import java.net.URL;
import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.talend.help.Activator;

/**
 * 
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: ImageUtil.java,v 1.3 2006/10/26 21:44:30 qiang.zhang Exp $
 * 
 */
public class ImageUtil {

	private static HashMap pimages = new HashMap();

	/**
	 * Dispose of an image in cache. Once there are no more open handles to the
	 * image it will be disposed of.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void disposeImage(String imageName) {

		try {

			Image image = (Image) pimages.get(imageName);

			if (image == null) {
				return;
			}

			image.dispose();
			pimages.remove(imageName);

		} catch (Throwable e) {
			Activator.log("Error disposing images", e);
		}
	}

	/**
	 * Create an image descriptor for the given image property in the
	 * text.properties file.
	 * 
	 * @param imageName
	 * @return
	 */
	public static ImageDescriptor getDescriptor(String imageName) {

		try {

			if (imageName == null) {
				return null;
			}

			if (imageName == null || imageName.trim().length() == 0) {
				Activator.log("Missing image path for " + imageName, null);
				return null;
			}

			// create image
			URL url = URLUtil.getResourceURL(imageName);
			return ImageDescriptor.createFromURL(url);

		} catch (Exception e) {
			Activator.log("Couldn't create image for " + imageName, e);
			return null;
		}

	}

	/**
	 * Get an image object from cache or create one if it doesn't exist yet.
	 * Everytime an object is retrieved, it should be disposed of using the
	 * ImageUtil.disposeImage method.
	 * 
	 * @param imageName
	 */
	@SuppressWarnings("unchecked")
	public static Image getImage(String imageName) {

		Image image = (Image) pimages.get(imageName);

		if (image == null) {
			image = getDescriptor(imageName).createImage();

			if (image == null) {
				return null;
			}

			pimages.put(imageName, image);
		}

		return image;
	}
}
