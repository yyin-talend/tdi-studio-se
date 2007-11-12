// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
import org.talend.repository.i18n.Messages;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractPositionalFileStepForm extends AbstractForm {

    protected int maximumRowsToPreview = CorePlugin.getDefault().getPreferenceStore().getInt(
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
        String newValue = ""; //$NON-NLS-1$
        if (!"".equals(value)) { //$NON-NLS-1$
            for (int f = 0; f < value.length(); f++) {
                char c = value.charAt(f);
                String s1 = "" + value.charAt(f); //$NON-NLS-1$
                if (Character.isDigit(c) || (s1.equals(","))) { //$NON-NLS-1$
                    newValue = newValue + s1;
                }
            }
            // Character must be sorted
            String[] newValues = newValue.split(","); //$NON-NLS-1$
            Integer[] newIntValues = new Integer[newValues.length];
            for (int f = 0; f < newValues.length; f++) {
                newIntValues[f] = new Integer(newValues[f]);
            }
            Arrays.sort(newIntValues);
            newValue = ""; //$NON-NLS-1$
            for (int f = 0; f < newIntValues.length; f++) {
                newValue = newValue + new String("" + newIntValues[f]); //$NON-NLS-1$
                if (f < newIntValues.length - 1) {
                    newValue = newValue + ","; //$NON-NLS-1$
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
                    if (string.substring(position - 1, position).equals(",")) { //$NON-NLS-1$
                        return false;
                    }
                    if (position + 1 < string.length()) {
                        if (string.substring(position, position + 1).equals(",")) { //$NON-NLS-1$
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
        if (string.lastIndexOf("*") > -1) { //$NON-NLS-1$
            if ((Character.isDigit(character) || character == Character.valueOf(','))) {
                // after *, nothing must be insered
                if (string.lastIndexOf("*") < position) { //$NON-NLS-1$
                    return false;
                }
            }
        }
        if ((Character.getType(character) == 15) || Character.isDigit(character) || (character) == Character.valueOf(',')) {
            // Check unique comma
            if ((character) == Character.valueOf(',')) {
                if (position > 0) {
                    if (string.substring(position - 1, position).equals(",")) { //$NON-NLS-1$
                        return false;
                    }
                    if (position + 1 < string.length()) {
                        if (string.substring(position, position + 1).equals(",")) { //$NON-NLS-1$
                            return false;
                        }
                    }
                } else if (position == 0) {
                    return false;
                }
            }
        } else if ((character) == Character.valueOf('*')) {
            // Check unique *
            if (string.lastIndexOf("*") > 0) { //$NON-NLS-1$
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
        if (value.lastIndexOf(",") == value.length() - 1) { //$NON-NLS-1$
            value = value.substring(0, value.length() - 1);
        }

        // Each Character must be integer comma or *
        if (!"".equals(value)) { //$NON-NLS-1$
            String val = ""; //$NON-NLS-1$
            for (int f = 0; f < value.length(); f++) {
                char c = value.charAt(f);
                String s1 = "" + value.charAt(f); //$NON-NLS-1$
                if (Character.isDigit(c) || (s1.equals(",")) || (s1.equals("*"))) { //$NON-NLS-1$ //$NON-NLS-2$
                    val = val + s1;
                }
            }
            value = val;
        }
        // if needed add comma before *
        if ((value.lastIndexOf("*") > -1) && (!value.equals("*"))) { //$NON-NLS-1$ //$NON-NLS-2$
            if (!value.substring((value.lastIndexOf("*") - 1), value.lastIndexOf("*")).equals(",")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                value = value.substring(0, value.lastIndexOf("*")) + ",*"; //$NON-NLS-1$ //$NON-NLS-2$
            }
            // Character * must be the last char
            if ((value.lastIndexOf("*") < value.length() - 1)) { //$NON-NLS-1$
                value = value.substring(0, value.lastIndexOf("*")); //$NON-NLS-1$
            }
        }
        return value;
    }

    protected PositionalFileConnection getConnection() {
        return (PositionalFileConnection) connectionItem.getConnection();
    }
}
