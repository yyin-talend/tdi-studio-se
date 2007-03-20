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
package org.talend.repository.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.i18n.Messages;
import org.talend.core.model.action.ImportExternalJarAction;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.importExternalLib.ConfigExternalLibWizard;

/**
 * An action used to import external jar. <br/>
 * 
 * $Id: ImportExternalJARAction.java Mar 16, 20074:20:34 PM bqian $
 * 
 */
public class ConfigRoutineLibraryAction extends AContextualAction {

    public ConfigRoutineLibraryAction() {
        super();
        String label = org.talend.repository.i18n.Messages.getString("ConfigRoutineLibraryAction.actionLabel"); //$NON-NLS-1$
        this.setText(label);
        this.setDescription(label);
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.IMPORT_JAR));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = selection.size() == 1;
        if (canWork) {
            for (Object o : ((IStructuredSelection) selection).toArray()) {
                if (o instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) o;
                    switch (node.getType()) {
                    case REPOSITORY_ELEMENT:
                        if (node.getObjectType() == ERepositoryObjectType.ROUTINES) {
                            RoutineItem routine = (RoutineItem) node.getObject().getProperty().getItem();
                            if (routine.isBuiltIn()) {
                                canWork = false;
                            } else {
                                canWork = true;
                            }
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#isVisible()
     */
    public boolean isVisible() {
        return isEnabled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        ConfigExternalLibWizard wizard = new ConfigExternalLibWizard();
        IWorkbench workbench = this.getViewPart().getViewSite().getWorkbenchWindow().getWorkbench();
        wizard.init(workbench, (IStructuredSelection) this.getSelection());

        Shell activeShell = Display.getCurrent().getActiveShell();
        WizardDialog dialog = new WizardDialog(activeShell, wizard);
        dialog.open();
    }

}
