// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.views;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.part.PluginDropAdapter;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.business.BusinessType;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.actions.CopyObjectAction;
import org.talend.repository.model.actions.MoveObjectAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryDropAdapter extends PluginDropAdapter {

    public RepositoryDropAdapter(StructuredViewer viewer) {
        super(viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.PluginDropAdapter#performDrop(java.lang.Object)
     */
    @Override
    public boolean performDrop(Object data) {
        int operation = getCurrentOperation();
        RepositoryNode targetNode = (RepositoryNode) getCurrentTarget();
        boolean toReturn = true;

        for (Object obj : ((StructuredSelection) data).toArray()) {
            RepositoryNode sourceNode = (RepositoryNode) obj;
            try {
                switch (operation) {
                case DND.DROP_COPY:
                    CopyObjectAction.getInstance().execute(sourceNode, targetNode);
                    break;
                case DND.DROP_MOVE:
                    MoveObjectAction.getInstance().execute(sourceNode, targetNode);
                    break;
                default:
                    // Nothing to do
                }
            } catch (BusinessException e) {
                MessageBoxExceptionHandler.process(e);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.PluginDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
     */
    @Override
    public boolean validateDrop(Object target, int operation, TransferData transferType) {
        super.validateDrop(target, operation, transferType);

        boolean isValid = true;
        for (Object obj : ((StructuredSelection) getViewer().getSelection()).toArray()) {
            RepositoryNode sourceNode = (RepositoryNode) obj;

            if (sourceNode != null) {
                IRepositoryObject object = sourceNode.getObject();
                if (object == null) {
                    return false;
                }
                if (object.getType() == ERepositoryObjectType.JOB_DOC || object.getType() == ERepositoryObjectType.JOBLET_DOC) {
                    if (BusinessType.SHAP == CorePlugin.getDefault().getDiagramModelService().getBusinessModelType(target)) {
                        return true;
                    }
                    return false;
                } else if (object.getType() == ERepositoryObjectType.ROUTINES) {
                    Property property = object.getProperty();
                    RoutineItem item = (RoutineItem) property.getItem();
                    if (item.isBuiltIn() && target instanceof RepositoryNode) {
                        return false;
                    }
                } else if (object.getType() == ERepositoryObjectType.SQLPATTERNS) {
                    Property property = object.getProperty();
                    SQLPatternItem item = (SQLPatternItem) property.getItem();
                    if (item.isSystem() && target instanceof RepositoryNode) {
                        return false;
                    }
                }

            }

            switch (operation) {
            case DND.DROP_COPY:
                isValid = CopyObjectAction.getInstance().validateAction(sourceNode, (RepositoryNode) target);
                break;
            case DND.DROP_NONE:
            case DND.DROP_MOVE:
                isValid = MoveObjectAction.getInstance().validateAction(sourceNode, (RepositoryNode) target);
                break;
            case DND.DROP_DEFAULT:
            default:
                isValid = false;
            }

            if (!isValid) {
                return isValid;
            }
        }

        return isValid;
    }
}
