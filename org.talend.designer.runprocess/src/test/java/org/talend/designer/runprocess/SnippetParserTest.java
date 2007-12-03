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
package org.talend.designer.runprocess;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 */
public class SnippetParserTest {

    public static String systemEOL = SnippetParser.systemEOL;

    static String javaTestCode = null;

    static String perlTestCode = null;

    static String snippetID = "_34ejdsfy8768sdf79";

    static String snippetName = "SnippetAA";

    /**
     * format is: <br>
     * SNIPPET_START:test <br>
     * ListVar=new java.util.ArrayList<String>() <br>
     * SourceType=String <br>
     * {ID}=_2k63gJ7qEdyHlb_RsYibMg<br>
     * 
     * code... <br>
     * SNIPPET_END
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Java comment format
        StringBuilder sb = new StringBuilder();
        {
            String para = "Var1=xxx" + systemEOL + "Var2=yyy";
            String code = "String.valueof(${Var1})";
            sb.append("other code start");
            sb.append(systemEOL);
            sb.append("/*SNIPPET_START:").append(snippetName).append(systemEOL).append(para).append(systemEOL).append(
                    "{ID}=" + snippetID).append("*/");
            sb.append(systemEOL);
            sb.append(code);
            sb.append(systemEOL);
            sb.append("/*SNIPPET_END*/");
            sb.append(systemEOL);
            sb.append("other code end");
            javaTestCode = sb.toString();
        }

        sb = new StringBuilder();
        {
            String para = "#Var1=xxx" + systemEOL + "#Var2=yyy";
            String code = "my $foo = \'bar\';";
            sb.append("other code start");
            sb.append(systemEOL);
            sb.append("#SNIPPET_START:").append(snippetName).append(systemEOL).append(para).append(systemEOL).append(
                    "#{ID}=" + snippetID);
            sb.append(systemEOL);
            sb.append(code);
            sb.append(systemEOL);
            sb.append("#SNIPPET_END");
            sb.append(systemEOL);
            sb.append("other code end");
            perlTestCode = sb.toString();
        }

    }

    /**
     * DOC bqian Comment method "tearDownAfterClass".
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC bqian Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * DOC bqian Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    /**
     * Test method for {@link org.talend.designer.runprocess.SnippetParser#findFirstSnippetId(java.lang.String)}.
     */
    @Test
    public void testFindFirstSnippetIdForJava() {
        SnippetParser sp = new SnippetParser();
        String[] result = sp.findFirstSnippetId(javaTestCode).toArray(new String[0]);

        Assert.assertEquals(4, result.length);
        Assert.assertEquals(result[0], snippetID);
        Assert.assertEquals(result[1], snippetName);
        Assert.assertEquals(result[2], "Var1=xxx");
        Assert.assertEquals(result[3], "Var2=yyy");

    }

    @Test
    public void testFindFirstSnippetIdForPerl() {
        SnippetParser sp = new SnippetParser();
        String[] result = sp.findFirstSnippetId(perlTestCode).toArray(new String[0]);

        Assert.assertEquals(4, result.length);
        Assert.assertEquals(result[0], snippetID);
        Assert.assertEquals(result[1], snippetName);
        Assert.assertEquals(result[2], "Var1=xxx");
        Assert.assertEquals(result[3], "Var2=yyy");

    }

    @Test
    public void testFindFirstSnippetIdNoResult() {
        SnippetParser sp = new SnippetParser();
        String[] result = sp.findFirstSnippetId("sdfsdf").toArray(new String[0]);
        Assert.assertEquals(result.length, 0);
    }

    /**
     * Test method for
     * {@link org.talend.designer.runprocess.SnippetParser#replaceFristSnippet(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplaceFristSnippet4Java() {
        SnippetParser sp = new SnippetParser();

        String result = sp.replaceFristSnippet("mycode", javaTestCode);
        Assert.assertEquals("other code start" + systemEOL + "mycode" + systemEOL + "other code end", result);
    }

    @Test
    public void testReplaceFristSnippet4Perl() {
        SnippetParser sp = new SnippetParser();
        System.out.println(perlTestCode);
        String myCode = "$foo";
        // myCode = StringUtils.replace(myCode, "$", "\\$");

        String result = sp.replaceFristSnippet(myCode, perlTestCode);
        System.out.println(result);
        Assert.assertEquals("other code start" + systemEOL + "$foo" + systemEOL + "other code end", result);
    }
}
