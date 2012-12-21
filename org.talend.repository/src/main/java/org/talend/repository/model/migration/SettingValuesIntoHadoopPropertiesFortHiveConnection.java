// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
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

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * 
 * created by jjzhou on 2012-12-21 Detailled comment
 * 
 */

public class SettingValuesIntoHadoopPropertiesFortHiveConnection extends AbstractJobMigrationTask {

    public ExecutionResult execute(Item item) {
        ProcessType processType = getProcessType(item);
        try {
            getHadoopPropertiesValues(item, processType);
            getHadoopAdvancedPropertiesValues(item, processType);
            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    /**
     * 
     * DOC talend Comment method "getHadoopPropertiesValues".
     * 
     * @param item
     * @param processType
     * @throws PersistenceException
     */
    private void getHadoopPropertiesValues(Item item, ProcessType processType) throws PersistenceException {
        EList node = processType.getNode();
        Map<String, String> map = new HashMap<String, String>();
        String connectionName = null;
        String mapredJt = null;
        String fsDefaultName = null;
        for (Object n : node) {
            NodeType type = (NodeType) n;
            if (type.getComponentName().equals("tELTHiveMap")) { //$NON-NLS-1$
                EList elementParameterList = type.getElementParameter();
                for (Object elem : elementParameterList) {
                    ElementParameterType elemType = (ElementParameterType) elem;
                    if (elemType.getName().equals("USE_EXISTING_CONNECTION") && elemType.getValue().equals("true")) { //$NON-NLS-1$ //$NON-NLS-2$
                        for (Object elem1 : elementParameterList) {
                            ElementParameterType elemType1 = (ElementParameterType) elem1;
                            if (elemType1.getName().equals("SET_MAPRED_JT") && elemType1.getValue().equals("true")) { //$NON-NLS-1$ //$NON-NLS-2${
                                for (Object elem2 : elementParameterList) {
                                    ElementParameterType elemType2 = (ElementParameterType) elem2;
                                    if (elemType2.getName().equals("CONNECTION")) { //$NON-NLS-1$
                                        connectionName = elemType2.getValue();
                                    } else if (elemType2.getName().equals("MAPRED_JT")) { //$NON-NLS-1$
                                        mapredJt = elemType2.getValue();
                                    }
                                    if (connectionName != null && mapredJt != null) {
                                        map.put(connectionName, mapredJt);
                                        setJobTrackerValueForHadoopProperties(item, processType, map);
                                        mapredJt = null;
                                        map.clear();
                                    }
                                }
                            } else if (elemType1.getName().equals("SET_FS_DEFAULT_NAME") && elemType1.getValue().equals("true")) { //$NON-NLS-1$ //$NON-NLS-2${
                                for (Object elem2 : elementParameterList) {
                                    ElementParameterType elemType2 = (ElementParameterType) elem2;
                                    if (elemType2.getName().equals("CONNECTION")) { //$NON-NLS-1$
                                        connectionName = elemType2.getValue();
                                    } else if (elemType2.getName().equals("FS_DEFAULT_NAME")) { //$NON-NLS-1$
                                        fsDefaultName = elemType2.getValue();
                                    }
                                    if (connectionName != null && fsDefaultName != null) {
                                        map.put(connectionName, fsDefaultName);
                                        setNameNodeValueForHadoopProperties(item, processType, map);
                                        fsDefaultName = null;
                                        map.clear();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * DOC talend Comment method "setJobTrackerValueForHadoopProperties".
     * 
     * @param item
     * @param processType
     * @param map
     * @return
     * @throws PersistenceException
     */
    private boolean setJobTrackerValueForHadoopProperties(Item item, ProcessType processType, Map<String, String> map)
            throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        boolean modified = false;
        String set_value = null;
        EList node = processType.getNode();
        for (Object n : node) {
            NodeType type = (NodeType) n;
            if (type.getComponentName().equals("tHiveConnection")) { //$NON-NLS-1$
                EList elementParameterList = type.getElementParameter();
                for (Object elem : elementParameterList) {
                    ElementParameterType elemType = (ElementParameterType) elem;
                    if (elemType.getName().equals("UNIQUE_NAME") && map.get(elemType.getValue()) != null) { //$NON-NLS-1$
                        set_value = map.get(elemType.getValue());
                        for (Object elem1 : elementParameterList) {
                            ElementParameterType elemType1 = (ElementParameterType) elem1;
                            if (elemType1.getName().equals("MAPRED_JT")) { //$NON-NLS-1$
                                ComponentUtilities.setNodeValue(type, elemType1.getName(), set_value);
                                modified = true;
                            } else if (elemType1.getName().equals("SET_MAPRED_JT")) { //$NON-NLS-1$
                                ComponentUtilities.setNodeValue(type, elemType1.getName(), "true"); //$NON-NLS-1$
                                modified = true;
                            }
                        }
                    }
                }
            }
        }
        if (modified) {
            factory.save(item, true);
        }
        return modified;
    }

    /**
     * 
     * DOC talend Comment method "setNameNodeValueForHadoopProperties".
     * 
     * @param item
     * @param processType
     * @param map
     * @return
     * @throws PersistenceException
     */
    private boolean setNameNodeValueForHadoopProperties(Item item, ProcessType processType, Map<String, String> map)
            throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        boolean modified = false;
        String set_value = null;
        EList node = processType.getNode();
        for (Object n : node) {
            NodeType type = (NodeType) n;
            if (type.getComponentName().equals("tHiveConnection")) { //$NON-NLS-1$
                EList elementParameterList = type.getElementParameter();
                for (Object elem : elementParameterList) {
                    ElementParameterType elemType = (ElementParameterType) elem;
                    if (elemType.getName().equals("UNIQUE_NAME") && map.get(elemType.getValue()) != null) { //$NON-NLS-1$
                        set_value = map.get(elemType.getValue());
                        for (Object elem1 : elementParameterList) {
                            ElementParameterType elemType1 = (ElementParameterType) elem1;
                            if (elemType1.getName().equals("FS_DEFAULT_NAME")) { //$NON-NLS-1$
                                ComponentUtilities.setNodeValue(type, elemType1.getName(), set_value);
                                modified = true;
                            } else if (elemType1.getName().equals("SET_FS_DEFAULT_NAME")) { //$NON-NLS-1$
                                ComponentUtilities.setNodeValue(type, elemType1.getName(), "true"); //$NON-NLS-1$
                                modified = true;
                            }
                        }
                    }
                }
            }
        }
        if (modified) {
            factory.save(item, true);
        }
        return modified;
    }

    /**
     * 
     * DOC talend Comment method "getHadoopAdvancedPropertiesValues".
     * 
     * @param item
     * @param processType
     * @throws PersistenceException
     */
    private void getHadoopAdvancedPropertiesValues(Item item, ProcessType processType) throws PersistenceException {
        EList node = processType.getNode();
        java.util.List<ElementValueType> hadoopAdvancedProperties = new ArrayList<ElementValueType>();
        Map<String, java.util.List<ElementValueType>> map = new HashMap<String, java.util.List<ElementValueType>>();
        String connectionName = null;
        for (Object n : node) {
            NodeType type = (NodeType) n;
            if (type.getComponentName().equals("tELTHiveMap")) { //$NON-NLS-1$
                EList elementParameterList = type.getElementParameter();

                for (Object elem : elementParameterList) {
                    ElementParameterType elemType = (ElementParameterType) elem;
                    if (elemType.getName().equals("USE_EXISTING_CONNECTION") && elemType.getValue().equals("true")) { //$NON-NLS-1$ //$NON-NLS-2$
                        for (Object elem1 : elementParameterList) {
                            ElementParameterType elemType1 = (ElementParameterType) elem1;
                            if (elemType1.getName().equals("HADOOP_ADVANCED_PROPERTIES")) { //$NON-NLS-1$
                                EList<ElementValueType> elemValue1 = elemType1.getElementValue();
                                ElementValueType columnNamePropertyElement = null;
                                ElementValueType columnNameValueElement = null;
                                if (elemValue1 != null && elemValue1.size() > 0) {
                                    for (int i = 0; i < elemValue1.size(); i++) {
                                        columnNamePropertyElement = TalendFileFactory.eINSTANCE.createElementValueType();
                                        columnNamePropertyElement.setElementRef("PROPERTY"); //$NON-NLS-1$
                                        columnNamePropertyElement.setValue(elemValue1.get(i).getValue());
                                        hadoopAdvancedProperties.add(columnNamePropertyElement);
                                        columnNameValueElement = TalendFileFactory.eINSTANCE.createElementValueType();
                                        columnNameValueElement.setElementRef("VALUE"); //$NON-NLS-1$
                                        columnNameValueElement.setValue(elemValue1.get(++i).getValue());
                                        hadoopAdvancedProperties.add(columnNameValueElement);
                                    }
                                }
                            } else if (elemType1.getName().equals("CONNECTION")) { //$NON-NLS-1$
                                connectionName = elemType1.getValue();
                            }
                        }

                        if (connectionName != null && hadoopAdvancedProperties.size() > 0) {
                            map.put(connectionName, hadoopAdvancedProperties);
                            setValueForHadoopAdvancedProperties(item, processType, map);
                            hadoopAdvancedProperties.clear();
                            map.clear();
                        }
                        break;
                    }
                }
            }
            connectionName = null;
        }
    }

    /**
     * 
     * DOC talend Comment method "setValueForHadoopAdvancedProperties".
     * 
     * @param item
     * @param processType
     * @param map
     * @return
     * @throws PersistenceException
     */
    public boolean setValueForHadoopAdvancedProperties(Item item, ProcessType processType,
            Map<String, java.util.List<ElementValueType>> map) throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        java.util.List<ElementValueType> oneHadoopAdvancedProperties = null;
        boolean modified = false;

        EList node = processType.getNode();
        for (Object n : node) {
            NodeType type = (NodeType) n;
            if (type.getComponentName().equals("tHiveConnection")) { //$NON-NLS-1$
                EList elementParameterList = type.getElementParameter();
                for (Object elem : elementParameterList) {
                    ElementParameterType elemType = (ElementParameterType) elem;
                    if (elemType.getName().equals("UNIQUE_NAME") && map.get(elemType.getValue()) != null) { //$NON-NLS-1$
                        oneHadoopAdvancedProperties = map.get(elemType.getValue());
                        for (Object elem1 : elementParameterList) {
                            ElementParameterType elemType1 = (ElementParameterType) elem1;
                            if (elemType1.getName().equals("HADOOP_ADVANCED_PROPERTIES")) { //$NON-NLS-1$
                                if (oneHadoopAdvancedProperties != null && oneHadoopAdvancedProperties.size() > 0) {
                                    ComponentUtilities.setNodeProperty(type, elemType1.getName(), oneHadoopAdvancedProperties);
                                    modified = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (modified) {
            factory.save(item, true);
        }
        return modified;
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2012, 11, 21, 12, 41, 10);
        return gc.getTime();
    }
}
