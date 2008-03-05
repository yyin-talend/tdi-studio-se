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
package org.talend.repository.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryContentProvider;
import org.talend.repository.ui.views.RepositoryView;

/**
 * bqian check the content of the repository view. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class RepositoryReviewDialog extends Dialog {

    ERepositoryObjectType type;

    private FakeRepositoryView repositoryView;

    private RepositoryNode result;

    /**
     * DOC bqian RepositoryReviewDialog constructor comment.
     * 
     * @param parentShell
     * @param type support ERepositoryObjectType.PROCESS<br>
     * ERepositoryObjectType.METADATA_CON_TABLE<br>
     * ERepositoryObjectType.METADATA_CON_QUERY <br>
     * ERepositoryObjectType.METADATA_FILE_DELIMITED<br>
     * ERepositoryObjectType.METADATA_FILE_LDIF<br>
     * ERepositoryObjectType.METADATA_FILE_POSITIONAL<br>
     * ERepositoryObjectType.METADATA_FILE_REGEXP<br>
     * ERepositoryObjectType.METADATA_FILE_XML<br>
     * ERepositoryObjectType.METADATA_GENERIC_SCHEMA<br>
     * ERepositoryObjectType.METADATA_LDAP_SCHEMA<br>
     * ERepositoryObjectType.METADATA_WSDL_SCHEMA)<br>
     * 
     */
    public RepositoryReviewDialog(Shell parentShell, ERepositoryObjectType type) {
        super(parentShell);
        setShellStyle(SWT.SHELL_TRIM | SWT.APPLICATION_MODAL | getDefaultOrientation());
        this.type = type;
    }

    /**
     * Configures the shell
     * 
     * @param shell the shell
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        // Set the title bar text and the size
        shell.setText("Repository Content");
        shell.setSize(500, 400);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite content = (Composite) super.createDialogArea(parent);

        IRepositoryView view = RepositoryView.show();
        repositoryView = new FakeRepositoryView(type);
        try {
            repositoryView.init(view.getViewSite());
        } catch (PartInitException e) {
            e.printStackTrace();
        }

        repositoryView.createPartControl(content);
        repositoryView.refresh();
        repositoryView.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                boolean highlightOKButton = true;
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection == null || selection.size() != 1) {
                    highlightOKButton = false;
                }
                RepositoryNode node = (RepositoryNode) selection.getFirstElement();
                ERepositoryObjectType t = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                if (node.getType() != ENodeType.REPOSITORY_ELEMENT) {
                    highlightOKButton = false;
                }
                if (type == ERepositoryObjectType.PROCESS || type == ERepositoryObjectType.METADATA_CON_TABLE
                        || type == ERepositoryObjectType.METADATA_CON_QUERY) {
                    if (node.getProperties(EProperties.CONTENT_TYPE) != type) {
                        highlightOKButton = false;
                    }
                } else {
                    if (node.getProperties(EProperties.CONTENT_TYPE) != ERepositoryObjectType.METADATA_CON_TABLE) {
                        highlightOKButton = false;
                    }
                }

                getButton(IDialogConstants.OK_ID).setEnabled(highlightOKButton);
            }
        });
        return content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        IStructuredSelection selection = (IStructuredSelection) repositoryView.getViewer().getSelection();
        result = (RepositoryNode) selection.getFirstElement();
        super.okPressed();
    }

    public RepositoryNode getResult() {
        return result;
    }
}

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class FakeRepositoryView extends RepositoryView {

    ERepositoryObjectType type;

    /**
     * DOC bqian SnippetsDialogTrayView constructor comment.
     * 
     * @param type
     * @param type
     */
    public FakeRepositoryView(ERepositoryObjectType type) {
        super();
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        // getViewer().addFilter(makeFilter());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#refresh(java.lang.Object)
     */
    @Override
    public void refresh(Object object) {
        refresh();
        // viewer.refresh(object);
        if (object != null) {
            // getViewer().setExpandedState(object, true);
            getViewer().expandToLevel(object, AbstractTreeViewer.ALL_LEVELS);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#refresh()
     */
    @Override
    public void refresh() {
        super.refresh();
        getViewer().setInput(this.getViewSite());
        getViewer().setInput(getInput());

    }

    private RepositoryNode getInput() {
        RepositoryContentProvider contentProvider = (RepositoryContentProvider) getViewer().getContentProvider();
        if (type == ERepositoryObjectType.PROCESS) {
            return contentProvider.getProcessNode();
        }
        if (type == ERepositoryObjectType.METADATA_CON_TABLE || type == ERepositoryObjectType.METADATA_CON_QUERY) {
            return contentProvider.getMetadataConNode();
        }
        if (type == ERepositoryObjectType.METADATA_FILE_DELIMITED) {
            return contentProvider.getMetadataFileNode();
        }
        if (type == ERepositoryObjectType.METADATA_FILE_LDIF) {
            return contentProvider.getMetadataFileLdifNode();
        }
        if (type == ERepositoryObjectType.METADATA_FILE_POSITIONAL) {
            return contentProvider.getMetadataFilePositionalNode();
        }
        if (type == ERepositoryObjectType.METADATA_FILE_REGEXP) {
            return contentProvider.getMetadataFileRegexpNode();
        }
        if (type == ERepositoryObjectType.METADATA_FILE_XML) {
            return contentProvider.getMetadataFileXmlNode();
        }

        if (type == ERepositoryObjectType.METADATA_GENERIC_SCHEMA) {
            return contentProvider.getMetadataGenericSchemaNode();
        }
        if (type == ERepositoryObjectType.METADATA_LDAP_SCHEMA) {
            return contentProvider.getMetadataLDAPSchemaNode();
        }
        if (type == ERepositoryObjectType.METADATA_WSDL_SCHEMA) {
            return contentProvider.getMetadataWSDLSchemaNode();
        }

        return null;
    }

    // @Override
    // public void dragFinished() {
    // LocalSelectionTransfer.getTransfer().setSelection(null);
    // LocalSelectionTransfer.getTransfer().setSelectionSetTime(0);
    // }

    /**
     * DOC bqian Comment method "makeFilter".
     * 
     * @return
     */
    private ViewerFilter makeFilter() throws IllegalArgumentException {
        if (type == ERepositoryObjectType.PROCESS || type == ERepositoryObjectType.METADATA_CON_TABLE
                || type == ERepositoryObjectType.METADATA_CON_QUERY) {
            return createProcessFilter(type);
        }
        throw new IllegalArgumentException("invalid type: " + type);
    }

    @Override
    protected void makeActions() {
    }

    @Override
    protected void hookContextMenu() {
    }

    @Override
    protected void contributeToActionBars() {
    }

    @Override
    protected void initDragAndDrop() {
    }

    @Override
    protected void hookDoubleClickAction() {
    }

    /**
     * DOC bqian Comment method "createProcessFilter".
     */
    private ViewerFilter createProcessFilter(final ERepositoryObjectType type) {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                RepositoryNode node = (RepositoryNode) element;
                if (node.getProperties(EProperties.CONTENT_TYPE) != null) {
                    if (node.getProperties(EProperties.CONTENT_TYPE).equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

}
