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
package org.talend.designer.core.ui.views.statsandlogs;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.cmd.ChangeValuesFromRepository;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;
import org.talend.designer.core.ui.preferences.StatsAndLogsPreferencePage;
import org.talend.repository.UpdateRepositoryUtils;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.ui.views.RepositoryContentProvider;
import org.talend.repository.ui.views.RepositoryView;

/**
 * ftang class global comment. Detailed comment. <br/>
 * 
 */
public class StatsAndLogsViewHelper {

    private static final String LANGUAGE_PREFIX = LanguageManager.getCurrentLanguage().toString() + "_";

    private static final IPreferenceStore PREFERENCE_STORE = DesignerPlugin.getDefault().getPreferenceStore();

    public static final String FILE_NAME_REGEX = "[^\\>\\<\\\\\\/\\!\\:\\|\\?\\\"\\'\\s\\.]"
            + "+[\\.][^\\>\\<\\\\\\/\\!\\:\\|\\?\\\"\\'\\s\\.]+";

    public static final String OTHER_FILE_NAME_REGEX = "[^\\\"\\'\\s]*";

    public static ConnectionItem findConnectionItem(RepositoryContentProvider contentProvider, RepositoryNode repositoryNode,
            String connectionLabel) {

        ConnectionItem connectionItem = null;

        if ((repositoryNode.getType() == ENodeType.SYSTEM_FOLDER || repositoryNode.getType() == ENodeType.SIMPLE_FOLDER)
                && contentProvider.getChildren(repositoryNode).length > 0) {
            for (RepositoryNode node : repositoryNode.getChildren()) {
                connectionItem = findConnectionItem(contentProvider, node, connectionLabel);
                if (connectionItem != null) {
                    return connectionItem;
                }
            }
        }

        if (repositoryNode.getObject() != null && repositoryNode.getObject().getLabel().equals(connectionLabel)) {
            return (ConnectionItem) repositoryNode.getObject().getProperty().getItem();
        }

        return connectionItem;

    }

    /**
     * ftang Comment method "reloadValuesFromPreferencePage".
     * 
     * @param preferenceStore
     * @param element
     */
    public static void reloadValuesFromPreferencePage(Element element, IDynamicProperty propertyComposite) {

        List<? extends IElementParameter> elementParameters = element.getElementParameters();
        for (IElementParameter elementParameter : elementParameters) {
            String name = elementParameter.getName();

            if (name.equals(EParameterName.ON_STATCATCHER_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.ON_STATCATCHER_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.ON_LOGCATCHER_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.ON_LOGCATCHER_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.ON_METERCATCHER_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                        + EParameterName.ON_METERCATCHER_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.ON_FILES_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX + EParameterName.ON_FILES_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.FILE_PATH.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.FILE_PATH.getName())));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_STATS.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.FILENAME_STATS.getName())));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_LOGS.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.FILENAME_LOGS.getName())));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_METTER.getName())) {
                elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.FILENAME_METTER.getName())));
                continue;
            }

            if (name.equals(EParameterName.ON_DATABASE_FLAG.getName())) {
                elementParameter.setValue(PREFERENCE_STORE
                        .getBoolean(LANGUAGE_PREFIX + EParameterName.ON_DATABASE_FLAG.getName()));
                continue;
            }

            if (name.equals(EParameterName.PROPERTY_TYPE.getName())) {
                String propertyType = PREFERENCE_STORE.getString(LANGUAGE_PREFIX + EParameterName.PROPERTY_TYPE.getName());
                String id = PREFERENCE_STORE.getString(LANGUAGE_PREFIX + EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
                String connectionLabel = PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                        + EParameterName.REPOSITORY_PROPERTY_TYPE.getName() + StatsAndLogsPreferencePage.CONNECTION_ITEM_LABEL);

                RepositoryContentProvider contentProvider = (RepositoryContentProvider) RepositoryView.show().getViewer()
                        .getContentProvider();
                RepositoryNode repositoryNode = (contentProvider).getMetadataConNode();

                IElementParameter parameterRepositoryType = element.getElementParameter(EParameterName.PROPERTY_TYPE.getName())
                        .getChildParameters().get(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
                if (parameterRepositoryType != null) {
                    parameterRepositoryType.setLinkedRepositoryItem(findConnectionItem(contentProvider, repositoryNode,
                            connectionLabel));
                }

                Connection repositoryConnection = null;
                Map<String, ConnectionItem> repositoryConnectionItemMap = propertyComposite.getRepositoryConnectionItemMap();

                if (repositoryConnectionItemMap.containsKey(id)) {
                    repositoryConnection = repositoryConnectionItemMap.get(id).getConnection();
                } else {
                    repositoryConnection = null;
                }

                ChangeValuesFromRepository cmd1 = new ChangeValuesFromRepository(element, repositoryConnection,
                        EParameterName.PROPERTY_TYPE.getName() + ":" + EParameterName.PROPERTY_TYPE.getName(), propertyType);

                ChangeValuesFromRepository cmd2 = new ChangeValuesFromRepository(element, repositoryConnection,
                        EParameterName.PROPERTY_TYPE.getName() + ":" + EParameterName.REPOSITORY_PROPERTY_TYPE.getName(), id);
                cmd2.setMaps(propertyComposite.getRepositoryTableMap());

                AbstractMultiPageTalendEditor part = ((Process) element).getEditor();
                if (part instanceof AbstractMultiPageTalendEditor) {
                    Object adapter = ((AbstractMultiPageTalendEditor) part).getTalendEditor().getAdapter(CommandStack.class);
                    if (adapter != null) {
                        CommandStack commandStack = ((CommandStack) adapter);
                        commandStack.execute(cmd1);
                        commandStack.execute(cmd2);
                    }
                }

                continue;
            }

            if (PREFERENCE_STORE.getString(LANGUAGE_PREFIX + EParameterName.PROPERTY_TYPE.getName()).equals(EmfComponent.BUILTIN)) {
                if (name.equals(EParameterName.DB_TYPE.getName())) {
                    elementParameter.setValue(PREFERENCE_STORE.getString(LANGUAGE_PREFIX + EParameterName.DB_TYPE.getName()));
                    continue;
                }

                if (name.equals(EParameterName.HOST.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.HOST.getName())));
                    continue;
                }

                if (name.equals(EParameterName.PORT.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.PORT.getName())));
                    continue;
                }

                if (name.equals(EParameterName.DBNAME.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.DBNAME.getName())));
                    continue;
                }
                if (name.equals(EParameterName.PROPERTIES.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.PROPERTIES.getName())));
                    continue;
                }

                if (name.equals(EParameterName.DBFILE.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.DBFILE.getName())));
                    continue;
                }
                if (name.equals(EParameterName.SCHEMA_DB.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.SCHEMA_DB.getName())));
                    continue;
                }

                if (name.equals(EParameterName.USER.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.USER.getName())));
                    continue;
                }

                if (name.equals(EParameterName.PASS.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.PASS.getName())));
                    continue;
                }

                if (name.equals(EParameterName.TABLE_STATS.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.TABLE_STATS.getName())));
                    continue;
                }

                if (name.equals(EParameterName.TABLE_LOGS.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.TABLE_LOGS.getName())));
                    continue;
                }

                if (name.equals(EParameterName.TABLE_METER.getName())) {
                    elementParameter.setValue(checkAndAddQuote(PREFERENCE_STORE.getString(LANGUAGE_PREFIX
                            + EParameterName.TABLE_METER.getName())));
                    continue;
                }

                if (name.equals(EParameterName.CATCH_RUNTIME_ERRORS.getName())) {
                    elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                            + EParameterName.CATCH_RUNTIME_ERRORS.getName()));
                    continue;
                }

                if (name.equals(EParameterName.CATCH_USER_ERRORS.getName())) {
                    elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                            + EParameterName.CATCH_USER_ERRORS.getName()));
                    continue;
                }

                if (name.equals(EParameterName.CATCH_USER_WARNING.getName())) {
                    elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                            + EParameterName.CATCH_USER_WARNING.getName()));
                    continue;
                }

                if (name.equals(EParameterName.CATCH_REALTIME_STATS.getName())) {
                    elementParameter.setValue(PREFERENCE_STORE.getBoolean(LANGUAGE_PREFIX
                            + EParameterName.CATCH_REALTIME_STATS.getName()));
                    continue;
                }
            }

        }
    }

    /**
     * ftang "saveValuesToPreferencePage".
     * 
     * @param preferenceStore
     * @param element
     */
    public static void saveValuesToPreferencePage(Element element, IDynamicProperty dynamicProperty) {

        List<? extends IElementParameter> elementParameters = element.getElementParameters();
        for (IElementParameter elementParameter : elementParameters) {
            String name = elementParameter.getName();
            Object elementValue = elementParameter.getValue();

            if (name.equals(EParameterName.ON_STATCATCHER_FLAG.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.ON_STATCATCHER_FLAG.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.ON_LOGCATCHER_FLAG.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.ON_LOGCATCHER_FLAG.getName(), (Boolean) elementValue);
                continue;

            }

            if (name.equals(EParameterName.ON_METERCATCHER_FLAG.getName())) {
                PREFERENCE_STORE
                        .setValue(LANGUAGE_PREFIX + EParameterName.ON_METERCATCHER_FLAG.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.ON_FILES_FLAG.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.ON_FILES_FLAG.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.FILE_PATH.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.FILE_PATH.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_STATS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.FILENAME_STATS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_LOGS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.FILENAME_LOGS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.FILENAME_METTER.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.FILENAME_METTER.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.ON_DATABASE_FLAG.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.ON_DATABASE_FLAG.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.DB_TYPE.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.DB_TYPE.getName(), (String) elementValue);
                continue;
            }

            if (name.equals(EParameterName.HOST.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.HOST.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.PORT.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.PORT.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.DBNAME.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.DBNAME.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }
            if (name.equals(EParameterName.PROPERTIES.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.PROPERTIES.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }
            if (name.equals(EParameterName.DBFILE.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.DBFILE.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }
            if (name.equals(EParameterName.SCHEMA_DB.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.SCHEMA_DB.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.USER.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.USER.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.PASS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.PASS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.TABLE_STATS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.TABLE_STATS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.TABLE_LOGS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.TABLE_LOGS.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.TABLE_METER.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.TABLE_METER.getName(),
                        checkAndRemoveQuote((String) elementValue));
                continue;
            }

            if (name.equals(EParameterName.CATCH_RUNTIME_ERRORS.getName())) {
                PREFERENCE_STORE
                        .setValue(LANGUAGE_PREFIX + EParameterName.CATCH_RUNTIME_ERRORS.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.CATCH_USER_ERRORS.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.CATCH_USER_ERRORS.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.CATCH_USER_WARNING.getName())) {
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.CATCH_USER_WARNING.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.CATCH_REALTIME_STATS.getName())) {
                PREFERENCE_STORE
                        .setValue(LANGUAGE_PREFIX + EParameterName.CATCH_REALTIME_STATS.getName(), (Boolean) elementValue);
                continue;
            }

            if (name.equals(EParameterName.PROPERTY_TYPE.getName())) {
                String itemId = (String) elementParameter.getChildParameters().get("REPOSITORY_PROPERTY_TYPE").getValue();
                String propertyType = (String) elementParameter.getChildParameters().get("PROPERTY_TYPE").getValue();

                Item item = elementParameter.getLinkedRepositoryItem();
                if (item == null || (item != null && !item.getProperty().getId().equals(itemId))) {
                    Map<String, ConnectionItem> itemMap = dynamicProperty.getRepositoryConnectionItemMap();
                    item = itemMap.get(itemId);
                    if (item == null) {
                        item = UpdateRepositoryUtils.getConnectionItemByItemId(itemId);
                    }
                }

                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.PROPERTY_TYPE.getName(), propertyType);
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.REPOSITORY_PROPERTY_TYPE.getName(), itemId);
                PREFERENCE_STORE.setValue(LANGUAGE_PREFIX + EParameterName.REPOSITORY_PROPERTY_TYPE.getName()
                        + StatsAndLogsPreferencePage.CONNECTION_ITEM_LABEL, item.getProperty().getLabel());
            }
        }
    }

    /*
     * add quote.
     */
    private static String checkAndAddQuote(String value) {
        if (value == null) {
            return TalendTextUtils.addQuotes("");
        }
        value = value.trim();
        return value;
    }

    /*
     * remove quote.
     */
    private static String checkAndRemoveQuote(String value) {
        if (value == null) {
            return "";
        }
        value = value.trim();
        return value;
    }
}
