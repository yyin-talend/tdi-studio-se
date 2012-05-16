// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.dnd;

import org.eclipse.gef.requests.CreationFactory;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;

/**
 * wchen class global comment. Detailled comment
 */
public class NewNodeCreationFactory implements CreationFactory {

    private DropType dropType;

    private NodeType nodeType;

    private AbstractNode newObject;

    public NewNodeCreationFactory(DropType dropType, NodeType nodeType) {
        this.dropType = dropType;
        this.nodeType = nodeType;
        if (nodeType == null) {
            nodeType = NodeType.ELEMENT;
        }
    }

    public AbstractNode getNewObject() {
        if (newObject == null && dropType != null) {
            switch (dropType) {
            case DROP_OUTPUT_DOC_CHILD:
                OutputTreeNode outputNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                outputNode.setNodeType(nodeType);
                newObject = outputNode;
                break;
            case DROP_INSERT_INPUT:
                newObject = XmlmapFactory.eINSTANCE.createTreeNode();
                break;
            case DROP_INSERT_OUTPUT:
                newObject = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                break;
            case DROP_INSERT_VAR:
                newObject = XmlmapFactory.eINSTANCE.createVarNode();
                break;
            default:
                break;
            }
        }
        return newObject;
    }

    public DropType getObjectType() {
        return dropType;
    }

}
