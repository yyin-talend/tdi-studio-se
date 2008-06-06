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
package org.talend.designer.abstractmap;

import java.util.List;

import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Substitution;
import org.apache.oro.text.regex.Util;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.abstractmap.ui.prefs.MapPrefsConstants;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractMapComponent extends AbstractExternalNode {

    /**
     * DOC amaumont AbstractMapComponent constructor comment.
     */
    public AbstractMapComponent() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#initialize()
     */
    public void initialize() {
        initElementParameters();
    }

    /**
     * DOC amaumont Comment method "initElementParameters".
     */
    private void initElementParameters() {
        IElementParameter elementParameter = getElementParameter(MapPrefsConstants.LINK_STYLE);
        if (elementParameter != null && ((String) elementParameter.getValue()).trim().equals("")) {
            elementParameter.setValue(MapPlugin.getDefault().getPreferenceStore().getString(
                    MapPrefsConstants.LINK_STYLE));
        }
    }

    /**
     * DOC amaumont Comment method "refreshMapperConnectorData".
     */
    public void refreshMapperConnectorData() {

    }

    protected String renameDataIntoExpression(Pattern pattern, PatternMatcher matcher, Substitution substitution,
            String expression) {
        String replacedExpression = Util.substitute(matcher, pattern, substitution, expression, Util.SUBSTITUTE_ALL);
        return replacedExpression;
    }

    protected boolean hasDataIntoExpression(Pattern pattern, PatternMatcher matcher, String expression) {
        if (expression != null) {
            if (matcher.contains(expression, pattern)) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#useData(java.lang.String)
     */
    public boolean useData(String name) {
        if (super.useData(name)) {
            return true;
        }
        if (hasOrRenameData(name, null, false)) {
            return true;
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#renameData(java.lang.String, java.lang.String)
     */
    public void renameData(String oldName, String newName) {
        super.renameData(oldName, newName);

        hasOrRenameData(oldName, newName, true);

    }

    protected abstract boolean hasOrRenameData(String oldName, String newName, boolean renameAction);

}
