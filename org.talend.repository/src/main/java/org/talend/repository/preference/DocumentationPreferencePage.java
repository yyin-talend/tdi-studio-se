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
package org.talend.repository.preference;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.CorePlugin;
import org.talend.core.PluginChecker;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.repository.i18n.Messages;

/**
 * ftang class global comment. Detailed comment <br/>
 * 
 */
public class DocumentationPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    // private BooleanFieldEditor booleanFieldEditor;

    private boolean isDocumentationPluginLoaded = true;

    private boolean isHidePassords = true, isGenerateSourcecode = false;

    private BooleanFieldEditor passwordHideFieldEditor;

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
        // booleanFieldEditor = new BooleanFieldEditor(ITalendCorePrefConstants.DOC_GENERATION, Messages
        //                .getString("DocumentationPreferencePage.autoUpdateDoc"), getFieldEditorParent()); //$NON-NLS-1$
        passwordHideFieldEditor = new BooleanFieldEditor(ITalendCorePrefConstants.DOC_HIDEPASSWORDS, Messages
                .getString("DocumentationPreferencePage.hidePwd"), getFieldEditorParent()); //$NON-NLS-1$
        sourceCodeGenFieldEditor = new BooleanFieldEditor(ITalendCorePrefConstants.DOC_GENERATESOURCECODE, Messages
                .getString("DocumentationPreferencePage.sourceCodeToHTML"), getFieldEditorParent()); //$NON-NLS-1$
        // addField(booleanFieldEditor);
        addField(passwordHideFieldEditor);
        addField(sourceCodeGenFieldEditor);
        // booleanFieldEditor.setEnabled(isDocumentationPluginLoaded, getFieldEditorParent());

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
