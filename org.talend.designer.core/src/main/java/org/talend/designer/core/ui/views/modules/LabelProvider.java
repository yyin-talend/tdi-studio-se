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
package org.talend.designer.core.ui.views.modules;

import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.swt.tableviewer.DefaultTableLabelProvider;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.core.CorePlugin;
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.designer.core.model.components.ComponentImportNeeds;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class LabelProvider extends DefaultTableLabelProvider {

    private static final String WARNING_IMAGE = "icons/warn_tsk.gif";

    private static final String UNKNOWN_IMAGE = "icons/unknown.gif";

    private Image warningImage = CorePlugin.getImageDescriptor(WARNING_IMAGE).createImage();;

    private Image errorImage = ImageProvider.getImage(EImage.ERROR_ICON);

    private Image unknownImage = CorePlugin.getImageDescriptor(UNKNOWN_IMAGE).createImage();

    private Image okImage = ImageProvider.getImage(EImage.CHECKED_ICON);

    public LabelProvider(TableViewerCreator tableViewerCreator) {
        super(tableViewerCreator);
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        ComponentImportNeeds componentImportNeeds = (ComponentImportNeeds) element;
        TableViewerCreatorColumn column = (TableViewerCreatorColumn) tableViewerCreator.getColumns().get(columnIndex);
        Image image = null;
        if (column.getId().equals(ModulesView.ID_STATUS)) {
            switch (componentImportNeeds.getStatus()) {
            case ComponentImportNeeds.INSTALLED:
                image = okImage;
                break;
            case ComponentImportNeeds.NOT_INSTALLED:
                if (componentImportNeeds.isRequired()) {
                    image = errorImage;
                } else {
                    image = warningImage;
                }
                break;
            default:
                image = unknownImage;
            }
        }
        return image;
    }
}
