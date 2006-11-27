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
package org.talend.sqlbuilder.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.sqlbuilder.IConstants;
import org.talend.sqlbuilder.SqlBuilderPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	public void initializeDefaultPreferences() {
		IPreferenceStore store = SqlBuilderPlugin.getDefault()
				.getPreferenceStore();

		// for PassPolicy Preference
		store.setDefault(IConstants.CLIP_EXPORT_SEPARATOR, "|");
		store.setDefault(IConstants.DATASETRESULT_DATE_FORMAT, "yyyy-MM-dd");
		store.setDefault(IConstants.DATASETRESULT_FORMAT_DATES, true);
		store.setDefault(IConstants.SQL_ASSIST, false);

		store.setDefault(IConstants.LINE_DELIMITER, "\n");

		store.setDefault(IConstants.COMMENT_DELIMITER, "#");

		store.setDefault(IConstants.ALTERNATE_DELIMITER, "");
		
		store.setDefault(IConstants.QUERY_DELIMITER, ";");

		store.setDefault(IConstants.WARN_RESEULTS, 5000);
	}
}
