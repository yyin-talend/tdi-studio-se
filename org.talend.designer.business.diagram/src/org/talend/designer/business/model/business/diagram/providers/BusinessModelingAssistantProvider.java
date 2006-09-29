package org.talend.designer.business.model.business.diagram.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.talend.designer.business.diagram.custom.edit.parts.BusinessItemShapeEditPart;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessProcessEditPart;
import org.talend.designer.business.model.business.diagram.part.BusinessDiagramEditorPlugin;

/**
 * @generated
 */
public class BusinessModelingAssistantProvider extends ModelingAssistantProvider {

    /**
     * @generated
     */
    public List getTypesForPopupBar(IAdaptable host) {
        IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
        if (editPart instanceof BusinessProcessEditPart) {
            List types = new ArrayList();
            types.add(BusinessElementTypes.ActionBusinessItem_1001);
            types.add(BusinessElementTypes.TerminalBusinessItem_1002);
            types.add(BusinessElementTypes.DocumentBusinessItem_1003);
            types.add(BusinessElementTypes.DatabaseBusinessItem_1004);
            types.add(BusinessElementTypes.ListBusinessItem_1005);
            types.add(BusinessElementTypes.DataBusinessItem_1006);
            types.add(BusinessElementTypes.InputBusinessItem_1007);
            types.add(BusinessElementTypes.DecisionBusinessItem_1008);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    public List getRelTypesOnSource(IAdaptable source) {
        IGraphicalEditPart editPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        if (editPart instanceof BusinessItemShapeEditPart) {
            List types = new ArrayList();
            types.add(BusinessElementTypes.BusinessItemRelationship_3001);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    public List getRelTypesOnTarget(IAdaptable target) {
        IGraphicalEditPart editPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (editPart instanceof BusinessItemShapeEditPart) {
            List types = new ArrayList();
            types.add(BusinessElementTypes.BusinessItemRelationship_3001);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    public List getRelTypesOnSourceAndTarget(IAdaptable source, IAdaptable target) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof BusinessItemShapeEditPart && targetEditPart instanceof BusinessItemShapeEditPart) {
            List types = new ArrayList();
            types.add(BusinessElementTypes.BusinessItemRelationship_3001);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    public List getTypesForSource(IAdaptable target, IElementType relationshipType) {
        IGraphicalEditPart editPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (editPart instanceof BusinessItemShapeEditPart
                && relationshipType.equals(BusinessElementTypes.BusinessItemRelationship_3001)) {
            List types = new ArrayList();
            types.add(BusinessElementTypes.ActionBusinessItem_1001);
            types.add(BusinessElementTypes.TerminalBusinessItem_1002);
            types.add(BusinessElementTypes.DocumentBusinessItem_1003);
            types.add(BusinessElementTypes.DatabaseBusinessItem_1004);
            types.add(BusinessElementTypes.ListBusinessItem_1005);
            types.add(BusinessElementTypes.DataBusinessItem_1006);
            types.add(BusinessElementTypes.InputBusinessItem_1007);
            types.add(BusinessElementTypes.DecisionBusinessItem_1008);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    public List getTypesForTarget(IAdaptable source, IElementType relationshipType) {
        IGraphicalEditPart editPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        IGraphicalEditPart relationShipEditPart = (IGraphicalEditPart) relationshipType.getAdapter(IGraphicalEditPart.class);
        if (editPart instanceof BusinessItemShapeEditPart
                && relationshipType.equals(BusinessElementTypes.BusinessItemRelationship_3001)) {
            List types = new ArrayList();
            types.add(BusinessElementTypes.ActionBusinessItem_1001);
            types.add(BusinessElementTypes.TerminalBusinessItem_1002);
            types.add(BusinessElementTypes.DocumentBusinessItem_1003);
            types.add(BusinessElementTypes.DatabaseBusinessItem_1004);
            types.add(BusinessElementTypes.ListBusinessItem_1005);
            types.add(BusinessElementTypes.DataBusinessItem_1006);
            types.add(BusinessElementTypes.InputBusinessItem_1007);
            types.add(BusinessElementTypes.DecisionBusinessItem_1008);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * @generated
     */
    public EObject selectExistingElementForSource(IAdaptable target, IElementType relationshipType) {
        return selectExistingElement(target, getTypesForSource(target, relationshipType));
    }

    /**
     * @generated
     */
    public EObject selectExistingElementForTarget(IAdaptable source, IElementType relationshipType) {
        return selectExistingElement(source, getTypesForTarget(source, relationshipType));
    }

    /**
     * @generated
     */
    protected EObject selectExistingElement(IAdaptable host, Collection types) {
        if (types.isEmpty()) {
            return null;
        }
        IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
        if (editPart == null) {
            return null;
        }
        Diagram diagram = (Diagram) editPart.getRoot().getContents().getModel();
        Collection elements = new HashSet();
        for (Iterator it = diagram.getElement().eAllContents(); it.hasNext();) {
            EObject element = (EObject) it.next();
            if (isApplicableElement(element, types)) {
                elements.add(element);
            }
        }
        if (elements.isEmpty()) {
            return null;
        }
        return selectElement((EObject[]) elements.toArray(new EObject[elements.size()]));
    }

    /**
     * @generated
     */
    protected boolean isApplicableElement(EObject element, Collection types) {
        IElementType type = ElementTypeRegistry.getInstance().getElementType(element);
        return types.contains(type);
    }

    /**
     * @generated
     */
    protected EObject selectElement(EObject[] elements) {
        Shell shell = Display.getCurrent().getActiveShell();
        ILabelProvider labelProvider = new AdapterFactoryLabelProvider(BusinessDiagramEditorPlugin.getInstance()
                .getItemProvidersAdapterFactory());
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
        dialog.setMessage("Available domain model elements:");
        dialog.setTitle("Select domain model element");
        dialog.setMultipleSelection(false);
        dialog.setElements(elements);
        EObject selected = null;
        if (dialog.open() == Window.OK) {
            selected = (EObject) dialog.getFirstResult();
        }
        return selected;
    }
}
