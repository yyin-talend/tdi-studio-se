// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.process;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.talend.core.model.repository.FakePropertyImpl;


/**
 * created by nrousseau on Aug 29, 2016
 * Detailled comment
 *
 */
public class ProcessTest {

    /**
     * Test method for {@link org.talend.designer.core.ui.editor.process.Process#checkProcess()}.
     */
    @Test
    public void testCheckProcess() {
        
        Process p = new Process(new FakePropertyImpl()) {

            /* (non-Javadoc)
             * @see org.talend.designer.core.ui.editor.process.Process#checkProblems()
             */
            @Override
            protected void checkProblems() {
                assertThat(isDuplicate(), is(false));
                assertThat(isActivate(), is(true));
            }
            
        };
        p.setActivate(false);
        p.setDuplicate(false);
        p.checkProcess();
        p.setActivate(false);
        p.setDuplicate(true);
        p.checkProcess();
        p.setActivate(true);
        p.setDuplicate(false);
        p.checkProcess();
        p.setActivate(true);
        p.setDuplicate(true);
        p.checkProcess();
    }

}
