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
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;

/**
 * DOC admin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class RepositoryChangeQueryCommand extends Command {


    private Element elem;

    private Query query;

    private String value;

    private String propertyName;

    private Object oldMetadata;

    private Object oldValue;
    /**
     * DOC admin ChangeQueryCommand constructor comment.
     */
    public RepositoryChangeQueryCommand(Element elem, Query query, String propertyName, String value) {
        this.elem = elem;
        this.query = query;
        this.value = value;
        this.propertyName = propertyName;

        setLabel(Messages.getString("PropertyChangeCommand.Label")); //$NON-NLS-1$
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        if (tabbedPropertySheetPage.getCurrentTab() != null) {
            tabbedPropertySheetPage.refresh();
        }
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    @Override
    public void execute() {
        // Force redraw of Commponents propoerties
        elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));
        if (propertyName.equals(EParameterName.QUERYSTORE_TYPE.getName()) && (EmfComponent.BUILTIN.equals(value))) {
            for (IElementParameter param : elem.getElementParameters()) {
                param.setRepositoryValueUsed(false);
            }
        } else {
            for (IElementParameter param : (List<IElementParameter>) elem.getElementParameters()) {
                if (param.getField() == EParameterFieldType.MEMO_SQL) {
                    oldValue = elem.getPropertyValue(param.getName());
                    elem.setPropertyValue(param.getName(), convertSQL(query.getValue()));
                    param.setRepositoryValueUsed(true);
                }
            }
        }
        if (propertyName.equals(EParameterName.QUERYSTORE_TYPE.getName())) {
            elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), value);
        } else {
            oldMetadata = elem.getPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName());
            elem.setPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName(), value);
        }
        refreshPropertyView();
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    @Override
    public void undo() {
//      Force redraw of Commponents propoerties
        elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));

        if (propertyName.equals(EParameterName.QUERYSTORE_TYPE.getName()) && (EmfComponent.BUILTIN.equals(value))) {
            for (IElementParameter param : elem.getElementParameters()) {
                String repositoryValue = param.getRepositoryValue();
                if (param.isShow(elem.getElementParameters()) && (repositoryValue != null)
                        && (!param.getName().equals(EParameterName.QUERYSTORE_TYPE.getName()))) {
                    param.setRepositoryValueUsed(true);
                }
            }
        } else {
            for (IElementParameter param : (List<IElementParameter>) elem.getElementParameters()) {
                if (param.getField() == EParameterFieldType.MEMO_SQL) {
                    elem.setPropertyValue(param.getName(), oldValue);
                    param.setRepositoryValueUsed(false);
                }
            }
        }
        if (propertyName.equals(EParameterName.QUERYSTORE_TYPE.getName())) {
            if (value.equals(EmfComponent.BUILTIN)) {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.REPOSITORY);
            } else {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
            }
        } else {
            elem.setPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName(), oldMetadata);
        }
        refreshPropertyView();
    }
    
    /**
     * DOC admin Comment method "convertSQL".
     * @param sql
     * @return
     */
    private String convertSQL(String sql) {
        if (sql.startsWith("'")) { //$NON-NLS-1$
            return sql;
        }
        return "'" + sql + "'"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
