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
package org.talend.repository.ui.actions;

import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.model.actions.CopyObjectAction;
import org.talend.repository.ui.dialog.DuplicateDialog;

/**
 * DOC zwang class global comment. Detailled comment
 */
public class DuplicateAction extends AContextualAction {

    private static DuplicateAction singleton;

    private DuplicateDialog dlg = null;

    private String rename = null;;

    private IStructuredSelection selection = null;

    private Map<String, String> names = null;

    private boolean isError = false;

    public DuplicateAction() {
        super();
        this.setText(Messages.getString("DuplicateAction.thisText.duplicate")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.COPY_ICON));
        singleton = this;
    }

    public static DuplicateAction getInstance() {
        return singleton;
    }

    @Override
    public void run() {
        // int count = 0;
        boolean isOk = false;

        if (null != (IStructuredSelection) getSelection()) {
            selection = (IStructuredSelection) getSelection();
        }

        RepositoryNode target = ((RepositoryNode) selection.getFirstElement()).getParent();

        CopyObjectAction copyObjectAction = CopyObjectAction.getInstance();

        TreeSelection selectionInClipboard = (TreeSelection) selection;

        while (true) {
            isOk = openInputNameDialog();
            if (!isOk) {
                return;
            }
            String newName = rename;
            boolean valid = isValid(newName);
            if (!valid) {
                openErrorDialog();
                isError = true;
                continue;
            } else {
                break;
            }
        }
        createOperation(target, copyObjectAction, selectionInClipboard);
        refresh();
    }

    /**
     * DOC zwang Comment method "createOperation".
     */
    private void createOperation(RepositoryNode target, CopyObjectAction copyObjectAction, TreeSelection selectionInClipboard) {
        // TODO Auto-generated method stub
        if (names != null && names.size() == 1 && selectionInClipboard != null && selectionInClipboard.toArray().length == 1) {
            for (String name : names.keySet()) {
                for (Object currentSource : selectionInClipboard.toArray()) {
                    if (name.trim().equals(((RepositoryNode) currentSource).getObject().getProperty().getLabel().trim())) {
                        try {
                            copyObjectAction.execute((RepositoryNode) currentSource, null, names.get(name), target);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
    }

    private Item createNewItem() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        ProcessItem processItem = PropertiesFactory.eINSTANCE.createProcessItem();
        processItem.setProperty(property);
        return processItem;
    }

    // validate if the new name is existent.
    public boolean isValid(String itemName) {
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();
        try {
            return repositoryFactory.isNameAvailable(createNewItem(), itemName);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
    }

    /**
     * DOC zwang Comment method "openInputNameDialog".
     */
    private boolean openInputNameDialog() {
        // TODO Auto-generated method stub
        boolean isOk = false;
        if (isError) {
            dlg = new DuplicateDialog(Display.getCurrent().getActiveShell(), selection, Messages
                    .getString("DuplicateDialog.warn.title"), Messages.getString("DuplicateDialog.rename"), rename, null);
            isError = false;
        } else {
            dlg = new DuplicateDialog(Display.getCurrent().getActiveShell(), selection, Messages
                    .getString("DuplicateDialog.warn.title"), Messages.getString("DuplicateDialog.rename"), "", null);
        }

        if (dlg.open() == Dialog.OK) {
            isOk = true;
            names = dlg.getCopyNameMap();
            rename = names.values().toArray()[0].toString();
        }
        return isOk;
    }

    /**
     * DOC zwang Comment method "openInputNameDialog".
     */
    private void openErrorDialog() {
        // TODO Auto-generated method stub
        MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
        box.setText(Messages.getString("DuplicateDialog.warn.title"));
        box.setMessage(Messages.getString("DuplicateDialog.warn.message"));
        box.open();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        // TODO Auto-generated method stub
        boolean canWork = true;
        RepositoryNode node = (RepositoryNode) selection.getFirstElement();
        if (selection.isEmpty()) {
            setEnabled(false);
            return;
        }
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            canWork = false;
        }

        if (selection != null) {
            if (((StructuredSelection) selection).toArray().length > 1) {
                canWork = false;
            } else if (((StructuredSelection) selection).toArray().length == 1) {
                Object obj = ((StructuredSelection) selection).toList().get(0);
                if (canWork) {
                    RepositoryNode sourceNode = (RepositoryNode) obj;
                    if (!CopyObjectAction.getInstance().validateAction(sourceNode, null)) {
                        canWork = false;
                    } else if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOB_DOC
                            || node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET_DOC) {
                        canWork = false;
                    } else if (node.getContentType() == ERepositoryObjectType.JOBS
                            || node.getContentType() == ERepositoryObjectType.JOBLETS
                            || node.getContentType() == ERepositoryObjectType.GENERATED
                            || node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOB_DOC
                            || node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET_DOC) {
                        canWork = false;
                    }
                }
            }
        } else {
            canWork = false;
        }

        setEnabled(canWork);
    }
}
