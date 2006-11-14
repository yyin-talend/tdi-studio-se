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
package org.talend.sqlbuilder.ui;


import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.actions.CloseSQLResultTabAction;
import org.talend.sqlbuilder.actions.IResultDisplayer;
import org.talend.sqlbuilder.sqlcontrol.AbstractSQLExecution;
import org.talend.sqlbuilder.util.TextUtil;


/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: SQLResultComposite.java,v 1.18 2006/11/06 09:18:06 qiang.zhang Exp $
 * 
 */
public class SQLResultComposite extends Composite implements IResultDisplayer {

    public static IResultDisplayer instance;

    private int lastTabNumber = 0;

    private TabFolder tabFolder;

    
    private AbstractSQLExecution sqlExecution;
    /**
     * 
     * DOC dev SQLResultComposite constructor comment.
     * @param parent
     * @param style
     */
    public SQLResultComposite(Composite parent, int style) {
        super(parent, style);
        
        GridLayout gLay = new GridLayout();
        gLay.marginLeft = 0;
        gLay.marginRight = 0;
        gLay.marginTop = 0;
        gLay.marginBottom = 0;

        gLay.horizontalSpacing = 0;
        gLay.verticalSpacing = 0;
        gLay.marginWidth = 0;
        gLay.marginHeight = 0;
        setLayout(gLay);
        setDefaultMessage();
        instance = this;
        
    }
    /**
     * 
     * DOC dev Comment method "addSQLExecution".
     * @param sqlExe sql execution object
     * @throws Exception throw all exception
     */
    public void addSQLExecution(AbstractSQLExecution sqlExe) throws Exception {
        this.sqlExecution = sqlExe;
        createTabFolder();
        createTabItem();
    }

    /**
     * 
     * DOC dev Comment method "createTabFolder".
     *
     */
    private void createTabFolder() {

        if (tabFolder == null || tabFolder.isDisposed()) {

            clearParent();

            // create tab folder for different sessions
            tabFolder = new TabFolder(this, SWT.NULL);
            tabFolder.setToolTipText(Messages.getString("SQLResultComposite.SQLResults.ToolTip")); //$NON-NLS-1$
            GridData gd = new GridData(GridData.FILL_BOTH);
            tabFolder.setLayoutData(gd);
            
            
            this.layout();
            this.redraw();

        }
    }

    /**
     * 
     * DOC dev Comment method "clearParent".
     */
    private void clearParent() {

        Control[] children = this.getChildren();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                children[i].dispose();
            }
        }

        lastTabNumber = 0;
    }

    /**
     * 
     * DOC dev Comment method "createTabItem".
     * @throws Exception throw all exception
     */
    private void createTabItem() throws Exception {
        lastTabNumber = lastTabNumber + 1;
        final TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
        String labelText = "" + lastTabNumber; //$NON-NLS-1$
        tabItem.setText(labelText);
        tabItem.setData("tabLabel", labelText); //$NON-NLS-1$
        tabItem.setToolTipText(TextUtil.getWrappedText(sqlExecution.getSqlStatement()));
        Composite composite = new Composite(tabFolder, SWT.NULL);
       
        
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginLeft = 0;
        layout.marginTop = 0;
        layout.marginBottom = 0;

        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        tabItem.setControl(composite);
        tabItem.setData(sqlExecution);

        tabItem.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(final DisposeEvent e) {
                BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                    public void run() {
                        TabItem tabItem = (TabItem) e.getSource();
                        AbstractSQLExecution sqlExe = (AbstractSQLExecution) tabItem.getData();
                        sqlExe.stop();
                        tabItem.setData(null);
                        if (tabFolder != null && !tabFolder.isDisposed()) {

                            if (tabFolder.getItemCount() == 0) {
                                // this is last tab..
                                clearParent();
                                setDefaultMessage();
                            }

                        } else if (tabFolder.isDisposed()) {
                            clearParent();
                            setDefaultMessage();
                        }
                    }
                });
            }
        });
        
        createHeaderComposite(composite, tabItem);
        
 
        createDetailComposite(composite, tabItem);

        // refresh view
        composite.layout();
        tabFolder.layout();
        tabFolder.redraw();
    }


    /**
     * Set a default message, this method is called when no results are
     * available for viewing.
     */
    private void setDefaultMessage() {

        clearParent();

        // add message
        String message = Messages.getString("SQLResultsView.NoResults"); //$NON-NLS-1$
        Label label = new Label(this, SWT.FILL);
        label.setText(message);
        label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        this.layout();
        this.redraw();
    }
    /**
     * 
     * DOC dev Comment method "createHeaderComposite".
     * @param parent a TabItem 's Control
     * @param tabItem a TabItem
     */
    private void createHeaderComposite(Composite parent, TabItem tabItem) {
        //add sql statement, first create temp label to calculate correct size
        
        
        int labelHeight = 60;
        int labelStyle = SWT.WRAP | SWT.MULTI;

        Text tmpLabel = new Text(parent, labelStyle);
        tmpLabel.setText(TextUtil.removeLineBreaks(sqlExecution.getSqlStatement()));
        tmpLabel.setLayoutData(new FillLayout());
        int parentWidth = this.getClientArea().width;
        Point idealSize = tmpLabel.computeSize(parentWidth - 30, SWT.DEFAULT);

        if (idealSize.y <= 60) {
            // we don't need a scroll bar. minimize
            labelHeight = idealSize.y;
        } else {
            // we need a scroll bar
            labelStyle = SWT.WRAP | SWT.MULTI | SWT.V_SCROLL;
        }

        tmpLabel.dispose();
        // now create real label
        // create spanned cell for table data
        Composite headerComposite = new Composite(parent, SWT.FILL);
        headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        GridLayout hLayout = new GridLayout();
        hLayout.numColumns = 2;
        hLayout.marginLeft = 0;
        
        hLayout.horizontalSpacing = 0;
        hLayout.verticalSpacing = 0;
        hLayout.marginWidth = 0;
        hLayout.marginHeight = 0;

        headerComposite.setLayout(hLayout);

        Text label = new Text(headerComposite, labelStyle);
//        label.setEnabled(false);
        label.setEditable(false);
        label.setBackground(this.getBackground());
        
        label.setText(TextUtil.removeLineBreaks(sqlExecution.getSqlStatement()));
        label.setToolTipText(TextUtil.getWrappedText(sqlExecution.getSqlStatement()));

        GridData labelGridData = new GridData(SWT.FILL, SWT.TOP, true, true);
//        labelGridData.heightHint = labelHeight;
        label.setLayoutData(labelGridData);
        label.setVisible(true);
        // add action bar

        ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
        toolBarMgr.createControl(headerComposite);
        toolBarMgr.add(new CloseSQLResultTabAction(tabItem));
        toolBarMgr.update(true);
        GridData gid = new GridData();
        gid.horizontalAlignment = SWT.RIGHT;
        gid.verticalAlignment = SWT.TOP;
        toolBarMgr.getControl().setLayoutData(gid);

    }
    /**
     * 
     * DOC dev Comment method "createDetailComposite".
     * @param parent a TabItem 's Control
     * @param tabItem a TabItem
     */
    private void createDetailComposite(Composite parent, TabItem tabItem) {
        // add sql execute result TableView composite to show progress bar and results
        Composite detailComposite = new Composite(parent, SWT.FILL);
        detailComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        sqlExecution.setComposite(detailComposite);
        sqlExecution.setParentTab(tabItem);
        sqlExecution.startExecution();
        // set new tab as the active one
        tabFolder.setSelection(tabFolder.getItemCount() - 1);
    }
}
