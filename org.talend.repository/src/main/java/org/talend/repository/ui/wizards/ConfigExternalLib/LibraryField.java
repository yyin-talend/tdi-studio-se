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
package org.talend.repository.ui.wizards.ConfigExternalLib;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.core.model.properties.helper.StatusHelper;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preference.StatusDialog;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id: StatusEditor.java 1937 2007-02-09 06:34:36 +0000 (Fri, 09 Feb 2007) bqian $
 * 
 */
public class LibraryField extends TableField {

    /**
     * DOC tguiu StatusEditor constructor comment.
     * 
     * @param name
     * @param labelText
     * @param parent
     */
    public LibraryField(String name, Composite parent) {
        super(name, parent);
    }

    @Override
    protected Table createTable(Composite parent) {
        Table contextTable = new Table(parent, SWT.BORDER | SWT.SINGLE|SWT.BORDER);
        contextTable.setLinesVisible(false);
        contextTable.setHeaderVisible(false);

        TableColumn colName = new TableColumn(contextTable, SWT.NONE);
        colName.setText(Messages.getString("StatusEditor.codeColumnTitle")); //$NON-NLS-1$
        colName.setWidth(500);

        return contextTable;
    }

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

    @Override
    protected ITableLabelProvider createLabelProvider() {
        return new ITableLabelProvider() {

            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            public String getColumnText(Object element, int columnIndex) {

                if (columnIndex == 0) {
                    if (element instanceof IMPORTType) {
                        IMPORTType type = (IMPORTType) element;
                        return type.getMODULE();
                    }
                }
                return "";
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

    @Override
    protected List getNewInputObject() {
        return null;
    }
}
