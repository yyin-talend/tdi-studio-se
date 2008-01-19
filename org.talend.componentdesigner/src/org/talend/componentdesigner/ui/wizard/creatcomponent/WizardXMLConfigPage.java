// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.componentdesigner.ui.wizard.creatcomponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.componentdesigner.model.ILibEntry;
import org.talend.componentdesigner.model.componentpref.ComponentPref;
import org.talend.componentdesigner.ui.composite.TreeNodeAttrCompsite;
import org.talend.componentdesigner.ui.composite.xmltree.ATreeNodeUtil;
import org.talend.componentdesigner.ui.composite.xmltree.TreeNodeData;
import org.talend.componentdesigner.ui.composite.xmltree.TreePopulator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * DOC rli  class global comment. Detailled comment
 */
public class WizardXMLConfigPage extends AbstractComponentPage {
	
    private static final String IMPORTS = "IMPORTS";

	private static final String IMPORT = "IMPORT";

	private static final String REQUIRED = "REQUIRED";

	private static final String MODULE = "MODULE";

	private static final String NAME = "NAME";

	private static final String CODEGENERATION = "CODEGENERATION";
    
    private static final String IMPORTITEMPATH = "COMPONENT/CODEGENERATION/IMPORTS/IMPORT";
    
    private static final String IMPORTSITEMPATH = "COMPONENT/CODEGENERATION/IMPORTS";

	private transient Tree availableXmlTree;
    
    private TreePopulator treePopulator;
    
    private Composite rightComposite;

	private TreeNodeAttrCompsite nodeAttrCompsite;

	/**
	 * DOC rli WizardXMLConfigPage constructor comment.
	 * @param pageName
	 * @param componentPref
	 */
	public WizardXMLConfigPage(String pageName, ComponentPref componentPref) {
		super(pageName, componentPref);
	}

	/* (non-Javadoc)
	 * @see org.talend.componentdesigner.ui.wizard.creatcomponent.AbstractComponentPage#createPageContent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPageContent(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		GridData data = new GridData(GridData.FILL_BOTH);
		topComposite.setLayoutData(data);
		availableXmlTree = new Tree(topComposite,  SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		GridData treeGD = new GridData(GridData.FILL_BOTH);
		treeGD.widthHint = 110;
		treeGD.grabExcessHorizontalSpace = true;
		treeGD.grabExcessVerticalSpace = true;
		availableXmlTree.setLayoutData(treeGD);
		
		rightComposite = new Composite(topComposite, SWT.NONE);
		GridData rightPanelGd = new GridData(GridData.FILL_BOTH);
		rightPanelGd.widthHint = 180;
		rightComposite.setLayoutData(rightPanelGd);
		rightComposite.setLayout(new GridLayout());
		
		Label label = new Label(rightComposite, SWT.NONE);
		label.setText("Set the properties for selected element.");
		GridData labelGd = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(labelGd);
		

		this.setControl(topComposite);
	}
	
	/* (non-Javadoc)
	 * @see org.talend.componentdesigner.ui.wizard.creatcomponent.AbstractComponentPage#initialize()
	 */
	protected void initialize() {
		availableXmlTree.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				if (availableXmlTree.getMenu() != null) {
					availableXmlTree.getMenu().dispose();
				}			

				final TreeItem[] treeItem = ((Tree) event.getSource()).getSelection();
				
				Menu popMenu = new Menu(availableXmlTree);
				MenuItem itemNew = new MenuItem(popMenu, SWT.CASCADE);
				itemNew.setText("New");
				MenuItem itemDel = new MenuItem(popMenu, SWT.CASCADE);
				itemDel.setText("Delete");
				itemDel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						TreeNodeData currentNodeData = (TreeNodeData) treeItem[0]
								.getParentItem().getData();
						currentNodeData.getXMLNode().removeChild(
								((TreeNodeData) treeItem[0].getData())
										.getXMLNode());
						treeItem[0].dispose();
					}
				});
				
				
				final TreeNodeData treeNodeData = ((TreeNodeData) treeItem[0].getData());
				if (treeNodeData.isHasChildTreeNode()) {
					Menu subNewMenu = new Menu(itemNew);
					itemNew.setMenu(subNewMenu);
					
					Object[] subNodeNames = treeNodeData
							.getChildNodeNames();
					for (Object nodeName : subNodeNames) {
						final MenuItem nodeMenuItem = new MenuItem(subNewMenu, SWT.CASCADE);
						final String itemName = (String) nodeName;
						nodeMenuItem.setText(itemName);
						nodeMenuItem
								.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent e) {
										TreeItem newItem = new TreeItem(
												treeItem[0], 0);
										newItem.setText(itemName);
										TreeNodeData newNodeData = null;
										newNodeData = new TreeNodeData();	
										Node parentNode = treeNodeData.getXMLNode();
										newNodeData
												.setXMLNode(parentNode
														.appendChild(parentNode
																.getOwnerDocument()
																.createElement(
																		itemName)));
										newNodeData.setTreePath(treeNodeData
												.getTreePath()
												+ "/" + itemName);
										newNodeData.setTreeNode(ATreeNodeUtil
												.getTreeNodeByPath(newNodeData.getTreePath()));
										newItem.setData(newNodeData);
									}
								});
					}
				} else {
					itemNew.setEnabled(false);
				}		

				availableXmlTree.setMenu(popMenu);
				
				if (nodeAttrCompsite != null) {
					nodeAttrCompsite.dispose();
				}
				rebuildAttrComposite(treeNodeData);
				rightComposite.layout();
			}

		});
		
		this.treePopulator = new TreePopulator(availableXmlTree, componentPref.getXmlFileName());
		treePopulator.populateTree();
		componentPref.setXmlDocument(treePopulator.getReadDocument());
	}

	/* (non-Javadoc)
	 * @see org.talend.componentdesigner.ui.wizard.creatcomponent.AbstractComponentPage#validatePage()
	 */
	protected boolean validatePage() {		
		return false;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (componentPref.getLibEntries() != null
				&& componentPref.getLibEntries().length != 0) {
			fillImportItems();
		}
		super.setVisible(visible);
	}

	private void fillImportItems() {
		for (int i = 0; i < availableXmlTree.getItems().length; i++) {
			TreeItem item = availableXmlTree.getItem(i);
			if (item.getText().equals(CODEGENERATION)) {
				Node codeGenNode = ((TreeNodeData) item.getData()).getXMLNode();
				
				TreeItem importsItem = new TreeItem(item, SWT.CASCADE);
				importsItem.setText(IMPORTS);
				Element importsEle = codeGenNode.getOwnerDocument()
						.createElement(IMPORTS);
				codeGenNode.appendChild(importsEle);
				
				TreeNodeData importsData = new TreeNodeData();
				importsData.setXMLNode(importsEle);
				importsData.setTreeNode(ATreeNodeUtil.getTreeNodeByPath(IMPORTSITEMPATH));
				importsItem.setData(importsData);
				
				for (ILibEntry libEntry : componentPref.getLibEntries()) {
					TreeItem importItem = new TreeItem(importsItem, SWT.CASCADE);
					importItem.setText(IMPORT);
					Element importEle = importsEle.getOwnerDocument()
							.createElement(IMPORT);
					importsEle.appendChild(importEle);
					importEle.setAttribute(NAME, libEntry.getNamePrefix());
					importEle.setAttribute(MODULE, libEntry.getNamePrefix());
					importEle.setAttribute(REQUIRED, "true");
					
					TreeNodeData nodeData = new TreeNodeData();
					nodeData.putAttrValue(NAME, libEntry.getNamePrefix());
					nodeData.putAttrValue(MODULE, libEntry.getNamePrefix());
					nodeData.putAttrValue(REQUIRED, "true");
					nodeData.setXMLNode(importEle);
					nodeData.setTreeNode(ATreeNodeUtil.getTreeNodeByPath(IMPORTITEMPATH));
					importItem.setData(nodeData);
				}

			}
		}
	}

	private void rebuildAttrComposite(TreeNodeData nodeData) {
		nodeAttrCompsite = new TreeNodeAttrCompsite(
				rightComposite, SWT.NONE, nodeData);
		nodeAttrCompsite.setLayoutData(new GridData(GridData.FILL_BOTH));
		nodeAttrCompsite.creatPart();
	}
	

}
