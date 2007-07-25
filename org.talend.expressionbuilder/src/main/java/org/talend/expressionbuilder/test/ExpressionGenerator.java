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
package org.talend.expressionbuilder.test;

import org.talend.expressionbuilder.test.ExpressionArguments;
import org.talend.expressionbuilder.test.Variable;
import java.util.List;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionGenerator.java 上午10:06:29 2007-7-24 +0000 (2007-7-24) yzhang $
 * 
 */
public class ExpressionGenerator {

    protected static String nl;

    public static synchronized ExpressionGenerator create(String lineSeparator) {
        nl = lineSeparator;
        ExpressionGenerator result = new ExpressionGenerator();
        nl = null;
        return result;
    }

    protected static final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;

    protected static final String TEXT_1 = "// ============================================================================"
            + NL
            + "//"
            + NL
            + "// Talend Community Edition"
            + NL
            + "//"
            + NL
            + "// Copyright (C) 2006-2007 Talend - www.talend.com"
            + NL
            + "//"
            + NL
            + "// This library is free software; you can redistribute it and/or"
            + NL
            + "// modify it under the terms of the GNU Lesser General Public"
            + NL
            + "// License as published by the Free Software Foundation; either"
            + NL
            + "// version 2.1 of the License."
            + NL
            + "//"
            + NL
            + "// This library is distributed in the hope that it will be useful,"
            + NL
            + "// but WITHOUT ANY WARRANTY; without even the implied warranty of"
            + NL
            + "// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU"
            + NL
            + "// Lesser General Public License for more details."
            + NL
            + "//"
            + NL
            + "// You should have received a copy of the GNU General Public License"
            + NL
            + "// along with this program; if not, write to the Free Software"
            + NL
            + "// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA."
            + NL
            + "//"
            + NL
            + "// ============================================================================";

    protected static final String TEXT_2 = NL + "package routines;\t" + NL + "" + NL + "import java.io.IOException;"
            + NL + "import java.io.PrintWriter;" + NL + "import java.net.Socket;" + NL
            + "import java.net.UnknownHostException;" + NL + "" + NL + "public class ExpressionVariableTest {" + NL
            + "" + NL + "    private Socket client;" + NL + "" + NL + "    private PrintWriter writer;" + NL + "    ";

    protected static final String TEXT_3 = NL + "    private String ";

    protected static final String TEXT_4 = "=\"";

    protected static final String TEXT_5 = "\";" + NL + "    ";

    protected static final String TEXT_6 = NL
            + NL
            + NL
            + "    public ExpressionVariableTest() {"
            + NL
            + "        try {"
            + NL
            + "            client = new Socket(\"localhost\", 6666);"
            + NL
            + "            writer = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.OutputStreamWriter(client"
            + NL + "                    .getOutputStream())), true);" + NL
            + "        } catch (UnknownHostException e) {" + NL + "            e.printStackTrace();" + NL
            + "        } catch (IOException e) {" + NL + "            e.printStackTrace();" + NL + "        }" + NL
            + "" + NL + "    }" + NL + "" + NL + "    private void exec() {" + NL
            + "        String msg = String.valueOf(";

    protected static final String TEXT_7 = ");" + NL + "        sendMsg(msg);" + NL + "    }" + NL + "" + NL
            + "    public void sendMsg(String msg) {" + NL + "        writer.print(msg);" + NL
            + "        writer.flush();" + NL + "        try {" + NL + "            writer.close();" + NL
            + "            client.close();" + NL + "            writer = null;" + NL + "            client = null;"
            + NL + "        } catch (IOException e) {" + NL + "            e.printStackTrace();" + NL + "        }"
            + NL + "    }" + NL + "" + NL + "    public static void main(String[] args) {" + NL
            + "\t\tnew ExpressionVariableTest().exec();" + NL + "    }" + NL + "}";

    protected static final String TEXT_8 = NL;

    public String generate(Object argument) {
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(TEXT_1);

        ExpressionArguments args = (ExpressionArguments) argument;
        String expression = args.getExpression();
        List<Variable> variables = args.getVariables();

        stringBuffer.append(TEXT_2);

        for (Variable var : variables) {

            stringBuffer.append(TEXT_3);
            stringBuffer.append(var.getName());
            stringBuffer.append(TEXT_4);
            stringBuffer.append(var.getValue());
            stringBuffer.append(TEXT_5);

        }

        stringBuffer.append(TEXT_6);
        stringBuffer.append(expression);
        stringBuffer.append(TEXT_7);
        stringBuffer.append(TEXT_8);
        return stringBuffer.toString();
    }
}
