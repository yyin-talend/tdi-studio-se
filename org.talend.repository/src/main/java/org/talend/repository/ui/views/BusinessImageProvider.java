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
package org.talend.repository.ui.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.ui.images.OverlayImage;
import org.talend.core.ui.images.OverlayImage.EPosition;
import org.talend.repository.model.ERepositoryStatus;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class BusinessImageProvider {

    public static Image getImage(ERepositoryObjectType type, ERepositoryStatus status) {
        ImageDescriptor img;
        EPosition position;
        switch (status) {
        case NEW:
            img = ImageProvider.getImageDesc(ECoreImage.NEW_OVERLAY);
            position = EPosition.TOP_RIGHT;
            break;
        case LOCK_BY_USER:
            img = ImageProvider.getImageDesc(ECoreImage.LOCKED_USER_OVERLAY);
            position = EPosition.BOTTOM_LEFT;
            break;
        default:
            return CoreImageProvider.getImage(type);
        }
        OverlayImage overlayImage = new OverlayImage(CoreImageProvider.getImage(type), img, position);
        return overlayImage.createImage();
    }
}
