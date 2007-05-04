// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;
import java.io.File;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryConstants;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ImageController.java 1 2006-12-12 下午01:49:18 +0000 (下午01:49:18) yzhang $
 * 
 */
public class ImageController extends AbstractElementPropertySectionController {

    /**
     * DOC yzhang ImageController constructor comment.
     * 
     * @param parameterBean
     */
    public ImageController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createCommand()
     */
    @Override
    public Command createCommand() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createControl()
     */
    @Override
    public Control createControl(final Composite subComposite, final IElementParameter param, final int numInRow,
            final int nbInRow, final int top, final Control lastControl) {

        String fileName = (String) param.getValue();
        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        String filePath = service.getPathFileName(RepositoryConstants.IMG_DIRECTORY, fileName).toPortableString();

        if (filePath != null) {
            File fileOrFolder = new java.io.File(filePath);
            if (!fileOrFolder.isFile() || !fileOrFolder.canRead()) {
                return lastControl;
            }
            final Composite compositeImage = new Composite(subComposite, SWT.BORDER);
            final Image image = new Image(subComposite.getDisplay(), filePath);
            compositeImage.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    image.dispose();
                    compositeImage.removeDisposeListener(this);
                }

            });

            CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, param.getDisplayName() + " :"); //$NON-NLS-1$
            FormData formDataLabel = new FormData();
            labelLabel.setVisible(true);
            if (numInRow != 1) {
                labelLabel.setAlignment(SWT.RIGHT);
            }
            if (lastControl != null) {
                formDataLabel.left = new FormAttachment(lastControl, 0);
            } else {
                formDataLabel.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
                formDataLabel.top = new FormAttachment(0, top);
            }
            labelLabel.setLayoutData(formDataLabel);

            compositeImage.setToolTipText(param.getDisplayName());
            Point size = new Point(image.getImageData().width, image.getImageData().height);
            FormData formData = new FormData(size.x, size.y);
            formData.top = new FormAttachment(0, top);
            formData.left = new FormAttachment(labelLabel);
            compositeImage.setBackgroundImage(image);
            compositeImage.setLayoutData(formData);
            return compositeImage;
        } else {
            return lastControl;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub

    }


    @Override
    public void refresh(IElementParameter param, boolean check) {
    }

}
