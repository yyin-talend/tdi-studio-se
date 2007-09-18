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
package org.talend.designer.core.ui.editor.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyComposite;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.IExtendedTableCommand;
import org.talend.commons.utils.data.container.Content;
import org.talend.commons.utils.data.container.ContentList;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.threading.ExecutionLimiter;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.connections.ConnectionLabel;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.outline.NodeReturnsTreeEditPart;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DynamicTabbedPropertySection extends AbstractPropertySection {

	protected MultiPageTalendEditor part;

	protected Element elem;

	protected Composite composite;

	protected BidiMap hashCurControls;

	protected String currentComponent;

	protected EComponentCategory section;

	protected int curRowSize;

	protected DynamicTabbedPropertyGenerator generator = DynamicTabbedPropertyGenerator
			.getDefault();

	private String oldProcessType;

	private Map<String, IMetadataTable> repositoryTableMap;

	private Map<String, ConnectionItem> repositoryConnectionItemMap;

	private Map<String, IRepositoryObject> processMap;

	private String oldPropertyType;

	private Map<String, Query> repositoryQueryStoreMap;

	private String oldQueryStoreType;

	private static CommandStackEventListener commandStackEventListener;

	private Map<String, String> tableIdAndDbTypeMap;

	private Map<String, String> tableIdAndDbSchemaMap;

	/**
	 * ftang Comment method "showQueryStoreRepositoryList".
	 * 
	 * @param show
	 */
	private void showQueryStoreRepositoryList(boolean show) {
		for (int i = 0; i < elem.getElementParameters().size(); i++) {
			IElementParameter param = elem.getElementParameters().get(i);
			if (param.getName().equals(
					EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName())) {
				param.setShow(show);
			}
		}
	}

	/**
	 * ftang Comment method "showPropertyRepositoryList".
	 * 
	 * @param show
	 *            boolean
	 */
	private void showPropertyRepositoryList(boolean show) {
		for (int i = 0; i < elem.getElementParameters().size(); i++) {
			IElementParameter param = elem.getElementParameters().get(i);
			if (param.getName().equals(
					EParameterName.REPOSITORY_PROPERTY_TYPE.getName())) {
				param.setShow(show);
			}
		}
	}

	/**
	 * ftang Comment method "updateProcessList".
	 */
	private void updateProcessList() {
		List<String> processNameList = new ArrayList<String>();
		List<String> processValueList = new ArrayList<String>();
		processMap = new HashMap<String, IRepositoryObject>();

		IProxyRepositoryFactory factory = DesignerPlugin.getDefault()
				.getProxyRepositoryFactory();
		try {
			RootContainer<String, IRepositoryObject> processContainer = factory
					.getProcess();
			ContentList<String, IRepositoryObject> processAbsoluteMembers = processContainer
					.getAbsoluteMembers();

			String currentProcess = part.getTalendEditor().getProcess()
					.getLabel();
			for (Content<String, IRepositoryObject> object : processAbsoluteMembers
					.values()) {
				IRepositoryObject process = (IRepositoryObject) object
						.getContent();
				if (factory.getStatus(process) != ERepositoryStatus.DELETED
						&& !currentProcess.equals(process.getLabel())) {
					String path = object.getParent().getPath().toString();
					String name;
					if (path.equals("")) { //$NON-NLS-1$
						name = IPath.SEPARATOR + process.getLabel();
					} else {
						name = IPath.SEPARATOR + path + IPath.SEPARATOR
								+ process.getLabel();
					}
					processNameList.add(name);
					processMap.put(name, process);
				}
			}
		} catch (PersistenceException e) {
			e.printStackTrace();
		}

		List<String> tempFolderList = new ArrayList<String>();
		List<String> tempProcessNameList = new ArrayList<String>();
		tempProcessNameList.addAll(processNameList);

		for (String string : tempProcessNameList) {
			// Put jobs which in a folder into a new list.s
			if (string.lastIndexOf("/") != 0) {
				tempFolderList.add(string);
				processNameList.remove(string);
			}
		}

		sortList(processNameList);
		sortList(tempFolderList);

		// Always put the jobs which in a folder on the top of the job list
		tempFolderList.addAll(processNameList);

		processNameList = tempFolderList;

		for (String name : (List<String>) processNameList) {
			IRepositoryObject process = processMap.get(name);
			processValueList.add(process.getLabel()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		String[] processTableNameList = (String[]) processNameList
				.toArray(new String[0]);
		String[] processTableValueList = (String[]) processValueList
				.toArray(new String[0]);

		for (int i = 0; i < elem.getElementParameters().size(); i++) {
			IElementParameter param = elem.getElementParameters().get(i);
			if (param.getName().equals(
					EParameterName.PROCESS_TYPE_PROCESS.getName())) {
				param.setListItemsDisplayName(processTableNameList);
				param.setListItemsValue(processTableValueList);
				if (elem instanceof Node) {
					((Node) elem).checkAndRefreshNode();
				}
			}
		}
	}

	/**
	 * Sort the element order of the inputted list.
	 * 
	 * @param compareList
	 */
	private void sortList(List<String> compareList) {
		Collections.sort(compareList, new Comparator<String>() {

			public int compare(String str1, String str2) {

				// For example: avoid job name "a_b_c" before "a_b" in the job
				// list.
				String newStr1 = str1.replaceAll("_", " ");
				String newStr2 = str2.replaceAll("_", " ");
				return newStr1.compareToIgnoreCase(newStr2);
			}
		});
	}

	/**
	 * ftang Comment method "updateContextList".
	 */
	private void updateContextList() {
		List<String> contextNameList = new ArrayList<String>();
		List<String> contextValueList = new ArrayList<String>();
		IProxyRepositoryFactory factory = DesignerPlugin.getDefault()
				.getProxyRepositoryFactory();

		String selectedProcess = null;

		for (int i = 0; (i < elem.getElementParameters().size())
				&& (selectedProcess == null); i++) {
			IElementParameter param = elem.getElementParameters().get(i);
			if (param.getName().equals(
					EParameterName.PROCESS_TYPE_PROCESS.getName())) {
				selectedProcess = (String) param.getValue();
			}
		}

		if (selectedProcess == null) {
			return;
		}

		try {
			List<IRepositoryObject> list = factory
					.getAll(ERepositoryObjectType.PROCESS);

			for (IRepositoryObject process : list) {
				String id = process.getLabel();
				if (selectedProcess.equals(id)) {
					if (process.getProperty().getItem() instanceof ProcessItem) {
						ProcessItem processItem = (ProcessItem) process
								.getProperty().getItem();

						for (Object o : processItem.getProcess().getContext()) {
							if (o instanceof ContextType) {
								ContextType context = (ContextType) o;
								contextNameList.add(context.getName());
								contextValueList.add(context.getName()); //$NON-NLS-1$ //$NON-NLS-2$
							}
						}
					}
				}
			}
		} catch (PersistenceException e) {
			ExceptionHandler.process(e);
		}

		String[] contextTableNameList = (String[]) contextNameList
				.toArray(new String[0]);
		String[] contextTableValueList = (String[]) contextValueList
				.toArray(new String[0]);

		for (int i = 0; i < elem.getElementParameters().size(); i++) {
			IElementParameter param = elem.getElementParameters().get(i);
			if (param.getName().equals(
					EParameterName.PROCESS_TYPE_CONTEXT.getName())) {
				param.setListItemsDisplayName(contextTableNameList);
				param.setListItemsValue(contextTableValueList);
				if (!contextValueList.contains(param.getValue())) {
					if (contextTableNameList.length > 0) {
						elem.setPropertyValue(
								EParameterName.PROCESS_TYPE_CONTEXT.getName(),
								contextTableValueList[0]);
					}
				} else {
					// force to store the value again to activate the code
					// generation in Node.setPropertyValue
					elem.setPropertyValue(EParameterName.PROCESS_TYPE_CONTEXT
							.getName(), param.getValue());
				}
			}
		}

	}

	private String getRepositoryAliasName(ConnectionItem connectionItem) {
		ERepositoryObjectType repositoryObjectType = ERepositoryObjectType
				.getItemType(connectionItem);
		String aliasName = repositoryObjectType.getAlias();
		Connection connection = (Connection) connectionItem.getConnection();
		if (connection instanceof DatabaseConnection) {
			String currentDbType = (String) RepositoryToComponentProperty
					.getValue(connection, "TYPE"); //$NON-NLS-1$
			aliasName += " (" + currentDbType + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return aliasName;
	}

	private Map<String, List<String>> tablesMap = new HashMap<String, List<String>>();

	private Map<String, List<String>> queriesMap = new HashMap<String, List<String>>();

	/**
	 * ftang Comment method "updateRepositoryList".
	 */
	@SuppressWarnings("unchecked")//$NON-NLS-1$
	public void updateRepositoryList() {
		IProxyRepositoryFactory factory = DesignerPlugin.getDefault()
				.getProxyRepositoryFactory();
		tableIdAndDbTypeMap = new HashMap<String, String>();
		tableIdAndDbSchemaMap = new HashMap<String, String>();
		List<ConnectionItem> metadataConnectionsItem = null;
		String[] repositoryTableNameList = new String[] {};
		String[] repositoryTableValueList = new String[] {};
		String[] repositoryConnectionNameList = new String[] {};
		String[] repositoryConnectionValueList = new String[] {};
		String[] repositoryQueryNameList = new String[] {};
		String[] repositoryQueryValueList = new String[] {};
		try {
			metadataConnectionsItem = factory.getMetadataConnectionsItem();
		} catch (PersistenceException e) {
			throw new RuntimeException(e);
		}
		if (metadataConnectionsItem != null) {
			repositoryTableMap.clear();
			repositoryQueryStoreMap.clear();
			repositoryConnectionItemMap.clear();

			tablesMap.clear();
			queriesMap.clear();
			List<String> tableNamesList = new ArrayList<String>();
			List<String> tableValuesList = new ArrayList<String>();
			List<String> queryStoreNameList = new ArrayList<String>();
			List<String> queryStoreValuesList = new ArrayList<String>();
			for (ConnectionItem connectionItem : metadataConnectionsItem) {
				Connection connection = (Connection) connectionItem
						.getConnection();
				if (!connection.isReadOnly()) {
					repositoryConnectionItemMap.put(connectionItem
							.getProperty().getId()
							+ "", connectionItem); //$NON-NLS-1$
					for (Object tableObj : connection.getTables()) {
						org.talend.core.model.metadata.builder.connection.MetadataTable table;

						table = (org.talend.core.model.metadata.builder.connection.MetadataTable) tableObj;

						if (factory.getStatus(connectionItem) != ERepositoryStatus.DELETED) {
							if (!factory.isDeleted(table)) {
								String name = getRepositoryAliasName(connectionItem)
										+ ":" //$NON-NLS-1$
										+ connectionItem.getProperty()
												.getLabel()
										+ " - " + table.getLabel(); //$NON-NLS-1$
								String value = connectionItem.getProperty()
										.getId()
										+ " - " + table.getLabel(); //$NON-NLS-1$
								IMetadataTable newTable = ConvertionHelper
										.convert(table);
								repositoryTableMap.put(value, newTable);
								if (connection instanceof DatabaseConnection) {
									String dbType = ((DatabaseConnection) connection)
											.getDatabaseType();
									String schema = ((DatabaseConnection) connection)
											.getSchema();
									tableIdAndDbTypeMap.put(newTable.getId(),
											dbType);
									if (schema != null && !schema.equals("")) {
										tableIdAndDbSchemaMap.put(newTable
												.getId(), schema);
									}
								}
								addOrderDisplayNames(tableValuesList,
										tableNamesList, value, name);
								// tableNamesList.add(name);
								// tableValuesList.add(value);
							}
						}
					}
				}
				tablesMap.put(connectionItem.getProperty().getId(),
						tableValuesList);
				if (connection instanceof DatabaseConnection
						&& !connection.isReadOnly()) {
					DatabaseConnection dbConnection = (DatabaseConnection) connection;
					QueriesConnection queriesConnection = dbConnection
							.getQueries();
					if (queriesConnection != null) {
						List<Query> qs = queriesConnection.getQuery();
						for (Query query : qs) {
							String name = getRepositoryAliasName(connectionItem)
									+ ":" //$NON-NLS-1$
									+ connectionItem.getProperty().getLabel()
									+ " - " + query.getLabel(); //$NON-NLS-1$
							String value = connectionItem.getProperty().getId()
									+ " - " + query.getLabel(); //$NON-NLS-1$
							repositoryQueryStoreMap.put(value, query);
							addOrderDisplayNames(queryStoreValuesList,
									queryStoreNameList, value, name);
							// queryStoreNameList.add(name);
							// queryStoreValuesList.add(value);
						}
					}
				}
				queriesMap.put(connectionItem.getProperty().getId(),
						queryStoreValuesList);
			}
			repositoryTableNameList = (String[]) tableNamesList
					.toArray(new String[0]);
			repositoryTableValueList = (String[]) tableValuesList
					.toArray(new String[0]);
			repositoryQueryNameList = (String[]) queryStoreNameList
					.toArray(new String[0]);
			repositoryQueryValueList = (String[]) queryStoreValuesList
					.toArray(new String[0]);
		}
		initMaps();
		for (int i = 0; i < elem.getElementParameters().size(); i++) {
			IElementParameter param = elem.getElementParameters().get(i);
			if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
				IElementParameter repositorySchemaType = param
						.getChildParameters()
						.get(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
				repositorySchemaType
						.setListItemsDisplayName(repositoryTableNameList);
				repositorySchemaType
						.setListItemsValue(repositoryTableValueList);
				if (!repositoryTableMap.keySet().contains(
						repositorySchemaType.getValue())) {
					List<String> list2 = tablesMap
							.get(elem
									.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
											.getName()));
					boolean isNeeded = list2 != null && !list2.isEmpty();
					if (repositoryTableNameList.length > 0
							&& repositoryConnectionValueList.length > 0
							&& isNeeded) {
						repositorySchemaType.setValue(getDefaultRepository(
								param, true, repositoryConnectionValueList[0]));
						// elem.setPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName(),
						// getDefaultRepository(
						// true, repositoryConnectionValueList[0]));
					}
				}
			}
			// if
			// (param.getName().equals(EParameterName.REPOSITORY_SCHEMA_TYPE.getName()))
			// {
			// param.setListItemsDisplayName(repositoryTableNameList);
			// param.setListItemsValue(repositoryTableValueList);
			// if (!repositoryTableMap.keySet().contains(param.getValue())) {
			// List<String> list2 =
			// tablesMap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
			// .getName()));
			// boolean isNeeded = list2 != null && !list2.isEmpty();
			// if (repositoryTableNameList.length > 0 &&
			// repositoryConnectionValueList.length > 0 && isNeeded) {
			// elem.setPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName(),
			// getDefaultRepository(
			// true, repositoryConnectionValueList[0]));
			// }
			// }
			// }
			if (param.getName().equals(
					EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName())) {
				param.setListItemsDisplayName(repositoryQueryNameList);
				param.setListItemsValue(repositoryQueryValueList);
				if (!repositoryQueryStoreMap.keySet()
						.contains(param.getValue())) {
					List<String> list2 = queriesMap
							.get(elem
									.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
											.getName()));
					boolean isNeeded = list2 != null && !list2.isEmpty();
					if (repositoryQueryNameList.length > 0
							&& repositoryConnectionValueList.length > 0
							&& isNeeded) {
						elem
								.setPropertyValue(
										EParameterName.REPOSITORY_QUERYSTORE_TYPE
												.getName(),
										getDefaultRepository(
												elem
														.getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE),
												false,
												repositoryConnectionValueList[0]));
					}
				}
			}
			if (param.getName().equals(
					EParameterName.REPOSITORY_PROPERTY_TYPE.getName())) {
				String repositoryValue = elem.getElementParameter(
						EParameterName.PROPERTY_TYPE.getName())
						.getRepositoryValue();
				if (repositoryValue != null) {
					List<String> connectionNamesList = new ArrayList<String>();
					List<String> connectionValuesList = new ArrayList<String>();
					for (String key : repositoryConnectionItemMap.keySet()) {
						ConnectionItem connectionItem = repositoryConnectionItemMap
								.get(key);
						Connection connection = (Connection) connectionItem
								.getConnection();
						String name = getRepositoryAliasName(connectionItem)
								+ ":" //$NON-NLS-1$
								+ connectionItem.getProperty().getLabel();
						if ((connection instanceof DelimitedFileConnection)
								&& (repositoryValue.equals("DELIMITED"))) { //$NON-NLS-1$
							addOrderDisplayNames(connectionValuesList,
									connectionNamesList, key, name);
						}
						if ((connection instanceof PositionalFileConnection)
								&& (repositoryValue.equals("POSITIONAL"))) { //$NON-NLS-1$
							addOrderDisplayNames(connectionValuesList,
									connectionNamesList, key, name);
						}
						if ((connection instanceof RegexpFileConnection)
								&& (repositoryValue.equals("REGEX"))) { //$NON-NLS-1$
							addOrderDisplayNames(connectionValuesList,
									connectionNamesList, key, name);
						}
						if ((connection instanceof XmlFileConnection)
								&& (repositoryValue.equals("XML"))) { //$NON-NLS-1$
							addOrderDisplayNames(connectionValuesList,
									connectionNamesList, key, name);
						}
						if ((connection instanceof GenericSchemaConnection)
								&& (repositoryValue.equals("GENERIC"))) { //$NON-NLS-1$
							addOrderDisplayNames(connectionValuesList,
									connectionNamesList, key, name);
						}
                        if ((connection instanceof LDAPSchemaConnection)
                                && (repositoryValue.equals("LDAP"))) { //$NON-NLS-1$
                            addOrderDisplayNames(connectionValuesList,
                                    connectionNamesList, key, name);
                        }

						if ((connection instanceof DatabaseConnection)
								&& (repositoryValue.startsWith("DATABASE"))) { //$NON-NLS-1$
							String currentDbType = (String) RepositoryToComponentProperty
									.getValue(connection, "TYPE"); //$NON-NLS-1$
							if (repositoryValue.contains(":")) { // database
								// is
								// specified
								// //$NON-NLS-1$
								String neededDbType = repositoryValue
										.substring(repositoryValue.indexOf(":") + 1); //$NON-NLS-1$
								if (neededDbType.equals(currentDbType)) {
									addOrderDisplayNames(connectionValuesList,
											connectionNamesList, key, name);
								}
							} else {
								addOrderDisplayNames(connectionValuesList,
										connectionNamesList, key, name);
							}
						}
					}
					// if (elem instanceof Node) {
					// if (("".equals(repositoryPropType) ||
					// !currentNodeUName.equals(((Node) elem).getUniqueName())))
					// {
					// getDefaultRepository(true, null);
					// } else {
					// if (!repositoryPropType.equals(elem
					// .getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName())))
					// {
					// if (repositoryTableNameList.length > 0) {
					// List<String> list2 = tablesMap.get(elem
					// .getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()));
					// if (list2 != null && !list2.isEmpty()) {
					// elem.setPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName(),
					// getDefaultRepository(true, null));
					// }
					// }
					// if (repositoryQueryNameList.length > 0) {
					// List<String> list2 = queriesMap.get(elem
					// .getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()));
					// if (list2 != null && !list2.isEmpty()) {
					// elem.setPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName(),
					// getDefaultRepository(false, null));
					// }
					// }
					// }
					// }
					// }

					repositoryConnectionNameList = (String[]) connectionNamesList
							.toArray(new String[0]);
					repositoryConnectionValueList = (String[]) connectionValuesList
							.toArray(new String[0]);
				} else {
					List<String> connectionValuesList = new ArrayList<String>();
					List<String> connectionStringList = new ArrayList<String>();
					for (String key : repositoryConnectionItemMap.keySet()) {
						ConnectionItem connectionItem = repositoryConnectionItemMap
								.get(key);
						String name = connectionItem.getProperty().getLabel();
						addOrderDisplayNames(connectionValuesList,
								connectionStringList, key, name);
					}
					repositoryConnectionNameList = (String[]) connectionStringList
							.toArray(new String[0]);
					repositoryConnectionValueList = (String[]) connectionValuesList
							.toArray(new String[0]);
				}
				param.setListItemsDisplayName(repositoryConnectionNameList);
				param.setListItemsValue(repositoryConnectionValueList);
				if (!repositoryConnectionItemMap.keySet().contains(
						param.getValue())) {
					if (repositoryConnectionNameList.length > 0) {
						elem.setPropertyValue(
								EParameterName.REPOSITORY_PROPERTY_TYPE
										.getName(),
								repositoryConnectionValueList[0]);
					}
				}
			}
		}
		updateQuery();
	}

	/**
	 * qzhang Comment method "addOrderDisplayNames".
	 * 
	 * @param connectionValuesList
	 * @param connectionStringList
	 * @param key
	 * @param name
	 */
	private void addOrderDisplayNames(List<String> connectionValuesList,
			List<String> connectionStringList, String key, String name) {
		int i = 0;

		for (; i < connectionStringList.size(); i++) {
			String string = connectionStringList.get(i);
			if (name.compareTo(string) < 0) {
				connectionStringList.add(i, name);
				connectionValuesList.add(i, key);
				break;
			}
		}
		if (connectionStringList.size() == 0
				|| i == connectionStringList.size()) {
			connectionStringList.add(name);
			connectionValuesList.add(key);
		}
	}

	/**
	 * ftang Comment method "getElement".
	 * 
	 * @return an instance of Element
	 */
	public Element getElement() {
		return elem;
	}

	/**
	 * 
	 */
	private boolean checkErrorsWhenViewRefreshed;

	public void addComponents(boolean forceRedraw) {
		addComponents(forceRedraw, true);
	}

	/**
	 * Initialize all components for the defined section for this node.
	 */
	public void addComponents(boolean forceRedraw, boolean reInitialize) {
		registerListenerForRefreshingSection();
		checkErrorsWhenViewRefreshed = true;
		int heightSize = 0, maxRowSize = 0, nbInRow, numInRow;
		int estimatedHeightSize = 0, estimatedMaxRowSize = 0;
		int maxRow;
		List<? extends IElementParameter> listParam = elem
				.getElementParameters();

		oldQueryStoreType = (String) elem
				.getPropertyValue(EParameterName.QUERYSTORE_TYPE.getName());
		if (oldQueryStoreType != null) {
			if (oldQueryStoreType.equals(EmfComponent.REPOSITORY)) {
				showQueryStoreRepositoryList(true);
				updateRepositoryList();
			} else {
				showQueryStoreRepositoryList(false);
			}
		}

		IElementParameter param = elem
				.getElementParameter(EParameterName.PROPERTY_TYPE.getName());
		if (param != null) {
			oldPropertyType = (String) param.getValue();
			if (param.isShow(elem.getElementParameters())) {
				if (oldPropertyType.equals(EmfComponent.REPOSITORY)) {
					showPropertyRepositoryList(true);
					updateRepositoryList();
				} else {
					showPropertyRepositoryList(false);
				}
			} else {
				showPropertyRepositoryList(false);
			}
		}

		/*
		 * oldPropertyType = (String)
		 * elem.getPropertyValue(EParameterName.PROPERTY_TYPE.getName()); if
		 * (oldPropertyType != null) { if
		 * (oldPropertyType.equals(EmfComponent.REPOSITORY)) {
		 * showPropertyRepositoryList(true); updateRepositoryList(); } else {
		 * showPropertyRepositoryList(false); } }
		 */

		oldProcessType = (String) elem
				.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName());
		if (oldProcessType != null) {
			String[] list = elem.getElementParameter(
					EParameterName.PROCESS_TYPE_PROCESS.getName())
					.getListItemsDisplayName();
			if ((oldProcessType.equals("NO_PROCESS") || (list.length == 0))) { //$NON-NLS-1$
				updateProcessList();
				updateContextList();
				if (elem instanceof Node) {
					((Node) elem).checkAndRefreshNode();
				}
			}
		}

		IElementParameter encodingTypeParameter = elem
				.getElementParameter(EParameterName.ENCODING_TYPE.getName());
		if (encodingTypeParameter != null) {
			IElementParameter repositoryEncodingTypeParameter = elem
					.getElementParameter(EParameterName.REPOSITORY_ENCODING_TYPE
							.getName());
			String encodingType = (String) encodingTypeParameter.getValue();
			if (encodingType != null
					&& encodingType.equals(EmfComponent.ENCODING_TYPE_CUSTOM)) {
				repositoryEncodingTypeParameter.setShow(true);
			} else {
				repositoryEncodingTypeParameter.setShow(false);
			}
		}

		if (!forceRedraw) {
			boolean needRedraw = false;
			for (IElementParameter elementParameter : elem
					.getElementParametersWithChildrens()) {
				if (elementParameter.getCategory().equals(section)
						&& (elementParameter.getField() != EParameterFieldType.SCHEMA_TYPE)
						&& (elementParameter.getField() != EParameterFieldType.QUERYSTORE_TYPE)
						&& (elementParameter.getField() != EParameterFieldType.ENCODING_TYPE)) {
					// if the component must be displayed, then check if the
					// control exists or not.
					boolean show = elementParameter.isShow(elem
							.getElementParameters());
					Object control;
					if (elementParameter.getParentParameter() == null) {
						control = this.hashCurControls.get(elementParameter
								.getName());
					} else {
						control = this.hashCurControls.get(elementParameter
								.getParentParameter().getName()
								+ ":" + elementParameter.getName());
					}
					if ((control == null && show) || (control != null && !show)) {
						needRedraw = true;
						// System.out.println(elementParameter.getName() + "
						// need redraw");
					}
				}
			}
			if (!needRedraw) {
				// System.out.println("no need redraw");
				return;
			}
		}

		Control lastControl = null;
		if (reInitialize) {
			if (currentComponent != null) {
				Control[] ct = composite.getChildren();
				for (int i = 0; i < ct.length; i++) {
					ct[i].dispose();
				}
			}
		} else {
			Control[] ct = composite.getChildren();
			int curY = 0, maxY = 0;
			for (int i = 0; i < ct.length; i++) {
				curY = ct[i].getLocation().y
						+ ct[i].computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
				if (curY > maxY) {
					maxY = curY;
				}
			}
			heightSize = maxY;
		}

		hashCurControls = new DualHashBidiMap();

		maxRow = 0;
		for (int i = 0; i < listParam.size(); i++) {
			if (listParam.get(i).getCategory() == section) {
				if (listParam.get(i).getNumRow() > maxRow
						&& listParam.get(i).isShow(listParam)) {
					maxRow = listParam.get(i).getNumRow();
				}
			}
		}

		generator.initController(this);

		// System.out.println("********************** NEW ADDCOMPONENTS
		// **********************");
		TabbedPropertyComposite tabbedPropertyComposite = this
				.getTabbedPropertyComposite();
		int compositeHeight = tabbedPropertyComposite.getClientArea().height
				- tabbedPropertyComposite.getTitle().computeSize(SWT.DEFAULT,
						SWT.DEFAULT).y;

		// System.out.println("size composite:" + compositeHeight);

		int nbDynamic = 0;
		for (int curRow = 1; curRow <= maxRow; curRow++) {
			estimatedMaxRowSize = 0;
			for (int i = 0; i < listParam.size(); i++) {
				IElementParameter curParam = listParam.get(i);
				if (curParam.getCategory() == section) {
					if (curParam.getNumRow() == curRow
							&& (curParam.getField() != EParameterFieldType.TECHNICAL)) {
						// System.out.println("test:" + curParam.getName() + "
						// field:"+curParam.getField());
						if (curParam.isShow(listParam)) {
							// System.out.println("show:" + curParam.getName()+
							// " field:"+curParam.getField());
							AbstractElementPropertySectionController controller = generator
									.getController(curParam.getField(), this);

							if (controller == null) {
								break;
							}
							int estimatedSize = controller.estimateRowSize(
									composite, curParam);
							if (controller.hasDynamicRowSize()) {
								nbDynamic++;
							}
							// System.out.println("param:" + curParam.getName()
							// + " - estimatedSize:" + estimatedSize);

							if (estimatedSize > estimatedMaxRowSize) {
								estimatedMaxRowSize = estimatedSize;
							}
						}
					}
				}
			}
			estimatedHeightSize += estimatedMaxRowSize;
		}
		// System.out.println("*** ESTIMATED SIZE:" + estimatedHeightSize + "
		// ***");
		int emptySpace = compositeHeight - estimatedHeightSize;
		// System.out.println("--- EMPTY SPACE:" + emptySpace);
		int additionalHeightSize = 0;
		if (emptySpace > 0 && nbDynamic > 0) {
			additionalHeightSize = emptySpace / nbDynamic;
			// System.out.println("--- DIVIDED ADDITIONAL HEIGHT (for each
			// dynamic):" + additionalHeightSize);
		}

		for (int curRow = 1; curRow <= maxRow; curRow++) {
			maxRowSize = 0;
			nbInRow = 0;
			for (int i = 0; i < listParam.size(); i++) {
				IElementParameter curParam = listParam.get(i);
				if (curParam.getCategory() == section) {
					if (curParam.getNumRow() == curRow
							&& curParam.isShow(listParam)
							&& (curParam.getField() != EParameterFieldType.TECHNICAL)) {
						nbInRow++;
					}
				}
			}
			numInRow = 0;
			lastControl = null;
			curRowSize = 0;
			for (int i = 0; i < listParam.size(); i++) {
				IElementParameter curParam = listParam.get(i);
				if (curParam.getCategory() == section) {
					if (curParam.getNumRow() == curRow
							&& (curParam.getField() != EParameterFieldType.TECHNICAL)) {
						// System.out.println("test:" + curParam.getName() + "
						// field:"+curParam.getField());
						if (curParam.isShow(listParam)) {
							// System.out.println("show:" + curParam.getName()+
							// " field:"+curParam.getField());
							numInRow++;
							AbstractElementPropertySectionController controller = generator
									.getController(curParam.getField(), this);

							if (controller == null) {
								break;
							}
							if (controller.hasDynamicRowSize()) {
								controller
										.setAdditionalHeightSize(additionalHeightSize);
							}

							lastControl = controller.createControl(composite,
									curParam, numInRow, nbInRow, heightSize,
									lastControl);

							// System.out.println("param:" + curParam.getName()
							// + " - curRowSize:" + curRowSize);

							if (curRowSize > maxRowSize) {
								maxRowSize = curRowSize;
							}
						}
					}
				}
			}
			heightSize += maxRowSize;

		}
		// System.out.println("*** FINAL SIZE =" + heightSize + " ***");
		resizeScrolledComposite();
	}

	private TabbedPropertyComposite getTabbedPropertyComposite() {
		TabbedPropertyComposite tabbedPropertyComposite = null;
		Composite tmpComposite = composite;
		while (tabbedPropertyComposite == null) { // to retrieve the main
			// composite of the
			// TabbedProperties
			if (tmpComposite.getParent() instanceof TabbedPropertyComposite) {
				tabbedPropertyComposite = (TabbedPropertyComposite) tmpComposite
						.getParent();
			} else {
				tmpComposite = tmpComposite.getParent();
			}
		}
		return tabbedPropertyComposite;
	}

	/**
	 * added to fix the bugs 1107 & 1434. This code is added because the
	 * function resizeScrolledComposite is set as private in
	 * TabbedPropertySheetPage. So the code bellow will do the same operation
	 * but will access to specific eclipse functions.
	 */
	private void resizeScrolledComposite() {
		try {
			Point compositeSize = composite.computeSize(SWT.DEFAULT,
					SWT.DEFAULT);

			lastCompositeSize = getTabbedPropertyComposite().getClientArea().height;

			TabbedPropertyComposite tabbedPropertyComposite = getTabbedPropertyComposite();
			compositeSize.y += tabbedPropertyComposite.getTitle().computeSize(
					SWT.DEFAULT, SWT.DEFAULT).y;
			tabbedPropertyComposite.getScrolledComposite().setMinSize(
					compositeSize);
		} catch (org.eclipse.swt.SWTException e) {
			// nothing
		}
	}

	/*
	 * (non-Java)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#
	 *      createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(final Composite parent,
			final TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		composite = parent;

		FormLayout layout = new FormLayout();
		layout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
		layout.marginHeight = ITabbedPropertyConstants.VSPACE;
		layout.spacing = ITabbedPropertyConstants.VMARGIN + 1;
		composite.setLayout(layout);
	}

	public void refresh() {
		if (elem == null) {
			return;
		}
		List<? extends IElementParameter> listParam = elem
				.getElementParameters();

		if (oldProcessType != null) {
			String newProcessType = (String) elem
					.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS
							.getName());
			if (!oldProcessType.equals(newProcessType)) {
				updateProcessList();
				updateContextList();
				if (elem instanceof Node) {
					((Node) elem).checkAndRefreshNode();
				}
			}
		}

		Boolean updateNeeded = (Boolean) elem
				.getPropertyValue(EParameterName.UPDATE_COMPONENTS.getName());
		if (updateNeeded != null) {
			if (updateNeeded) {
				addComponents(false);
				elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS
						.getName(), new Boolean(false));
			}
		}

		for (int i = 0; i < listParam.size(); i++) {
			if (listParam.get(i).getCategory() == section) {
				if (listParam.get(i).isShow(listParam)) {
					AbstractElementPropertySectionController controller = generator
							.getController(listParam.get(i).getField(), this);
					if (controller != null) {
						controller.refresh(listParam.get(i),
								checkErrorsWhenViewRefreshed);
					}
				}
			}
		}
		composite.getParent().layout(true, true);
		checkErrorsWhenViewRefreshed = false;
	}

	int lastCompositeSize = 0;

	// private ISelection lastSelection;
	/*
	 * @Override (non-Java)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#
	 *      setInput(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void setInput(final IWorkbenchPart workbenchPart,
			final ISelection selection) {
		if (!(selection instanceof IStructuredSelection)) {
			return;
		}

		if (workbenchPart instanceof MultiPageTalendEditor) {
			part = (MultiPageTalendEditor) workbenchPart;
		} else {
			part = (MultiPageTalendEditor) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
		}
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		if (input instanceof NodeContainerPart) {
			NodeContainerPart nContainer = (NodeContainerPart) input;
			elem = (Element) nContainer.getParent().getModel();
		} else if (input instanceof NodeTreeEditPart) {
			NodeTreeEditPart nTreePart = (NodeTreeEditPart) input;
			elem = (Element) nTreePart.getModel();
		} else if (!(input instanceof NodeReturnsTreeEditPart)) {
			Assert.isTrue(input instanceof EditPart);
			EditPart editPart = (EditPart) input;
			elem = (Element) editPart.getModel();
		}
		if (elem instanceof NodeLabel) {
			elem = ((NodeLabel) elem).getNode();
		}

		if (elem instanceof ConnectionLabel) {
			elem = ((ConnectionLabel) elem).getConnection();
		}

		if (currentComponent == null) {
			addComponents(true);
		} else {
			if (!currentComponent.equals(elem.getElementName())) {
				addComponents(true);
			} else {
				updateRepositoryList();
				updateProcessList();
				updateContextList();
				refresh();
			}
		}
		currentComponent = elem.getElementName();

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
		PropertySheet sheet = (PropertySheet) view;
		if (sheet != null && sheet.getCurrentPage() != null) {
			if (sheet.getCurrentPage() instanceof TabbedPropertySheetPage) {
				TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet
						.getCurrentPage();
				Control control = tabbedPropertySheetPage.getControl();
				control.addListener(SWT.Resize, new Listener() {

					public void handleEvent(Event event) {
						refreshLimiter.resetTimer();
						refreshLimiter.startIfExecutable(true);
					}
				});
			}
		}
	}

	final ExecutionLimiter refreshLimiter = new ExecutionLimiter(250, true) {

		@Override
		public void execute(final boolean isFinalExecution) {
			if (composite.isDisposed()) {
				return;
			}

			composite.getDisplay().asyncExec(new Runnable() {

				public void run() {
					int currentSize = getTabbedPropertyComposite()
							.getClientArea().height;
					if (lastCompositeSize != currentSize) {
						// System.out.println("resize : currentSize=" +
						// currentSize + " / lastCompositeSize="
						// + lastCompositeSize);

						addComponents(true);
						refresh();
					}
				}

			});

		}
	};

	/**
	 * Set the section of the tabbed property.
	 * 
	 * @param section
	 */
	public DynamicTabbedPropertySection(final EComponentCategory section) {
		super();
		this.section = section;
		// currentLanguage = ((RepositoryContext)
		// CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
		// .getProject().getLanguage();
		repositoryQueryStoreMap = new HashMap<String, Query>();
		repositoryConnectionItemMap = new HashMap<String, ConnectionItem>();
		repositoryTableMap = new HashMap<String, IMetadataTable>();
		hashCurControls = new DualHashBidiMap();
	}

	/**
	 * yzhang Comment method "setCurRowSize" Sets the curRowSize.
	 * 
	 * @param curRowSize
	 *            int
	 */
	public void setCurRowSize(int curRowSize) {
		this.curRowSize = curRowSize;
	}

	/**
	 * dev Comment method "getRepositoryTableMap".
	 * 
	 * @return Map
	 */
	public Map<String, IMetadataTable> getRepositoryTableMap() {
		if (this.repositoryTableMap.keySet().isEmpty()) {
			updateRepositoryList();
		}
		return this.repositoryTableMap;
	}

	/**
	 * dev Comment method "getRepositoryConnectionItemMap".
	 * 
	 * @return Map
	 */
	public Map<String, ConnectionItem> getRepositoryConnectionItemMap() {
		return this.repositoryConnectionItemMap;
	}

	/**
	 * Getter for composite.
	 * 
	 * @return the composite
	 */
	public Composite getComposite() {
		return this.composite;
	}

	/**
	 * Getter for currentComponent.
	 * 
	 * @return the currentComponent
	 */
	public String getCurrentComponent() {
		return this.currentComponent;
	}

	/**
	 * Getter for curRowSize.
	 * 
	 * @return the curRowSize
	 */
	public int getCurRowSize() {
		return this.curRowSize;
	}

	/**
	 * Getter for hashCurControls.
	 * 
	 * @return the hashCurControls
	 */
	public BidiMap getHashCurControls() {
		return this.hashCurControls;
	}

	/**
	 * Getter for part.
	 * 
	 * @return the part
	 */
	public MultiPageTalendEditor getPart() {
		return this.part;
	}

	/**
	 * Getter for section.
	 * 
	 * @return the section
	 */
	public EComponentCategory getSection() {
		return this.section;
	}

	/**
	 * Getter for repositoryQueryStoreMap.
	 * 
	 * @return the repositoryQueryStoreMap
	 */
	public Map<String, Query> getRepositoryQueryStoreMap() {
		return repositoryQueryStoreMap;
	}

	/**
	 * Get the command stack of the Gef editor.
	 * 
	 * @return
	 */
	protected CommandStack getCommandStack() {
		TalendEditor talendEditor = part.getTalendEditor();
		Object adapter = talendEditor.getAdapter(CommandStack.class);
		return (CommandStack) adapter;
	}

	/**
	 * amaumont Comment method "registerListenerForRefreshingSection".
	 */
	private void registerListenerForRefreshingSection() {
		if (commandStackEventListener == null) {

			DynamicTabbedPropertySection.commandStackEventListener = new CommandStackEventListener() {

				public void stackChanged(CommandStackEvent event) {
					int detail = event.getDetail();
					if (0 != (detail & CommandStack.POST_UNDO)
							|| 0 != (detail & CommandStack.POST_REDO)) {
						if (event.getCommand() instanceof IExtendedTableCommand) {

							IWorkbenchPage page = PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage();
							IViewPart view = page
									.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
							PropertySheet sheet = (PropertySheet) view;
							TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet
									.getCurrentPage();
							tabbedPropertySheetPage.refresh();

						}
					}

				}

			};
			getCommandStack().addCommandStackEventListener(
					DynamicTabbedPropertySection.commandStackEventListener);
		}
	}

	@SuppressWarnings("unchecked")
	private void updateQuery() {
		Object propertyValue = elem
				.getPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE
						.getName());
		if (propertyValue == null
				|| !(propertyValue instanceof String)
				|| "".equals(propertyValue)
				|| elem.getPropertyValue(
						EParameterName.QUERYSTORE_TYPE.getName()).equals(
						EmfComponent.BUILTIN)) {
			return;
		}
		if (repositoryQueryStoreMap.containsKey(propertyValue)) {
			Query query = repositoryQueryStoreMap.get(propertyValue);
			for (IElementParameter param : (List<IElementParameter>) elem
					.getElementParameters()) {
				if (param.getField() == EParameterFieldType.MEMO_SQL) {
					elem.setPropertyValue(param.getName(), convertSQL(query
							.getValue()));
					param.setRepositoryValueUsed(true);
				}
			}
		}
	}

	private String convertSQL(String sql) {

		if (sql.startsWith("'") || sql.startsWith("\"")) { //$NON-NLS-1$
			return sql;
		}
		return TalendTextUtils.addSQLQuotes(sql); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * qzhang Comment method "getDefaultRepository".
	 * 
	 * @return
	 */
	private String getDefaultRepository(IElementParameter baseParam,
			boolean istable, String defaultPropertyValue) {
		boolean metadataInput = false;
		if (((Node) elem)
				.getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) > 0
				|| ((Node) elem)
						.getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) > 0
				|| ((Node) elem)
						.getCurrentActiveLinksNbInput(EConnectionType.TABLE) > 0) {
			metadataInput = true;
		}

		if (metadataInput && istable) {
			return (String) baseParam.getChildParameters().get(
					EParameterName.REPOSITORY_SCHEMA_TYPE.getName()).getValue();
		}
		Object propertyValue = elem
				.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
						.getName());
		if ((propertyValue == null || !(propertyValue instanceof String))
				&& defaultPropertyValue != null) {
			propertyValue = defaultPropertyValue;
		}
		if (propertyValue == null || propertyValue.equals("")) {
			return "";
		}
		if (istable) {
			List<String> list = tablesMap.get(propertyValue);
			if (list != null) {
				if (list.size() > 0) {
					return tablesMap.get(propertyValue).get(0);
				}
			}
		} else {
			List<String> list = queriesMap.get(propertyValue);
			if (list != null) {
				if (queriesMap.get(propertyValue).size() > 0) {
					return queriesMap.get(propertyValue).get(0);
				}
			}
		}
		return "";
	}

	/**
	 * qzhang Comment method "initMaps".
	 */
	private void initMaps() {
		for (String key : tablesMap.keySet()) {
			List<String> tablesName = new ArrayList<String>();
			List<String> queriesName = new ArrayList<String>();
			queriesName.addAll(queriesMap.get(key));
			tablesName.addAll(tablesMap.get(key));
			for (String string : tablesMap.get(key)) {
				if (!string.startsWith(key)) {
					tablesName.remove(string);
				}
			}

			for (String string : queriesMap.get(key)) {
				if (!string.startsWith(key)) {
					queriesName.remove(string);
				}
			}
			tablesMap.put(key, tablesName);
			queriesMap.put(key, queriesName);
		}
	}

	/**
	 * Getter for tablesMap.
	 * 
	 * @return the tablesMap
	 */
	public Map<String, List<String>> getTablesMap() {
		initMaps();
		return this.tablesMap;
	}

	/**
	 * Getter for queriesMap.
	 * 
	 * @return the queriesMap
	 */
	public Map<String, List<String>> getQueriesMap() {
		initMaps();
		return this.queriesMap;
	}

	/**
	 * Getter for tableIdAndDbTypeMap.
	 * 
	 * @return the tableIdAndDbTypeMap
	 */
	public Map<String, String> getTableIdAndDbTypeMap() {
		return this.tableIdAndDbTypeMap;
	}

	/**
	 * Getter for tableIdAndDbSchemaMap.
	 * 
	 * @return the tableIdAndDbSchemaMap
	 */
	public Map<String, String> getTableIdAndDbSchemaMap() {
		return this.tableIdAndDbSchemaMap;
	}
}
