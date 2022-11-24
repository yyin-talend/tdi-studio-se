// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;


public class AddPubkeyAcceptedKeyTypesToFTPTPS5390 extends AbstractJobMigrationTask {
	
	/*
	 * (non-Javadoc)
	 *
	 * @see org.talend.migration.IMigrationTask#getOrder()
	 */
	public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2022, 10, 24, 11, 0, 0);
        return gc.getTime();
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.
     * ProcessItem)
     */
    @Override
    public ExecutionResult execute(Item item) {
        ProcessType processType = getProcessType(item);
        if (getProject().getLanguage() != ECodeLanguage.JAVA || processType == null) {
            return ExecutionResult.NOTHING_TO_DO;
        }
        String[] componentsName = new String[] { "tFTPConnection","tFTPDelete","tFTPFileExist","tFTPFileList",
        		"tFTPFileProperties","tFTPGet","tFTPPut","tFTPRename","tFTPTruncate" };

        try {
            boolean modified = false;
            for (int i = 0; i < componentsName.length; i++) {
                IComponentFilter filter = new NameComponentFilter(componentsName[i]);
                modified |= ModifyComponentsAction.searchAndModify(item, processType, filter,
                        Arrays.<IComponentConversion> asList(new IComponentConversion() {

                            public void transform(NodeType node) {
                            	String useExistConnection = ComponentUtilities.getNodePropertyValue(node, "USE_EXISTING_CONNECTION");
                            	boolean ifConfigClient = "true".equals(ComponentUtilities.getNodePropertyValue(node, "CONFIG_CLIENT"));
                            	boolean useSFTP = "true".equals(ComponentUtilities.getNodePropertyValue(node, "SFTP"));
                            	boolean ifHasKey = (ComponentUtilities.getNodePropertyValue(node, "__CLIENT_PARAMETERS__")).contains("PubkeyAcceptedKeyTypes");
                                if (useSFTP && (useExistConnection == null || "false". equals(useExistConnection)) 
                                		&& ifConfigClient && !ifHasKey) {
                                	
                                	List<ElementValueType> values = new ArrayList<ElementValueType>();
                                    TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;
                                	
                                	ElementValueType elementKey = fileFact.createElementValueType();
                                    elementKey.setElementRef("PARAMETER"); //$NON-NLS-1$
                                    elementKey.setValue("\"PubkeyAcceptedKeyTypes\"");
                                    values.add(elementKey);
                                    
                                    ElementValueType elementValue = fileFact.createElementValueType();
                                    elementValue.setElementRef("VALUE"); //$NON-NLS-1$
                                    elementValue.setValue("\"ssh-rsa\"");
                                    values.add(elementValue);
                                    ComponentUtilities.setNodeProperty(node, "CLIENT_PARAMETERS", values);
                                }
                            }

                        }));
            }

            if (modified) {
                return ExecutionResult.SUCCESS_NO_ALERT;
            } else {
                return ExecutionResult.NOTHING_TO_DO;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }
}