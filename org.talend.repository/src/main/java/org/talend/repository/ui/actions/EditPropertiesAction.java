// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.repository.ui.actions;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.editor.IUIRefresher;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.wizards.PropertiesWizard;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class EditPropertiesAction extends AContextualAction {

    public EditPropertiesAction() {
        super();
        this.setText(Messages.getString("EditPropertiesAction.action.title")); //$NON-NLS-1$
        this.setToolTipText(Messages.getString("EditPropertiesAction.action.toolTipText")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.EDIT_ICON));
    }

    public void run() {
        ISelection selection = getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;

        IPath path = RepositoryNodeUtilities.getPath(node);

        PropertiesWizard wizard = new PropertiesWizard(node.getObject(), path);
        WizardDialog dlg = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
        if (dlg.open() == Window.OK) {
            refresh(node);
            // refresh the corresponding editor's name
            IEditorPart part = getCorrespondingEditor(node);
            if (part != null && part instanceof IUIRefresher) {
                ((IUIRefresher) part).refreshName();
            }
        }
    }

    /**
     * Find the editor that is related to the node.
     * 
     * @param node
     * @return
     */
    private IEditorPart getCorrespondingEditor(RepositoryNode node) {
        Object o = getActivePage().getInput();
        IEditorReference[] eidtors = getActivePage().getEditorReferences();

        for (int i = 0; i < eidtors.length; i++) {
            try {
                IEditorInput input = eidtors[i].getEditorInput();
                if (!(input instanceof RepositoryEditorInput)) {
                    continue;
                }

                RepositoryEditorInput repositoryInput = (RepositoryEditorInput) input;
                if (repositoryInput.getItem().equals(node.getObject().getProperty().getItem())) {

                    IPath path = repositoryInput.getFile().getLocation();

                    return eidtors[i].getEditor(false);
                }
            } catch (PartInitException e) {
                continue;
            }
        }
        return null;
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = selection.size() == 1;
        for (Object o : ((IStructuredSelection) selection).toArray()) {
            if (canWork) {
                if (o instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) o;
                    switch (node.getType()) {
                    case REPOSITORY_ELEMENT:
                        if (node.getObjectType() == ERepositoryObjectType.BUSINESS_PROCESS
                                || node.getObjectType() == ERepositoryObjectType.PROCESS
                                || node.getObjectType() == ERepositoryObjectType.ROUTINES) {
                            IRepositoryObject repObj = node.getObject();
                            IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
                            ERepositoryStatus status = repFactory.getStatus(repObj);
                            boolean isEditable = status.isPotentiallyEditable() || status.isEditable();
                            canWork = isEditable;
                        } else {
                            canWork = false;
                        }
                        break;
                    default:
                        canWork = false;
                        break;
                    }
                }
            }
        }
        setEnabled(canWork);
    }

}
