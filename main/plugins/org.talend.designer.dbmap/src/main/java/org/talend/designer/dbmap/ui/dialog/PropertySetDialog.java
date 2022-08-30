// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dbmap.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.components.IComponent;
import org.talend.designer.dbmap.i18n.Messages;
import org.talend.designer.dbmap.language.generation.DbGenerationManager;
import org.talend.designer.dbmap.managers.MapperManager;
/**
 * 
 * created by hcyi on Nov 24, 2020 Detailled comment
 *
 */
public class PropertySetDialog extends Dialog {

    private MapperManager mapperManager;

    private DbGenerationManager generationManager;

    private Button addQuotesInColumnsButton;
    
    private Button addQuotesInTableNamesButton;

    private Button useAliasButton;

    private final Color color = new Color(Display.getDefault(), 238, 238, 0);

    public PropertySetDialog(Shell parentShell, MapperManager mapperManager) {
        super(parentShell);
        this.mapperManager = mapperManager;
        generationManager = mapperManager.getComponent().getGenerationManager();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginLeft = 10;
        gridLayout.marginTop = 20;
        gridLayout.marginHeight = 0;
        container.setLayout(gridLayout);

        addQuotesInColumnsButton = new Button(container, SWT.CHECK);
        addQuotesInColumnsButton.setText(Messages.getString("PropertySetDialog.add_quotes_in_columns.title")); //$NON-NLS-1$

        addQuotesInTableNamesButton = new Button(container, SWT.CHECK);
        addQuotesInTableNamesButton.setText(Messages.getString("PropertySetDialog.add_quotes_in_table_names.title"));//$NON-NLS-1$
        
        useAliasButton = new Button(container, SWT.CHECK);
        String useAliasTitle = Messages.getString("PropertySetDialog.useAlias.title");//$NON-NLS-1$
        IComponent component = mapperManager.getComponent().getComponent();
        if (component != null && "tELTOracleMap".equals(component.getName())) { //$NON-NLS-1$
            useAliasTitle = Messages.getString("PropertySetDialog.useAlias.oracle.title");//$NON-NLS-1$
        }
        useAliasButton.setText(useAliasTitle);

        init();
        addListener();
        updateStatus();
        return container;
    }

    private void init() {
        addQuotesInColumnsButton.setSelection(generationManager.isAddQuotesInColumns());
        addQuotesInTableNamesButton.setSelection(generationManager.isAddQuotesInTableNames());
        useAliasButton.setSelection(generationManager.isUseAliasInOutputTable());

        // Implement the column alias only for tELTTeradataMap/tELTOracleMap now.
        boolean enabled = false;
        IComponent component = mapperManager.getComponent().getComponent();
        if (component != null && ("tELTTeradataMap".equals(component.getName()) || "tELTOracleMap".equals(component.getName()))) { //$NON-NLS-1$ //$NON-NLS-2$
            enabled = true;
        }
        useAliasButton.setVisible(enabled);
    }

    private void addListener() {
        addQuotesInColumnsButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                mapperManager.setAddQuotesInColumns(addQuotesInColumnsButton.getSelection());
                updateStatus();
            }
        });
        useAliasButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                mapperManager.useAliasInOutputTable(useAliasButton.getSelection());
                updateStatus();
            }
        });
        addQuotesInTableNamesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                mapperManager.setAddQuotesInColumns(addQuotesInTableNamesButton.getSelection());
                updateStatus();
            }
        });
    }

    private void updateStatus() {
        if (generationManager.isAddQuotesInColumns() == addQuotesInColumnsButton.getSelection()) {
            addQuotesInColumnsButton.setBackground(null);
        } else {
            addQuotesInColumnsButton.setBackground(color);
        }
        if (generationManager.isAddQuotesInTableNames() == addQuotesInTableNamesButton.getSelection()) {
            addQuotesInTableNamesButton.setBackground(null);
        } else {
            addQuotesInTableNamesButton.setBackground(color);
        }
        if (generationManager.isUseAliasInOutputTable() == useAliasButton.getSelection()) {
            useAliasButton.setBackground(null);
        } else {
            useAliasButton.setBackground(color);
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(500, 300);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("PropertySetDialog.title")); //$NON-NLS-1$
    }

    @Override
    protected void okPressed() {
        mapperManager.setAddQuotesInColumns(addQuotesInColumnsButton.getSelection());
        mapperManager.useAliasInOutputTable(useAliasButton.getSelection());
        super.okPressed();
    }
}
