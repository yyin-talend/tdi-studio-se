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
package org.talend.repository.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Test
 * 
 * $Id$
 * 
 */

public class Test4 {

    static PositionalTextTest positionalText;
    
    static RegleTest regleTest;
    
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Test");

        regleTest = new RegleTest(shell, SWT.NONE);
        regleTest.setBounds(3, 0, 500, 25);
        
        positionalText = new PositionalTextTest(shell, SWT.NONE);
        positionalText.setBounds(0, 25, 500, 500);
        positionalText.setText("\n123456789\niin texte\nsalut le test\ntoto");
//        positionalText.setFieldSeparatorValue("1,1");
        regleTest.moveAbove(positionalText);
        
        shell.pack();
        shell.open();
        while (!shell.isDisposed())
            if (!display.readAndDispatch())
                display.sleep();

        display.dispose();
    }
}
