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
package org.talend.designer.webservice.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.designer.webservice.i18n.Messages;
import org.talend.designer.webservice.ui.ParameterInfoUtil;
import org.talend.designer.webservice.ui.tree.WebServiceTreeContentProvider;
import org.talend.designer.webservice.ui.tree.WebServiceTreeLabelProvider;
import org.talend.designer.webservice.ws.wsdlinfo.ParameterInfo;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class AddListDialog extends Dialog {

    private static final String NAME = "NAME"; //$NON-NLS-1$

    private static final String INDEX = "INDEX"; //$NON-NLS-1$

    private TreeViewer treeViewer = null;

    private Tree tree = null;

    private ParameterInfo para;

    private boolean maximized;

    private Rectangle size;

    private String title;

    private String inOrOut;

    private ParameterInfo selectedParaInfo;

    private List<ParameterInfo> paramList;

    private ParameterInfoUtil paraUtil;

    private Shell parentShell;

    private SelectAction selectAction;

    /**
     * DOC Administrator AddListDialog constructor comment.
     * 
     * @param parentShell
     */
    protected AddListDialog(Shell parentShell) {
        super(parentShell);
        this.parentShell = parentShell;
        selectAction = new SelectAction();
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    /**
     * DOC Administrator AddListDialog constructor comment.
     * 
     * @param parentShell
     */
    public AddListDialog(Shell parentShell, ParameterInfo para, String inOrOut) {
        super(parentShell);
        this.para = para;
        this.inOrOut = inOrOut;
        this.parentShell = parentShell;
        selectAction = new SelectAction();
        // Composite baseCom = new Composite(parentShell, SWT.NONE);
        // createTreeDialogArea(baseCom);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Composite baseCom = (Composite) super.createDialogArea(parent);
        Composite createDialogArea = (Composite) super.createDialogArea(parent);
        createDialogArea.setLayout(new FillLayout());
        SashForm baseCom = new SashForm(createDialogArea, SWT.VERTICAL | SWT.BORDER);
        treeViewer = new TreeViewer(baseCom, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        tree = treeViewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Paramater Name Column
        TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
        column1.setText("Parameter Name");
        column1.setWidth(200);

        // Array Index Column
        TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
        column2.setText("Array Index");
        column2.setWidth(100);

        treeViewer.setColumnProperties(new String[] { NAME, INDEX });
        CellEditor editor = new TextCellEditor(treeViewer.getTree());
        treeViewer.setCellEditors(new CellEditor[] { null, editor });
        treeViewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                if (element instanceof ParameterInfo) {
                    ParameterInfo para = (ParameterInfo) element;
                    if (INDEX.equals(property) && para.getArraySize() != 0) { // if item is array it can edit.
                        return true;
                    }
                }
                return false;
            }

            public Object getValue(Object element, String property) {
                if (element instanceof ParameterInfo) {
                    ParameterInfo para = (ParameterInfo) element;
                    if (INDEX.equals(property)) {
                        return para.getIndex() == null ? "" : para.getIndex();
                    } else if (NAME.equals(property)) {
                        return para.getName();
                    }
                }
                return null;
            }

            public void modify(Object element, String property, Object value) {
                if (element instanceof TreeItem) {
                    ParameterInfo para = (ParameterInfo) ((TreeItem) element).getData();
                    if (INDEX.equals(property)) {
                        para.setIndex(value.toString());
                    }
                }
                treeViewer.refresh();

            }

        });
        // DrillDownAdapter adapter = new DrillDownAdapter(treeView);
        treeViewer.setContentProvider(new WebServiceTreeContentProvider());

        treeViewer.setLabelProvider(new WebServiceTreeLabelProvider());

        treeViewer.setInput(para);
        treeViewer.addSelectionChangedListener(selectAction);

        // tree.addSelectionListener(new SelectionAdapter() {
        //
        // @Override
        // public void widgetSelected(SelectionEvent e) {
        // TreeItem[] selects = tree.getSelection();
        // ISelection select = (ISelection) treeViewer.getSelection();
        // Object obj = ((IStructuredSelection) select).getFirstElement();
        // if (obj instanceof ParameterInfo) {
        // // if (((ParameterInfo) obj).getParameterInfos().size() > 0) {
        // // // ErrorDialog dialog = new ErrorDialog(parentShell, "", "", null, 0);
        // // // dialog.open();
        // // selectedParaInfo = null;
        // // } else {
        // selectedParaInfo = (ParameterInfo) obj;
        // // }
        // }
        // }
        //
        // });
        return baseCom;
    }

    public class SelectAction implements ISelectionChangedListener {

        public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            if (selection.size() == 1) {
                Object obj = selection.getFirstElement();
                if (obj instanceof ParameterInfo) {
                    selectedParaInfo = (ParameterInfo) obj;
                }
            } else if (selection.size() > 1) {
                // Iterator iterator=selection.iterator();
                paramList = selection.toList();
                // for (ParameterInfo para : paramList) {
                // if (getAllArrayFromParents(para, null).size() != 0 || para.getArraySize() != 0) {
                // paramList = null;
                // Object obj = selection.getFirstElement();
                // if (obj instanceof ParameterInfo) {
                // selectedParaInfo = (ParameterInfo) obj;
                // }
                // break;
                // }

                // }

            }

        }
    }

    public ParameterInfo getSelectedParaInfo() {
        return selectedParaInfo;
    }

    public List<ParameterInfo> getParamList() {
        return this.paramList;
    }

    @Override
    protected void okPressed() {
        boolean falg = false;
        ParameterInfo selPara = getSelectedParaInfo();
        ParameterInfo usePara = null;
        paraUtil = new ParameterInfoUtil();
        int currentindex = -1;
        // int arraySize = selPara.getArraySize();

        // if select multi simple type items.
        if (paramList != null && !paramList.isEmpty()) {

        }

        // if selected have branch.
        if (selPara == null) {
            return;
        }
        if (!selPara.getParameterInfos().isEmpty()) {
            MessageBox box = new MessageBox(parentShell, SWT.ICON_ERROR | SWT.OK);
            box.setText(Messages.getString("AddListDialog.Error")); //$NON-NLS-1$
            box.setMessage("Please Select " + selPara.getName() + " branch Item."); //$NON-NLS-1$
            box.open();
            return;
        }

        // if select a array item.
        // if (selPara.getArraySize() == -1) {
        // AddArrayIndexDialog dlg = new AddArrayIndexDialog(parentShell, selPara);
        // int openCode = dlg.open();
        // if (openCode == AddArrayIndexDialog.OK) {
        // String indexValue = dlg.getIndexText();
        // currentindex = Integer.valueOf(indexValue);
        // } else {
        // return;
        // }
        // paraUtil.setCurrentindex(currentindex);
        // // super.okPressed();
        // }

        // if select item's parent is array.
        // if (arraySize == 0 && selPara.getParent() != null && !getAllArrayFromParents(selPara, null).isEmpty()) {
        // List<ParameterInfo> paraList = paraUtil.getAllParameterInfo(selPara);
        // goout: for (ParameterInfo para : paraList) {
        // if (para.getArraySize() != 0) {
        // falg = true;
        // usePara = para;
        // arraySize = para.getArraySize();
        // break goout;
        // }
        // }
        // }
        // if (falg) {
        // String title = "";
        // if (usePara != null && usePara.getName() != null) {
        // title = usePara.getName() + " index is :";
        // }
        // if (getAllArrayFromParents(selPara, null).size() == 1 && "input".equals(inOrOut) && false) {
        //                InputDialog dlg = new InputDialog(parentShell, title, title, "", new InputIndexValidator()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        // int openCode = dlg.open();
        // if (openCode == InputDialog.OK) {
        // String indexValue = dlg.getValue();
        // currentindex = Integer.valueOf(indexValue);
        // } else if (openCode == InputDialog.CANCEL) {
        // // super.cancelPressed();
        // return;
        // }
        // if (arraySize != -1 && currentindex > arraySize) {
        // currentindex = -1;
        // MessageBox box = new MessageBox(parentShell, SWT.ICON_ERROR | SWT.OK | SWT.CANCEL);
        //                    box.setText(Messages.getString("AddListDialog.Error")); //$NON-NLS-1$
        //                    box.setMessage(Messages.getString("AddListDialog.CHECKSIZE")); //$NON-NLS-1$
        // box.open();
        // return;
        // }
        // paraUtil.setCurrentindex(currentindex);
        // super.okPressed();
        // } else if (getAllArrayFromParents(selPara, null).size() >= 1) {
        //
        // AddArrayIndexForParentsDialog multiDialog = new AddArrayIndexForParentsDialog(parentShell, selPara);
        // int openCode = multiDialog.open();
        // if (openCode == AddArrayIndexForParentsDialog.OK) {
        // List indexList = null;
        // if (multiDialog.getArrayIndexList() != null) {
        // indexList = multiDialog.getArrayIndexList();
        // }
        // paraUtil.setCurrenIndexList(indexList);
        // super.okPressed();
        // } else {
        // return;
        // }
        // }
        //
        // }

        // paraUtil.setCurrentindex(currentindex);
        super.okPressed();
    }

    private void checkSizeBox() {

    }

    private List<ParameterInfo> getAllArrayFromParents(ParameterInfo parameter, List<ParameterInfo> allArrayParents) {
        if (allArrayParents == null) {
            allArrayParents = new ArrayList<ParameterInfo>();
        }
        if (parameter.getParent() != null) {
            ParameterInfo parent = parameter.getParent();
            if (parent.getArraySize() != 0) {
                allArrayParents.add(parent);
            }

            getAllArrayFromParents(parent, allArrayParents);
        }

        return allArrayParents;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        if (title != null) {
            newShell.setText(title);
        }
        // if (maximized) {
        // newShell.setMaximized(true);
        // } else {
        // newShell.setBounds(size);
        newShell.setBounds(500, 300, 400, 350);
        // }
    }

    public void setTitle(String title) {
        this.title = title;

    }

    /**
     * Sets the size.
     * 
     * @param size the size to set
     */
    public void setSize(Rectangle size) {
        this.size = size;
    }

    /**
     * Sets the maximizedSize.
     * 
     * @param maximizedSize the maxmimizedSize to set
     */
    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    public ParameterInfoUtil getParaUtil() {
        return this.paraUtil;
    }

    public void setParaUtil(ParameterInfoUtil paraUtil) {
        this.paraUtil = paraUtil;
    }

}
