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
package org.talend.expressionbuilder.ui;

import java.util.Stack;

import org.eclipse.swt.widgets.Button;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: UndoRecorder.java 上午11:12:22 2007-9-13 +0000 (2007-9-13) yzhang $
 * 
 */

public class ExpressionRecorder {

    private final Stack<String> stack;

    private final Button button;

    private boolean isUndo;

    private int undoCounter;

    /**
     * yzhang ExpressionRecorder constructor comment.
     */
    public ExpressionRecorder(Button button) {
        stack = new Stack<String>();
        stack.push("");
        this.button = button;
    }

    /**
     * yzhang Comment method "addRecored".
     * 
     * @param expression
     */
    public void pushRecored(String expression) {
        if (!isUndo) {
            if (stack.size() == 0) {
                stack.push("");
            }
            stack.push(expression);
            if (!button.isEnabled()) {
                button.setEnabled(true);
            }
            undoCounter = 0;
        } else {
            return;
        }
    }

    /**
     * yzhang Comment method "undo".
     */
    public void undo() {
        isUndo = true;
    }

    /**
     * yzhang Comment method "undoFinished".
     */
    public void undoFinished() {
        isUndo = false;
    }

    /**
     * yzhang Comment method "popRecored".
     * 
     * @return
     */
    public String popRecored() {
        if (stack.size() != 0) {
            if (undoCounter == 0) {
                stack.pop();
            }
            String expression = stack.pop();
            undoCounter++;
            if (stack.size() == 0) {
                button.setEnabled(false);
            }
            return expression;
        }
        return "";
    }

}
