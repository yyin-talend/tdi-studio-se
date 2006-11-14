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
 * This class is responsible for saving current text into a file.
 * <br/>
 *
 * @author ftang
 *
 */
public class SaveFileAsAction extends AbstractEditorAction {

    private ImageDescriptor image = ImageUtil.getDescriptor("Images.SaveFileAsIcon");


    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        return image;
    }


    /* (non-Javadoc)
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#getText()
     */
    public String getText() {
        return Messages.getString("SQLEditor.Actions.SaveAs");
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#isEnabled()
     */
    public boolean isEnabled() {
        return true;
    }


    /* (non-Javadoc)
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#getToolTipText()
     */
    public String getToolTipText() {
        return Messages.getString("SQLEditor.Actions.SaveAsToolTip"); //$NON-NLS-1$
    }


    /* (non-Javadoc)
     * @see org.talend.sqlbuilder.actions.AbstractEditorAction#run()
     */
    public void run() {
        editor.doSaveAs();
    };
}