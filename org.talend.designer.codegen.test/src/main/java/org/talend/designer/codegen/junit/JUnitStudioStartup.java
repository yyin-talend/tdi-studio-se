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
package org.talend.designer.codegen.junit;

import org.junit.Test;
import org.talend.designer.codegen.model.CodeGeneratorEmittersPoolFactory;

/**
 * created by nrousseau on Jul 25, 2012 Detailled comment
 * 
 */
public class JUnitStudioStartup {

    private static final long ONE_MINUTE_IN_MILLISEC = 60000;

    private static final long INIT_TIMEOUT = ONE_MINUTE_IN_MILLISEC * 10;

    private static final long INIT_PAUSE = 100;

    @Test
    public void test() throws InterruptedException {
        long startTimer = System.currentTimeMillis();
        long endTimer = startTimer;
        while ((!CodeGeneratorEmittersPoolFactory.isInitialized()) && ((endTimer - startTimer) < INIT_TIMEOUT)) {
            Thread.sleep(INIT_PAUSE);
            endTimer = System.currentTimeMillis();
        }
        System.out.println("Jet emitters initialized successfully, will proceed to standards junits");
    }

}
