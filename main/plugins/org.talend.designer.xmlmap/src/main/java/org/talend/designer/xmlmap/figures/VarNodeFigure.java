// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures;

import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.designer.xmlmap.figures.borders.ColumnBorder;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.TableItemLayout;
import org.talend.designer.xmlmap.figures.table.Table;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class VarNodeFigure extends Figure {

    private VarNode varNode;

    protected Label expression;

    protected ComboCellLabel type;

    // protected Label expression, type;

    protected VariableContainerFigure variable;

    public VarNodeFigure(VarNode varNode) {
        this.varNode = varNode;
        createComponents();
    }

    public Table getTable() {
        return (Table) this.getParent().getParent();
    }

    /**
     * DOC Administrator Comment method "createComponents".
     */
    protected void createComponents() {
        TableItemLayout manager = new TableItemLayout();
        this.setLayoutManager(manager);
        expression = new VarNodeExpression();
        expression.setText(varNode.getExpression());

        expression.setBorder(new CompoundBorder(new ColumnBorder(), new RowBorder(2, 5, 2, -1)));

        type = new ComboCellLabel();
        type.setDirectEditType(DirectEditType.VAR_NODE_TYPE);
        type.setText(getTypeDisplayValue(varNode));
        type.setLabelAlignment(PositionConstants.LEFT);

        type.setBorder(new CompoundBorder(new ColumnBorder(), new RowBorder(2, 5, 2, -1)));

        variable = new VariableContainerFigure(varNode);
        variable.setBorder(new RowBorder());
        this.add(expression);
        this.add(type);
        this.add(variable);

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

    public VarNode getVarNode() {
        return this.varNode;
    }

    public Label getExpression() {
        return this.expression;
    }

    public VariableContainerFigure getVariable() {
        return this.variable;
    }

    public void updateVarNodeType() {
        this.type.setText(getTypeDisplayValue(varNode));
    }

}
