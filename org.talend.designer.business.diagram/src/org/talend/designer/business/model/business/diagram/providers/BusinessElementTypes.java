package org.talend.designer.business.model.business.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.diagram.part.BusinessDiagramEditorPlugin;

/**
 * @generated
 */
public class BusinessElementTypes {

    /**
     * @generated
     */
    private BusinessElementTypes() {
    }

    /**
     * @generated
     */
    private static Map elements;

    /**
     * @generated
     */
    private static ImageRegistry imageRegistry;

    /**
     * @generated
     */
    private static ImageRegistry getImageRegistry() {
        if (imageRegistry == null) {
            imageRegistry = new ImageRegistry();
        }
        return imageRegistry;
    }

    /**
     * @generated
     */
    private static String getImageRegistryKey(ENamedElement element) {
        return element.getName();
    }

    /**
     * @generated
     */
    private static ImageDescriptor getProvidedImageDescriptor(ENamedElement element) {
        if (element instanceof EStructuralFeature) {
            element = ((EStructuralFeature) element).getEContainingClass();
        }
        if (element instanceof EClass) {
            EClass eClass = (EClass) element;
            if (!eClass.isAbstract()) {
                return BusinessDiagramEditorPlugin.getInstance().getItemImageDescriptor(
                        eClass.getEPackage().getEFactoryInstance().create(eClass));
            }
        }
        // TODO : support structural features
        return null;
    }

    /**
     * @generated
     */
    public static ImageDescriptor getImageDescriptor(ENamedElement element) {
        String key = getImageRegistryKey(element);
        ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
        if (imageDescriptor == null) {
            imageDescriptor = getProvidedImageDescriptor(element);
            if (imageDescriptor == null) {
                imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
            }
            getImageRegistry().put(key, imageDescriptor);
        }
        return imageDescriptor;
    }

    /**
     * @generated
     */
    public static Image getImage(ENamedElement element) {
        String key = getImageRegistryKey(element);
        Image image = getImageRegistry().get(key);
        if (image == null) {
            ImageDescriptor imageDescriptor = getProvidedImageDescriptor(element);
            if (imageDescriptor == null) {
                imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
            }
            getImageRegistry().put(key, imageDescriptor);
            image = getImageRegistry().get(key);
        }
        return image;
    }

    /**
     * @generated
     */
    public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
        ENamedElement element = getElement(hint);
        if (element == null) {
            return null;
        }
        return getImageDescriptor(element);
    }

    /**
     * @generated
     */
    public static Image getImage(IAdaptable hint) {
        ENamedElement element = getElement(hint);
        if (element == null) {
            return null;
        }
        return getImage(element);
    }

    /**
     * Returns 'type' of the ecore object associated with the hint.
     * 
     * @generated
     */
    public static ENamedElement getElement(IAdaptable hint) {
        Object type = hint.getAdapter(IElementType.class);
        if (elements == null) {
            elements = new IdentityHashMap();
            elements.put(BusinessProcess_79, BusinessPackage.eINSTANCE.getBusinessProcess());
            elements.put(ActionBusinessItem_1001, BusinessPackage.eINSTANCE.getActionBusinessItem());
            elements.put(TerminalBusinessItem_1002, BusinessPackage.eINSTANCE.getTerminalBusinessItem());
            elements.put(DocumentBusinessItem_1003, BusinessPackage.eINSTANCE.getDocumentBusinessItem());
            elements.put(DatabaseBusinessItem_1004, BusinessPackage.eINSTANCE.getDatabaseBusinessItem());
            elements.put(ListBusinessItem_1005, BusinessPackage.eINSTANCE.getListBusinessItem());
            elements.put(DataBusinessItem_1006, BusinessPackage.eINSTANCE.getDataBusinessItem());
            elements.put(InputBusinessItem_1007, BusinessPackage.eINSTANCE.getInputBusinessItem());
            elements.put(DecisionBusinessItem_1008, BusinessPackage.eINSTANCE.getDecisionBusinessItem());
            elements.put(ActorBusinessItem_1009, BusinessPackage.eINSTANCE.getActorBusinessItem());
            elements.put(EllipseBusinessItem_1010, BusinessPackage.eINSTANCE.getEllipseBusinessItem());
            elements.put(GearBusinessItem_1011, BusinessPackage.eINSTANCE.getGearBusinessItem());
            elements.put(BusinessItemRelationship_3001, BusinessPackage.eINSTANCE.getBusinessItemRelationship());
        }
        return (ENamedElement) elements.get(type);
    }

    /**
     * @generated
     */
    public static final IElementType BusinessProcess_79 = getElementType("org.talend.designer.business.diagram.BusinessProcess_79"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType ActionBusinessItem_1001 = getElementType("org.talend.designer.business.diagram.ActionBusinessItem_1001"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType TerminalBusinessItem_1002 = getElementType("org.talend.designer.business.diagram.TerminalBusinessItem_1002"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType DocumentBusinessItem_1003 = getElementType("org.talend.designer.business.diagram.DocumentBusinessItem_1003"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType DatabaseBusinessItem_1004 = getElementType("org.talend.designer.business.diagram.DatabaseBusinessItem_1004"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType ListBusinessItem_1005 = getElementType("org.talend.designer.business.diagram.ListBusinessItem_1005"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType DataBusinessItem_1006 = getElementType("org.talend.designer.business.diagram.DataBusinessItem_1006"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType InputBusinessItem_1007 = getElementType("org.talend.designer.business.diagram.InputBusinessItem_1007"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType DecisionBusinessItem_1008 = getElementType("org.talend.designer.business.diagram.DecisionBusinessItem_1008"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType ActorBusinessItem_1009 = getElementType("org.talend.designer.business.diagram.ActorBusinessItem_1009"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType EllipseBusinessItem_1010 = getElementType("org.talend.designer.business.diagram.EllipseBusinessItem_1010"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType GearBusinessItem_1011 = getElementType("org.talend.designer.business.diagram.GearBusinessItem_1011"); //$NON-NLS-1$

    /**
     * @generated
     */
    public static final IElementType BusinessItemRelationship_3001 = getElementType("org.talend.designer.business.diagram.BusinessItemRelationship_3001"); //$NON-NLS-1$

    /**
     * @generated
     */
    private static IElementType getElementType(String id) {
        return ElementTypeRegistry.getInstance().getType(id);
    }

    /**
     * @generated
     */
    private static Set KNOWN_ELEMENT_TYPES;

    /**
     * @generated
     */
    public static boolean isKnownElementType(IElementType elementType) {
        if (KNOWN_ELEMENT_TYPES == null) {
            KNOWN_ELEMENT_TYPES = new HashSet();
            KNOWN_ELEMENT_TYPES.add(BusinessProcess_79);
            KNOWN_ELEMENT_TYPES.add(ActionBusinessItem_1001);
            KNOWN_ELEMENT_TYPES.add(TerminalBusinessItem_1002);
            KNOWN_ELEMENT_TYPES.add(DocumentBusinessItem_1003);
            KNOWN_ELEMENT_TYPES.add(DatabaseBusinessItem_1004);
            KNOWN_ELEMENT_TYPES.add(ListBusinessItem_1005);
            KNOWN_ELEMENT_TYPES.add(DataBusinessItem_1006);
            KNOWN_ELEMENT_TYPES.add(InputBusinessItem_1007);
            KNOWN_ELEMENT_TYPES.add(DecisionBusinessItem_1008);
            KNOWN_ELEMENT_TYPES.add(ActorBusinessItem_1009);
            KNOWN_ELEMENT_TYPES.add(EllipseBusinessItem_1010);
            KNOWN_ELEMENT_TYPES.add(GearBusinessItem_1011);
            KNOWN_ELEMENT_TYPES.add(BusinessItemRelationship_3001);
        }
        return KNOWN_ELEMENT_TYPES.contains(elementType);
    }
}
