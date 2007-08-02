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
package org.talend.designer.rowgenerator.ui.tabs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.designer.rowgenerator.data.Parameter;
import org.talend.expressionbuilder.IExpressionBuilderDialogService;
import org.talend.expressionbuilder.IExpressionConsumer;
import org.talend.expressionbuilder.test.shadow.Variable;
import org.talend.expressionbuilder.ui.IExpressionBuilderDialogController;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExtendedTextCellEditor.java 下午03:55:26 2007-8-1 +0000 (2007-8-1) yzhang $
 * 
 */
public class ExtendedTextCellEditor extends TextCellEditor implements IExpressionConsumer {

    private IExpressionBuilderDialogController expressionBuilderDialog;

    private TableViewerCreator tableCreator;

    private String expression;

    /**
     * yzhang ExtendedTextCellEditor constructor comment.
     */
    public ExtendedTextCellEditor(Composite parent) {
        super(parent);
        configDoubleClickListener();

        IService expressionBuilderDialogService = GlobalServiceRegister.getDefault().getService(
                IExpressionBuilderDialogService.class);
        expressionBuilderDialog = ((IExpressionBuilderDialogService) expressionBuilderDialogService)
                .getExpressionBuilderInstance(parent, this);
    }

    /**
     * yzhang Comment method "configDoubleClickListener".
     */
    private void configDoubleClickListener() {
        text.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem[] items = tableCreator.getTable().getSelection();
                TableItem item = items[0];
                if (item != null) {
                    Parameter param = (Parameter) item.getData();
                    expressionBuilderDialog.setDefaultExpression(param.getValue());
                }
                expressionBuilderDialog.openDialog();

            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.IExpressionConsumer#setExpression(java.lang.String)
     */
    public void setConsumerExpression(String expression) {
        TableItem[] items = tableCreator.getTable().getSelection();
        TableItem item = items[0];
        if (item != null) {
            Parameter param = (Parameter) item.getData();
            param.setValue(expression);
            tableCreator.getTableViewer().refresh();
        }

    }

    /**
     * yzhang Comment method "setColumn".
     * 
     * @param column
     */
    public void setTableViewerCreator(TableViewerCreator creator) {
        this.tableCreator = creator;

    }

}
