// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.update;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.update.EUpdateItemType;
import org.talend.core.model.update.EUpdateResult;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.model.update.UpdatesConstants;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * ggu class global comment. Detailled comment
 */
public class UpdateLabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == 0) {
            if (element instanceof Job) {
                Job job = (Job) element;
                if (job.isJoblet()) {
                    return ImageProvider.getImage(ECoreImage.JOBLET_ICON);
                } else {
                    return ImageProvider.getImage(ECoreImage.PROCESS_ICON);
                }
            } else if (element instanceof Category) {
                Category category = (Category) element;
                EUpdateItemType type = category.getType();
                if (type != null) {
                    switch (type) {
                    case JOB_PROPERTY_EXTRA:
                    case JOB_PROPERTY_STATS_LOGS:
                        return ImageProvider.getImage(ECoreImage.CONTEXT_ICON);
                    case CONTEXT:
                    case JOBLET_CONTEXT:
                        return ImageProvider.getImage(ECoreImage.CONTEXT_ICON);
                    case NODE_PROPERTY:
                    case NODE_SCHEMA:
                    case NODE_QUERY:
                    case JOBLET_SCHEMA:
                    case JOBLET_RENAMED:
                    case RELOAD:
                        return getImageFromNode(category.getNode());
                    default:
                        // return ImageProvider.getImage(ECoreImage.TALEND_PICTO);
                    }
                }
            } else if (element instanceof Item) {
                Item item = (Item) element;
                switch (item.getResultObject().getUpdateType()) {
                case NODE_QUERY:
                    return ImageProvider.getImage(ECoreImage.METADATA_QUERY_ICON);
                case NODE_SCHEMA:
                case JOBLET_SCHEMA:
                    return ImageProvider.getImage(ECoreImage.METADATA_TABLE_ICON);
                case NODE_PROPERTY:
                case JOB_PROPERTY_EXTRA:
                case JOB_PROPERTY_STATS_LOGS:
                    ERepositoryObjectType type = RepositoryUpdateManager.getTypeFromSource(item.getRemark());
                    if (type != null) {
                        return CoreImageProvider.getImage(type);
                    }
                case JOBLET_RENAMED:
                case RELOAD:
                    // return ImageProvider.getImage(ECoreImage.TALEND_PICTO);
                case CONTEXT:
                case JOBLET_CONTEXT:
                    // return ImageProvider.getImage(ECoreImage.CONTEXT_ICON);
                default:
                }
            }

        }
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        switch (columnIndex) {
        case 0:
            if (element instanceof Job) {
                String name = ((Job) element).getName();
                return name == null ? UpdatesConstants.EMPTY : name;

            } else if (element instanceof Category) {
                return ((Category) element).getName();
            } else if (element instanceof Item) {
                return ((Item) element).getProperty();
            }
            break;
        case 1:
            if (element instanceof Job || element instanceof Category) {
                return UpdatesConstants.SEGMENT_LINE;
            } else if (element instanceof Item) {
                Item item = (Item) element;
                if (isBuilInMode(item)) {
                    return EUpdateResult.BUIL_IN.getDisplayName();
                }
                return item.getOperation().getDisplayName();
            }
            break;
        case 2:
            if (element instanceof Job || element instanceof Category) {
                return UpdatesConstants.SEGMENT_LINE;
            } else if (element instanceof Item) {
                Item item = (Item) element;
                if (isBuilInMode(item)) {
                    return UpdatesConstants.EMPTY;
                }
                return item.getRemark();
            }
            break;
        default:

        }

        return UpdatesConstants.EMPTY;
    }

    public void addListener(ILabelProviderListener listener) {

    }

    public void dispose() {

    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {

    }

    private boolean isBuilInMode(Item item) {
        if (item != null && !item.isChecked() && item.getOperation() == EUpdateResult.UPDATE) {
            return true;
        }
        return false;
    }

    private Image getImageFromNode(Node node) {
        if (node == null) {
            return ImageProvider.getImage(ECoreImage.TALEND_PICTO);
        }
        return node.getComponent().getIcon16().createImage();
    }
}
