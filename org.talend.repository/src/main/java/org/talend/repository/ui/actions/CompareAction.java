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

import java.util.Collections;
import java.util.Comparator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.ui.EImage;
import org.talend.core.ui.ImageProvider;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;

/**
 * Action used to refresh a repository view.<br/>
 * 
 * $Id: RefreshAction.java 219 2006-10-24 13:45:54 +0000 (mar., 24 oct. 2006) smallet $
 * 
 */
public class CompareAction extends Action {

    public CompareAction() {
        super();
        this.setText("Tagada"); //$NON-NLS-1$
        this.setToolTipText("Tagada"); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.PASTE_ICON));
    }

    public void run() {
        InputDialog id = new InputDialog(new Shell(), "Other project", "Enter other project name", "tagada", null);
        if (id.open() == Window.OK) {
            String projectName = id.getValue();
            IRepositoryFactory rep = RepositoryFactoryProvider.getInstance();
            Project toCompare = null;
            try {
                Project[] list = rep.readProject();
                for (Project current : list) {
                    if (current.getLabel().equals(projectName)) {
                        toCompare = current;
                    }
                }
            } catch (PersistenceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (toCompare == null) {
                MessageDialog.openError(new Shell(), "Project not found", "Project " + projectName + " cannot be found");
                return;
            }
            RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY);
            Project currentProject = repositoryContext.getProject();
            MessageDialog.openInformation(new Shell(), "Result", ""
                    + new MyComparator().compare(toCompare.getEmfProject().getFolders(), currentProject.getEmfProject()
                            .getFolders()));
        }
    }

    /**
     * 
     * DOC smallet CompareAction class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private class MyComparator implements Comparator<EList> {

        public int compare(EList a, EList b) {
            Collections.sort(a, new FolderIdComparator());
            Collections.sort(b, new FolderIdComparator());

            for (Object c : a) {

            }

            return 0;
        }

    }

    /**
     * 
     * DOC smallet CompareAction class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private class FolderIdComparator implements Comparator<Object> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
            FolderItem a = (FolderItem) o1;
            FolderItem b = (FolderItem) o2;
            return a.getProperty().getId().compareTo(b.getProperty().getId());
        }

    }
}
