// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.CorePlugin;
import org.talend.core.PluginChecker;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.core.i18n.Messages;

/**
 * ftang class global comment. Detailed comment <br/>
 * 
 */
public class DocumentationPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private boolean isDocumentationPluginLoaded = true;

    private BooleanFieldEditor sourceCodeGenFieldEditor;

    /**
     * ftang DocumentationPreferencePage constructor comment.
     */
    public DocumentationPreferencePage() {
        super(GRID);
        setPreferenceStore(CorePlugin.getDefault().getPreferenceStore());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {
        sourceCodeGenFieldEditor = new BooleanFieldEditor(
                ITalendCorePrefConstants.DOC_GENERATESOURCECODE,
                org.talend.repository.i18n.Messages.getString("DocumentationPreferencePage.sourceCodeToHTML"), getFieldEditorParent()); //$NON-NLS-1$
        addField(sourceCodeGenFieldEditor);

        // see the bug 7073,qli
        if (isDocumentationPluginLoaded) {
            BooleanFieldEditor booleanFieldEditor = new BooleanFieldEditor(ITalendCorePrefConstants.DOC_GENERATION, Messages
                    .getString("PerformancePreferencePage.autoUpdateDoc"), getFieldEditorParent()); //$NON-NLS-1$
            addField(booleanFieldEditor);

            FileFieldEditor documentationLogo = new FileFieldEditor(ITalendCorePrefConstants.DOC_USER_LOGO, Messages
                    .getString("DocumentationPreferencePage.userDocLogo"), getFieldEditorParent());
            addField(documentationLogo);

            StringFieldEditor companyName = new StringFieldEditor(ITalendCorePrefConstants.DOC_COMPANY_NAME, Messages
                    .getString("DocumentationPreferencePage.companyName"), getFieldEditorParent());
            addField(companyName);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
        isDocumentationPluginLoaded = PluginChecker.isDocumentationPluginLoaded();
    }

}
