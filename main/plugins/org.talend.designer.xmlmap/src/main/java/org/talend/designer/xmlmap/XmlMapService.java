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
package org.talend.designer.xmlmap;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.service.IXmlMapService;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;

/**
 * DOC talend class global comment. Detailled comment
 */
public class XmlMapService implements IXmlMapService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IXmlMapService#isXmlMapComponent(org.talend.core.model.process.IExternalNode)
     */
    @Override
    public boolean isXmlMapComponent(IExternalNode node) {
        if (node instanceof XmlMapComponent) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IXmlMapService#checkXMLMapDifferents(org.talend.core.model.process.INode,
     * org.talend.core.model.process.INode)
     */
    @Override
    public boolean checkXMLMapDifferents(INode testNode, INode originalNode) {
        AbstractExternalData oriExternalData = originalNode.getExternalNode().getExternalEmfData();
        AbstractExternalData testExternalData = testNode.getExternalNode().getExternalEmfData();

        if (oriExternalData == null || testExternalData == null) {
            if (oriExternalData != testExternalData) {
                return true;
            }
        }

        if (oriExternalData == null && testExternalData == null) {
            return false;
        }
        if (!(oriExternalData instanceof XmlMapData) || !(testExternalData instanceof XmlMapData)) {
            return false;
        }
        XmlMapData oriXmlData = (XmlMapData) oriExternalData;
        XmlMapData testXmlData = (XmlMapData) testExternalData;

        EList<InputXmlTree> oriInputs = oriXmlData.getInputTrees();
        EList<OutputXmlTree> oriOutputs = oriXmlData.getOutputTrees();
        EList<VarTable> oriVars = oriXmlData.getVarTables();

        EList<InputXmlTree> testInputs = testXmlData.getInputTrees();
        EList<OutputXmlTree> testOutputs = testXmlData.getOutputTrees();
        EList<VarTable> testVars = testXmlData.getVarTables();

        if (oriInputs == null || testInputs == null) {
            if (oriInputs != testInputs) {
                return true;
            }
        }

        if (oriInputs == null && testInputs == null) {
            return false;
        }

        if (oriOutputs == null || testOutputs == null) {
            if (oriOutputs != testOutputs) {
                return true;
            }
        }

        if (oriOutputs == null && testOutputs == null) {
            return false;
        }

        if (oriVars == null || testVars == null) {
            if (oriVars != testVars) {
                return true;
            }
        }

        if (oriVars == null && testVars == null) {
            return false;
        }

        if (oriInputs.size() != testInputs.size()) {
            return true;
        }
        if (oriOutputs.size() != testOutputs.size()) {
            return true;
        }
        if (oriVars.size() != testVars.size()) {
            return true;
        }

        for (InputXmlTree oriInput : oriInputs) {
            String oriName = oriInput.getName();
            InputXmlTree testInput = null;
            for (InputXmlTree input : testInputs) {
                if (input.getName().equals(oriName)) {
                    testInput = input;
                    break;
                }
            }
            if (testInput == null) {
                return true;
            }
            if (oriInput.isActivateExpressionFilter() != testInput.isActivateExpressionFilter()) {
                return true;
            }
            if (oriInput.getExpressionFilter() != testInput.getExpressionFilter()) {
                return true;
            }
            if (oriInput.isMinimized() != testInput.isMinimized()) {
                return true;
            }
            if (oriInput.isActivateCondensedTool() != testInput.isActivateCondensedTool()) {
                return true;
            }
            EList<TreeNode> oriEntrys = oriInput.getNodes();
            EList<TreeNode> testEntrys = testInput.getNodes();
            if (oriEntrys == null && testEntrys != null) {
                if (testEntrys.isEmpty()) {
                    return true;
                }
            }
            if (oriEntrys != null && testEntrys == null) {
                if (oriEntrys.isEmpty()) {
                    return true;
                }
            }
            if (oriEntrys == null || testEntrys == null) {
                continue;
            }
            if (oriEntrys.size() != testEntrys.size()) {
                return true;
            }
            for (TreeNode oriEntry : oriEntrys) {
                String oriEntryName = oriEntry.getName();
                boolean found = false;
                for (TreeNode testEntry : testEntrys) {
                    if (oriEntryName.equals(testEntry.getName())) {
                        found = true;
                        if (oriEntry.getExpression() == null || testEntry.getExpression() == null) {
                            if (oriEntry.getExpression() != testEntry.getExpression()) {
                                return true;
                            }
                        }
                        if (oriEntry.getExpression() == null && testEntry.getExpression() == null) {
                            continue;
                        }
                        if (!oriEntry.getExpression().equals(testEntry.getExpression())) {
                            return true;
                        }
                    }
                }
                if (!found) {
                    return true;
                }
            }
        }

        for (OutputXmlTree oriOutput : oriOutputs) {
            String oriName = oriOutput.getName();
            OutputXmlTree testOutput = null;
            for (OutputXmlTree output : testOutputs) {
                if (output.getName().equals(oriName)) {
                    testOutput = output;
                    break;
                }
            }
            if (testOutput == null) {
                return true;
            }
            if (oriOutput.isActivateExpressionFilter() != testOutput.isActivateExpressionFilter()) {
                return true;
            }
            if (oriOutput.getExpressionFilter() != testOutput.getExpressionFilter()) {
                return true;
            }
            if (oriOutput.isMinimized() != testOutput.isMinimized()) {
                return true;
            }
            if (oriOutput.isActivateCondensedTool() != testOutput.isActivateCondensedTool()) {
                return true;
            }

            EList<OutputTreeNode> oriEntrys = oriOutput.getNodes();
            EList<OutputTreeNode> testEntrys = testOutput.getNodes();
            if (oriEntrys == null && testEntrys != null) {
                if (!testEntrys.isEmpty()) {
                    return true;
                }
            }
            if (oriEntrys != null && testEntrys == null) {
                if (!oriEntrys.isEmpty()) {
                    return true;
                }
            }
            if (oriEntrys == null || testEntrys == null) {
                continue;
            }
            if (oriEntrys.size() != testEntrys.size()) {
                return true;
            }
            for (OutputTreeNode oriEntry : oriEntrys) {
                String oriEntryName = oriEntry.getName();
                boolean found = false;
                for (OutputTreeNode testEntry : testEntrys) {
                    if (oriEntryName.equals(testEntry.getName())) {
                        found = true;
                        if (oriEntry.getExpression() == null || testEntry.getExpression() == null) {
                            if (oriEntry.getExpression() != testEntry.getExpression()) {
                                return true;
                            }
                        }
                        if (oriEntry.getExpression() == null && testEntry.getExpression() == null) {
                            continue;
                        }
                        if (!oriEntry.getExpression().equals(testEntry.getExpression())) {
                            return true;
                        }
                    }
                }
                if (!found) {
                    return true;
                }
            }
        }

        for (VarTable oriVar : oriVars) {
            String oriName = oriVar.getName();
            VarTable testVar = null;
            for (VarTable var : testVars) {
                if (var.getName().equals(oriName)) {
                    testVar = var;
                    break;
                }
            }
            if (testVar == null) {
                return true;
            }
            if (oriVar.isMinimized() != testVar.isMinimized()) {
                return true;
            }
            EList<VarNode> oriEntrys = oriVar.getNodes();
            EList<VarNode> testEntrys = testVar.getNodes();
            if (oriEntrys == null && testEntrys != null) {
                if (!testEntrys.isEmpty()) {
                    return true;
                }
            }
            if (oriEntrys != null && testEntrys == null) {
                if (!oriEntrys.isEmpty()) {
                    return true;
                }
            }
            if (oriEntrys == null || testEntrys == null) {
                continue;
            }
            if (oriEntrys.size() != testEntrys.size()) {
                return true;
            }
            for (VarNode oriEntry : oriEntrys) {
                String oriEntryName = oriEntry.getName();
                boolean found = false;
                for (VarNode testEntry : testEntrys) {
                    if (oriEntryName.equals(testEntry.getName())) {
                        found = true;
                        if (oriEntry.getExpression() == null || testEntry.getExpression() == null) {
                            if (oriEntry.getExpression() != testEntry.getExpression()) {
                                return true;
                            }
                        }
                        if (oriEntry.getExpression() == null && testEntry.getExpression() == null) {
                            continue;
                        }
                        if (!oriEntry.getExpression().equals(testEntry.getExpression())) {
                            return true;
                        }
                    }
                }
                if (!found) {
                    return true;
                }
            }
        }

        return false;
    }

}
