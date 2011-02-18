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
package org.talend.designer.xmlmap.figures;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.designer.xmlmap.figures.borders.ColumnBorder;
import org.talend.designer.xmlmap.figures.borders.RowBorder;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class VarNodeFigure extends ToolBarContainer {

    private VarNode varNode;

    protected Label expression;

    protected VarNodeTypeLabel type;

    // protected Label expression, type;

    protected VariableContainerFigure variable;

    public VarNodeFigure(VarNode varNode) {
        this.varNode = varNode;
        createComponents();
    }

    /**
     * DOC Administrator Comment method "createComponents".
     */
    protected void createComponents() {
        this.setLayoutManager(new EqualWidthLayout());
        expression = new ExpressionFigure();
        expression.setText(varNode.getExpression());
        CompoundBorder compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        expression.setBorder(compoundBorder);

        type = new VarNodeTypeLabel();
        type.setText(getTypeDisplayValue(varNode));
        compoundBorder = new CompoundBorder(new RowBorder(), new ColumnBorder());
        type.setBorder(compoundBorder);

        variable = new VariableContainerFigure(varNode);
        variable.setBorder(new RowBorder());
        this.add(expression);
        this.add(type);
        this.add(variable);
        Addlistener();

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

    /**
     * DOC Administrator Comment method "Addlistener".
     */
    private void Addlistener() {
        this.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent me) {
            }

            public void mousePressed(MouseEvent me) {
                IFigure parent = VarNodeFigure.this.getParent();
                for (IFigure child : (List<IFigure>) parent.getChildren()) {
                    child.setBackgroundColor(ColorConstants.white);
                }
                VarNodeFigure.this.setBackgroundColor(ColorConstants.yellow);
                CenterVarFigure tabelFigure = (CenterVarFigure) VarNodeFigure.this.getParent().getParent().getParent();
                tabelFigure.imageButtonsFigure.remove.setEnabled(true);
                tabelFigure.imageButtonsFigure.move_up.setEnabled(true);
                tabelFigure.imageButtonsFigure.move_down.setEnabled(true);
                tabelFigure.validate();
                tabelFigure.getSelectionNodes().clear();
                tabelFigure.getSelectionNodes().add(varNode);
            }

            public void mouseDoubleClicked(MouseEvent me) {

            }
        });
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
