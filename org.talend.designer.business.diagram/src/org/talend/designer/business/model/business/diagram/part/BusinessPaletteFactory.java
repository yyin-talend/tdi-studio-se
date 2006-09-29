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
        PaletteContainer paletteContainer = new PaletteDrawer("business");
        paletteContainer.add(createDecision1CreationTool());
        paletteContainer.add(createAction2CreationTool());
        paletteContainer.add(createTerminal3CreationTool());
        paletteContainer.add(createData4CreationTool());
        paletteContainer.add(createDocument5CreationTool());
        paletteContainer.add(createInput6CreationTool());
        paletteContainer.add(createList7CreationTool());
        paletteContainer.add(createDatabase8CreationTool());
        paletteContainer.add(new PaletteSeparator());
        paletteContainer.add(createRelationship10CreationTool());
        return paletteContainer;
    }

    /**
     * @generated
     */
    private ToolEntry createDecision1CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.DecisionBusinessItem_1008);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.DecisionBusinessItem_1008);
        ToolEntry result = new NodeToolEntry("Decision", "Create new Decision", smallImage, largeImage, elementTypes);

        return result;
    }

    /**
     * @generated
     */
    private ToolEntry createAction2CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.ActionBusinessItem_1001);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.ActionBusinessItem_1001);
        ToolEntry result = new NodeToolEntry("Action", "Create new Action", smallImage, largeImage, elementTypes);

        return result;
    }

    /**
     * @generated
     */
    private ToolEntry createTerminal3CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.TerminalBusinessItem_1002);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.TerminalBusinessItem_1002);
        ToolEntry result = new NodeToolEntry("Terminal", "Create new Terminal", smallImage, largeImage, elementTypes);

        return result;
    }

    /**
     * @generated
     */
    private ToolEntry createData4CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.DataBusinessItem_1006);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.DataBusinessItem_1006);
        ToolEntry result = new NodeToolEntry("Data", "Create new Data", smallImage, largeImage, elementTypes);

        return result;
    }

    /**
     * @generated
     */
    private ToolEntry createDocument5CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.DocumentBusinessItem_1003);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.DocumentBusinessItem_1003);
        ToolEntry result = new NodeToolEntry("Document", "Create new Document", smallImage, largeImage, elementTypes);

        return result;
    }

    /**
     * @generated
     */
    private ToolEntry createInput6CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.InputBusinessItem_1007);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.InputBusinessItem_1007);
        ToolEntry result = new NodeToolEntry("Input", "Create new Input", smallImage, largeImage, elementTypes);

        return result;
    }

    /**
     * @generated
     */
    private ToolEntry createList7CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.ListBusinessItem_1005);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.ListBusinessItem_1005);
        ToolEntry result = new NodeToolEntry("List", "Create new List", smallImage, largeImage, elementTypes);

        return result;
    }

    /**
     * @generated
     */
    private ToolEntry createDatabase8CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.DatabaseBusinessItem_1004);

        largeImage = smallImage;

        final List elementTypes = new ArrayList();
        elementTypes.add(BusinessElementTypes.DatabaseBusinessItem_1004);
        ToolEntry result = new NodeToolEntry("Database", "Create new Database", smallImage, largeImage, elementTypes);

        return result;
    }

    /**
     * @generated
     */
    private ToolEntry createRelationship10CreationTool() {
        ImageDescriptor smallImage;
        ImageDescriptor largeImage;

        smallImage = BusinessElementTypes.getImageDescriptor(BusinessElementTypes.BusinessItemRelationship_3001);

        largeImage = smallImage;

        final List relationshipTypes = new ArrayList();
        relationshipTypes.add(BusinessElementTypes.BusinessItemRelationship_3001);
        ToolEntry result = new LinkToolEntry("Relationship", "Create new Relationship", smallImage, largeImage, relationshipTypes);

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
