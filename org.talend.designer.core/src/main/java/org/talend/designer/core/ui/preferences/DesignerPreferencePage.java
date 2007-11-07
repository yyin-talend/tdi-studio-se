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
package org.talend.designer.core.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.TalendEditor;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing
 * <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that allows us to create a
 * page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the preference store.
 */

public class DesignerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    String oldLargeIconsSize;

    public DesignerPreferencePage() {
        super(GRID);
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    public void init(IWorkbench workbench) {
        oldLargeIconsSize = getPreferenceStore().getString(TalendDesignerPrefConstants.LARGE_ICONS_SIZE);
    }

    @Override
    protected void performApply() {
        super.performApply();

        checkPropertyChanged();
    }

    @Override
    public boolean performOk() {
        final boolean toReturn = super.performOk();
        checkPropertyChanged();
        return toReturn;
    }

    private void checkPropertyChanged() {
        if (!oldLargeIconsSize.equals(getPreferenceStore().getString(TalendDesignerPrefConstants.LARGE_ICONS_SIZE))) {
            TalendEditor.resetPaletteRoot();
        }
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    @Override
    public void createFieldEditors() {
        StringFieldEditor labelField;
        StringFieldEditor hintField;
        StringFieldEditor connectionField;
        BooleanFieldEditor showHint;
        BooleanFieldEditor displayComponent;

        labelField = new StringFieldEditor(TalendDesignerPrefConstants.DEFAULT_LABEL, Messages
                .getString("DesignerPreferencePage.component.defaultLabel"), //$NON-NLS-1$
                getFieldEditorParent());
        hintField = new StringFieldEditor(TalendDesignerPrefConstants.DEFAULT_HINT, Messages
                .getString("DesignerPreferencePage.component.defaultHint"), //$NON-NLS-1$
                getFieldEditorParent());
        connectionField = new StringFieldEditor(TalendDesignerPrefConstants.DEFAULT_CONNECTION_FORMAT, "Components default connection",
                getFieldEditorParent());
        showHint = new BooleanFieldEditor(TalendDesignerPrefConstants.DEFAULT_HINT_USED, Messages
                .getString("DesignerPreferencePage.hintShowed"), //$NON-NLS-1$
                getFieldEditorParent());
        displayComponent = new BooleanFieldEditor(TalendDesignerPrefConstants.DEFAULT_DISPLAY, Messages
                .getString("DesignerPreferencePage.display.hiddenComponents"), getFieldEditorParent()); //$NON-NLS-1$

        addField(labelField);
        addField(hintField);
        addField(connectionField);
        addField(showHint);
        addField(displayComponent);

        DirectoryFieldEditor compDefaultFileDir = new DirectoryFieldEditor(TalendDesignerPrefConstants.COMP_DEFAULT_FILE_DIR,
                Messages.getString("DesignerPreferencePage.defaultFilePathDirectory"), getFieldEditorParent()); //$NON-NLS-1$
        addField(compDefaultFileDir);

        addField(new BooleanFieldEditor(TalendDesignerPrefConstants.PROPERTY_CODE_CHECK, Messages
                .getString("DesignerPreferencePage.propertyCodeCheck"), getFieldEditorParent())); //$NON-NLS-1$

        RadioGroupFieldEditor largeIconsSizeField = new RadioGroupFieldEditor(TalendDesignerPrefConstants.LARGE_ICONS_SIZE,
                Messages.getString("DesignerPreferencePage.largeIconsSize"), 2, new String[][] { { "24 pixels", "" + 24 }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        { "32 pixels", "" + 32 } }, getFieldEditorParent()); //$NON-NLS-1$ //$NON-NLS-2$
        addField(largeIconsSizeField);

        BooleanFieldEditor antialiasing;
        antialiasing = new BooleanFieldEditor(TalendDesignerPrefConstants.EDITOR_ANTIALIASING,
                "enhanced using antialiasing and interpolation in the Job Designer", getFieldEditorParent()); //$NON-NLS-1$
        addField(antialiasing);
        // BooleanFieldEditor interpolation;
        // interpolation = new BooleanFieldEditor(TalendDesignerPrefConstants.EDITOR_INTERPOLATION,
        // "interpolation in the Job Designer", getFieldEditorParent());
        // addField(interpolation);
    }
}
