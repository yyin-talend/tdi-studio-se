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
package org.talend.repository.ui.swt.utils;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.prefs.ITalendCorePrefConstants;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractPositionalFileStepForm extends AbstractForm {

    protected static final int MAXIMUM_ROWS_TO_PREVIEW = CorePlugin.getDefault().getPreferenceStore().getInt(
            ITalendCorePrefConstants.PREVIEW_LIMIT);

    protected ConnectionItem connectionItem;

    protected static PositionalFileConnection connection;

    /**
     * DOC ocarbone AbstractPositionalFileStepForm constructor comment.
     * 
     * @param parent
     * @param connection
     * @param existingNames
     * @param originalName
     */
    public AbstractPositionalFileStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
    }

    /**
     * DOC ocarbone AbstractPositionalFileStepForm constructor comment.
     * 
     * @param parent
     * @param connection
     * @param existingNames
     * @param originalName
     */
    public AbstractPositionalFileStepForm(Composite parent, ConnectionItem connectionItem) {
        this(parent, connectionItem, null);
    }

    /**
     * DOC tguiu AbstractDelimitedFileStepForm constructor comment. Use to step1
     */
    public AbstractPositionalFileStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
    }

    /**
     * DOC ocarbone Comment method "checkPositionalFieldSeparatorValue". return a cleaned value of FieldSeparatorValue
     * 
     * @param value
     * @return value
     */
    protected String getValidateFieldPosition(String value) {
        // Character must be number or comma
        String newValue = "";
        if (!"".equals(value)) {
            for (int f = 0; f < value.length(); f++) {
                char c = value.charAt(f);
                String s1 = "" + value.charAt(f);
                if (Character.isDigit(c) || (s1.equals(","))) {
                    newValue = newValue + s1;
                }
            }
            // Character must be sorted
            String[] newValues = newValue.split(",");
            Integer[] newIntValues = new Integer[newValues.length];
            for (int f = 0; f < newValues.length; f++) {
                newIntValues[f] = new Integer(newValues[f]);
            }
            Arrays.sort(newIntValues);
            newValue = "";
            for (int f = 0; f < newIntValues.length; f++) {
                newValue = newValue + new String("" + newIntValues[f]);
                if (f < newIntValues.length - 1) {
                    newValue = newValue + ",";
                }
            }
        }
        return newValue;
    }

    /**
     * DOC ocarbone Comment method "charIsAcceptedOnFieldSeparatorValue".
     * 
     * @param string
     * @param character
     * @param position
     * @return boolean
     */
    protected boolean charIsAcceptedOnFieldPosition(final String string, final char character, final int position) {
        if ((Character.getType(character) == 15) || Character.isDigit(character) || (character) == Character.valueOf(',')) {
            // Check unique comma
            if ((character) == Character.valueOf(',')) {
                if (position > 0) {
                    if (string.substring(position - 1, position).equals(",")) {
                        return false;
                    }
                    if (position + 1 < string.length()) {
                        if (string.substring(position, position + 1).equals(",")) {
                            return false;
                        }
                    }
                } else if (position == 0) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * DOC ocarbone Comment method "charIsAcceptedOnFieldSeparatorValue".
     * 
     * @param string
     * @param character
     * @param position
     * @return boolean
     */
    protected boolean charIsAcceptedOnFieldSeparator(final String string, final char character, final int position) {
        if (string.lastIndexOf("*") > -1) {
            if ((Character.isDigit(character) || character == Character.valueOf(','))) {
                // after *, nothing must be insered
                if (string.lastIndexOf("*") < position) {
                    return false;
                }
            }
        }
        if ((Character.getType(character) == 15) || Character.isDigit(character) || (character) == Character.valueOf(',')) {
            // Check unique comma
            if ((character) == Character.valueOf(',')) {
                if (position > 0) {
                    if (string.substring(position - 1, position).equals(",")) {
                        return false;
                    }
                    if (position + 1 < string.length()) {
                        if (string.substring(position, position + 1).equals(",")) {
                            return false;
                        }
                    }
                } else if (position == 0) {
                    return false;
                }
            }
        } else if ((character) == Character.valueOf('*')) {
            // Check unique *
            if (string.lastIndexOf("*") > 0) {
                return false;
            }
            // Check * is in the last position
            if ((position < string.length())) {
                return false;
            }

        } else {
            return false;
        }

        return true;
    }

    /**
     * DOC ocarbone Comment method "checkPositionalFieldSeparatorValue". return a cleaned value of FieldSeparatorValue
     * 
     * @param value
     * @return value
     */
    protected String getValidateFieldSeparator(String value) {
        // if needed delete last comma
        if (value.lastIndexOf(",") == value.length() - 1) {
            value = value.substring(0, value.length() - 1);
        }

        // Each Character must be integer comma or *
        if (!"".equals(value)) {
            String val = "";
            for (int f = 0; f < value.length(); f++) {
                char c = value.charAt(f);
                String s1 = "" + value.charAt(f);
                if (Character.isDigit(c) || (s1.equals(",")) || (s1.equals("*"))) {
                    val = val + s1;
                }
            }
            value = val;
        }
        // if needed add comma before *
        if ((value.lastIndexOf("*") > -1) && (!value.equals("*"))) {
            if (!value.substring((value.lastIndexOf("*") - 1), value.lastIndexOf("*")).equals(",")) {
                value = value.substring(0, value.lastIndexOf("*")) + ",*";
            }
            // Character * must be the last char
            if ((value.lastIndexOf("*") < value.length() - 1)) {
                value = value.substring(0, value.lastIndexOf("*"));
            }
        }
        return value;
    }

    protected PositionalFileConnection getConnection() {
        return (PositionalFileConnection) connectionItem.getConnection();
    }
}
