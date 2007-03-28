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
package org.talend.sqlbuilder.dataset.dataset;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.sqlbuilder.IConstants;
import org.talend.sqlbuilder.SqlBuilderPlugin;

/**
 * Label Provider for DataSet.
 * 
 * @author Davy Vanherbergen
 */
/**
 * @author Davy Vanherbergen
 * 
 */
public class DataSetTableLabelProvider implements ITableLabelProvider {

    private SimpleDateFormat pdateFormatter = new SimpleDateFormat(
            SqlBuilderPlugin.getDefault().getPluginPreferences().getString(IConstants.DATASETRESULT_DATE_FORMAT));

    private DecimalFormat pdecimalFormat = new DecimalFormat();

    
    private boolean pformatDates = SqlBuilderPlugin.getDefault().getPluginPreferences().getBoolean(
            IConstants.DATASETRESULT_FORMAT_DATES);


    public DataSetTableLabelProvider() {

        pdecimalFormat.setGroupingUsed(false);
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void addListener(ILabelProviderListener listener) {

        // noop
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    public void dispose() {

        // noop

    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
     *      int)
     */
    public Image getColumnImage(Object element, int columnIndex) {

        return null;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
     *      int)
     */
    public String getColumnText(Object element, int columnIndex) {

        DataSetRow row = (DataSetRow) element;

        Object tmp = row.getObjectValue(columnIndex);

        if (tmp != null) {

            Class clazz = tmp.getClass();

            // filter out scientific values
            if (clazz == Double.class || clazz == Integer.class) {
                return pdecimalFormat.format(tmp);
            }

            // format dates
            if (pformatDates && clazz == Timestamp.class) {
                return pdateFormatter.format(new java.util.Date(((Timestamp) tmp).getTime()));
            }
            if (pformatDates && clazz == Date.class) {
                return pdateFormatter.format(new java.util.Date(((Date) tmp).getTime()));
            }

            return tmp.toString();
        }
        return "<null>"; //$NON-NLS-1$

    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
     *      java.lang.String)
     */
    public boolean isLabelProperty(Object element, String property) {

        return false;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void removeListener(ILabelProviderListener listener) {

        // noop
    }

}
