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
package org.talend.designer.rowgenerator.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.talend.commons.exception.ExceptionHandler;

/**
 * class global comment. Detailled comment <br/>
 * 
 * $Id: ParameterFactory.java,v 1.6 2007/01/31 05:20:51 pub Exp $
 * 
 */
public class ParameterFactory {

    /**
     * 
     */
    private static final String PARAMETER_TYPE_REGEX = "(^[^\\s^\\(]+)(\\s|\\()";

    public static final String PARAMETER_TYPE_STRING = "string";

    public static final String PARAMETER_TYPE_INT = "int";

    public  static final  String PARAMETER_TYPE_LIST = "list";

    /**
     * qzhang Comment method "getParameter". <br/>.
     * 
     * format is: {param} <type/>[(<default value or closed list values/>)] <name/>[ : <comment/>]
     * 
     * @param string
     * @return
     */
    public Parameter getParameter(String string) {
        String type = getType(string);
        Parameter p = createParameter(type);

        string = string.replaceFirst(type, FunctionParser.EMPTY_STRING).trim();
        String value = null;
        // get Value
        if (string.startsWith("(")) {
            int end = string.indexOf(")");
            value = string.substring(1, end);
            string = string.substring(end + 1).trim();
            setDefaultValue(p, value);
        }

        String[] s = string.split(":");
        if (s != null && s.length != 0) {
            if (s[0] != null) {
                p.setName(s[0]);
            }

            if (s.length == 2 && s[1] != null) {
                p.setComment(s[1]);
            }
        }
        return p;
    }

    /**
     * qzhang Comment method "setDefaultValue".
     * 
     * @param p
     * @param value
     */
    private void setDefaultValue(Parameter p, String value) {
        if (p instanceof ListParameter) {
            ListParameter lp = (ListParameter) p;
            String[] values = value.split(",");
            lp.setValues(values);
            if (values.length > 0) {
                lp.setValue(values[0]);
            }
        } else {
            p.setValue(value);
        }

    }

    /**
     * qzhang Comment method "getType".
     * 
     * @param string
     * @return
     */
    private String getType(String string) {
        try {
            Pattern regex = Pattern.compile(PARAMETER_TYPE_REGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);
            if (matcher.find()) {
                String s = matcher.group(1);
                return s;
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return FunctionParser.EMPTY_STRING;

    }

    private Parameter createParameter(String type) {
        if (type.startsWith(PARAMETER_TYPE_STRING)) {
            return new StringParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_INT)) {
            return new IntParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_LIST)) {
            return new ListParameter();
        }
        return null;
    }

    public static void main(String[] args) {
        String string = "string(\'2007-01-01\') min : minimum date";

        String type = "string";
        string = string.replaceFirst(type, "");
        System.out.println(string);
    }
}
