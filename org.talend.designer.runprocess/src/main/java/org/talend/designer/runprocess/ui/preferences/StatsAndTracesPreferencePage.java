// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.runprocess.ui.preferences;

import org.eclipse.gmf.runtime.common.ui.preferences.CheckBoxFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.language.ECodeLanguage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public abstract class StatsAndTracesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private ECodeLanguage language;

    private String languagePrefix;

    /** controls. * */
    private CheckBoxFieldEditor onSavebeforeField;

    private CheckBoxFieldEditor onClearbeforeField;

    private CheckBoxFieldEditor onExecTimeField;

    private CheckBoxFieldEditor onStatisticsField;

    private CheckBoxFieldEditor onTracesField;

    private Composite parent;

    /**
     * Default constructor.
     */
    public StatsAndTracesPreferencePage(ECodeLanguage language) {
        super(GRID);
        this.language = language;
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        languagePrefix = language.toString() + "_";
        createFields();

    }

    private void createFields() {
        parent = getFieldEditorParent();
        int languageType = language == ECodeLanguage.JAVA ? 1 : 0;

        Composite titlePart = new Composite(parent, SWT.None);
        titlePart.setLayout(new GridLayout());

        onSavebeforeField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_SAVEBEFORE_FLAG.getName(),
                EParameterName.ON_SAVEBEFORE_FLAG.getDisplayName(), titlePart);
        onClearbeforeField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_CLEARBEFORE_FLAG.getName(),
                EParameterName.ON_CLEARBEFORE_FLAG.getDisplayName(), titlePart);
        onExecTimeField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_EXECTIME_FLAG.getName(),
                EParameterName.ON_EXECTIME_FLAG.getDisplayName(), titlePart);
        onStatisticsField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_STATISTICS_FLAG.getName(),
                EParameterName.ON_STATISTICS_FLAG.getDisplayName(), titlePart);
        onTracesField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_TRACES_FLAG.getName(),
                EParameterName.ON_TRACES_FLAG.getDisplayName(), titlePart);
        addField(onStatisticsField);
        addField(onTracesField);
        addField(onExecTimeField);
        addField(onSavebeforeField);
        addField(onClearbeforeField);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        super.performDefaults();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

}
