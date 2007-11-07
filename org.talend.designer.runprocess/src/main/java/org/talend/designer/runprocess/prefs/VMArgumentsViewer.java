// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.runprocess.prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.preferences.TableEditor;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class VMArgumentsViewer extends TableEditor {

    private static final List<String> EMPTY_STRING_LIST = Collections.unmodifiableList(new ArrayList<String>());

    private static final String ARG_DELIMITER = " -";

    public static final String EQ_DELIMITER = "=";

    /**
     * qzhang VMArgumentsViewer constructor comment.
     * 
     * @param name
     * @param labelText
     * @param parent
     */
    public VMArgumentsViewer(String name, String labelText, Composite parent) {
        super(name, labelText, parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.preferences.TableEditor#createContentProvider()
     */
    @Override
    protected IStructuredContentProvider createContentProvider() {
        return new IStructuredContentProvider() {

            public Object[] getElements(Object inputElement) {
                return ((List) inputElement).toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.preferences.TableEditor#createLabelProvider()
     */
    @Override
    protected ITableLabelProvider createLabelProvider() {
        return new ITableLabelProvider() {

            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            public String getColumnText(Object element, int columnIndex) {
                String value = ((String) element);
                String[] texts = value.split(EQ_DELIMITER);
                if (texts.length == 1) {
                    if (columnIndex == 0) {
                        return texts[0];
                    }
                    if (columnIndex == 1) {
                        return "";
                    }
                } else {
                    return texts[columnIndex];
                }
                throw new IllegalStateException();
            }

            public void addListener(ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void removeListener(ILabelProviderListener listener) {
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.preferences.TableEditor#createTable(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Table createTable(Composite parent) {
        Table contextTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        contextTable.setLinesVisible(true);
        contextTable.setHeaderVisible(true);

        TableColumn engineName = new TableColumn(contextTable, SWT.NONE);
        engineName.setText("Argument");
        engineName.setWidth(150);

        TableColumn shortDescription = new TableColumn(contextTable, SWT.NONE);
        shortDescription.setText("Value");
        shortDescription.setWidth(150);
        return contextTable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.preferences.TableEditor#getExistingInputObject(java.lang.String)
     */
    @Override
    protected String getExistingInputObject(String obj) {
        VMArgumentDialog dialog = new VMArgumentDialog(getShell(), obj);
        if (dialog.open() == Dialog.OK) {
            return dialog.getNewItem();
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.preferences.TableEditor#getNewInputObject()
     */
    @Override
    protected String getNewInputObject() {
        VMArgumentDialog dialog = new VMArgumentDialog(getShell(), null);
        if (dialog.open() == Dialog.OK) {
            return dialog.getNewItem();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.preferences.TableEditor#readString(java.lang.String)
     */
    @Override
    protected List<String> readString(String stringList) {
        if (stringList == null || "".equals(stringList)) { //$NON-NLS-1$        
            return EMPTY_STRING_LIST;
        }
        ArrayList<String> result = new ArrayList<String>(50);
        for (String tmp : stringList.split(ARG_DELIMITER)) {
            if (tmp != null && !"".equals(tmp) && check(tmp)) {
                result.add(tmp);
            }
        }
        return result;
    }

    /**
     * DOC qzhang Comment method "check".
     * 
     * @param tmp
     * @return
     */
    private boolean check(String tmp) {
        return tmp.contains(EQ_DELIMITER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.preferences.TableEditor#writeString(java.util.List)
     */
    @Override
    protected String writeString(List<String> items) {
        int size = items.size();
        StringBuffer buf = new StringBuffer(size * 50);
        buf.append(ARG_DELIMITER);
        for (int i = 0; i < size; i++) {
            buf.append(items.get(i));
            if (i != size - 1) {
                buf.append(ARG_DELIMITER);
            }
        }
        return buf.toString();
    }

    /**
     * qzhang VMArgumentsViewer class global comment. Detailled comment. <br/>
     * 
     */
    class VMArgumentDialog extends Dialog {

        private Text argText;

        private final String arg;

        private final String value;

        private Text valueText;

        private String newItem;

        /**
         * DOC qzhang VMArgumentDialog constructor comment.
         * 
         * @param parentShell
         */
        protected VMArgumentDialog(Shell parentShell, String item) {
            super(parentShell);
            if (item == null || "".equals(item)) {
                arg = "";
                value = "";
            } else {
                String[] split = item.split(EQ_DELIMITER);
                arg = split[0];
                if (split.length > 1) {
                    value = split[1];
                } else {
                    value = "";
                }
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
         */
        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText("Set the VM Argument");
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
         */
        @Override
        protected Control createDialogArea(Composite parent) {
            Composite container = (Composite) super.createDialogArea(parent);
            container.setLayout(new GridLayout(2, false));
            Label addLabel = new Label(container, SWT.NONE);
            addLabel.setText("Argument:");
            argText = new Text(container, SWT.BORDER);
            GridData gridData = new GridData(GridData.FILL_BOTH);
            argText.setLayoutData(gridData);
            argText.setText(arg);

            addLabel = new Label(container, SWT.NONE);
            addLabel.setText("Value:");
            valueText = new Text(container, SWT.BORDER);
            gridData = new GridData(GridData.FILL_BOTH);
            valueText.setLayoutData(gridData);
            valueText.setText(value);
            return container;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.dialogs.Dialog#okPressed()
         */
        @Override
        protected void okPressed() {
            newItem = argText.getText() + EQ_DELIMITER + valueText.getText();
            super.okPressed();
        }

        public String getNewItem() {
            return newItem;
        }
    }
}
