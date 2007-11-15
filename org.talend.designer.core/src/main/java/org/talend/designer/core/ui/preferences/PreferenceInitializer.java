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

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.TalendEditorPaletteFactory;

/**
 * Preference Initializer for the designer.
 * 
 * $Id$
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    private static final String DEFAULT_LOGS_FILE_NAME = "logs_file.txt";

    private static final String DEFAULT_STATS_FILE_NAME = "stats_file.txt";

    private static final String DEFAULT_METTER_FILE_NAME = "metter_file.txt";

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = DesignerPlugin.getDefault().getPreferenceStore();

        String logPath = Platform.getLogFileLocation().toOSString();
        int lastIndex = logPath.lastIndexOf(File.separatorChar);
        logPath = logPath.substring(0, lastIndex);

        store.setDefault(TalendDesignerPrefConstants.DEFAULT_LABEL, "__UNIQUE_NAME__"); //$NON-NLS-1$
        store.setDefault(TalendDesignerPrefConstants.DEFAULT_HINT, "<b>__UNIQUE_NAME__</b><br>__COMMENT__"); //$NON-NLS-1$
        store.setDefault(TalendDesignerPrefConstants.DEFAULT_CONNECTION_FORMAT, "row");//$NON-NLS-1$
        store.setDefault(TalendDesignerPrefConstants.DEFAULT_HINT_USED, false);
        store.setDefault(TalendDesignerPrefConstants.DEFAULT_DISPLAY, false);
        store.setDefault(TalendEditorPaletteFactory.PALETTE_STATE, FlyoutPaletteComposite.STATE_PINNED_OPEN);
        store.setDefault(TalendDesignerPrefConstants.COMP_DEFAULT_FILE_DIR, Platform.getLocation().toOSString());
        store.setDefault(TalendDesignerPrefConstants.PROPERTY_CODE_CHECK, false);
        store.setDefault(TalendDesignerPrefConstants.LARGE_ICONS_SIZE, "24");

        // defaults for the stats preferences for java
        store.setDefault(ECodeLanguage.JAVA.toString() + "_" + EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
        store.setDefault(ECodeLanguage.JAVA.toString() + "_" + EParameterName.CATCH_REALTIME_STATS.getName(), false);
        store.setDefault(ECodeLanguage.JAVA.toString() + "_" + EParameterName.CATCH_RUNTIME_ERRORS.getName(), true);
        store.setDefault(ECodeLanguage.JAVA.toString() + "_" + EParameterName.CATCH_USER_ERRORS.getName(), true);
        store.setDefault(ECodeLanguage.JAVA.toString() + "_" + EParameterName.CATCH_USER_WARNING.getName(), true);

        store.setDefault(ECodeLanguage.JAVA.toString() + "_" + EParameterName.FILE_PATH.getName(), logPath);
        store.setDefault(ECodeLanguage.JAVA.toString() + "_" + EParameterName.FILENAME_LOGS.getName(), DEFAULT_LOGS_FILE_NAME);
        store.setDefault(ECodeLanguage.JAVA.toString() + "_" + EParameterName.FILENAME_STATS.getName(), DEFAULT_STATS_FILE_NAME);
        store
                .setDefault(ECodeLanguage.JAVA.toString() + "_" + EParameterName.FILENAME_METTER.getName(),
                        DEFAULT_METTER_FILE_NAME);

        // defaults for the stats preferences for perl
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.CATCH_REALTIME_STATS.getName(), false);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.CATCH_RUNTIME_ERRORS.getName(), true);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.CATCH_USER_ERRORS.getName(), true);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.CATCH_USER_WARNING.getName(), true);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.FILE_PATH.getName(), logPath);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.FILENAME_LOGS.getName(), DEFAULT_LOGS_FILE_NAME);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.FILENAME_STATS.getName(), DEFAULT_STATS_FILE_NAME);
        store
                .setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.FILENAME_METTER.getName(),
                        DEFAULT_METTER_FILE_NAME);

        if (!CorePlugin.getContext().isHeadless()) {
            Font font = new Font(Display.getDefault(), "courier", 10, SWT.NONE);
            store.setDefault(TalendDesignerPrefConstants.MEMO_TEXT_FONT, font.getFontData()[0].toString());
            store.setDefault(TalendDesignerPrefConstants.EDITOR_ANTIALIASING, false);
            // store.setDefault(TalendDesignerPrefConstants.EDITOR_INTERPOLATION, false);
        }
    }
}
