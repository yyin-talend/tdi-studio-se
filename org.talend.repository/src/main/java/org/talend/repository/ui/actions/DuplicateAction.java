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
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.talend.commons.exception.BusinessException;
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
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.model.actions.CopyObjectAction;
import org.talend.repository.ui.dialog.DuplicateDialog;

/**
 * zwang class global comment. Detailled comment
 */
public class DuplicateAction extends AContextualAction {

    private static DuplicateAction singleton;

    private DuplicateDialog dlg = null;

    private String rename = null;;

    private IStructuredSelection selection = null;

    private Map<String, String> names = null;

    private boolean isError = false;

    private boolean isEmptyName = false;

    private boolean isErrorName = false;

    private boolean isExistName = false;

    private IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

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

        // see feature 0001563: Display "Save job" prompt when "copy" action for a job is requested.
        promptForSavingIfNecessary();
        createOperation(target, copyObjectAction, selectionInClipboard);
        refresh();
    }

    /**
     * see feature 0001563: Display "Save job" prompt when "copy" action for a job is requested.
     */
    private void promptForSavingIfNecessary() {
        try {
            IEditorReference[] references = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getEditorReferences();
            if (references == null || references.length == 0) {
                return;
            }

            RepositoryNode selectedNode = (RepositoryNode) selection.getFirstElement();
            String label = selectedNode.getObject().getProperty().getLabel();

            for (int i = 0; i < references.length; i++) {
                IEditorPart part = references[i].getEditor(false);
                // find unsaved dialog
                if (part == null || part.isDirty() == false) {
                    continue;
                }

                IEditorInput input = part.getEditorInput();

                if (label.equals(input.getName())) {
                    // we have found an unsaved editor that matches the selected repository node
                    if (promptForSavingDialog(part) == ISaveablePart2.YES) {
                        part.doSave(new NullProgressMonitor());
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * Display a prompt dialog to ask the user if we should save the job before duplicating.
     * 
     * @param part
     * @return
     */
    @SuppressWarnings("restriction")
    private int promptForSavingDialog(IEditorPart part) {
        String[] buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL };
        String message = NLS.bind(WorkbenchMessages.EditorManager_saveChangesQuestion, part.getTitle());
        Dialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), WorkbenchMessages.Save_Resource, null, message,
                MessageDialog.QUESTION, buttons, 0) {

            @Override
            protected int getShellStyle() {
                return SWT.NONE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | getDefaultOrientation();
            }
        };
        return dialog.open();
    }

    /**
     * DOC zwang Comment method "createOperation".
     */
    private void createOperation(RepositoryNode target, CopyObjectAction copyObjectAction, TreeSelection selectionInClipboard) {
        Item item = null;

        if (selectionInClipboard != null && selectionInClipboard.toArray().length == 1) {
            Object currentSource = selectionInClipboard.toArray()[0];
            // if (name.trim().equals(((RepositoryNode) currentSource).getObject().getProperty().getLabel().trim())) {
            try {
                IPath path = RepositoryNodeUtilities.getPath(target);

                if (((RepositoryNode) currentSource).getType().equals(ENodeType.REPOSITORY_ELEMENT)) {
                    // Source is an repository element :
                    Item originalItem = ((RepositoryNode) currentSource).getObject().getProperty().getItem();
                    String newName = getPropNewName(originalItem.getProperty());
                    while (true) {
                        if (!openInputNameDialog(newName)) {
                            return;
                        }
                        if (!isValid(rename, selectionInClipboard)) {
                            openErrorDialog();
                            isError = true;
                            continue;
                        }
                        break;
                    }
                    item = factory.copy(originalItem, path);
                    String name = (String) names.keySet().toArray()[0];
                    item.getProperty().setLabel(names.get(name));
                    // refresh();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
            // }
        }
    }

    /**
     * yzhang Comment method "setPropNewName".
     * 
     * @param copiedProperty
     * @throws PersistenceException
     * @throws BusinessException
     */
    private String getPropNewName(Property copiedProperty) throws PersistenceException, BusinessException {
        String originalLabel = copiedProperty.getLabel();
        String add1 = "Copy_of_"; //$NON-NLS-1$
        String initialTry = add1 + originalLabel;

        String nextTry = initialTry;

        if (factory.isNameAvailable(copiedProperty.getItem(), initialTry)) {
            return initialTry;
        } else {
            char j = 'a';
            while (!factory.isNameAvailable(copiedProperty.getItem(), nextTry)) {
                if (j > 'z') {
                    throw new BusinessException("Cannot generate pasted item label.");
                }
                nextTry = initialTry + "_" + (j++) + ""; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        return nextTry;
    }

    private Item createNewItem() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        ProcessItem processItem = PropertiesFactory.eINSTANCE.createProcessItem();
        processItem.setProperty(property);
        return processItem;
    }

    // validate if the new name is existent.
    public boolean isValid(String itemName, TreeSelection selectionInClipboard) {
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();

        try {
            if (itemName.length() == 0) {
                isEmptyName = true;
                return false;
            } else if (!Pattern.matches(RepositoryConstants.getPattern(((RepositoryNode) selectionInClipboard.toArray()[0])
                    .getObject().getType()), itemName)) {
                isErrorName = true;
                return false;
            } else {
                if (repositoryFactory.isNameAvailable(createNewItem(), itemName)) {
                    return true;
                } else {
                    isExistName = true;
                    return false;
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
    }

    /**
     * DOC zwang Comment method "openInputNameDialog".
     */
    private boolean openInputNameDialog(String newName) {
        // TODO Auto-generated method stub
        boolean isOk = false;
        if (isError) {
            dlg = new DuplicateDialog(Display.getCurrent().getActiveShell(), selection, Messages
                    .getString("DuplicateDialog.title"), Messages.getString("DuplicateDialog.rename"), rename, null);
            isError = false;
        } else {
            dlg = new DuplicateDialog(Display.getCurrent().getActiveShell(), selection, Messages
                    .getString("DuplicateDialog.title"), Messages.getString("DuplicateDialog.rename"), newName, null);
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

        if (isEmptyName) {
            isEmptyName = false;
            box.setMessage(Messages.getString("DuplicateDialog.warn.message.emptyName"));
        } else if (isErrorName) {
            box.setMessage(Messages.getString("DuplicateDialog.warn.message.errorName"));
            isErrorName = false;
        } else if (isExistName) {
            box.setMessage(Messages.getString("DuplicateDialog.warn.message"));
            isExistName = false;
        }
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
