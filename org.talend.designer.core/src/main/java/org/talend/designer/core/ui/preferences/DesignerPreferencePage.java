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
package org.talend.designer.core.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.designer.core.DesignerPlugin;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing
 * <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that allows us to create a
 * page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the preference store.
 */

public class DesignerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public DesignerPreferencePage() {
        super(GRID);
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        StringFieldEditor labelField;
        StringFieldEditor hintField;
        BooleanFieldEditor showHint;

        labelField = new StringFieldEditor(TalendDesignerPrefConstants.DEFAULT_LABEL, "Components default label",
                getFieldEditorParent());
        hintField = new StringFieldEditor(TalendDesignerPrefConstants.DEFAULT_HINT, "Components default hint",
                getFieldEditorParent());
        showHint = new BooleanFieldEditor(TalendDesignerPrefConstants.DEFAULT_HINT_USED, "Components hint showed",
                getFieldEditorParent());
        addField(labelField);
        addField(hintField);
        addField(showHint);
    }

    public void init(IWorkbench workbench) {
    }

}
