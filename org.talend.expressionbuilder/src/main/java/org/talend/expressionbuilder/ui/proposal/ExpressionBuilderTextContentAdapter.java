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
package org.talend.expressionbuilder.ui.proposal;

import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.talend.expressionbuilder.ui.ExpressionBuilderDialog;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressoinTextContentAdapter.java 下午03:33:53 2007-7-30 +0000 (2007-7-30) yzhang $
 * 
 */
public class ExpressionBuilderTextContentAdapter extends TextContentAdapter {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.TextContentAdapter#insertControlContents(org.eclipse.swt.widgets.Control,
     * java.lang.String, int)
     */
    @Override
    public void insertControlContents(Control control, String text, int cursorPosition) {
        Point selection = ((Text) control).getSelection();
        ExpressionBuilderDialog.getExpressionComposite().replacedContent(text, selection);

        if (cursorPosition < text.length()) {
            ((Text) control).setSelection(selection.x + cursorPosition, selection.x + cursorPosition);
        }
    }
}
