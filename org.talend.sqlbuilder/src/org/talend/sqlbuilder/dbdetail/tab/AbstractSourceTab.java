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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.sqlbuilder.Messages;


/**
 * @author k709335
 *
 */
public abstract class AbstractSourceTab extends AbstractTab {

    private String pSource = null;
    
    private Text pViewer = null;
    
    public final void fillDetailComposite(Composite parent) {

        if (pSource == null) {
            pSource = getSource();
        }

        Composite composite = new Composite(parent, SWT.FILL);
        
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;                
        layout.marginLeft = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 2;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        
        composite.setLayout(layout);
        composite.setLayoutData(gridData);
        
        
        pViewer = new Text(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER | SWT.WRAP);
        if (pSource != null) {
            pViewer.setText(pSource);
        } else {
            pViewer.setText("");
        }
        pViewer.setLayoutData(gridData);
        
        // add status bar labels
        String info = getStatusMessage();
        if (info == null) {
            info = "";
        }
        Label infoLabel = new Label(composite, SWT.NULL);
        infoLabel.setText(info);
        infoLabel.setLayoutData(new GridData(SWT.LEFT, SWT.NULL, true, false));
    }

    public String getLabelText() {
        return Messages.getString("DatabaseDetailView.Tab.Source");
    }

    public abstract String getSource();
    
    
    public final void refresh() {
       pSource = null;
    }
    
    public String getStatusMessage() {
        return Messages.getString("DatabaseDetailView.Tab.SourceFor") + " " + getNode().getQualifiedName();
    }

}
