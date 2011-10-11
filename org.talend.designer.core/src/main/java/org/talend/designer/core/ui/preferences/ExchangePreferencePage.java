// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.general.Project;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.core.i18n.Messages;
import org.talend.repository.ProjectManager;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class ExchangePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public ExchangePreferencePage() {
        super(GRID);
        IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
        Project project = ProjectManager.getInstance().getCurrentProject();
        if (project.getAuthor() != null) {
            String connectionEmail = project.getAuthor().getLogin();

            String string = prefStore.getString(connectionEmail);
            if (string != null) {
                String[] split = string.split(":");
                if (split.length == 3) {
                    prefStore.setValue(ITalendCorePrefConstants.EXCHANGE_USER_NAME, split[1]);
                    prefStore.setValue(ITalendCorePrefConstants.EXCHANGE_USER_PASSWORD, split[2]);
                }
            }
        }
        setPreferenceStore(prefStore);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {

        StringFieldEditor userNameTitle = new StringFieldEditor(ITalendCorePrefConstants.EXCHANGE_USER_NAME,
                Messages.getString("ExchangePreferencePage.userNameTitle"), getFieldEditorParent());
        addField(userNameTitle);

        StringFieldEditor userPasswordTitle = new StringFieldEditor(ITalendCorePrefConstants.EXCHANGE_USER_PASSWORD,
                Messages.getString("ExchangePreferencePage.userPasswordTitle"), getFieldEditorParent());
        Text textControl = userPasswordTitle.getTextControl(getFieldEditorParent());
        textControl.setEchoChar('*');
        addField(userPasswordTitle);

        BooleanFieldEditor downloadedCheckUpdates = new BooleanFieldEditor(
                ITalendCorePrefConstants.EXCHANGE_DOWNLOADED_CHECK_UPDATES,
                Messages.getString("ExchangePreferencePage.exchangeCheckUpdates"), getFieldEditorParent()); //$NON-NLS-1$
        addField(downloadedCheckUpdates);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
        Project project = ProjectManager.getInstance().getCurrentProject();
        if (project.getAuthor() != null) {
            String connectionEmail = project.getAuthor().getLogin();
            prefStore.setValue(
                    connectionEmail,
                    connectionEmail + ":" + prefStore.getString(ITalendCorePrefConstants.EXCHANGE_USER_NAME) + ":"
                            + prefStore.getString(ITalendCorePrefConstants.EXCHANGE_USER_PASSWORD));
        }
        return super.performOk();
    }

}
