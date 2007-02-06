package org.talend.designer.business.model.business.diagram.part;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessProcessEditPart;

/**
 * @generated
 */
public class BusinessCreationWizardPage extends EditorWizardPage {

    /**
     * @generated
     */
    public BusinessCreationWizardPage(IWorkbench workbench, IStructuredSelection selection) {
        super("CreationWizardPage", workbench, selection); //$NON-NLS-1$
        setTitle(Messages.getString("BusinessCreationWizardPage.CreateBusinessDiagram")); //$NON-NLS-1$
        setDescription(Messages.getString("BusinessCreationWizardPage.CreatenewBusinessDiagram")); //$NON-NLS-1$
    }

    /**
     * @generated
     */
    public IFile createAndOpenDiagram(IPath containerPath, String fileName, InputStream initialContents, String kind,
            IWorkbenchWindow dWindow, IProgressMonitor progressMonitor, boolean saveDiagram) {
        return BusinessDiagramEditorUtil.createAndOpenDiagram(getDiagramFileCreator(), containerPath, fileName, initialContents,
                kind, dWindow, progressMonitor, isOpenNewlyCreatedDiagramEditor(), saveDiagram);
    }

    /**
     * @generated
     */
    protected String getDefaultFileName() {
        return "default"; //$NON-NLS-1$
    }

    /**
     * @generated
     */
    public DiagramFileCreator getDiagramFileCreator() {
        return BusinessDiagramFileCreator.getInstance();
    }

    /**
     * @generated
     */
    protected String getDiagramKind() {
        return BusinessProcessEditPart.MODEL_ID;
    }

}
