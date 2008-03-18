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

import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.impl.ElementParameterTypeImpl;
import org.talend.designer.core.model.utils.emf.talendfile.impl.ElementValueTypeImpl;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC xtan when feature:2214, there rewrite the tFilterRow in java, and add a migrationTask. 
 */
public class UpgradeAttributestFilterRow extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public ExecutionResult executeOnProcess(ProcessItem item) {


        if (getProject().getLanguage() == ECodeLanguage.PERL) {

            return ExecutionResult.NOTHING_TO_DO;

        } else {

            String functionName = "FUNCTION";
            String operatorName = "OPERATOR";

            String[][] replaceFuntions = new String[][] { { "S_VALUE_OF", "" }, { "N_VALUE_OF_FLOAT", "" },
                    { "N_VALUE_OF_INTEGER", "" }, { "ABS_VALUE_FLOAT", "Math.abs($source) $operator $target" },
                    { "ABS_VALUE_INTEGER", "Math.abs($source) $operator $target" },
                    { "LC", "$source == null? false : $source.toLowerCase().compareTo($target) $operator 0" },
                    { "UC", "$source == null? false : $source.toUpperCase().compareTo($target) $operator 0" },
                    { "LCFIRST", "$source == null? false : $source.toLowerCase().charAt(0).compareTo($target) $operator 0" },
                    { "UCFIRST", "$source == null? false : $source.toUpperCase().charAt(0).compareTo($target) $operator 0" },
                    { "LENGTH", "$source == null? false : $source.length() $operator $target" } };

            String[][] replaceOperator = new String[][] { { "EQ", "==" }, { "NE", "!=" }, { "GT", ">" }, { "LT", "<" },
                    { "GE", ">=" }, { "LE", "<=" }, { "MATCH", "==" }, { "NMATCH", "!=" } };

            boolean isModified = false;

            ProcessType processType = item.getProcess();

            NodeType tFilterRow = null;
            for (Object oNodeType : processType.getNode()) {
                NodeType nodeType = (NodeType) oNodeType;
                if (nodeType.getComponentName().equals("tFilterRow")) {
                    tFilterRow = nodeType;
                    break;
                }
            }

            if (tFilterRow != null) {

                EList elementParameter = tFilterRow.getElementParameter();
                for (Object object : elementParameter) {
                    ElementParameterTypeImpl parameter = (ElementParameterTypeImpl) object;
                    if (parameter.getName().equals("CONDITIONS")) {
                        EList elementValue = parameter.getElementValue();
                        ElementValueTypeImpl lastFunctionForMatch = null;
                        for (Object object2 : elementValue) {

                            ElementValueTypeImpl tableElement = (ElementValueTypeImpl) object2;
                            if (tableElement.getElementRef().equals(functionName)) {
                                for (String[] element : replaceFuntions) {
                                    if (element[0].equals(tableElement.getValue())) {
                                        tableElement.setValue(element[1]);
                                        lastFunctionForMatch = tableElement;
                                        isModified = true;
                                    }
                                }

                            } else if (tableElement.getElementRef().equals(operatorName)) {

                                for (String[] element : replaceOperator) {
                                    // the old version "MATCH" belong to "operator list", now it belong to "function
                                    // list"
                                    if ("MATCH".equals(tableElement.getValue()) || "NMATCH".equals(tableElement.getValue())) {
                                        if (lastFunctionForMatch != null) {
                                            lastFunctionForMatch
                                                    .setValue("$source == null? false : $source.matches($target) $operator true");
                                            isModified = true;
                                        }
                                    }

                                    if (element[0].equals(tableElement.getValue())) {
                                        tableElement.setValue(element[1]);
                                        isModified = true;
                                    }

                                }
                            }

                        }
                    } else if (parameter.getName().equals("LOGICAL_OP")) {
                        if (parameter.getValue().equals("AND")) {
                            parameter.setValue("&&");
                            isModified = true;
                        } else if (parameter.getValue().equals("OR")) {
                            parameter.setValue("||");
                            isModified = true;
                        }
                    }
                }

            }

            if (isModified) {

                IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IRepositoryService.class);
                IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
                try {
                    factory.save(item, true);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                    return ExecutionResult.FAILURE;
                }
            }

            return ExecutionResult.SUCCESS_NO_ALERT;
        }
    
    }
    
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2008, 2, 17, 12, 0, 0);
        return gc.getTime();
    }
}
