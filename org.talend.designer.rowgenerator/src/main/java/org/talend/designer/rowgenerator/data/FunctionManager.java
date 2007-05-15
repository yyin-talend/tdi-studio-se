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
package org.talend.designer.rowgenerator.data;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.designer.rowgenerator.RowGeneratorComponent;
import org.talend.designer.rowgenerator.i18n.Messages;
import org.talend.designer.rowgenerator.managers.UIManager;
import org.talend.designer.rowgenerator.ui.editor.MetadataColumnExt;

/**
 * class global comment. Detailled comment <br/>
 * 
 * $Id: FunctionManager.java,v 1.13 2007/01/31 05:20:51 pub Exp $
 * 
 */
public class FunctionManager {

    public static final String PURE_PERL_NAME = "...";

    public static final String PURE_PERL_DESC = Messages.getString("FunctionManager.PurePerl.Desc"); //$NON-NLS-1$

    public static final String PURE_PERL_PARAM = Messages.getString("FunctionManager.PurePerl.ParaName"); //$NON-NLS-1$

    private List<TalendType> talendTypes = null;

    public static final String PERL_FUN_PREFIX = "sub{";

    public static final String PERL_FUN_SUFFIX = ")}";

    public static final String FUN_PARAM_SEPARATED = ",";

    /**
     * qzhang Comment method "getFunctionByName".
     * 
     * @param name is TalendType name.
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public List<Function> getFunctionByName(String name) {
        List<Function> funtions = new ArrayList<Function>();

        for (TalendType talendType : talendTypes) {
            if (talendType.getName().equals(name)) {
                funtions.addAll(talendType.getFunctions());
            }
        }
        funtions.add(createCustomizeFunction());
        return funtions;
    }

    private Function createCustomizeFunction() {
        Function function = new Function();
        function.setName(PURE_PERL_NAME);
        function.setDescription(PURE_PERL_DESC);
        StringParameter param = new StringParameter();
        List<Parameter> params = new ArrayList<Parameter>();
        param.setName(PURE_PERL_PARAM);
        params.add(param);
        function.setParameters(params);
        return function;
    }

    public RepositoryContext getRepositoryContext() {
        Context ctx = CorePlugin.getContext();
        return (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    @SuppressWarnings("unchecked")
    public FunctionManager() {
        // this code move to FunctionParser .
        //        
        // for (int i = 0; i < list.size(); i++) {
        // URL url = list.get(i);
        // try {
        // url = FileLocator.toFileURL(url);
        // File file = new File(url.getFile());
        // files.add(file);
        // } catch (Exception e) {
        // ExceptionHandler.process(e);
        // }
        // }
        AbstractFunctionParser parser = null;
        if (UIManager.isJavaProject()) {
            parser = new JavaFunctionParser();
        } else {
            parser = new PerlFunctionParser();
        }
        parser.parse();
        talendTypes = parser.getList();
    }

    public Function getCurrentFunction(String funName, MetadataColumnExt bean) {
        Function currentFun = new Function();
        List<Function> functions = getFunctionByName(bean.getTalendType());
        String[] arrayTalendFunctions2 = new String[functions.size()];
        if (functions.isEmpty()) {
            currentFun.setDescription(""); //$NON-NLS-1$
            currentFun.setPreview(""); //$NON-NLS-1$
            currentFun.setParameters(new ArrayList<Parameter>());
            bean.setArrayFunctions(arrayTalendFunctions2);
        } else {
            for (int i = 0; i < functions.size(); i++) {
                arrayTalendFunctions2[i] = functions.get(i).getName();
                if (funName.equals(functions.get(i).getName())) {
                    currentFun = functions.get(i);
                }
            }
            bean.setArrayFunctions(arrayTalendFunctions2);
        }
        return currentFun;
    }

    public Function getDefaultFunction(MetadataColumnExt bean, String talendType) {
        Function currentFun = new Function();
        List<Function> functions = getFunctionByName(talendType);
        String[] arrayTalendFunctions2 = new String[functions.size()];
        if (functions.isEmpty()) {
            currentFun.setDescription(""); //$NON-NLS-1$
            currentFun.setPreview(""); //$NON-NLS-1$
            currentFun.setParameters(new ArrayList<Parameter>());
            bean.setArrayFunctions(arrayTalendFunctions2);
        } else {
            for (int i = 0; i < functions.size(); i++) {
                arrayTalendFunctions2[i] = functions.get(i).getName();
            }
            currentFun = (Function) functions.get(0).clone();
            bean.setArrayFunctions(arrayTalendFunctions2);
        }

        return currentFun;
    }

    public Function getFuntionFromArray(MetadataColumnExt bean, RowGeneratorComponent externalNode, int index) {
        String value = externalNode.getColumnValue(bean, index);
        List<Function> functions = getFunctionByName(bean.getTalendType());
        Function currentFun = getAvailableFunFromValue(value, functions);
        if (currentFun == null) {
            currentFun = new Function();
            String[] arrayTalendFunctions2 = new String[functions.size()];
            if (functions.isEmpty()) {
                currentFun.setDescription(""); //$NON-NLS-1$
                currentFun.setPreview(""); //$NON-NLS-1$
                currentFun.setParameters(new ArrayList<Parameter>());
                bean.setArrayFunctions(arrayTalendFunctions2);
            } else {
                for (int i = 0; i < functions.size(); i++) {
                    arrayTalendFunctions2[i] = functions.get(i).getName();
                }
                currentFun = (Function) functions.get(0).clone();
                bean.setArrayFunctions(arrayTalendFunctions2);
            }
        }

        return currentFun;

    }

    /**
     * qzhang Comment method "isAvailableSubValue".
     * 
     * @param value
     * @return
     */
    private Function getAvailableFunFromValue(String value, List<Function> funs) {
        Function currentFun = null;
        boolean isExsit = false;
        for (Function function : funs) {
            if (value != null && value.indexOf(function.getName()) != -1) {
                isExsit = true;
            }
        }
        if (value != null) {
            boolean isPure = true;
            int paramLength = value.length() - 2;
            if (UIManager.isJavaProject()) {
                isPure = value.indexOf(".") != -1 && value.indexOf("(") > value.indexOf(".") && value.endsWith(")");
                paramLength = value.length() - 1;
            } else {
                isPure = value.startsWith(PERL_FUN_PREFIX) && value.endsWith(PERL_FUN_SUFFIX);
            }
            if (isPure && isExsit) {
                for (Function function : funs) {
                    int indexOf = value.indexOf(function.getName());
                    if (indexOf != -1) {
                        String para = value.substring(indexOf + function.getName().length() + 1, paramLength);
                        String[] ps = para.split(FUN_PARAM_SEPARATED); //$NON-NLS-1$
                        if (ps.length == function.getParameters().size()) {
                            currentFun = (Function) function.clone(ps);
                        }
                    }
                }
            } else {
                currentFun = createPureFunctions(value, funs, currentFun);
            }

        }
        return currentFun;
    }

    /**
     * qzhang Comment method "createPureFunctions".
     * 
     * @param value
     * @param funs
     * @param currentFun
     * @return
     */
    private Function createPureFunctions(String value, List<Function> funs, Function currentFun) {
        for (Function function : funs) {
            if (function.getName().equals(PURE_PERL_NAME)) {
                currentFun = (Function) function.clone();
                ((StringParameter) currentFun.getParameters().get(0)).setValue(value);
            }
        }
        return currentFun;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static String getOneColData(MetadataColumnExt bean) {
        if (bean != null && bean.getFunction() != null) {
            String newValue = PERL_FUN_PREFIX; //$NON-NLS-1$
            if (bean.getFunction().getName().equals(PURE_PERL_NAME)) {
                newValue = ((StringParameter) bean.getFunction().getParameters().get(0)).getValue();
            } else {
                if (bean.getFunction().getName() == null || "".equals(bean.getFunction().getName())) { //$NON-NLS-1$
                    return ""; //$NON-NLS-1$
                }
                final List<Parameter> parameters = (List<Parameter>) bean.getFunction().getParameters();
                if (UIManager.isJavaProject()) {
                    String fullName = JavaFunctionParser.getTypeMethods().get(
                            bean.getTalendType() + "." + bean.getFunction().getName());
                    newValue = fullName + "(";
                    for (Parameter pa : parameters) {
                        newValue += pa.getValue() + FUN_PARAM_SEPARATED; //$NON-NLS-1$
                    }
                    if (!parameters.isEmpty()) {
                        newValue = newValue.substring(0, newValue.length() - 1);
                    }
                    newValue += ")"; //$NON-NLS-1$

                } else {
                    newValue += bean.getFunction().getName() + "("; //$NON-NLS-1$
                    for (Parameter pa : parameters) {
                        newValue += pa.getValue() + FUN_PARAM_SEPARATED; //$NON-NLS-1$
                    }
                    newValue = newValue.substring(0, newValue.length() - 1);
                    newValue += PERL_FUN_SUFFIX; //$NON-NLS-1$
                }
            }
            return newValue;
        }
        return null;
    }

}
