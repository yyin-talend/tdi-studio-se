package org.talend.designer.business.model.business.diagram.edit.parts;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.talend.designer.business.model.business.diagram.part.BusinessVisualIDRegistry;

/**
 * @generated
 */
public class BusinessEditPartFactory implements EditPartFactory {

    /**
     * @generated
     */
    public static final String EXTERNAL_NODE_LABELS_LAYER = "External Node Labels"; //$NON-NLS-1$

    /**
     * @generated
     */
    public EditPart createEditPart(EditPart context, Object model) {
        if (model instanceof View) {
            View view = (View) model;
            int viewVisualID = BusinessVisualIDRegistry.getVisualID(view);
            switch (viewVisualID) {
            case ActionBusinessItemEditPart.VISUAL_ID:
                return new ActionBusinessItemEditPart(view);
            case ActionBusinessItemNameEditPart.VISUAL_ID:
                return new ActionBusinessItemNameEditPart(view);
            case TerminalBusinessItemEditPart.VISUAL_ID:
                return new TerminalBusinessItemEditPart(view);
            case TerminalBusinessItemNameEditPart.VISUAL_ID:
                return new TerminalBusinessItemNameEditPart(view);
            case DocumentBusinessItemEditPart.VISUAL_ID:
                return new DocumentBusinessItemEditPart(view);
            case DocumentBusinessItemNameEditPart.VISUAL_ID:
                return new DocumentBusinessItemNameEditPart(view);
            case DatabaseBusinessItemEditPart.VISUAL_ID:
                return new DatabaseBusinessItemEditPart(view);
            case DatabaseBusinessItemNameEditPart.VISUAL_ID:
                return new DatabaseBusinessItemNameEditPart(view);
            case ListBusinessItemEditPart.VISUAL_ID:
                return new ListBusinessItemEditPart(view);
            case ListBusinessItemNameEditPart.VISUAL_ID:
                return new ListBusinessItemNameEditPart(view);
            case DataBusinessItemEditPart.VISUAL_ID:
                return new DataBusinessItemEditPart(view);
            case DataBusinessItemNameEditPart.VISUAL_ID:
                return new DataBusinessItemNameEditPart(view);
            case InputBusinessItemEditPart.VISUAL_ID:
                return new InputBusinessItemEditPart(view);
            case InputBusinessItemNameEditPart.VISUAL_ID:
                return new InputBusinessItemNameEditPart(view);
            case DecisionBusinessItemEditPart.VISUAL_ID:
                return new DecisionBusinessItemEditPart(view);
            case DecisionBusinessItemNameEditPart.VISUAL_ID:
                return new DecisionBusinessItemNameEditPart(view);
            case ActorBusinessItemEditPart.VISUAL_ID:
                return new ActorBusinessItemEditPart(view);
            case ActorBusinessItemNameEditPart.VISUAL_ID:
                return new ActorBusinessItemNameEditPart(view);
            case EllipseBusinessItemEditPart.VISUAL_ID:
                return new EllipseBusinessItemEditPart(view);
            case EllipseBusinessItemNameEditPart.VISUAL_ID:
                return new EllipseBusinessItemNameEditPart(view);
            case GearBusinessItemEditPart.VISUAL_ID:
                return new GearBusinessItemEditPart(view);
            case GearBusinessItemNameEditPart.VISUAL_ID:
                return new GearBusinessItemNameEditPart(view);
            case BusinessProcessEditPart.VISUAL_ID:
                return new BusinessProcessEditPart(view);
            case BusinessItemRelationshipEditPart.VISUAL_ID:
                return new BusinessItemRelationshipEditPart(view);
            case BusinessItemRelationshipNameEditPart.VISUAL_ID:
                return new BusinessItemRelationshipNameEditPart(view);
            }
        }
        return createUnrecognizedEditPart(context, model);
    }

    /**
     * @generated
     */
    private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
        // Handle creation of unrecognized child node EditParts here
        return null;
    }

    /**
     * @generated
     */
    public static CellEditorLocator getTextCellEditorLocator(ITextAwareEditPart source) {
        if (source.getFigure() instanceof WrapLabel)
            return new TextCellEditorLocator((WrapLabel) source.getFigure());
        else {
            IFigure figure = source.getFigure();
            return new LabelCellEditorLocator((Label) figure);
        }
    }

    /**
     * @generated
     */
    static private class TextCellEditorLocator implements CellEditorLocator {

        /**
         * @generated
         */
        private WrapLabel wrapLabel;

        /**
         * @generated
         */
        public TextCellEditorLocator(WrapLabel wrapLabel) {
            super();
            this.wrapLabel = wrapLabel;
        }

        /**
         * @generated
         */
        public WrapLabel getWrapLabel() {
            return wrapLabel;
        }

        /**
         * @generated
         */
        public void relocate(CellEditor celleditor) {
            Text text = (Text) celleditor.getControl();
            Rectangle rect = getWrapLabel().getTextBounds().getCopy();
            getWrapLabel().translateToAbsolute(rect);

            if (getWrapLabel().isTextWrapped() && getWrapLabel().getText().length() > 0)
                rect.setSize(new Dimension(text.computeSize(rect.width, SWT.DEFAULT)));
            else {
                int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
                rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0));
            }

            if (!rect.equals(new Rectangle(text.getBounds())))
                text.setBounds(rect.x, rect.y, rect.width, rect.height);
        }

    }

    /**
     * @generated
     */
    private static class LabelCellEditorLocator implements CellEditorLocator {

        /**
         * @generated
         */
        private Label label;

        /**
         * @generated
         */
        public LabelCellEditorLocator(Label label) {
            this.label = label;
        }

        /**
         * @generated
         */
        public Label getLabel() {
            return label;
        }

        /**
         * @generated
         */
        public void relocate(CellEditor celleditor) {
            Text text = (Text) celleditor.getControl();
            Rectangle rect = getLabel().getTextBounds().getCopy();
            getLabel().translateToAbsolute(rect);

            int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
            rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0));

            if (!rect.equals(new Rectangle(text.getBounds())))
                text.setBounds(rect.x, rect.y, rect.width, rect.height);
        }
    }
}
