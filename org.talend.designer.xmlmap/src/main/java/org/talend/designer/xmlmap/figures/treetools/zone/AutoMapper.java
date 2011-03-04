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
package org.talend.designer.xmlmap.figures.treetools.zone;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class AutoMapper {

    private XmlMapData xmlMapData;

    /**
     * Map all empty output expression cells if possible.
     * 
     * @param manager
     */
    public AutoMapper(XmlMapData xmlMapData) {
        this.xmlMapData = xmlMapData;
    }

    /**
     * DOC amaumont Comment method "map".
     */
    public void map() {
        EList<InputXmlTree> inputTrees = xmlMapData.getInputTrees();
        EList<OutputXmlTree> outputTrees = xmlMapData.getOutputTrees();

        // output tables are the references
        for (OutputXmlTree outputTree : outputTrees) {

            List<OutputTreeNode> outputEntries = outputTree.getNodes();
            boolean mapFound = false;
            for (OutputTreeNode outputEntry : outputEntries) {

                mapFound = false;

                if (outputEntry.getExpression() == null || "".equals(outputEntry.getExpression())) {

                    String outputColumnName = outputEntry.getName();

                    for (InputXmlTree inputTable : inputTrees) {

                        List<TreeNode> inputColumnEntries = inputTable.getNodes();
                        for (TreeNode inputEntry : inputColumnEntries) {
                            if (inputEntry.getName().equalsIgnoreCase(outputColumnName)) {
                                outputEntry.setExpression(XmlMapUtil.convertToExpression(inputEntry.getXpath()));
                                Connection conn = XmlmapFactory.eINSTANCE.createConnection();
                                conn.setSource(inputEntry);
                                conn.setTarget(outputEntry);
                                outputEntry.getIncomingConnections().add(conn);
                                inputEntry.getOutgoingConnections().add(conn);
                                xmlMapData.getConnections().add(conn);
                                mapFound = true;
                                break;
                            }
                        }
                        if (mapFound) {
                            break;
                        }

                    }

                }

            }

        }

    }

}
