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

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.EImage;
import org.talend.core.ui.ImageProvider;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * Label provider for the repository view. <code>DEBUG</code> boolean field specify if details (such as objects ids)
 * must appears on display or not.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryLabelProvider extends LabelProvider implements IColorProvider, IFontProvider {

    private static final Color INACTIVE_REPOSITORY_MEMBER = new Color(null, 100, 100, 100);

    private static final Color SYSTEM_FOLDER = new Color(null, 200, 200, 200);

    private IRepositoryView view;

    private static final boolean DEBUG = false;

    public RepositoryLabelProvider(IRepositoryView view) {
        super();
        this.view = view;
    }

    public String getText(Object obj) {
        RepositoryNode node = (RepositoryNode) obj;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            IRepositoryObject object = node.getObject();

            StringBuffer string = new StringBuffer();
            if (DEBUG) {
                string.append(object.getLabel() + " [id:" + object.getId() + "-v" + object.getVersion() + "]");
            } else {
                string.append(object.getLabel() + " " + object.getVersion());
            }

            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (factory.getStatus(object) == ERepositoryStatus.DELETED) {
                    string.append(" (" + factory.getOldPath(object) + ")");
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }

            string.append(" [" + factory.getStatus(object.getProperty().getItem()) + "]");

            return string.toString();
        } else if (node.getType() == ENodeType.SIMPLE_FOLDER) {
            IRepositoryObject object = node.getObject();
            StringBuffer string = new StringBuffer(node.getProperties(EProperties.LABEL).toString());

            if (DEBUG) {
                string.append(" [id:" + object.getId() + "]");
            }

            return string.toString();
        } else {
            return node.getProperties(EProperties.LABEL).toString();
        }
    }

    public Image getImage(Object obj) {
        RepositoryNode node = (RepositoryNode) obj;
        switch (node.getType()) {
        case STABLE_SYSTEM_FOLDER:
        case SYSTEM_FOLDER:
            ERepositoryObjectType contentType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (contentType.equals(ERepositoryObjectType.RECYCLE_BIN)) {
                if (node.hasChildren()) {
                    return ImageProvider.getImage(EImage.RECYCLE_BIN_FULL_ICON);
                } else {
                    return ImageProvider.getImage(EImage.RECYCLE_BIN_EMPTY_ICON);
                }
            } else {
                return ImageProvider.getImage(contentType);
            }
        case SIMPLE_FOLDER:
            EImage image = (view.getExpandedState(obj) ? EImage.FOLDER_OPEN_ICON : EImage.FOLDER_CLOSE_ICON);
            return ImageProvider.getImageDesc(image).createImage();
        default:
            if (node.getObject().getType() == ERepositoryObjectType.DOCUMENTATION) {
                DocumentationItem item = (DocumentationItem) node.getObject().getProperty().getItem();
                return ImageProvider.getImage(item.getExtension());
            }
            return ImageProvider.getImage(node.getObject().getType());
        }
    }

    public Color getBackground(Object element) {
        return null;
    }

    public Color getForeground(Object element) {
        RepositoryNode node = (RepositoryNode) element;
        switch (node.getType()) {
        case STABLE_SYSTEM_FOLDER:
            if (((ERepositoryObjectType) node.getProperties(EProperties.LABEL)) == ERepositoryObjectType.SNIPPETS) {
                return SYSTEM_FOLDER;
            }
        case SYSTEM_FOLDER:
            return INACTIVE_REPOSITORY_MEMBER;
        default:
            return null;
        }
    }

    public Font getFont(Object element) {
        RepositoryNode node = (RepositoryNode) element;
        switch (node.getType()) {
        case STABLE_SYSTEM_FOLDER:
            if (((ERepositoryObjectType) node.getProperties(EProperties.LABEL)) == ERepositoryObjectType.SNIPPETS) {
                return JFaceResources.getFontRegistry().defaultFont();
            }
        case SYSTEM_FOLDER:
            return JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
        default:
            return JFaceResources.getFontRegistry().defaultFont();
        }
    }
}
