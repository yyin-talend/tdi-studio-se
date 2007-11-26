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
package org.talend.repository.ui.actions.snippet;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.SnippetItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.wizards.snippet.SnippetWizard;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class EditSnippetAction extends AContextualAction {

    private static final String EDIT_LABEL = "Edit Snippet";

    private RepositoryNode node = null;

    /**
     * DOC nrousseau EditContextAction constructor comment.
     */
    public EditSnippetAction() {
        super();

        this.setText(EDIT_LABEL);
        this.setToolTipText(EDIT_LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.SNIPPETS_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (canWork) {
            Object o = selection.getFirstElement();
            node = (RepositoryNode) o;
            switch (node.getType()) {
            case REPOSITORY_ELEMENT:
                if (node.getObjectType() != ERepositoryObjectType.SNIPPETS) {
                    canWork = false;
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
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        SnippetWizard snippetWizard = new SnippetWizard(PlatformUI.getWorkbench(), false, getSelection(), false);
        WizardDialog dlg = new WizardDialog(Display.getCurrent().getActiveShell(), snippetWizard);
        dlg.open();

        if (node != null) {
            refresh(node);
        }
    }

    @Override
    public Class getClassForDoubleClick() {
        return SnippetItem.class;
    }
}
