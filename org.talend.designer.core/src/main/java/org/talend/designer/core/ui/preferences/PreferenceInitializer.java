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

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = DesignerPlugin.getDefault().getPreferenceStore();

        store.setDefault(TalendDesignerPrefConstants.DEFAULT_LABEL, "__UNIQUE_NAME__"); //$NON-NLS-1$
        store.setDefault(TalendDesignerPrefConstants.DEFAULT_HINT, "<b>__UNIQUE_NAME__</b><br>__COMMENT__"); //$NON-NLS-1$
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

        // defaults for the stats preferences for perl
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.CATCH_REALTIME_STATS.getName(), false);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.CATCH_RUNTIME_ERRORS.getName(), true);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.CATCH_USER_ERRORS.getName(), true);
        store.setDefault(ECodeLanguage.PERL.toString() + "_" + EParameterName.CATCH_USER_WARNING.getName(), true);

        Font font = new Font(Display.getDefault(), "courier", 10, SWT.NONE);
        store.setDefault(TalendDesignerPrefConstants.MEMO_TEXT_FONT, font.getFontData()[0].toString());
        store.setDefault(TalendDesignerPrefConstants.EDITOR_ANTIALIASING, false);
        // store.setDefault(TalendDesignerPrefConstants.EDITOR_INTERPOLATION, false);
    }

}
