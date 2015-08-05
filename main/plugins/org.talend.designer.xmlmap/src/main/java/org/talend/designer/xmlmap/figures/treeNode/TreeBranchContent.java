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
package org.talend.designer.xmlmap.figures.treeNode;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.jface.window.Window;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.cells.ITextCell;
import org.talend.designer.xmlmap.figures.treetools.ToolBarButtonImageFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputLoopNodesTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.directedit.DirectEditType;
import org.talend.designer.xmlmap.ui.dialog.SetLoopFunctionDialog;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeBranchContent extends Figure implements ITextCell {

    private int alpha = 255;

    protected Label nameFigure;

    protected Label statusFigure;

    protected Label defaultValue;

    private TreeNode treeNode;

    private ToolBarButtonImageFigure loopButtonFigure;

    private InputXmlTree inputMainTable;

    public TreeBranchContent(final TreeNodeEditPart treeNodePart) {
        setDirectEditType(DirectEditType.NODE_NAME);
        this.treeNode = (TreeNode) treeNodePart.getModel();
        inputMainTable = ((XmlMapGraphicViewer) treeNodePart.getViewer()).getMapperManager().getMainInputTree();

        GridLayout manager = new GridLayout(4, false);
        manager.horizontalSpacing = 5;
        manager.verticalSpacing = 1;
        manager.marginHeight = -1;
        manager.marginWidth = 5;
        setLayoutManager(manager);
        nameFigure = new Label();
        nameFigure.setText(getTreeBranchName(treeNode));

        statusFigure = new Label();
        statusFigure.setForegroundColor(ColorConstants.red);
        statusFigure.setText(getStatus(treeNode));

        defaultValue = new Label();
        defaultValue.setForegroundColor(ColorConstants.blue);
        defaultValue.setText(getDefaultValue(treeNode));

        setOpaque(true);

        this.add(nameFigure);
        //

        ImageInfo infor = ImageInfo.SETLOOPFUNCTION_BUTTON;
        if (treeNode instanceof OutputTreeNode) {
            InputLoopNodesTable inputLoopNodesTable = ((OutputTreeNode) treeNode).getInputLoopNodesTable();
            infor = (inputLoopNodesTable == null || inputLoopNodesTable.getInputloopnodes().isEmpty()) ? ImageInfo.SETLOOPFUNCTION_BUTTON_ERROR
                    : ImageInfo.SETLOOPFUNCTION_BUTTON;
        }
        loopButtonFigure = new ToolBarButtonImageFigure(ImageProviderMapper.getImage(infor));
        loopButtonFigure.addMouseListener(new MouseListener.Stub() {

            @Override
            public void mousePressed(MouseEvent me) {
                OutputTreeNode outputTreeNode = (OutputTreeNode) treeNode;
                List<TreeNode> loopNodes = new ArrayList<TreeNode>();
                if (inputMainTable != null && inputMainTable.isMultiLoops()) {
                    loopNodes.addAll(XmlMapUtil.getMultiLoopsForXmlTree(inputMainTable));
                }
                InputLoopNodesTable inputLoopNodesTable = null;
                if (outputTreeNode.getInputLoopNodesTable() != null) {
                    inputLoopNodesTable = outputTreeNode.getInputLoopNodesTable();
                } else {
                    inputLoopNodesTable = XmlmapFactory.eINSTANCE.createInputLoopNodesTable();
                    outputTreeNode.setInputLoopNodesTable(inputLoopNodesTable);
                    AbstractInOutTree abstractInOutTree = XmlMapUtil.getAbstractInOutTree(outputTreeNode);
                    if (abstractInOutTree != null) {
                        ((OutputXmlTree) abstractInOutTree).getInputLoopNodesTables().add(inputLoopNodesTable);
                    }
                }
                SetLoopFunctionDialog nsDialog = new SetLoopFunctionDialog(null, outputTreeNode.getInputLoopNodesTable(),
                        loopNodes);
                if (nsDialog.open() == Window.OK) {
                    ((XmlMapGraphicViewer) treeNodePart.getViewer()).getMapperManager().getProblemsAnalyser()
                            .checkProblems(XmlMapUtil.getAbstractInOutTree(outputTreeNode));
                    ((XmlMapGraphicViewer) treeNodePart.getViewer()).getMapperManager().getMapperUI().updateStatusBar();
                }
            }
        });

        if (treeNode != null && treeNode instanceof OutputTreeNode) {
            // display loop setup button only when input main is multiloop
            if (treeNode.isLoop() && inputMainTable != null && inputMainTable.isMultiLoops()) {
                this.add(loopButtonFigure);
            }
        }
        this.add(statusFigure);
        this.add(defaultValue);
    }

    @Override
    public void paint(Graphics graphics) {

        if (alpha != -1) {
            graphics.setAlpha(alpha);
        } else {
            graphics.setAlpha(255);
        }
        super.paint(graphics);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    private String getTreeBranchName(TreeNode model) {
        String name = "";
        if (NodeType.ATTRIBUT.equals(model.getNodeType())) {
            name = XmlMapUtil.XPATH_ATTRIBUTE + model.getName();
        } else if (NodeType.NAME_SPACE.equals(model.getNodeType())) {
            name = XmlMapUtil.XPATH_NAMESPACE + model.getName();
        } else {
            if (model.isChoice()) {
                name = XSDPopulationUtil2.CHOICE;
            } else {
                name = model.getName();
            }
        }
        return name;
    }

    private String getDefaultValue(TreeNode node) {
        if (NodeType.NAME_SPACE.equals(node.getNodeType())) {
            String defaultValue2 = node.getDefaultValue();
            if (defaultValue2 == null || "".equals(defaultValue2)) {
                return "";
            }
            return "(Default Value :" + defaultValue2 + ")";
        } else {
            return "";
        }
    }

    public String getStatus(TreeNode node) {
        String status = "";
        if (node.isLoop()) {
            status = "(loop" + (node.isOptional() ? " :optional" : "");
        }
        if (node instanceof OutputTreeNode) {
            OutputTreeNode outputNode = (OutputTreeNode) node;
            if (outputNode.isGroup()) {
                if ("".equals(status)) {
                    status = "(group";
                } else {
                    status = status + " , group";
                }

            }

            if (outputNode.isAggregate()) {
                if ("".equals(status)) {
                    status = "(aggregate";
                } else {
                    status = status + " , aggregate";
                }

            }
        }

        if (!"".equals(status)) {
            status = status + ")";
        }

        return status;
    }

    public void updateLoopButtonFigure() {
        if (treeNode instanceof OutputTreeNode) {
            if (treeNode.isLoop() && inputMainTable != null && inputMainTable.isMultiLoops()) {
                if (!this.getChildren().contains(loopButtonFigure)) {
                    this.add(loopButtonFigure, 1);
                }
                InputLoopNodesTable loopNodesTable = ((OutputTreeNode) treeNode).getInputLoopNodesTable();
                if (loopNodesTable == null || loopNodesTable.getInputloopnodes().isEmpty()) {
                    loopButtonFigure.setImage(ImageProviderMapper.getImage(ImageInfo.SETLOOPFUNCTION_BUTTON_ERROR));
                } else {
                    loopButtonFigure.setImage(ImageProviderMapper.getImage(ImageInfo.SETLOOPFUNCTION_BUTTON));
                }

            } else {
                if (this.getChildren().contains(loopButtonFigure)) {
                    this.remove(loopButtonFigure);
                }
            }
        }

    }

    public void updateStatus() {
        statusFigure.setText(getStatus(treeNode));
    }

    public void updataNameFigure() {
        nameFigure.setText(getTreeBranchName(treeNode));
    }

    public void updateDefaultValueFigure() {
        defaultValue.setText(getDefaultValue(treeNode));
    }

    private DirectEditType type;

    @Override
    public void setDirectEditType(DirectEditType type) {
        this.type = type;
    }

    @Override
    public DirectEditType getDirectEditType() {
        return this.type;
    }

}
