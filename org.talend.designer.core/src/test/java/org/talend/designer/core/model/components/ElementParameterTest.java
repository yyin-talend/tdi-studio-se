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
package org.talend.designer.core.model.components;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.model.components.ElementParameter;

/**
 * Test of the ElementParameter class with the Expression.
 * 
 * $Id$
 * 
 */
public class ElementParameterTest {

    private static boolean testParam(String expression, ElementParameter paramToTest, List<IElementParameter> listParams) {
        boolean returnValue;
        paramToTest.setShowIf(expression);
        System.out.println("Test: " + expression);
        returnValue = paramToTest.isShow(listParams);
        return returnValue;
    }

    /**
     * DOC nrousseau Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {
        List<IElementParameter> listParams = new ArrayList<IElementParameter>();
        ElementParameter param = new ElementParameter();
        param.setName("VAR1");
        param.setValue("value1");
        param.setListItemsDisplayCodeName(new String[] { "VALUE1", "VALUE2", "VALUE3", "VALUE4", "VALUE5", "VALUE5_6" });
        param.setListItemsValue(new String[] { "value1", "value2", "value3", "value4", "value5", "value5;value6" });
        listParams.add(param);

        param = new ElementParameter();
        param.setName("VAR2");
        param.setValue("value2");
        param.setListItemsDisplayCodeName(new String[] { "VALUE1", "VALUE2", "VALUE3", "VALUE4", "VALUE5", "VALUE5_6" });
        param.setListItemsValue(new String[] { "value1", "value2", "value3", "value4", "value5", "value5;value6" });
        listParams.add(param);

        param = new ElementParameter();
        param.setName("VAR3");
        param.setValue("value3");
        param.setListItemsDisplayCodeName(new String[] { "VALUE1", "VALUE2", "VALUE3", "VALUE4", "VALUE5", "VALUE5_6" });
        param.setListItemsValue(new String[] { "value1", "value2", "value3", "value4", "value5", "value5;value6" });
        listParams.add(param);

        param = new ElementParameter();
        param.setName("VAR4");
        param.setValue("value4");
        param.setListItemsDisplayCodeName(new String[] { "VALUE1", "VALUE2", "VALUE3", "VALUE4", "VALUE5", "VALUE5_6" });
        param.setListItemsValue(new String[] { "value1", "value2", "value3", "value4", "value5", "value5;value6" });
        listParams.add(param);

        param = new ElementParameter();
        param.setName("VAR5");
        param.setValue("value5;value6");
        param.setListItemsDisplayCodeName(new String[] { "VALUE1", "VALUE2", "VALUE3", "VALUE4", "VALUE5", "VALUE5_6" });
        param.setListItemsValue(new String[] { "value1", "value2", "value3", "value4", "value5", "value5;value6" });
        listParams.add(param);

        ElementParameter paramToTest = new ElementParameter();
        paramToTest.setName("VAR_TEST");

        Assert.assertTrue(testParam(
                "((VAR1 == 'VALUE1' and VAR2 == 'VALUE2') or (VAR3 != 'VALUE3')) or (VAR4 == 'VALUE4')", paramToTest,
                listParams));
        Assert.assertTrue(testParam(
                "((VAR1 == 'VALUE1' AND VAR2 == 'VALUE2') OR (VAR3 != 'VALUE3')) OR (VAR4 == 'VALUE4')", paramToTest,
                listParams));
        Assert.assertTrue(testParam(
                "(VAR4 == 'VALUE4') or ((VAR1 == 'VALUE1' and VAR2 == 'VALUE2') or VAR3 != 'VALUE3')", paramToTest,
                listParams));
        Assert.assertTrue(testParam("VAR1 == 'VALUE1' and VAR2 == 'VALUE2' and VAR3 != 'VALUE3' or VAR4 == 'VALUE4'",
                paramToTest, listParams));
        Assert.assertTrue(testParam(
                "((VAR1 == 'VALUE1' and VAR2 == 'VALUE2') and (VAR3 != 'VALUE3' or VAR4 == 'VALUE4'))", paramToTest,
                listParams));
        Assert.assertFalse(testParam("VAR4 != 'VALUE4' and (VAR2 == 'VALUE2' or VAR1 == 'VALUE1')", paramToTest,
                listParams));

        Assert.assertTrue(testParam("VAR5 == 'VALUE5_6'", paramToTest, listParams));
    }

}
