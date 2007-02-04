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

import java.io.File;
import java.io.FileInputStream;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.talend.commons.exception.ExceptionHandler;

/**
 * class global comment. Detailled comment <br/>
 * 
 * $Id: FunctionParser.java,v 1.3 2007/01/31 05:20:51 pub Exp $
 * 
 */
public class FunctionParser {

    List<TalendType> list = new ArrayList<TalendType>();

    /**
     * 
     */
    private static final String FUNCTION_PARAMETERS_REGEX = "\\{param\\}(\\s)*(.*)"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String FUNCTION_NAME_REGEX = "sub(\\s)*(.*)"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String FUNCTION_TYPE_REGEX = "\\{talendTypes\\}(\\s)*(.*)"; //$NON-NLS-1$

    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private static final String FUNCTION_DESCRIPTION_REGEX = "##(\\s)*((\\r)*\\n)#(\\s)*(.*)"; //$NON-NLS-1$

    public static final String FUNCTION_REGEX = "##(\\s)*(.*(\\r)*\\n)+?(sub)(\\s)+([^\\s]+)"; //$NON-NLS-1$

    /**
     * @uml.property name="file"
     */
    private File[] files;

    public FunctionParser(File[] file) {
        this.files = file;
    }

    /**
     * Getter for list.
     * 
     * @return the list
     */
    public List<TalendType> getList() {
        return this.list;
    }

    public void parse() {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                String strFile = convertFileToString(file);
                String[] strGroups = parseGroupNeeded(strFile);

                parseToFunctions(strGroups);
            } catch (Exception e) {
                e.printStackTrace();
                ExceptionHandler.process(e);
            }
        }

    }

    /**
     * qzhang Comment method "parseToFunctions".
     * 
     * @param strGroups
     */
    private void parseToFunctions(String[] strGroups) {
        for (int i = 0; i < strGroups.length; i++) {
            String string = strGroups[i];

            String des = parseDescription(string);
            String functionType = parseFunctionType(string);

            String[] parameter = parseFunctionParameters(string);
            Parameter[] paras = convertToParameter(parameter);

            String functionName = parseFunctionName(string);

            Function function = new Function();
            function.setName(functionName);
            function.setDescription(des);
            function.setParameters(Arrays.asList(paras));

            TalendType talendType = getTalendType(functionType);
            talendType.addFunctions(function);

        }

    }

    /**
     * qzhang Comment method "convertToParameter".
     * 
     * @param parameter
     * @return
     */
    private Parameter[] convertToParameter(String[] p) {
        ParameterFactory pf = new ParameterFactory();
        Parameter[] parameters = new Parameter[p.length];
        for (int i = 0; i < p.length; i++) {
            parameters[i] = pf.getParameter(p[i]);
        }
        return parameters;
    }

    /**
     * Gets the TalendType that already created, if the wanted one is one existent ,create one.
     * 
     * @param name
     * @return
     */
    private TalendType getTalendType(String name) {
        for (TalendType type : list) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        TalendType type = new TalendType();
        type.setName(name);
        list.add(type);

        return type;
    }

    /**
     * qzhang Comment method "parseFunctionName".
     * 
     * @param string
     * @return
     */
    private String parseFunctionName(String string) {
        try {
            Pattern regex = Pattern.compile(FUNCTION_NAME_REGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);
            if (matcher.find()) {
                String s = matcher.group(2);
                return s;
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return EMPTY_STRING;
    }

    /**
     * qzhang Comment method "parseFunctionParameters".
     * 
     * @param string
     * @return
     */
    private String[] parseFunctionParameters(String string) {
        List<String> list1 = new ArrayList<String>();
        try {
            Pattern regex = Pattern.compile(FUNCTION_PARAMETERS_REGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);

            while (matcher.find()) {
                String s = matcher.group(2);
                list1.add(s);
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return list1.toArray(new String[list1.size()]);
    }

    /**
     * qzhang Comment method "parseFunction".
     * 
     * @param string
     * @return
     */
    private String parseFunctionType(String string) {
        try {
            Pattern regex = Pattern.compile(FUNCTION_TYPE_REGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);
            if (matcher.find()) {
                String s = matcher.group(2);
                return s;
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return EMPTY_STRING;
    }

    /**
     * qzhang Comment method "parse description of the function".
     * 
     * @param string
     */
    private String parseDescription(String string) {
        try {
            Pattern regex = Pattern.compile(FUNCTION_DESCRIPTION_REGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);
            if (matcher.find()) {
                String s = matcher.group(5);
                return s;
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return EMPTY_STRING;
    }

    /**
     * Use regex to parse the fileString to several groups, from ## to sub FunctionName.
     * 
     * @param strFile
     * @return
     */
    private String[] parseGroupNeeded(String strFile) {
        Pattern regex = Pattern.compile("##(\\s)*(.*(\\r)*\\n)+?(sub)(\\s)+([^\\s]+)", Pattern.CANON_EQ); //$NON-NLS-1$
        Matcher matcher = regex.matcher(strFile);

        List<String> list1 = new ArrayList<String>();
        while (matcher.find()) {
            String group = matcher.group();
            list1.add(group);
        }
        return list1.toArray(new String[list1.size()]);
    }

    /**
     * Read the file's content to a String.
     * 
     * @param file2
     * @return
     */
    private String convertFileToString(File file2) throws Exception {
        if (!file2.exists()) {
            return EMPTY_STRING;
        }
        FileInputStream fileInputStream = null;
        FileChannel fileChannel = null;
        long fileSize = 0;
        MappedByteBuffer mBuf = null;
        try {
            fileInputStream = new FileInputStream(file2);
            fileChannel = fileInputStream.getChannel();
            fileSize = fileChannel.size();
            mBuf = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

            Charset charset = Charset.defaultCharset();
            CharBuffer cb = charset.decode(mBuf);
            String value = String.valueOf(cb);
            return value;
        } finally {
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

    }

    public static void main(String[] args) {
        File f = new File("c:/String.pm"); //$NON-NLS-1$
        File f1 = new File("c:/Date.pm"); //$NON-NLS-1$
        File f2 = new File("c:/Numeric.pm"); //$NON-NLS-1$
        File f3 = new File("c:/Misc.pm"); //$NON-NLS-1$
        FunctionParser p = new FunctionParser(new File[] { f, f1, f2, f3 });
        p.parse();
        List<TalendType> list = p.getList();

        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            System.out.println(o);
        }
    }
}
