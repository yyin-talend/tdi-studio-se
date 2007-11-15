// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
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

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.designer.core.DesignerPlugin;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class StatsAndLogsPreferenceNodePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public StatsAndLogsPreferenceNodePage() {
        super(GRID);
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void performApply() {
        super.performApply();
    }

    public void createFieldEditors() {
    }

    public void init(IWorkbench workbench) {
    }
}
