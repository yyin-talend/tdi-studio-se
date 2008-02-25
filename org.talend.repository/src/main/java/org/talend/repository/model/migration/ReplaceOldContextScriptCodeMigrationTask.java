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
package org.talend.repository.model.migration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * ggu class global comment. Detailled comment.
 * 
 * such as: replace old script code ("(String)context.getProperty("var1")") to new one ("context.var1")
 */
public class ReplaceOldContextScriptCodeMigrationTask extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public ExecutionResult executeOnProcess(ProcessItem item) {
        if (getProject().getLanguage() == ECodeLanguage.PERL) {
            return ExecutionResult.NOTHING_TO_DO;
        }

        ProcessType processType = item.getProcess();

        // get old syntax map of the context variables.
        Map<String, String> varsMap = retrieveContextVariables((List<ContextType>) processType.getContext(), processType
                .getDefaultContext());
        if (varsMap.isEmpty()) {
            return ExecutionResult.NOTHING_TO_DO;
        }

        boolean changed = false;
        // modified the old syntax of script code
        changed |= replaceMainParameters(processType.getParameters(), varsMap);
        changed |= replaceNodesParameters((List<NodeType>) processType.getNode(), varsMap);

        if (changed) {
            try {
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                factory.save(item, true);
                return ExecutionResult.SUCCESS_WITH_ALERT;
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return ExecutionResult.FAILURE;
            }
        }
        return ExecutionResult.NOTHING_TO_DO;
    }

    /**
     * 
     * ggu Comment method "retrieveContextVariables".
     * 
     * get the new and old variables expression map.
     */
    private Map<String, String> retrieveContextVariables(List<ContextType> contextsList, final String contextName) {

        if (contextsList == null || contextsList.isEmpty() || contextName == null) {
            return Collections.emptyMap();
        }

        ContextType contextType = ContextUtils.getContextTypeByName(contextsList, contextName);
        if (contextType == null) {
            return Collections.emptyMap();
        }

        Map<String, String> varsMap = new HashMap<String, String>();
        ECodeLanguage language = getProject().getLanguage();
        for (ContextParameterType parameter : (List<ContextParameterType>) contextType.getContextParameter()) {
            String oldCode = ContextParameterUtils.getScriptCode(parameter, language);
            String newCode = ContextParameterUtils.getNewScriptCode(parameter.getName(), language);
            varsMap.put(newCode, replaceSpecialChar(oldCode));
        }
        return varsMap;
    }

    /**
     * 
     * ggu Comment method "replaceJobParameters".
     * 
     * replace job main parameters.
     */
    private boolean replaceMainParameters(ParametersType parametersType, Map<String, String> varsMap) {
        if (parametersType == null || varsMap == null || varsMap.isEmpty()) {
            return false;
        }

        return replaceElementParameter((List<ElementParameterType>) parametersType.getElementParameter(), varsMap);
    }

    /**
     * 
     * ggu Comment method "replaceNodesParameters".
     * 
     * replace nodes parameters.
     */
    private boolean replaceNodesParameters(List<NodeType> nodesList, Map<String, String> varsMap) {
        if (nodesList == null || nodesList.isEmpty() || varsMap == null || varsMap.isEmpty()) {
            return false;
        }
        boolean changed = false;
        for (NodeType node : nodesList) {
            // update parameter
            changed |= replaceElementParameter((List<ElementParameterType>) node.getElementParameter(), varsMap);

            // update extend node data
            // ?? maybe optimize or improve the replaced data
            String strdata = node.getStringData();
            if (strdata != null) {
                String newData = replaceQuotStringData(strdata, varsMap);
                if (newData != null && !newData.equals(strdata)) {
                    node.setStringData(newData);
                    changed = true;
                }
            }
        }

        return changed;
    }

    private boolean replaceElementParameter(List<ElementParameterType> eleParameterList, Map<String, String> varsMap) {
        if (eleParameterList == null || eleParameterList.isEmpty() || varsMap == null || varsMap.isEmpty()) {
            return false;
        }
        boolean changed = false;
        for (ElementParameterType eleParameterType : (List<ElementParameterType>) eleParameterList) {
            String oldValue = eleParameterType.getValue();
            String newValue = hasOrReplaceValue(oldValue, varsMap);
            if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
                eleParameterType.setValue(newValue);
                changed = true;
            }
        }
        return changed;
    }

    /**
     * 
     * ggu Comment method "hasOrReplaceValue".
     * 
     * check and replace values
     */
    private String hasOrReplaceValue(final String value, Map<String, String> varsMap) {
        if (value == null || varsMap == null || varsMap.isEmpty()) {
            return value; // keep original value
        }
        PatternCompiler compiler = new Perl5Compiler();
        Perl5Matcher matcher = new Perl5Matcher();
        matcher.setMultiline(true);

        String returnValue = value;
        for (String newExpression : varsMap.keySet()) {
            String oldExpression = varsMap.get(newExpression);
            if (oldExpression == null) {
                continue;
            }
            Pattern pattern;
            try {
                pattern = compiler.compile("(" + oldExpression + ")"); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (MalformedPatternException e) {
                return value; // keep original value
            }

            if (matcher.contains(returnValue, pattern)) {
                // replace
                Perl5Substitution substitution = new Perl5Substitution(newExpression, Perl5Substitution.INTERPOLATE_ALL);
                returnValue = Util.substitute(matcher, pattern, substitution, returnValue, Util.SUBSTITUTE_ALL);
            }
        }
        return returnValue;
    }

    private String replaceQuotStringData(final String data, Map<String, String> varsMap) {
        Map<String, String> varsMapExt = new HashMap<String, String>();

        for (String newExpression : varsMap.keySet()) {
            String oldExpression = varsMap.get(newExpression);
            if (oldExpression == null) {
                continue;
            }
            oldExpression = oldExpression.replaceAll("\"", "&quot;"); //$NON-NLS-1$ //$NON-NLS-2$
            varsMapExt.put(newExpression, oldExpression);
        }
        return hasOrReplaceValue(data, varsMapExt);

    }

    private String replaceSpecialChar(String expression) {
        if (expression == null) {
            return null;
        }
        expression = expression.replaceAll("\\(", "\\\\("); //$NON-NLS-1$ //$NON-NLS-2$
        expression = expression.replaceAll("\\)", "\\\\)"); //$NON-NLS-1$ //$NON-NLS-2$
        expression = expression.replaceAll("\\.", "\\\\."); //$NON-NLS-1$ //$NON-NLS-2$
        return expression;

    }
}
