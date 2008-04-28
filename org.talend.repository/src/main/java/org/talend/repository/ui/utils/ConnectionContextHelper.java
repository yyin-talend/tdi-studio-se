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
package org.talend.repository.ui.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.UpdateRepositoryUtils;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.wizards.context.ContextWizard;
import org.talend.repository.ui.wizards.metadata.ShowAddedContextdialog;

/**
 * ggu class global comment. Detailled comment
 */
public final class ConnectionContextHelper {

    public static final String LINE = "_"; //$NON-NLS-1$

    public static final String EMPTY = ""; //$NON-NLS-1$

    /**
     * 
     * ggu Comment method "checkContextMode".
     * 
     * initialize and check context mode for the ConnectionItem.
     */
    public static ContextItem checkContextMode(ConnectionItem connItem) {
        if (connItem == null) {
            return null;
        }
        Connection connection = connItem.getConnection();
        if (connection == null) {
            return null;
        }
        String contextId = connection.getContextId();
        if (contextId == null || EMPTY.equals(contextId.trim()) || RepositoryNode.NO_ID.equals(contextId.trim())) { //$NON-NLS-1$
            return null;
        }
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        try {
            IRepositoryObject context = factory.getLastVersion(contextId);
            if (context != null && factory.getStatus(context) != ERepositoryStatus.DELETED) {
                if (context.getProperty().getItem() instanceof ContextItem) {
                    connection.setContextMode(true);
                    return (ContextItem) context.getProperty().getItem();
                }
            }
        } catch (PersistenceException e) {
            // 
        }
        connection.setContextMode(false);
        connection.setContextId(null);
        return null;
    }

    public static void openInConetxtModeDialog() {
        MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages
                .getString("ConnectionContextHelper.ExistedTitle"), //$NON-NLS-1$
                Messages.getString("ConnectionContextHelper.ExistedMessages")); //$NON-NLS-1$
    }

    /**
     * 
     * ggu Comment method "exportAsContext".
     * 
     */
    public static ContextItem exportAsContext(ConnectionItem connItem) {
        if (connItem == null) {
            return null;
        }
        List<IContextParameter> varList = createContextParameters(connItem);
        if (varList == null || varList.isEmpty()) {
            return null;
        }

        return openContextDialog(connItem.getProperty().getLabel(), varList);
    }

    private static List<IContextParameter> createContextParameters(ConnectionItem connectionItem) {
        if (connectionItem == null) {
            return null;
        }
        Connection conn = connectionItem.getConnection();

        List<IContextParameter> varList = null;
        if (conn instanceof DatabaseConnection) {
            varList = DBConnectionContextUtils.getDBVariables(connectionItem.getProperty().getLabel(), (DatabaseConnection) conn);
        } else if (conn instanceof DelimitedFileConnection) {
            //
        } else if (conn instanceof FileExcelConnection) {
            //
        } else if (conn instanceof PositionalFileConnection) {
            //
        } else if (conn instanceof RegexpFileConnection) {
            //
        }

        return varList;
    }

    static void createParameters(List<IContextParameter> varList, String paramName, String value) {
        createParameters(varList, paramName, value, null);
    }

    static void createParameters(List<IContextParameter> varList, String paramName, String value, JavaType type) {
        if (varList == null || paramName == null) {
            return;
        }

        if (value == null) {
            value = EMPTY;
        }

        JobContextParameter contextParam = new JobContextParameter();
        contextParam.setName(paramName);

        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            if (type == null) {
                contextParam.setType(MetadataTalendType.getDefaultTalendType());
            } else {
                contextParam.setType(type.getId());
            }
            break;
        case PERL:
        default:
            if (type != null) {
                if (type == JavaTypesManager.DIRECTORY) {
                    contextParam.setType(ContextParameterJavaTypeManager.PERL_DIRECTORY);
                    break;
                } else if (type == JavaTypesManager.FILE) {
                    contextParam.setType(ContextParameterJavaTypeManager.PERL_FILE);
                    break;
                } else if (type == JavaTypesManager.INTEGER) {
                    contextParam.setType(MetadataTalendType.getPerlTypes()[3]); // int
                    break;
                }
            }
            contextParam.setType(MetadataTalendType.getPerlTypes()[5]); // string
            break;
        }

        contextParam.setPrompt(paramName + "?"); //$NON-NLS-1$
        if (value != null) {
            contextParam.setValue(value);
        }
        contextParam.setComment(EMPTY);
        varList.add(contextParam);
    }

    private static ContextItem openContextDialog(String contextName, List<IContextParameter> varList) {
        if (contextName == null) {
            return null;
        }
        ISelection selection = getRepositoryContext(contextName, false);
        if (selection == null) {
            return null;
        }
        boolean creation = true;
        if (!selection.isEmpty()) {
            creation = false;
        }
        ContextWizard contextWizard = new ContextWizard(contextName, creation, selection, varList);
        WizardDialog dlg = new WizardDialog(Display.getCurrent().getActiveShell(), contextWizard);
        if (dlg.open() == Window.OK) {

            return contextWizard.getContextItem();
        }
        return null;
    }

    private static ISelection getRepositoryContext(final String contextNameOrId, boolean isId) {
        if (contextNameOrId == null || "".equals(contextNameOrId.trim())) { //$NON-NLS-1$
            return null;
        }
        if (isId && RepositoryNode.NO_ID.equals(contextNameOrId.trim())) {
            return null;
        }
        IRepositoryObject contextObject = null;
        try {
            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            List<IRepositoryObject> contextObjectList = factory.getAll(ERepositoryObjectType.CONTEXT, true);
            if (contextObjectList != null) {
                for (IRepositoryObject object : contextObjectList) {
                    Item item = object.getProperty().getItem();
                    if (item != null && item instanceof ContextItem) {
                        String itemNameOrId = null;
                        if (isId) {
                            itemNameOrId = item.getProperty().getId();
                        } else {
                            itemNameOrId = item.getProperty().getLabel();
                        }
                        if (contextNameOrId.equals(itemNameOrId)) {
                            contextObject = object;
                            break;
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            // 
        }

        StructuredSelection selection = new StructuredSelection();
        if (contextObject != null) {
            RepositoryNode repositoryNode = RepositoryNodeUtilities.getRepositoryNode(contextObject);
            if (repositoryNode != null) {
                selection = new StructuredSelection(repositoryNode);
            }
        }
        return selection;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    static String getContextValue(ContextType contextType, final String value, final String paramName) {
        if (value == null) {
            return EMPTY;
        }

        if (contextType != null && ContextParameterUtils.isContainContextParam(value)) {
            ContextParameterType param = null;
            for (ContextParameterType paramType : (List<ContextParameterType>) contextType.getContextParameter()) {
                if (paramType.getName().equals(paramName)) {
                    param = paramType;
                    break;
                }
            }
            if (param != null && param.getValue() != null) {
                return param.getValue();
            }
            return EMPTY;
        }
        return value;

    }

    /**
     * 
     * ggu Comment method "upateContext".
     * 
     * open the context wizard to update context parameters.
     */
    public static boolean upateContext(ConnectionItem connItem) {
        if (connItem == null) {
            return false;
        }

        Shell activeShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        boolean checked = MessageDialog.openConfirm(activeShell, Messages.getString("ConnectionContextHelper.UpdateTitle"), //$NON-NLS-1$
                Messages.getString("ConnectionContextHelper.UpdateMessages")); //$NON-NLS-1$
        if (!checked) {
            return false;
        }
        ISelection selection = getRepositoryContext(connItem.getConnection().getContextId(), true);
        if (selection == null || selection.isEmpty()) {
            return false;
        }

        ContextWizard contextWizard = new ContextWizard(PlatformUI.getWorkbench(), false, selection, false);
        WizardDialog dlg = new WizardDialog(activeShell, contextWizard);
        if (dlg.open() == Window.OK) {
            return true;
        }
        return false;
    }

    /**
     * 
     * ggu Comment method "processContextForJob".
     * 
     * for DND repository node.
     */
    public static void addContextForNodeParameter(INode node, ConnectionItem connItem) {
        if (node == null || connItem == null) {
            return;
        }
        Connection connection = connItem.getConnection();
        ContextItem contextItem = ContextUtils.getContextItemById(connection.getContextId());
        if (connection.isContextMode() && contextItem != null) {
            // get the context variables from the node parameters.
            Set<String> neededVars = retrieveConetxtVar(node, connection);
            if (neededVars != null && !neededVars.isEmpty()) {
                // add needed vars into job
                Set<String> addedVars = addContextVarForJob(node.getProcess().getContextManager(), contextItem, neededVars);
                if (addedVars != null && !addedVars.isEmpty()) {
                    // refresh context view
                    RepositoryPlugin.getDefault().getDesignerCoreService().switchToCurContextsView();
                    // show
                    ShowAddedContextdialog showDialog = new ShowAddedContextdialog(addedVars, UpdateRepositoryUtils
                            .getRepositorySourceName(connItem));
                    showDialog.open();
                }
            }

        }
    }

    private static Set<String> retrieveConetxtVar(INode node, Connection connection) {
        if (node == null || connection == null) {
            return null;
        }
        Set<String> addedVars = new HashSet<String>();
        String var = null;
        for (IElementParameter param : node.getElementParameters()) {
            String repositoryValue = param.getRepositoryValue();
            if (repositoryValue != null) {
                Object objectValue = RepositoryToComponentProperty.getValue(connection, repositoryValue);
                if (objectValue != null && objectValue instanceof String) {
                    var = ContextParameterUtils.getVariableFromCode((String) objectValue);
                    if (var != null) {
                        addedVars.add(var);
                    }
                }
            }
        }

        return addedVars;
    }

    @SuppressWarnings("unchecked")
    private static Set<String> addContextVarForJob(IContextManager contextManager, ContextItem contextItem, Set<String> neededVars) {
        if (contextManager == null || contextItem == null || neededVars == null || neededVars.isEmpty()) {
            return null;
        }
        Set<String> addedVars = new HashSet<String>();
        for (IContext context : contextManager.getListContext()) {

            ContextType type = ContextUtils.getContextTypeByName(contextItem.getContext(), context.getName(), contextItem
                    .getDefaultContext());
            if (type != null) {
                for (String var : neededVars) {
                    if (context.getContextParameter(var) != null) {
                        continue;
                    }
                    ContextParameterType param = ContextUtils.getContextParameterTypeByName(type, var);
                    if (param != null) {
                        //
                        JobContextParameter contextParam = new JobContextParameter();

                        ContextUtils.updateParameter(param, contextParam);

                        contextParam.setSource(contextItem.getProperty().getLabel());
                        contextParam.setContext(context);

                        context.getContextParameterList().add(contextParam);
                        addedVars.add(var);
                    }
                }
            }
        }
        return addedVars;
    }
}
