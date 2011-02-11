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

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
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
        expression.setOpaque(true);
        expression.setText(varNode.getExpression());
        expression.setBorder(new LineBorder());
        type = new VarNodeTypeLabel();
        type.setOpaque(true);
        type.setText(varNode.getType());
        type.setBorder(new LineBorder());
        variable = new VariableContainerFigure(varNode);
        variable.setBorder(new LineBorder());
        variable.setOpaque(true);
        this.add(expression);
        this.add(type);
        this.add(variable);
        Addlistener();
    }

    /**
     * DOC Administrator Comment method "Addlistener".
     */
    private void Addlistener() {
        this.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent me) {
            }

            public void mousePressed(MouseEvent me) {
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

    public VarNodeTypeLabel getType() {
        return this.type;
    }

    public VariableContainerFigure getVariable() {
        return this.variable;
    }

}
