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
package org.talend.designer.mapper.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.talend.commons.utils.data.text.StringHelper;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class StringHelperTest {

    /**
     * Test method for
     * {@link org.talend.designer.mapper.utils.StringHelper#replacePrms(java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testReplacePrms() {
        assertEquals("abcdef", StringHelper.replacePrms("a{0}{1}d{2}f", new Object[] { "b", "c", "e" }));
        assertEquals("ab\\{c\\}def", StringHelper.replacePrms("a{0}\\{{1}\\}d{2}f", new Object[] { "b", "c", "e" }));
    }

}
