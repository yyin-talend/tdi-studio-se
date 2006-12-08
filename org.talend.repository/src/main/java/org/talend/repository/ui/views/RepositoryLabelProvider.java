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
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
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

    private static final Color ACTIVE_REPOSITORY_ENTRY = new Color(null, 100, 100, 100);

    private static final Color INACTIVE_REPOSITORY_ENTRY = new Color(null, 200, 200, 200);

    private IRepositoryView view;

    public RepositoryLabelProvider(IRepositoryView view) {
        super();
        this.view = view;
    }

    public String getText(Object obj) {
        RepositoryNode node = (RepositoryNode) obj;

        if (node.getType() == ENodeType.REPOSITORY_ELEMENT || node.getType() == ENodeType.SIMPLE_FOLDER) {
            IRepositoryObject object = node.getObject();

            StringBuffer string = new StringBuffer();
            string.append(object.getLabel());
            // PTODO SML [FOLDERS++] temp code
            if (object.getType() != ERepositoryObjectType.FOLDER) {
                string.append(" " + object.getVersion());
            }

            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (factory.getStatus(object) == ERepositoryStatus.DELETED) {
                    string.append(" (" + factory.getOldPath(object) + ")");
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }

            // PTODO SML [FOLDERS++] temp code
            if (object.getType() != ERepositoryObjectType.FOLDER) {
                string.append(" [" + factory.getStatus(object.getProperty().getItem()) + "]");
            }

            return string.toString();
        } else {
            return node.getLabel();
        }
    }

    public Image getImage(Object obj) {
        RepositoryNode node = (RepositoryNode) obj;

        switch (node.getType()) {
        case STABLE_SYSTEM_FOLDER:
        case SYSTEM_FOLDER:
            return ImageProvider.getImage(node.getIcon());
        case SIMPLE_FOLDER:
            // FIXME SML Move in repository node
            EImage image = (view.getExpandedState(obj) ? EImage.FOLDER_OPEN_ICON : EImage.FOLDER_CLOSE_ICON);
            return ImageProvider.getImage(image);
        default:
            if (node.getObject().getType() == ERepositoryObjectType.DOCUMENTATION) {
                DocumentationItem item = (DocumentationItem) node.getObject().getProperty().getItem();
                return ImageProvider.getImage(item.getExtension());
            }
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            ERepositoryStatus repositoryStatus = factory.getStatus(node.getObject());
            return BusinessImageProvider.getImage(node.getObject().getType(), repositoryStatus);
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
                return INACTIVE_REPOSITORY_ENTRY;
            }
        case SYSTEM_FOLDER:
            return ACTIVE_REPOSITORY_ENTRY;
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
