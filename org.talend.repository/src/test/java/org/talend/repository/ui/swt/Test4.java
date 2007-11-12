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
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
