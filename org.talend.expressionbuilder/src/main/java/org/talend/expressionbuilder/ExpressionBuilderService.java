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
    public IExpressionBuilderDialogController getExpressionBuilderInstance(Composite parent, IExpressionConsumer consumer) {
        return new ExpressionBuilderDialog(parent.getShell(), consumer);
    }

}
