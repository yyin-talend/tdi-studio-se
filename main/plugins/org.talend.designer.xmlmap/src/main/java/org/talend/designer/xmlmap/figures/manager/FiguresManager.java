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
package org.talend.designer.xmlmap.figures.manager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.AbstractInOutTreeFigure;
import org.talend.designer.xmlmap.figures.treetools.zone.InputZoneToolBar;
import org.talend.designer.xmlmap.figures.treetools.zone.OutputZoneToolBar;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.parts.AbstractInOutTreeEditPart;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.XmlMapDataEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class FiguresManager implements ISelectionChangedListener {

    private XmlMapGraphicViewer graphicViewer;

    private XmlMapDataEditPart mapDataEditPart;

    private InputXmlTreeEditPart currentSelectedInputTree;

    private OutputXmlTreeEditPart currentSelectedOutputTree;

    public FiguresManager(XmlMapGraphicViewer graphicViewer) {
        this.graphicViewer = graphicViewer;

    }

    public void selectionChanged(SelectionChangedEvent event) {
        List children2 = graphicViewer.getRootEditPart().getChildren();
        if (children2.size() == 1 && children2.get(0) instanceof XmlMapDataEditPart) {
            mapDataEditPart = (XmlMapDataEditPart) children2.get(0);
        }
        if (mapDataEditPart == null) {
            return;
        }
        List selectedEditParts = graphicViewer.getSelectedEditParts();
        if (selectedEditParts.isEmpty()) {
            return;
        }
        boolean outputRemoveEnable = false;
        boolean outputMoveUp = false;
        boolean outputMoveDown = false;
        boolean inputMoveUp = false;
        boolean inputMoveDown = false;
        EList<OutputXmlTree> outputTrees = ((XmlMapData) mapDataEditPart.getModel()).getOutputTrees();
        EList<InputXmlTree> inputTrees = ((XmlMapData) mapDataEditPart.getModel()).getInputTrees();

        OutputZoneToolBar outputToolBar = mapDataEditPart.getOutputZoneToolBar();
        InputZoneToolBar inputToolBar = mapDataEditPart.getInputZoneToolBar();

        boolean selectNode = false;

        Object lastSelection = selectedEditParts.get(selectedEditParts.size() - 1);
        if (lastSelection instanceof TreeNodeEditPart) {
            selectNode = true;
            AbstractInOutTreeEditPart findTreePart = XmlMapUtil.findTreePart((TreeNodeEditPart) lastSelection);
            if (findTreePart instanceof InputXmlTreeEditPart) {
                currentSelectedInputTree = (InputXmlTreeEditPart) findTreePart;
            } else if (findTreePart instanceof OutputXmlTreeEditPart) {
                currentSelectedOutputTree = (OutputXmlTreeEditPart) findTreePart;
            }

            // only change connection figures for nodes in the same tree as the lastSelection
            List<TreeNodeEditPart> nodes = new ArrayList<TreeNodeEditPart>();
            for (int i = 0; i < selectedEditParts.size() - 1; i++) {
                Object obj = selectedEditParts.get(i);
                if (obj instanceof TreeNodeEditPart) {
                    AbstractInOutTreeEditPart findOtherTreePart = XmlMapUtil.findTreePart((TreeNodeEditPart) obj);
                    if (findOtherTreePart == findTreePart) {
                        nodes.add((TreeNodeEditPart) obj);
                    }
                }
            }
            nodes.add((TreeNodeEditPart) lastSelection);
            onTreeSelected(nodes, findTreePart);
            // change color for other unselected node in the tree;

        } else if (lastSelection instanceof OutputXmlTreeEditPart) {
            currentSelectedOutputTree = (OutputXmlTreeEditPart) lastSelection;
            onTreeSelected(currentSelectedOutputTree.getChildren(), currentSelectedOutputTree);
        } else if (lastSelection instanceof InputXmlTreeEditPart) {
            currentSelectedInputTree = (InputXmlTreeEditPart) lastSelection;
            onTreeSelected(currentSelectedInputTree.getChildren(), currentSelectedInputTree);

        }

        if (currentSelectedOutputTree != null) {
            OutputXmlTree model = (OutputXmlTree) ((OutputXmlTreeEditPart) currentSelectedOutputTree).getModel();
            if (!model.isErrorReject()) {
                outputRemoveEnable = true;
            }
            int index = outputTrees.indexOf(model);
            if (index != -1 && index != 0) {
                if (index != 1 || !outputTrees.get(0).isErrorReject()) {
                    outputMoveUp = true;
                }
            }
            if (index != -1 && index != outputTrees.size() - 1) {
                if (!model.isErrorReject()) {
                    outputMoveDown = true;
                }
            }
        }
        outputToolBar.setRemoveButtonEnable(outputRemoveEnable);
        outputToolBar.setMoveUpEnable(outputMoveUp);
        outputToolBar.setMoveDownEnable(outputMoveDown);

        if (currentSelectedInputTree != null) {
            InputXmlTree tree = (InputXmlTree) currentSelectedInputTree.getModel();
            if (!tree.isLookup()) {
                inputMoveUp = false;
                inputMoveDown = false;
            } else {
                int indexOf = inputTrees.indexOf(tree);
                if (indexOf != -1 && indexOf > 1) {
                    inputMoveUp = true;
                }
                if (indexOf > 0 && indexOf != inputTrees.size() - 1) {
                    inputMoveDown = true;
                }
            }
        }
        inputToolBar.setMoveDownEnable(inputMoveDown);
        inputToolBar.setMoveUpEnable(inputMoveUp);

    }

    private void onTreeSelected(List nodes, AbstractInOutTreeEditPart selectedTreePart) {
        selectedTreePart.setTreeOrChildSelected(true);
        /*
         * clean colors for other trees in the same zone must be executed before highlight the selected tree ,or the
         * color for LookupConnection won't be changed
         */
        List children = mapDataEditPart.getChildren();
        for (Object obj : children) {
            AbstractInOutTreeEditPart abstractPart = null;
            if (selectedTreePart instanceof InputXmlTreeEditPart && obj instanceof InputXmlTreeEditPart) {
                abstractPart = (InputXmlTreeEditPart) obj;
            } else if (selectedTreePart instanceof OutputXmlTreeEditPart && obj instanceof OutputXmlTreeEditPart) {
                abstractPart = (OutputXmlTreeEditPart) obj;
            }
            if (abstractPart != null) {
                if (obj != selectedTreePart) {
                    ((AbstractInOutTreeFigure) abstractPart.getFigure()).highLightHeader(false);
                }
                abstractPart.updateChildrenConnections(abstractPart.getChildren(), false);
            }
        }

        AbstractInOutTreeFigure treeFigure = (AbstractInOutTreeFigure) selectedTreePart.getFigure();
        treeFigure.highLightHeader(true);
        selectedTreePart.updateChildrenConnections(nodes, true);

    }

    public InputXmlTreeEditPart getCurrentSelectedInputXmlTree() {
        return this.currentSelectedInputTree;
    }

    public OutputXmlTreeEditPart getCurrentSelectedOutputXmlTree() {
        return this.currentSelectedOutputTree;
    }

}
