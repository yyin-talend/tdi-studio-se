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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import net.sourceforge.sqlexplorer.SQLAlias;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.colorstyledtext.ColorManager;
import org.talend.commons.ui.swt.colorstyledtext.ColorStyledText;
import org.talend.commons.ui.swt.proposal.StyledTextContentAdapter;
import org.talend.core.CorePlugin;
import org.talend.sqlbuilder.IConstants;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.actions.AbstractEditorAction;
import org.talend.sqlbuilder.actions.ClearTextAction;
import org.talend.sqlbuilder.actions.ExecSQLAction;
import org.talend.sqlbuilder.actions.OpenFileAction;
import org.talend.sqlbuilder.actions.SQLEditorSessionSwitcher;
import org.talend.sqlbuilder.actions.SaveFileAsAction;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.ui.editor.ISQLEditor;
import org.talend.sqlbuilder.ui.proposal.SQLEditorProposalAdapter;
import org.talend.sqlbuilder.ui.proposal.SQLEditorProposalProvider;
import org.talend.sqlbuilder.ui.proposal.SQLEditorLabelProvider;
import org.talend.sqlbuilder.util.UIUtils;

/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: SQLBuilderEditorComposite.java,v 1.48 2006/11/07 10:36:49 peiqin.hou Exp $
 * 
 */
public class SQLBuilderEditorComposite extends Composite implements ISQLEditor {

    private AbstractEditorAction clearTextAction;

    private CoolBar coolBar;

    private CoolBarManager coolBarMgr;

    private ToolBarManager defaultToolBarMgr;

    private AbstractEditorAction execSQLAction;

    private AbstractEditorAction openFileAction;

    private AbstractEditorAction saveAsAction;

    private SQLEditorSessionSwitcher sessionSwitcher;

    private ToolBarManager sessionToolBarMgr;

    private SessionTreeNode sessionTreeNode;

    private boolean ifLimit = true;

    public static final String[] SUPPORTED_FILETYPES = new String[] { "*.txt", "*.sql", "*.*" };

    private Text maxResultText;

    private StyledText colorText;

    private CTabItem tabItem;

    public SQLBuilderEditorComposite(Composite parent, CTabItem tabItem, int style) {
        super(parent, style);
        this.tabItem = tabItem;
        initialContent(this);
    }

    /**
     * 
     * DOC dev Comment method "initialContent".
     */
    private void initialContent(Composite parent) {

        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginLeft = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        this.setLayout(layout);
        createToolBar();
        createEditorArea(parent);
        createStatusArea(parent);
    }
    final String language = "tsql";
    /**
     * 
     * DOC dev Comment method "createEditorArea".
     */
    private void createEditorArea(Composite parent) {

        
        // create divider line

        Composite div1 = new Composite(parent, SWT.NONE);
        GridData lgid = new GridData();
        lgid.grabExcessHorizontalSpace = true;
        lgid.horizontalAlignment = GridData.FILL;
        lgid.heightHint = 1;
        lgid.verticalIndent = 1;
        div1.setLayoutData(lgid);
        div1.setBackground(parent.getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));

        // create text viewer

        GridData gid = new GridData();
        gid.grabExcessHorizontalSpace = gid.grabExcessVerticalSpace = true;
        gid.horizontalAlignment = gid.verticalAlignment = GridData.FILL;

        ColorManager colorManager = new ColorManager(CorePlugin.getDefault().getPreferenceStore());
        colorText = new ColorStyledText(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL, colorManager, language);
        Font font = new Font(parent.getDisplay(), "courier", 10, SWT.NONE);
        colorText.setFont(font);

        GridData gd = new GridData(GridData.FILL_BOTH);
        colorText.setLayoutData(gd);

        colorText.addVerifyKeyListener(new VerifyKeyListener() {

            public void verifyKey(VerifyEvent event) {
                // if (event.stateMask == SWT.CTRL && ((event.keyCode == 82 || event.keyCode == 114))) {
                if (event.stateMask == SWT.CTRL && event.keyCode == 13) {
                    event.doit = false;
                    execSQLAction.run();
                }
            }
        });
        
    }

    /**
     * DOC dev Comment method "createEditorProposal".
     */
    private void createEditorProposal() {
        try {
            //create KeyStroke use Ctrl+Space 
            KeyStroke keyStroke = KeyStroke.getInstance("Ctrl+Space");
            IControlContentAdapter controlContentAdapter = 
                new StyledTextContentAdapter();
            IContentProposalProvider contentProposalProvider = 
                new SQLEditorProposalProvider(sessionTreeNode, language);
            
            SQLEditorProposalAdapter contentProposalAdapter = 
                new SQLEditorProposalAdapter(colorText, controlContentAdapter, 
                    contentProposalProvider, keyStroke, new char[]{' '});
            contentProposalAdapter.setPropagateKeys(true);
            contentProposalAdapter.setFilterStyle(ContentProposalAdapter.FILTER_CUMULATIVE);
            contentProposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
            contentProposalAdapter.setLabelProvider(new SQLEditorLabelProvider());
            contentProposalAdapter.setAutoActivationDelay(10);
            contentProposalAdapter.setPopupSize(new Point(300, 200));
        } catch (Exception e) {
           SqlBuilderPlugin.log("Create SQL Editor Proposal Failure", e);
        }
    }
     

    /**
     * 
     * DOC dev Comment method "createStatusArea".
     */
    private void createStatusArea(Composite parent) {

        // create bottom status bar

        Composite statusBar = new Composite(parent, SWT.NULL);

        GridLayout statusBarLayout = new GridLayout();
        statusBarLayout.numColumns = 3;
        statusBarLayout.verticalSpacing = 0;
        statusBarLayout.marginHeight = 0;
        statusBarLayout.marginWidth = 0;
        statusBarLayout.marginTop = 0;
        statusBarLayout.marginBottom = 0;
        statusBarLayout.marginRight = 5;
        statusBarLayout.horizontalSpacing = 5;
        statusBarLayout.verticalSpacing = 0;

        statusBar.setLayout(statusBarLayout);

        GridData statusBarGridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        statusBarGridData.verticalIndent = 0;
        statusBarGridData.horizontalIndent = 0;
        statusBar.setLayoutData(statusBarGridData);

        // add status line manager

        StatusLineManager statusMgr = new StatusLineManager();
        statusMgr.createControl(statusBar);

        GridData c1Grid = new GridData();
        c1Grid.horizontalAlignment = SWT.FILL;
        c1Grid.verticalAlignment = SWT.BOTTOM;
        c1Grid.grabExcessHorizontalSpace = true;
        c1Grid.grabExcessVerticalSpace = false;
        statusMgr.getControl().setLayoutData(c1Grid);

        // add checkbox for limiting results

        GridData c2Grid = new GridData();
        c2Grid.horizontalAlignment = SWT.RIGHT;
        c2Grid.verticalAlignment = SWT.CENTER;
        c2Grid.grabExcessHorizontalSpace = false;
        c2Grid.grabExcessVerticalSpace = false;

        final Button limitResults = new Button(statusBar, SWT.CHECK);
        // this.limitResults = limitResults;

        limitResults.setText(Messages.getString("SQL_Limit_Rows_2"));
        limitResults.setSelection(true);
        limitResults.setLayoutData(c2Grid);

        // add input field for result limit

        GridData c3Grid = new GridData();
        c3Grid.horizontalAlignment = SWT.RIGHT;
        c3Grid.verticalAlignment = SWT.CENTER;
        c3Grid.grabExcessHorizontalSpace = false;
        c3Grid.grabExcessVerticalSpace = false;
        c3Grid.widthHint = 30;

        maxResultText = new Text(statusBar, SWT.BORDER | SWT.SINGLE);
        maxResultText.setText(IConstants.MAX_SQL_ROWS);
        maxResultText.setLayoutData(c3Grid);

        limitResults.addMouseListener(new MouseAdapter() {

            // enable/disable input field when checkbox is clicked
            public void mouseUp(MouseEvent e) {
                maxResultText.setEnabled(limitResults.getSelection());
                ifLimit = limitResults.getSelection();
            }
        });

    }

    /**
     * Initialize toolbar.
     */
    private void createToolBar() {
        // create coolbar
        coolBar = new CoolBar(this, SWT.NONE);
        coolBarMgr = new CoolBarManager(coolBar);

        GridData gid = new GridData();
        gid.horizontalAlignment = GridData.FILL;
        coolBar.setLayoutData(gid);

        // initialize default actions
        defaultToolBarMgr = new ToolBarManager(SWT.NONE);

        execSQLAction = new ExecSQLAction(SQLResultComposite.instance, this);

        openFileAction = new OpenFileAction();
        openFileAction.setEditor(this);

        saveAsAction = new SaveFileAsAction();
        saveAsAction.setEditor(this);

        clearTextAction = new ClearTextAction();
        clearTextAction.setEditor(this);

        addDefaultActions(defaultToolBarMgr);

        // initialize session actions

        sessionToolBarMgr = new ToolBarManager(SWT.NONE);

        sessionSwitcher = new SQLEditorSessionSwitcher(this);
        sessionToolBarMgr.add(sessionSwitcher);

        // initialize catalog actions

        // add all toolbars to parent coolbar
        coolBar.setLocked(true);
        coolBarMgr.add(new ToolBarContributionItem(defaultToolBarMgr));
        coolBarMgr.add(new ToolBarContributionItem(sessionToolBarMgr));

        coolBarMgr.update(true);

    }

    public void addResizeListener(ControlListener listener) {

        coolBar.addControlListener(listener);
    }

    private void addDefaultActions(ToolBarManager mgr) {
        mgr.removeAll();

        execSQLAction.setEnabled(!execSQLAction.isDisabled());

        execSQLAction.setEnabled(true);
        mgr.add(execSQLAction);
        mgr.add(openFileAction);
        mgr.add(saveAsAction);
        mgr.add(clearTextAction);
    }

    /**
     * Refresh actions availability on the toolbar.
     */
    public void refresh(final boolean sessionChanged) {

        this.getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {

                if (sessionChanged) {

                    // reset actions
                    addDefaultActions(defaultToolBarMgr);
                    defaultToolBarMgr.update(true);
                }

                // update session toolbar
                sessionToolBarMgr.update(true);

                coolBarMgr.update(true);
                coolBar.update();
            }
        });
    }

    /**
     */
    public SessionTreeNode getSessionTreeNode() {
        return this.sessionTreeNode;
    }

    /**
     * 
     */
    public boolean getIfLimit() {
        return ifLimit;
    }

    /**
     * 
     */
    public String getMaxResult() {
        return maxResultText.getText();
    }

    /**
     * 
     */
    public void setSessionTreeNode(SessionTreeNode node) {
        Assert.isNotNull(node, "this node can not be null");
        this.sessionTreeNode = node;
        // Refresh TabItem's title
        String dbName = ((SQLAlias) node.getAlias()).getSchemaFilterExpression();

        String repositoryName = getRepositoryName();
        String title = dbName + "(" + repositoryName + ").sql";
        tabItem.setText(title);
        sessionSwitcher.refreshSelectedRepository();
        createEditorProposal();
    }

    
    public String getRepositoryName() {
        if (sessionTreeNode == null) {
            return "";
        }
        String repositoryName = sessionTreeNode.getRepositoryName();
        return repositoryName;
    }
    
    public String getSQLToBeExecuted() {
        return colorText.getText();
        // return sqlTextViewer.getTextWidget().getText();
    }

    public void setEditorContent(String str) {
        this.colorText.setText(str);

    }

    public void doSaveAs() {
        FileDialog dialog = new FileDialog(this.getShell(), SWT.SAVE);
        dialog.setText(Messages.getString("SQLEditor.SaveAsDialog.Title"));
        dialog.setFilterExtensions(SUPPORTED_FILETYPES);
        dialog.setFilterNames(SUPPORTED_FILETYPES);
        dialog.setFileName(tabItem.getText());

        String path = dialog.open();
        if (path == null) {
            return;
        }
        BufferedWriter writer = null;
        try {

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();

            String content = colorText.getText();
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content, 0, content.length());

        } catch (Exception e) {

            UIUtils.openErrorDialog(Messages.getString("SQLEditor.SaveAsDialog.Error"), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void clearText() {
        this.colorText.setText("");

    }
}

