// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures;

import org.talend.designer.gefabstractmap.figures.VarTableContainerFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;

/**
 * created by Administrator on 2013-1-11 Detailled comment
 * 
 */
public class XmlMapVarTableContainerFigure extends VarTableContainerFigure {

    private VarTable vartable;

    /**
     * DOC Administrator XmlMapVarTableContainerFigure constructor comment.
     * 
     * @param vartable
     */
    public XmlMapVarTableContainerFigure() {
        this.vartable = vartable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.xmlmap.figures.VarTableContainerFigure#isMiniSized()
     */
    @Override
    protected boolean isMiniSized() {
        if (vartable != null) {
            return vartable.isMinimized();
        }
        return false;
    }

}
