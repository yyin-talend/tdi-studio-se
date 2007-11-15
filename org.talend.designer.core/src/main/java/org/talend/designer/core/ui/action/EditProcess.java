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
package org.talend.designer.core.ui.action;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class EditProcess extends AContextualAction {

    private static final String EDIT_LABEL = Messages.getString("EditProcess.editJob"); //$NON-NLS-1$

    private static final String OPEN_LABEL = Messages.getString("EditProcess.openJob"); //$NON-NLS-1$

    public EditProcess() {
        super();

        this.setText(EDIT_LABEL);
        this.setToolTipText(EDIT_LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.PROCESS_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();

        RepositoryNode node = (RepositoryNode) obj;
        ProcessItem processItem = (ProcessItem) node.getObject().getProperty().getItem();

        IWorkbenchPage page = getActivePage();

        try {
            ProcessEditorInput fileEditorInput = new ProcessEditorInput(processItem, true);

            IEditorPart editorPart = page.findEditor(fileEditorInput);

            if (editorPart == null) {
                fileEditorInput.setView(getViewPart());
                fileEditorInput.setRepositoryNode(node);
                page.openEditor(fileEditorInput, MultiPageTalendEditor.ID, true);
            } else {
                ((MultiPageTalendEditor) editorPart).setReadOnly(fileEditorInput.setForceReadOnly(false));
                page.activate(editorPart);
            }
            refresh(obj);
        } catch (PartInitException e) {
            MessageBoxExceptionHandler.process(e);
        } catch (PersistenceException e) {
            MessageBoxExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (DesignerPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().isUserReadOnlyOnCurrentProject()) {
            canWork = false;
        }
        if (canWork) {
            Object o = selection.getFirstElement();
            RepositoryNode node = (RepositoryNode) o;
            switch (node.getType()) {
            case REPOSITORY_ELEMENT:
                if (node.getObjectType() != ERepositoryObjectType.PROCESS) {
                    canWork = false;
                } else {
                    IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
                    IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
                    if (repFactory.isPotentiallyEditable(node.getObject())) {
                        this.setText(EDIT_LABEL);
                    } else {
                        this.setText(OPEN_LABEL);
                    }
                }
                break;
            default:
                canWork = false;
            }
        }
        setEnabled(canWork);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualView#getClassForDoubleClick()
     */
    @Override
    public Class getClassForDoubleClick() {
        return ProcessItem.class;
    }
}
