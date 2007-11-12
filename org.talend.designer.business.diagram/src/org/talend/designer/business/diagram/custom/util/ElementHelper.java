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
package org.talend.designer.business.diagram.custom.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.model.business.BusinessItem;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ElementHelper {

    /**
     * DOC mhelleboid Comment method "getElement".
     * 
     * @param editPart
     * @return
     */
    public BusinessItem getElement(EditPart editPart) {
        Object model = editPart.getModel();
        if (model instanceof View) {
            View node = (View) model;
            EObject element = node.getElement();
            if (element instanceof BusinessItem) {
                return (BusinessItem) element;
            }
        }
        return null;
    }

}
