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

package org.talend.designer.business.diagram.custom.actions;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.EditorUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.BusinessProcess;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessProcessEditPart;
import org.talend.designer.business.model.business.diagram.part.BusinessDiagramEditorUtil;
import org.talend.designer.business.model.business.diagram.part.BusinessDiagramFileCreator;
import org.talend.repository.editor.RepositoryEditionManager;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DiagramResourceManager {

    private IWorkbenchPage page;

    private IProgressMonitor progressMonitor;

    public DiagramResourceManager(IWorkbenchPage page, IProgressMonitor progressMonitor) {
        this.page = page;
        this.progressMonitor = progressMonitor;
    }

    public DiagramResourceManager() {
    }

    public IFile createDiagramFile() {
        DiagramFileCreator fileCreator = BusinessDiagramFileCreator.getInstance();
        IPath temporaryFilePath = RepositoryEditionManager.getInstance().getTemporaryFilePath();
        IPath containerFullPath = temporaryFilePath.removeLastSegments(1);
        String fileName = temporaryFilePath.lastSegment();
        InputStream initialContents = EditorUtil.getInitialContents();
        String diagramKind = BusinessProcessEditPart.MODEL_ID;
        Shell shell = page.getWorkbenchWindow().getShell();

        IFile file = BusinessDiagramEditorUtil.createNewDiagramFile(fileCreator, containerFullPath, fileName, initialContents,
                diagramKind, shell, progressMonitor);

        return file;
    }

    public void updateFromResource(BusinessProcessItem businessProcessItem, IFile file) {
        Resource resource = createResource(file);

        BusinessProcess semantic = (BusinessProcess) EcoreUtil.getObjectByType(resource.getContents(), BusinessPackage.eINSTANCE
                .getBusinessProcess());
        Diagram notation = (Diagram) EcoreUtil.getObjectByType(resource.getContents(), NotationPackage.eINSTANCE.getDiagram());

        businessProcessItem.setSemantic(semantic);
        businessProcessItem.setNotation(notation);
    }

    public void updateResource(BusinessProcessItem businessProcessItem, IFile file) {
        Resource resource = createResource(file);

        resource.getContents().clear();
        resource.getContents().add(businessProcessItem.getSemantic());
        resource.getContents().add(businessProcessItem.getNotation());

        try {
            resource.save(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openEditor(BusinessProcessItem businessProcessItem, IFile file) {
        RepositoryEditionManager.getInstance().openEditor(page, file, businessProcessItem);
    }

    private Resource createResource(IFile file) {
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource resource = resourceSet.getResource(URI.createPlatformResourceURI(file.getFullPath().toString()), true);
        return resource;
    }
}
