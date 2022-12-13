package org.talend.repository.model.migration;

import java.util.*;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.*;


public class AddPubkeyAcceptedKeyTypesToFTP extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     *
     * @see org.talend.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2022, 11, 13, 17, 0, 0);
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

                                if (useSFTP && (useExistConnection == null || "false". equals(useExistConnection)) && ifConfigClient) {
                                	
                                	final ElementParameterType client_parameters = ComponentUtilities.getNodeProperty(node, "CLIENT_PARAMETERS");
                                    boolean ifHasKey = false;
                                    final EList configurations = client_parameters.getElementValue();
                                    final Iterator iterator = configurations.iterator();
                                    while (iterator.hasNext()){
                                        final ElementValueType next = (ElementValueType)iterator.next();
                                        if(next.getValue().trim().equalsIgnoreCase("\"PubkeyAcceptedKeyTypes\"")){
                                            ifHasKey = true;
                                        }
                                    }
                                    
                                    if (!ifHasKey) {

	                                    TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;
	
	                                    ElementValueType elementKey = fileFact.createElementValueType();
	                                    elementKey.setElementRef("PARAMETER"); //$NON-NLS-1$
	                                    elementKey.setValue("\"PubkeyAcceptedKeyTypes\"");
	                                    configurations.add(elementKey);
	
	                                    ElementValueType elementValue = fileFact.createElementValueType();
	                                    elementValue.setElementRef("VALUE"); //$NON-NLS-1$
	                                    elementValue.setValue("\"ssh-rsa\"");
	                                    configurations.add(elementValue);
	
	                                    ComponentUtilities.setNodeProperty(node, "CLIENT_PARAMETERS", configurations);
                                    }
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