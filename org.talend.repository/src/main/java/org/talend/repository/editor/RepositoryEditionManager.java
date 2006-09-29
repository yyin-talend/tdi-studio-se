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
package org.talend.repository.editor;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.ResourceModelUtils;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryEditionManager {

    private static RepositoryEditionManager instance = new RepositoryEditionManager();

    private Map<IEditorPart, RepositoryEditorInput> editors = new HashMap<IEditorPart, RepositoryEditorInput>();;

    public static RepositoryEditionManager getInstance() {
        return instance;
    }

    public IPath getTemporaryFilePath() {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        Long one = Calendar.getInstance().getTimeInMillis();
        Long two = new Random().nextLong();

        return getTempFolderPath(getRepositoryProject()).append(one.toString() + two);
    }

    /**
     * DOC mhelleboid Comment method "getRepositoryProject".
     * 
     * @return
     */
    private Project getRepositoryProject() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        return repositoryContext.getProject();
    }

    /**
     * DOC mhelleboid Comment method "getTempFolder".
     * 
     * @param project TODO
     * 
     * @return
     */
    private IPath getTempFolderPath(Project project) {
        IProject iProject;
        IFolder iFolder = null;
        try {
            iProject = ResourceModelUtils.getProject(project);
            iFolder = ResourceUtils.getFolder(iProject, RepositoryConstants.TEMP_DIRECTORY, true);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return iFolder.getFullPath();
    }

    public void openEditor(IWorkbenchPage page, IFile file, Item item) {
        try {
            IEditorDescriptor editorDesc = IDE.getEditorDescriptor(file);
            RepositoryEditorInput repositoryEditorInput = new RepositoryEditorInput(file, item);
            IEditorPart editorPart = IDE.openEditor(page, repositoryEditorInput, editorDesc.getId());
            editors.put(editorPart, repositoryEditorInput);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
