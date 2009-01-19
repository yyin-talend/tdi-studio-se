// ============================================================================
//
// Copyright (C) 2006-2008 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.filemultischemas.ui.provider.column;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.talend.designer.filemultischemas.data.EPropertyName;
import org.talend.designer.filemultischemas.data.MultiMetadataColumn;
import org.talend.designer.filemultischemas.data.MultiSchemasMetadataColumn;
import org.talend.designer.filemultischemas.data.SchemasKeyData;
import org.talend.designer.filemultischemas.managers.UIManager;
import org.talend.designer.filemultischemas.ui.provider.SchemaDetailsProvider;

/**
 * cLi class global comment. Detailled comment
 * 
 */
public class SchemaDetailsColumnsProvider extends SchemaDetailsProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof SchemasKeyData) {
            SchemasKeyData keyData = (SchemasKeyData) inputElement;

            List<ColumnLineData> lineDatas = new ArrayList<ColumnLineData>();
            // fist column name
            ColumnLineData line = new ColumnLineData(null, keyData);
            lineDatas.add(line);

            for (EPropertyName property : EPropertyName.values()) {
                line = new ColumnLineData(property, keyData);
                lineDatas.add(line);
            }
            return lineDatas.toArray();

        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof ColumnLineData) {
            ColumnLineData lineData = (ColumnLineData) element;

            final EPropertyName property = lineData.getProperty();
            if (columnIndex == 0) {
                if (UIManager.isFirstForColumnModel(property)) { // first column name
                    return EPropertyName.NAME.getName(); //$NON-NLS-1$
                } else if (property == EPropertyName.NAME) { // for data.
                    return "";
                }
                return property.getName();
            }
            List<MultiMetadataColumn> columnsData = lineData.getKeyData().getMetadataColumns();
            if (columnsData.size() >= columnIndex) {
                MultiMetadataColumn columnData = columnsData.get(columnIndex - 1);
                if (columnData != null) {
                    if (UIManager.isFirstForColumnModel(property)) {
                        return columnData.getLabel();
                    }
                    switch (property) {
                    case NAME:
                        List<MultiSchemasMetadataColumn> dataColumns = columnData.getDataColumns();
                        if (dataColumns.size() > 0) {
                            // only use the first line
                            MultiSchemasMetadataColumn c = dataColumns.get(0);
                            return c.getData();
                        }
                        return columnData.getData();
                    case TAGLEVEL:
                        if (columnIndex == 1) { // only the first data column display the level.
                            return validateValue(columnData.getContainerTagLevel());
                        }
                        break;
                    // case KEY:
                    // return validateValue(columnData.isKey());
                    case TYPE:
                        return getTypeLabel(columnData.getTalendType());
                        // case NULL:
                        // return validateValue(columnData.isNullable());
                    case LENGTH:
                        return validateValue(columnData.getLength());
                        // case PRECISION:
                        // return validateValue(columnData.getPrecision());
                    case CARD:
                        return columnData.getCard();
                    case PATTERN:
                        return columnData.getPattern();
                    default:
                    }
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
     */
    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof ColumnLineData) {
            ColumnLineData lineData = (ColumnLineData) element;
            final EPropertyName property = lineData.getProperty();
            if (UIManager.isFirstForColumnModel(property)) {
                return Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
            }
            if (columnIndex == 0) {
                final int index = EPropertyName.indexOf(property);
                if (index > -1 && index % 2 == 0) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
                } else {
                    return Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
     */
    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableFontProvider#getFont(java.lang.Object, int)
     */
    public Font getFont(Object element, int columnIndex) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
        return false;
    }

}
