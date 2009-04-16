// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.ui;

import org.eclipse.gmf.runtime.common.ui.preferences.CheckBoxFieldEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.commons.utils.workbench.preferences.ComboFieldEditor;
import org.talend.core.PluginChecker;
import org.talend.designer.components.ComponentsLocalProviderPlugin;
import org.talend.designer.components.i18n.Messages;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing
 * <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the preference store.
 */
public class ComponentsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    // private IntegerFieldEditor rowLimit;

    private IntegerFieldEditor fClientComPortEditor = null;

    private CheckBoxFieldEditor doNotShowJobAfterDoubleClickCheckBoxField;

    private DirectoryFieldEditor filePathTemp;

    private static final String TITLE = "tRunJob"; //$NON-NLS-1$

    /**
     * This class exists to provide visibility to the <code>refreshValidState</code> method and to perform more
     * intelligent clearing of the error message.
     */
    protected class ConsoleIntegerFieldEditor extends IntegerFieldEditor {

        public ConsoleIntegerFieldEditor(String name, String labelText, Composite parent) {
            super(name, labelText, parent);
        }

        /**
         * @see org.eclipse.jface.preference.FieldEditor#refreshValidState()
         */
        protected void refreshValidState() {
            super.refreshValidState();
        }

        /**
         * Clears the error message from the message line if the error message is the error message from this field
         * editor.
         */
        protected void clearErrorMessage() {
            if (canClearErrorMessage()) {
                super.clearErrorMessage();
            }
        }
    }

    public ComponentsPreferencePage() {
        super(GRID);
        setPreferenceStore(ComponentsLocalProviderPlugin.getDefault().getPreferenceStore());
    }

    public void createFieldEditors2() {

        LINK_STYLE[] linkStyles = LINK_STYLE.values();
        String[][] strComboValues = new String[linkStyles.length][2];

        for (int i = 0; i < linkStyles.length; i++) {
            strComboValues[i][0] = linkStyles[i].getDisplayName();
            strComboValues[i][1] = linkStyles[i].getName();
        }

        ComboFieldEditor dbTypeField = new ComboFieldEditor(IComponentPreferenceConstant.LINK_STYLE, Messages
                .getString("ComponentsPreferencePage.configuration.LINK_STYLE"), strComboValues, getFieldEditorParent()); //$NON-NLS-1$
        addField(dbTypeField);
    }

    public void propertyChange(PropertyChangeEvent event) {

        if (event.getSource() == filePathTemp) {
            propertyChangeForComponents(event);
        } else {
            Object nValue = event.getNewValue();
            if (event.getProperty().equals(FieldEditor.IS_VALID)) {
                boolean newValue = ((Boolean) nValue).booleanValue();
                if (newValue) {
                    checkState();
                } else {
                    super.propertyChange(event);
                }

            } else {
                super.propertyChange(event);
            }
        }
    }

    protected void createSpacer(Composite composite, int columnSpan) {
        Label label = new Label(composite, SWT.NONE);
        GridData gd = new GridData();
        gd.horizontalSpan = columnSpan;
        label.setLayoutData(gd);
    }

    protected boolean canClearErrorMessage() {
        return true;
    }

    @Override
    protected void performApply() {
        // TODO Auto-generated method stub
        super.performApply();
    }

    public void createFieldEditors() {
        filePathTemp = new DirectoryFieldEditor(IComponentPreferenceConstant.USER_COMPONENTS_FOLDER, Messages
                .getString("ComponentsPreferencePage.directoryFieldLabel"), //$NON-NLS-1$
                getFieldEditorParent());
        addField(filePathTemp);
        if (PluginChecker.isPreviewPluginLoaded()) {
            IntegerFieldEditor rowLimit = new IntegerFieldEditor(IComponentPreferenceConstant.LIMIT, Messages
                    .getString("ComponentsPreferencePage.rowLimit"), //$NON-NLS-1$
                    getFieldEditorParent());
            addField(rowLimit);
        }
        createFieldEditors2();

        doNotShowJobAfterDoubleClickCheckBoxField = new CheckBoxFieldEditor(IComponentPreferenceConstant.IS_AVOID, Messages
                .getString("ComponenttRunJobPreferencePage.label"), getFieldEditorParent()); //$NON-NLS-1$

        addField(doNotShowJobAfterDoubleClickCheckBoxField);
    }

    public void propertyChangeForComponents(PropertyChangeEvent event) {

        MessageDialog warningMessageDialog = new MessageDialog(getFieldEditorParent().getShell(), Messages
                .getString("ComponentsPreferencePage.WarningTitle"), null, //$NON-NLS-1$
                Messages.getString("ComponentsPreferencePage.WarningMsg"), MessageDialog.WARNING, //$NON-NLS-1$
                new String[] { Messages.getString("ComponentsPreferencePage.ButtonLabel0") }, 0); //$NON-NLS-1$
        warningMessageDialog.open();

    }

    public void init(IWorkbench workbench) {
    }

}
