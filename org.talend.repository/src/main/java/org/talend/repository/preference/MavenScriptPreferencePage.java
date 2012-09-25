// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
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

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.RepositoryManager;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class MavenScriptPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public MavenScriptPreferencePage() {
        super(FLAT);
        setPreferenceStore(RepositoryManager.getPreferenceStore());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite p) {
        Composite parent = (Composite) super.createContents(p);

        Label scriptLabel = new Label(parent, SWT.NONE);

        scriptLabel.setText("Script");
        scriptLabel.setLayoutData(new GridData());

        final StyledText scriptTxt = new StyledText(parent, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scriptTxt.setLayoutData(new GridData(GridData.FILL_BOTH));
        scriptTxt.setText(getPreferenceStore().getString(IRepositoryPrefConstants.MAVEN_SCRIPT_TEMPLATE));

        scriptTxt.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getPreferenceStore().setValue(IRepositoryPrefConstants.MAVEN_SCRIPT_TEMPLATE,
                        ((StyledText) e.getSource()).getText().trim());
            }
        });

        return parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

}
