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
package org.talend.sqlbuilder.dbdetail.tab;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dataset.dataset.DataSet;
import org.talend.sqlbuilder.dataset.dataset.DataSetTable;


/**
 * Details Tab.
 *
 * <br/>
 *
 * $Id: AbstractDataSetTab.java,v 1.7 2006/11/01 01:24:10 yi.zhang Exp $
 * @author yzhang
 *
 */
public abstract class AbstractDataSetTab extends AbstractTab {
   
    private DataSet pDataSet;
      
    private Composite pComposite;    
    
    public final void fillDetailComposite(Composite composite) {

        try {
            
            pComposite = composite;

            DataSet dataSet = getCachedDataSet();
            if (dataSet == null) {
                return;
            }
            
            // store for later use in dataset table
            composite.setData("IDetailTab", this);
            
            new DataSetTable(composite, dataSet, getStatusMessage());
                         
            
        } catch (Exception e) {
            SqlBuilderPlugin.log("DataSetTab Exception", e);
            // couldn't get results.. clean mess up
            Control[] controls = composite.getChildren();
            for (int i = 0; i < controls.length; i++) {
                controls[i].dispose();
            }
            
            // and show error message
            Label label = new Label(composite, SWT.FILL);
            label.setText(Messages.getString("DatabaseDetailView.Tab.Unavailable") + " " + e.getMessage());
            label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));    
            
            SqlBuilderPlugin.log("Error creating ResultSetTab:", e);
            
        }
        
    }
    
    
    /**
     * Returns dataset. if it doesn't exist yet, it is initialized first.
     */
    public final DataSet getCachedDataSet() throws Exception {
        
//    	CONSTANT_LOGGER.debug("getting cached data for " + this.getClass().getName());
    	
        if (pDataSet != null) {
            return pDataSet;
        }
        
        pDataSet = getDataSet();
        return pDataSet;
    }
    

    /*
     * Implement this method to initialzie the dataset;
     * @return
     * @throws Exception
     */
    public abstract DataSet getDataSet() throws Exception;
    
    
    /**
     * Refresh the contents of the dataset.
     */
    public final void refresh() {
        pDataSet = null;
        
        Control[] controls = pComposite.getChildren();
        for (int i = 0; i < controls.length; i++) {
            controls[i].dispose();
        }
        
        fillComposite(pComposite);
        pComposite.layout();
        pComposite.redraw();
    }

       
    /**
     * Implement this method to add a status message on the bottom of the dataset tab.
     */
    public abstract String getStatusMessage();
}
