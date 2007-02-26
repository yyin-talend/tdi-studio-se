package org.talend.designer.business.model.business.diagram.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.designer.business.model.business.diagram.providers.BusinessElementTypes;

/**
 * @generated
 */
public class BusinessPaletteFactory {

    /**
     * @generated
     */
    public void fillPalette(PaletteRoot paletteRoot) {
        paletteRoot.add(createbusiness1Group());
    }

    /**
     * @generated
     */
    private PaletteContainer createbusiness1Group() {
        PaletteContainer paletteContainer = new PaletteDrawer("business"); //$NON-NLS-1$
        paletteContainer.add(createDecision1CreationTool());
        paletteContainer.add(createAction2CreationTool());
        paletteContainer.add(createTerminal3CreationTool());
        paletteContainer.add(createData4CreationTool());
        paletteContainer.add(createDocument5CreationTool());
        paletteContainer.add(createInput6CreationTool());
        paletteContainer.add(createList7CreationTool());
        paletteContainer.add(createDatabase8CreationTool());
        paletteContainer.add(createActor9CreationTool());
        paletteContainer.add(createEllipse10CreationTool());
        paletteContainer.add(createGear11CreationTool());
        paletteContainer.add(new PaletteSeparator());
        paletteContainer.add(createRelationship13CreationTool());
        return paletteContainer;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createDecision1CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.DecisionBusinessItem_1008);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.DecisionBusinessItem_1008);
        ToolEntry result = new NodeToolEntry(
                Messages.getString("BusinessPaletteFactory.decision"), Messages.getString("BusinessPaletteFactory.DecisionComment"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createAction2CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.ActionBusinessItem_1001);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.ActionBusinessItem_1001);
        ToolEntry result = new NodeToolEntry(
                Messages.getString("BusinessPaletteFactory.Action"), Messages.getString("BusinessPaletteFactory.ActionComment"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createTerminal3CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.TerminalBusinessItem_1002);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.TerminalBusinessItem_1002);
        ToolEntry result = new NodeToolEntry(
                Messages.getString("BusinessPaletteFactory.Terminal"), Messages.getString("BusinessPaletteFactory.TerminalComment"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createData4CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.DataBusinessItem_1006);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.DataBusinessItem_1006);
        ToolEntry result = new NodeToolEntry(
                Messages.getString("BusinessPaletteFactory.Data"), Messages.getString("BusinessPaletteFactory.DataComment"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createDocument5CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.DocumentBusinessItem_1003);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.DocumentBusinessItem_1003);
        ToolEntry result = new NodeToolEntry(
                Messages.getString("BusinessPaletteFactory.Document"), Messages.getString("BusinessPaletteFactory.DocuemntComment"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createInput6CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.InputBusinessItem_1007);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.InputBusinessItem_1007);
        ToolEntry result = new NodeToolEntry(
                Messages.getString("BusinessPaletteFactory.Input"), Messages.getString("BusinessPaletteFactory.InputComment"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createList7CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.ListBusinessItem_1005);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.ListBusinessItem_1005);
        ToolEntry result = new NodeToolEntry(
                Messages.getString("BusinessPaletteFactory.List"), Messages.getString("BusinessPaletteFactory.ListComment"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createDatabase8CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.DatabaseBusinessItem_1004);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.DatabaseBusinessItem_1004);
        ToolEntry result = new NodeToolEntry(
                Messages.getString("BusinessPaletteFactory.Database"), Messages.getString("BusinessPaletteFactory.DatabaseComment"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createActor9CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.ActorBusinessItem_1009);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.ActorBusinessItem_1009);
        ToolEntry result = new NodeToolEntry(Messages.getString("BusinessPaletteFactory.Actor"), Messages.getString("BusinessPaletteFactory.CreateActor"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createEllipse10CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.EllipseBusinessItem_1010);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.EllipseBusinessItem_1010);
        ToolEntry result = new NodeToolEntry(Messages.getString("BusinessPaletteFactory.Ellipse"), Messages.getString("BusinessPaletteFactory.CreateEllipse"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createGear11CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.GearBusinessItem_1011);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.GearBusinessItem_1011);
        ToolEntry result = new NodeToolEntry(Messages.getString("BusinessPaletteFactory.Gear"), Messages.getString("BusinessPaletteFactory.CreateGear"), smallImage, largeImage, elementTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated NOT
     */
    private ToolEntry createRelationship13CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.BusinessItemRelationship_3001);

        largeImage = smallImage;

        final List relationshipTypes = new ArrayList();
        relationshipTypes.add(BusinessElementTypes.BusinessItemRelationship_3001);
        ToolEntry result = new LinkToolEntry(
                Messages.getString("BusinessPaletteFactory.Relationshop"), Messages.getString("BusinessPaletteFactory.RelationshopComment"), smallImage, largeImage, relationshipTypes); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @generated
     */
    private static class NodeToolEntry extends ToolEntry {

        /**
         * @generated
         */
        private final List elementTypes;

        /**
         * @generated
         */
        private NodeToolEntry(String title, String description, ImageDescriptor smallIcon, ImageDescriptor largeIcon,
                List elementTypes) {
            super(title, description, smallIcon, largeIcon);
            this.elementTypes = elementTypes;
        }

        /**
         * @generated
         */
        public Tool createTool() {
            Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
            tool.setProperties(getToolProperties());
            return tool;
        }
    }

    /**
     * @generated
     */
    private static class LinkToolEntry extends ToolEntry {

        /**
         * @generated
         */
        private final List relationshipTypes;

        /**
         * @generated
         */
        private LinkToolEntry(String title, String description, ImageDescriptor smallIcon, ImageDescriptor largeIcon,
                List relationshipTypes) {
            super(title, description, smallIcon, largeIcon);
            this.relationshipTypes = relationshipTypes;
        }

        /**
         * @generated
         */
        public Tool createTool() {
            Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
            tool.setProperties(getToolProperties());
            return tool;
        }
    }
}
