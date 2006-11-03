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
package org.talend.sqlbuilder.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * 
 * This class is used for clearing the text in Text component.
 * <br/>
 *
 * @author Tang Fengneng (Soyatec)
 *
 */
public class ClearTextAction extends AbstractEditorAction {

    private ImageDescriptor img = ImageUtil.getDescriptor("Images.ClearTextIcon");


    /**
     * Sets text.
     */
    public String getText() {
        return Messages.getString("Clear_1");
    }


   //Sets the clear action starts.
    public void run() {
        editor.clearText();
    }

    //Checks if enabled.
    public boolean isEnabled() {
        return true;
    }

   /**
    * Gets  tool tip text.
    */
    public String getToolTipText() {
        return Messages.getString("Clear_2");
    }

   /**
    * Gets  hover image descriptor.
    */
    public ImageDescriptor getHoverImageDescriptor() {
        return img;
    }

    /**
     * Gets image descriptor.
     */
    public ImageDescriptor getImageDescriptor() {
        return img;
    };
}