// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.generic.service;

import org.talend.core.model.process.IElementParameter;
import org.talend.core.runtime.services.IGenericService;
import org.talend.designer.core.generic.model.GenericElementParameter;

/**
 * created by ycbai on 2016年3月24日 Detailled comment
 *
 */
public class GenericService implements IGenericService {

    @Override
    public boolean callBeforeActivate(IElementParameter parameter) {
        if (parameter instanceof GenericElementParameter) {
            return ((GenericElementParameter) parameter).callBeforeActivate();
        }
        return false;
    }

}
