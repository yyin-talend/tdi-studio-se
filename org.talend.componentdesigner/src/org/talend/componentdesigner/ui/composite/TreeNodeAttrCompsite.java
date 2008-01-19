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
package org.talend.componentdesigner.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.ui.composite.xmltree.TreeNodeData;
import org.w3c.dom.Element;

/**
 * DOC rli class global comment. Detailled comment
 */
public class TreeNodeAttrCompsite extends Composite {

	private static final String MARK = ":";

	private final TreeNodeData nodeData;

	public TreeNodeAttrCompsite(Composite parent, int style,
			TreeNodeData nodeData) {
		super(parent, style);
		this.nodeData = nodeData;
		GridLayout layout = new GridLayout(2, false);
		setLayout(layout);
	}

	public void creatPart() {
		GridData gd = null;
		if ((!nodeData.isHasTreeAttr()) && (!nodeData.isHasChildTreeNode())) {
			Label label = new Label(this, SWT.NONE);
			label.setText(nodeData.getXMLNode().getNodeName() + MARK);
			gd = new GridData();
			gd.horizontalSpan = 2;
			label.setLayoutData(gd);
			final Text nodeText = new Text(this, SWT.BORDER | SWT.MULTI
					| SWT.WRAP);
			gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			nodeText.setLayoutData(gd);
			nodeText.setText(nodeData.getBodayText());
			
			nodeText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					nodeData.getXMLNode().setTextContent(nodeText.getText());
					nodeData.setBodayText(nodeText.getText());
				}
			});
		} else {
			for (Object attrName : nodeData.getTreeAttrNames()) {
				final Label attrLabel = new Label(this, SWT.NONE);
				attrLabel.setText(attrName + MARK);
				attrLabel.setLayoutData(new GridData());
				final Text attrText = new Text(this, SWT.BORDER);
				gd = new GridData(GridData.FILL_HORIZONTAL);
				attrText.setLayoutData(gd);
				if (nodeData.getAttrValue((String) attrName) != null) {
					attrText.setText(nodeData.getAttrValue((String) attrName));
				}
				
				attrText.addModifyListener(new ModifyListener() {

					public void modifyText(ModifyEvent e) {
						String attrName = attrLabel.getText().split(MARK)[0];
						if (attrText.getText().equals(
								PluginConstant.EMPTY_STRING)) {
							((Element) nodeData.getXMLNode())
									.removeAttribute(attrName);
							nodeData.removeAttrValue(attrName);
						} else {
							((Element) nodeData.getXMLNode()).setAttribute(
									attrName, attrText.getText());
							nodeData.putAttrValue(attrName, attrText.getText());
						}
					}

				});
			}
		}
	}
}
