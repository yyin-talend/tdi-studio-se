// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model.actions;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.ICDCProviderService;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.dialog.PastSelectorDialog;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CopyObjectAction {

    IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private static CopyObjectAction singleton = new CopyObjectAction();

    public static CopyObjectAction getInstance() {
        return singleton;
    }

    public boolean validateAction(RepositoryNode sourceNode, RepositoryNode targetNode) {
        if (sourceNode == null) {
            return false;
        }

        // Cannot copy folder or system folder :
        if (sourceNode.getType() != ENodeType.REPOSITORY_ELEMENT) {
            return false;
        }

        IRepositoryObject objectToCopy = sourceNode.getObject();

        // Cannot move logically deleted objects :
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        // try {
        if (objectToCopy != null && objectToCopy.getId() == null) {
            return false;
        }
        // Cannot copy for refProject
        // if (objectToCopy != null && factory.getStatus(objectToCopy) == ERepositoryStatus.READ_ONLY) {
        // return false;
        // }
        // objectToCopy = factory.getLastVersion(objectToCopy.getId());
        if (objectToCopy == null || factory.getStatus(objectToCopy) == ERepositoryStatus.DELETED) {
            return false;
        }

        // } catch (PersistenceException e) {
        // ExceptionHandler.process(e);
        // return false;
        // }

        // Cannot copy system routines:
        if (objectToCopy.getType() == ERepositoryObjectType.ROUTINES) {
            Property property = objectToCopy.getProperty();
            RoutineItem item = (RoutineItem) property.getItem();
            return !item.isBuiltIn();
        }
        // Cannot copy system sql pattern:
        if (objectToCopy.getType() == ERepositoryObjectType.SQLPATTERNS) {
            Property property = objectToCopy.getProperty();
            SQLPatternItem item = (SQLPatternItem) property.getItem();
            return !item.isSystem();
        }
        // for cdc
        if (PluginChecker.isCDCPluginLoaded()) {
            ICDCProviderService cdcService = (ICDCProviderService) GlobalServiceRegister.getDefault().getService(
                    ICDCProviderService.class);
            if (cdcService != null
                    && (cdcService.isSubscriberTableNode(sourceNode) || cdcService.isSystemSubscriberTable(sourceNode))) {
                return false;
            }
        }
        // Special rule : temp ?
        if (targetNode == null) {
            return true;
        }

        // for bug 0005454: Copy paste with keyboard in the repository view doesn't work.
        if (targetNode.getType() == ENodeType.REPOSITORY_ELEMENT || targetNode.getType() == ENodeType.SIMPLE_FOLDER
                || targetNode.getType() == ENodeType.SYSTEM_FOLDER) {
            return ((ERepositoryObjectType) targetNode.getProperties(EProperties.CONTENT_TYPE)) == objectToCopy.getType();
        }
        return false;
    }

    public void execute(RepositoryNode sourceNode, RepositoryNode targetNode) throws Exception {
        if (!validateAction(sourceNode, targetNode)) {
            return;
        }

        // for bug 0005454: Copy paste with keyboard in the repository view doesn't work.
        if (targetNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
            targetNode = targetNode.getParent();
        }

        IPath path = RepositoryNodeUtilities.getPath(targetNode);

        if (sourceNode.getType().equals(ENodeType.REPOSITORY_ELEMENT)) {
            // Source is an repository element :
            Item originalItem = sourceNode.getObject().getProperty().getItem();
            List<IRepositoryObject> allVersion = factory.getAllVersion(originalItem.getProperty().getId());
            // qli modified to fix the bug 5400 and 6185.
            Item newItem = factory.copy(originalItem, path);
            if (newItem instanceof RoutineItem) {
                ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                        ICodeGeneratorService.class);
                if (codeGenService != null) {
                    codeGenService.createRoutineSynchronizer().renameRoutineClass((RoutineItem) newItem);
                    codeGenService.createRoutineSynchronizer().syncRoutine((RoutineItem) newItem, true);
                }
            }
            factory.save(newItem);

            // for oldversions
            copyOldVersions(allVersion, sourceNode, newItem, path);

        }
    }

    private void copyOldVersions(List<IRepositoryObject> allVersion, RepositoryNode sourceNode, Item newLastVersionItem,
            IPath path) throws Exception {
        if (allVersion != null && allVersion.size() > 1) {
            PastSelectorDialog dialog = new PastSelectorDialog(Display.getCurrent().getActiveShell(), allVersion, sourceNode);
            if (dialog.open() == Window.OK) {
                for (IRepositoryObject object : dialog.getSelectedVersionItems()) {
                    Item copy = factory.copy(object.getProperty().getItem(), path);
                    copy.getProperty().setId(newLastVersionItem.getProperty().getId());
                    copy.getProperty().setLabel(newLastVersionItem.getProperty().getLabel());
                    factory.save(copy);

                }
            }
        }
    }
}
