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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.talend.repository.RepositoryPlugin;

/**
 * The HistoryUtils are used to save and load the history of input fields.
 * 
 * @author ftang, 18/09/2007
 */
public class HistoryUtils {

    /**
     * Saves the the given value under the given key in the dialog settings.
     * 
     * @param key the key
     * @param value the value
     */
    public static void save(String key, String value) {
        // get current history
        String[] history = load(key);
        List<String> list = new ArrayList<String>(Arrays.asList(history));

        // add new value or move to first position
        if (list.contains(value)) {
            list.remove(value);
        }
        list.add(0, value);

        // check history size
        while (list.size() > 20) {
            list.remove(list.size() - 1);
        }

        // save
        history = list.toArray(new String[list.size()]);
        RepositoryPlugin.getDefault().getDialogSettings().put(key, history);

    }

    /**
     * Loads the value of the given key from the dialog settings
     * 
     * @param key the key
     * @return the value
     */
    public static String[] load(String key) {
        String[] history = RepositoryPlugin.getDefault().getDialogSettings().getArray(key);
        if (history == null) {
            history = new String[0];
        }
        return history;
    }

}
