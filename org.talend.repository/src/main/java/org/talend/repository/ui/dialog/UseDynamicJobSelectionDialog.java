// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;

/**
 * DOC yhch class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class UseDynamicJobSelectionDialog extends Dialog {

    List<RepositoryNode> repositoryNodes = new ArrayList<RepositoryNode>();

    ERepositoryObjectType type;

    String repositoryType;

    ITypeProcessor typeProcessor;

    private Button upBtn;

    private Button downBtn;

    List<RepositoryNode> allJobNodes = new ArrayList<RepositoryNode>();

    private CheckboxTableViewer checkboxTableViewer;

    private Table table;

    /**
     * Renders a human readable representation of the meta model contributors.
     */
    class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(final Object element, final int columnIndex) {
            if (element instanceof RepositoryNode) {
                final RepositoryNode contributor = (RepositoryNode) element;
                StringBuffer strB = new StringBuffer();
                contributor.getObject().getVersion();
                strB.append(contributor.getObject().getLabel());
                strB.append(" " + contributor.getObject().getVersion()); //$NON-NLS-1$
                return strB.toString();
            }
            return element.toString();
        }

        public Image getColumnImage(final Object element, final int columnIndex) {
            return getDefaultJobletImage();
        }

        public Image getDefaultJobletImage() {
            return ImageProvider.getImage(ECoreImage.PROCESS_ICON);
        }

    }

    public UseDynamicJobSelectionDialog(IShellProvider parentShell) {
        super(parentShell);
    }

    public UseDynamicJobSelectionDialog(Shell parentShell, ERepositoryObjectType type, String repositoryType,
            boolean isSelectUseDynamic) {
        super(parentShell);
        setShellStyle(SWT.SHELL_TRIM | SWT.APPLICATION_MODAL | getDefaultOrientation());
        this.type = type;
        /*
         * avoid select self repository node for Process Type.
         * 
         * borrow the repositoryType to set the current process id here.
         */
        this.repositoryType = repositoryType;
        getAllJobNodesList();
    }

    /**
     * Configures the shell
     * 
     * @param shell the shell
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.getString("UseDynamicJobSelectionDialog.selectJobShellText"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(container);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).hint(500, 400).applyTo(container);

        Label label = new Label(container, SWT.NONE);
        label.setText(Messages.getString("UseDynamicJobSelectionDialog.selectJob")); //$NON-NLS-1$
        GridDataFactory.swtDefaults().span(2, 1).applyTo(label);

        checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER);
        checkboxTableViewer.setLabelProvider(new TableLabelProvider());
        checkboxTableViewer.setContentProvider(new ArrayContentProvider());
        table = checkboxTableViewer.getTable();
        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                updateButtonStates();
            }

        });
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        checkboxTableViewer.setInput(allJobNodes);
        createSelectionButton(container);
        setCheckedNodes();
        return container;
    }

    public void getAllJobNodesList() {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
        Project project = ProjectManager.getInstance().getCurrentProject();
        try {
            list.addAll(factory.getAll(project, ERepositoryObjectType.PROCESS, true, false));
            for (IRepositoryViewObject object : list) {
                String jobId = object.getId();
                RepositoryNode node = RepositoryNodeUtilities.getRepositoryNode(jobId);
                if (node != null) {
                    String id = node.getId();
                    if (repositoryType != null && !repositoryType.equals("") && id != null && !id.equals("")) {
                        if (!id.equals(repositoryType)) {
                            allJobNodes.add(node);
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {

        RepositoryNode[] repositoryObjects = getCheckNodes();
        repositoryNodes.clear();
        for (RepositoryNode repositoryObject : repositoryObjects) {
            repositoryNodes.add(repositoryObject);
            //
            ProcessItem processItem = (ProcessItem) repositoryObject.getObject().getProperty().getItem();
            RelationshipItemBuilder relationshipItemBuilder = new RelationshipItemBuilder();
            relationshipItemBuilder.addOrUpdateItem(processItem);
        }
        super.okPressed();
    }

    public List<RepositoryNode> getRepositoryNodes() {
        return this.repositoryNodes;
    }

    public void setRepositoryNodes(List<RepositoryNode> repositoryNodes) {
        this.repositoryNodes = repositoryNodes;
    }

    public RepositoryNode[] getCheckNodes() {
        List<RepositoryNode> ret = new ArrayList<RepositoryNode>();
        for (int i = 0; i < checkboxTableViewer.getCheckedElements().length; i++) {
            RepositoryNode node = (RepositoryNode) checkboxTableViewer.getCheckedElements()[i];
            if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                ret.add(node);
            }
        }
        return (RepositoryNode[]) ret.toArray(new RepositoryNode[0]);
    }

    public void setCheckedNodes() {
        if (repositoryNodes != null || repositoryNodes.size() != 0) {
            checkboxTableViewer.setCheckedElements(repositoryNodes.toArray());
        }
    }

    /**
     * 
     * DOC yhch Comment method "createSelectionButton".
     * 
     * @param itemComposite
     */
    private void createSelectionButton(Composite itemComposite) {
        Composite buttonComposite = new Composite(itemComposite, SWT.NONE);
        GridLayoutFactory.swtDefaults().margins(0, 25).applyTo(buttonComposite);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).applyTo(buttonComposite);

        Button selectAll = new Button(buttonComposite, SWT.PUSH);
        selectAll.setText(DataTransferMessages.DataTransfer_selectAll);
        selectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkboxTableViewer.setAllChecked(true);
            }
        });

        setButtonLayoutData(selectAll);

        Button deselectAll = new Button(buttonComposite, SWT.PUSH);
        deselectAll.setText(DataTransferMessages.DataTransfer_deselectAll);
        deselectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkboxTableViewer.setAllChecked(false);
            }
        });

        setButtonLayoutData(deselectAll);

        upBtn = new Button(buttonComposite, SWT.PUSH);
        upBtn.setText(Messages.getString("UseDynamicJobSelectionDialog.upBtnText")); //$NON-NLS-1$
        upBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                ISelection selection = checkboxTableViewer.getSelection();
                if (selection instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                    Object firstElement = structuredSelection.getFirstElement();
                    if (firstElement instanceof RepositoryNode) {
                        RepositoryNode contributor = (RepositoryNode) firstElement;
                        int index = allJobNodes.indexOf(contributor);
                        allJobNodes.remove(contributor);
                        allJobNodes.add(index - 1, contributor);
                        checkboxTableViewer.refresh();
                    }
                }
                updateButtonStates();
            }

        });
        setButtonLayoutData(upBtn);

        downBtn = new Button(buttonComposite, SWT.PUSH);
        downBtn.setText(Messages.getString("UseDynamicJobSelectionDialog.downBtnText")); //$NON-NLS-1$
        downBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                ISelection selection = checkboxTableViewer.getSelection();
                if (selection instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                    Object firstElement = structuredSelection.getFirstElement();
                    if (firstElement instanceof RepositoryNode) {
                        RepositoryNode contributor = (RepositoryNode) firstElement;
                        int index = allJobNodes.indexOf(contributor);
                        allJobNodes.remove(contributor);
                        allJobNodes.add(index + 1, contributor);
                        checkboxTableViewer.refresh();
                    }
                }
                updateButtonStates();
            }
        });
        setButtonLayoutData(downBtn);
    }

    private void updateButtonStates() {
        final int index = table.getSelectionIndex();
        if (index == 0) {
            upBtn.setEnabled(false);
        } else {
            upBtn.setEnabled(true);
        }
        if (index >= checkboxTableViewer.getTable().getItemCount() - 1) {
            downBtn.setEnabled(false);
        } else {
            downBtn.setEnabled(true);
        }
    }
}
