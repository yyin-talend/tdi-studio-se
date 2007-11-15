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
package org.talend.expressionbuilder;

import org.eclipse.swt.widgets.Composite;
import org.talend.expressionbuilder.ui.ExpressionBuilderDialog;
import org.talend.expressionbuilder.ui.IExpressionBuilderDialogController;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionBuilderService.java 下午04:40:37 2007-8-1 +0000 (2007-8-1) yzhang $
 * 
 */
public class ExpressionBuilderService implements IExpressionBuilderDialogService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.IExpressionBuilderDialogService#expressionBuilderFactory(org.eclipse.swt.widgets.Composite,
     * org.talend.expressionbuilder.IExpressionConsumer)
     */
    public IExpressionBuilderDialogController getExpressionBuilderInstance(Composite parent, IExpressionDataBean dataBean) {
        return new ExpressionBuilderDialog(parent.getShell(), dataBean);
    }

}
