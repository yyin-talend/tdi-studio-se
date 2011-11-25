package org.talend.designer.business.model.business.diagram.part;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorCreationWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

/**
 * @generated
 */
public class BusinessCreationWizard extends EditorCreationWizard {

    /**
     * @generated
     */
    public void addPages() {
        super.addPages();
        if (page == null) {
            page = new BusinessCreationWizardPage(getWorkbench(), getSelection());
        }
        addPage(page);
    }

    /**
     * @generated
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        super.init(workbench, selection);
        setWindowTitle("New Business Diagram"); //$NON-NLS-1$
        setDefaultPageImageDescriptor(BusinessDiagramEditorPlugin
                .getBundledImageDescriptor("icons/wizban/NewBusinessWizard.gif")); //$NON-NLS-1$
        setNeedsProgressMonitor(true);
    }
}
