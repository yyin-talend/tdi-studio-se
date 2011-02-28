// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.parts.directedit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.expressionbuilder.IExpressionBuilderDialogController;
import org.talend.commons.ui.swt.tableviewer.celleditor.CellEditorDialogBehavior;
import org.talend.commons.ui.swt.tableviewer.celleditor.ExtendedTextCellEditor;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.designer.xmlmap.figures.cells.IComboCell;
import org.talend.designer.xmlmap.figures.cells.IExpressionBuilderCell;
import org.talend.designer.xmlmap.figures.cells.ITextAreaCell;
import org.talend.designer.xmlmap.figures.cells.ITextCell;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.tree.IUILookupMode;
import org.talend.designer.xmlmap.model.tree.IUIMatchingMode;
import org.talend.designer.xmlmap.model.tree.XML_MAP_LOOKUP_MODE;
import org.talend.designer.xmlmap.model.tree.XML_MAP_MATCHING_MODE;
import org.talend.expressionbuilder.IExpressionBuilderDialogService;

/**
 * DOC talend class global comment. Detailled comment
 */
public class XmlMapNodeDirectEditManager extends DirectEditManager {

    private GraphicalEditPart source;

    private CellEditorLocator locator;

    private Object model;

    private Map<CellEditor, DirectEditType> cellAndType = new HashMap<CellEditor, DirectEditType>();

    public XmlMapNodeDirectEditManager(GraphicalEditPart source, CellEditorLocator locator) {
        super(source, null, locator);
        this.source = source;
        this.locator = locator;
        model = source.getModel();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
     */
    @Override
    protected void initCellEditor() {
        DirectEditType directEditType = cellAndType.get(getCellEditor());
        if (model instanceof AbstractNode) {
            AbstractNode abstractNode = (AbstractNode) model;
            if (directEditType != null) {
                switch (directEditType) {
                case EXPRESSION:
                    String expression = abstractNode.getExpression() == null ? "" : abstractNode.getExpression();
                    getCellEditor().setValue(expression);
                    Text text = ((ExtendedTextCellEditor) getCellEditor()).getTextControl();
                    text.selectAll();
                    break;
                case NODE_NAME:
                    String variable = abstractNode.getName();
                    if (variable == null) {
                        variable = "";
                    }
                    getCellEditor().setValue(variable);
                    text = (Text) ((TextCellEditor) getCellEditor()).getControl();
                    text.selectAll();

                    break;
                case VAR_NODE_TYPE:
                    if (getCellEditor() instanceof ComboBoxCellEditor) {
                        CCombo combo = (CCombo) getCellEditor().getControl();
                        combo.setText(getTypeDisplayValue((VarNode) abstractNode));
                    }
                    break;
                }

            }
        } else if (model instanceof InputXmlTree) {
            InputXmlTree inputxmlTree = (InputXmlTree) model;
            if (directEditType != null) {
                switch (directEditType) {
                case EXPRESSION_FILTER:
                    if (getCellEditor() instanceof TextCellEditor) {
                        Text text = (Text) getCellEditor().getControl();
                        String expressionFilter = inputxmlTree.getExpressionFilter();
                        if (expressionFilter == null) {
                            expressionFilter = "";
                        }
                        text.setText(expressionFilter);
                    }
                    break;
                case LOOKUP_MODEL:
                    if (getCellEditor() instanceof ComboBoxCellEditor) {
                        CCombo combo = (CCombo) getCellEditor().getControl();
                        combo.setText(getLookupDisplayName(inputxmlTree.getLookupMode()));
                    }
                    break;
                case MATCH_MODEL:
                    if (getCellEditor() instanceof ComboBoxCellEditor) {
                        CCombo combo = (CCombo) getCellEditor().getControl();
                        combo.setText(getMatchModelDisplayName(inputxmlTree.getMatchingMode()));
                    }
                    break;
                case JOIN_MODEL:
                    if (getCellEditor() instanceof ComboBoxCellEditor) {
                        CCombo combo = (CCombo) getCellEditor().getControl();
                        combo.setText(String.valueOf(inputxmlTree.isInnerJoin()));
                    }
                    break;
                case PERSISTENT_MODEL:
                    if (getCellEditor() instanceof ComboBoxCellEditor) {
                        CCombo combo = (CCombo) getCellEditor().getControl();
                        combo.setText(String.valueOf(inputxmlTree.isPersistent()));
                    }
                    break;

                }
            }

        } else if (model instanceof OutputXmlTree) {
            OutputXmlTree outputTree = (OutputXmlTree) model;
            if (directEditType != null) {
                switch (directEditType) {
                case EXPRESSION_FILTER:
                    if (getCellEditor() instanceof TextCellEditor) {
                        Text text = (Text) getCellEditor().getControl();
                        String expressionFilter = outputTree.getExpressionFilter();
                        if (expressionFilter == null) {
                            expressionFilter = "";
                        }
                        text.setText(expressionFilter);
                    }
                    break;
                case OUTPUT_REJECT:
                    if (getCellEditor() instanceof ComboBoxCellEditor) {
                        CCombo combo = (CCombo) getCellEditor().getControl();
                        combo.setText(String.valueOf(outputTree.isReject()));
                    }
                    break;
                case LOOK_UP_INNER_JOIN_REJECT:
                    if (getCellEditor() instanceof ComboBoxCellEditor) {
                        CCombo combo = (CCombo) getCellEditor().getControl();
                        combo.setText(String.valueOf(outputTree.isRejectInnerJoin()));
                    }
                    break;
                }
            }

        }

    }

    private String getTypeDisplayValue(VarNode varNode) {
        JavaType javaType = JavaTypesManager.getJavaTypeFromId(varNode.getType());
        Class primitiveClass = javaType.getPrimitiveClass();
        Boolean nullable = varNode.isNullable();
        String displayedValue = null;
        if (primitiveClass != null && !nullable.equals(Boolean.TRUE)) {
            displayedValue = primitiveClass.getSimpleName();
        } else if (varNode.getType().equals(JavaTypesManager.DIRECTORY.getId())
                || varNode.getType().equals(JavaTypesManager.FILE.getId())
                || varNode.getType().equals(JavaTypesManager.VALUE_LIST.getId())) {
            displayedValue = javaType.getLabel();
        } else {
            displayedValue = javaType.getNullableClass().getSimpleName();
        }
        if (displayedValue == null) {
            displayedValue = JavaTypesManager.getDefaultJavaType().getLabel();
        }
        return displayedValue;
    }

    private String getLookupDisplayName(String lookupModel) {
        IUILookupMode[] availableJoins = { XML_MAP_LOOKUP_MODE.LOAD_ONCE, XML_MAP_LOOKUP_MODE.RELOAD,
                XML_MAP_LOOKUP_MODE.CACHE_OR_RELOAD };
        for (IUILookupMode model : availableJoins) {
            if (model.toString().equals(lookupModel)) {
                return model.getLabel();
            }
        }
        return lookupModel;
    }

    private String getMatchModelDisplayName(String matcheModel) {
        IUIMatchingMode[] allMatchingModel = XML_MAP_MATCHING_MODE.values();
        for (IUIMatchingMode model : allMatchingModel) {
            if (model.toString().equals(matcheModel)) {
                return model.getLabel();
            }
        }
        return matcheModel;
    }

    protected CellEditor createCellEditorOn(Composite composite) {
        Composite parent = (Composite) source.getViewer().getControl();
        CellEditor cellEditor = null;
        Figure figure = null;
        if (this.locator instanceof XmlMapNodeCellEditorLocator) {
            XmlMapNodeCellEditorLocator lo = (XmlMapNodeCellEditorLocator) locator;
            figure = lo.getFigure();
        }
        if (figure instanceof IComboCell) {
            try {
                // Constructor constructor = ComboBoxCellEditor.class
                // .getConstructor(new Class[] { Composite.class, String[].class });
                // cellEditor = (CellEditor) constructor.newInstance(new Object[] { composite,
                // getComboItemsByType(((IComboCell) figure).getDirectEditType()) });
                cellEditor = new XmlComboCellEditor();
                cellEditor.create(composite);
                ((XmlComboCellEditor) cellEditor).setItems(getComboItemsByType(((IComboCell) figure).getDirectEditType()));

                cellAndType.put(cellEditor, ((IComboCell) figure).getDirectEditType());
            } catch (Exception e) {
                return null;
            }
        } else if (figure instanceof ITextCell) {
            cellEditor = new TextCellEditor(composite);
            cellAndType.put(cellEditor, ((ITextCell) figure).getDirectEditType());
        } else if (figure instanceof IExpressionBuilderCell && model instanceof AbstractNode) {
            IService expressionBuilderDialogService = GlobalServiceRegister.getDefault().getService(
                    IExpressionBuilderDialogService.class);
            CellEditorDialogBehavior behavior = new CellEditorDialogBehavior();
            cellEditor = new ExpressionCellEditor(composite, behavior);
            ((ExpressionCellEditor) cellEditor).setOwnerId(((AbstractNode) model).getExpression());
            IExpressionBuilderDialogController dialog = ((IExpressionBuilderDialogService) expressionBuilderDialogService)
                    .getExpressionBuilderInstance(parent, (ExpressionCellEditor) cellEditor, null);
            cellAndType.put(cellEditor, DirectEditType.EXPRESSION);
            behavior.setCellEditorDialog(dialog);
        } else if (figure instanceof ITextAreaCell) {
            cellEditor = new TextCellEditor(composite, SWT.MULTI);
            cellAndType.put(cellEditor, DirectEditType.EXPRESSION_FILTER);
        }

        // }
        return cellEditor;
    }

    @Override
    protected boolean isDirty() {
        // Object model = getEditPart().getModel();
        // if (model instanceof VarNode) {
        // Control control = getCellEditor().getControl();
        // if (control instanceof CCombo) {
        //
        // return !((CCombo) control).getText().equals(((VarNode) model).getType());
        // } else if (control instanceof Text) {
        // return !((Text) control).getText().equals(((VarNode) model).getName());
        // }
        // }
        //
        //
        //
        //
        //
        // DirectEditType directEditType = cellAndType.get(getCellEditor());
        // if (getCellEditor() instanceof ComboBoxCellEditor && directEditType != null) {
        // ComboBoxCellEditor combo = (ComboBoxCellEditor) getCellEditor();
        // int selectIndex = (Integer) combo.getValue();
        // String newValue = combo.getItems()[selectIndex];
        // if (model instanceof AbstractNode) {
        // switch (directEditType) {
        // case VAR_NODE_TYPE:
        // JavaType javaTypeFromLabel = JavaTypesManager.getJavaTypeFromLabel((String) newValue);
        // if (javaTypeFromLabel == null) {
        // javaTypeFromLabel = JavaTypesManager.getDefaultJavaType();
        // }
        // return ((VarNode) model).getType().equals(javaTypeFromLabel.getId());
        //
        // }
        // } else if (model instanceof InputXmlTree) {
        // InputXmlTree inputTree = (InputXmlTree) model;
        // switch (directEditType) {
        // case LOOKUP_MODEL:
        // return newValue.equals(inputTree.getLookupMode());
        // case MATCH_MODEL:
        // return newValue.equals(inputTree.getMatchingMode());
        // }
        //
        // }
        // }

        return super.isDirty();

    }

    @Override
    protected Object getDirectEditFeature() {
        return cellAndType.get(getCellEditor());
    }

    private String[] getComboItemsByType(DirectEditType type) {
        if (type == null) {
            return new String[0];
        }

        switch (type) {
        case VAR_NODE_TYPE:
            String[] items = JavaTypesManager.getJavaTypesLabels();
            return items;
        case LOOKUP_MODEL:
            IUILookupMode[] availableJoins = { XML_MAP_LOOKUP_MODE.LOAD_ONCE, XML_MAP_LOOKUP_MODE.RELOAD,
                    XML_MAP_LOOKUP_MODE.CACHE_OR_RELOAD };
            String names[] = new String[availableJoins.length];
            for (int i = 0; i < availableJoins.length; i++) {
                names[i] = availableJoins[i].getLabel();
            }
            return names;
        case MATCH_MODEL:
            IUIMatchingMode[] matchModel = getMatchModel();
            String names2[] = new String[matchModel.length];
            for (int i = 0; i < matchModel.length; i++) {
                names2[i] = (matchModel[i].getLabel());
            }
            return names2;
        case JOIN_MODEL:
        case PERSISTENT_MODEL:
        case OUTPUT_REJECT:
        case LOOK_UP_INNER_JOIN_REJECT:
            return new String[] { String.valueOf(Boolean.TRUE), String.valueOf(Boolean.FALSE) };
        default:
            return new String[0];
        }

    }

    private IUIMatchingMode[] getMatchModel() {
        IUIMatchingMode[] allMatchingModel = XML_MAP_MATCHING_MODE.values();
        List<IUIMatchingMode> avilable = new ArrayList<IUIMatchingMode>();
        if (model instanceof InputXmlTree) {
            InputXmlTree inputTree = (InputXmlTree) model;
            for (int i = 0; i < allMatchingModel.length; ++i) {
                IUIMatchingMode matchingMode = allMatchingModel[i];
                final String text = matchingMode.getLabel();
                if (matchingMode == XML_MAP_MATCHING_MODE.LAST_MATCH) {
                    continue;
                }
                if (text.length() != 0) {

                    if (matchingMode == XML_MAP_MATCHING_MODE.ALL_ROWS
                            && !inputTree.getMatchingMode().equals(XML_MAP_MATCHING_MODE.ALL_ROWS.toString())
                            || matchingMode != XML_MAP_MATCHING_MODE.ALL_ROWS
                            && !inputTree.getMatchingMode().equals(XML_MAP_MATCHING_MODE.ALL_ROWS.toString())) {
                        // avilable.add(matchingMode);
                    } else {
                        avilable.add(matchingMode);
                    }

                }
            }
        }

        return avilable.toArray(new IUIMatchingMode[avilable.size()]);
    }

}
