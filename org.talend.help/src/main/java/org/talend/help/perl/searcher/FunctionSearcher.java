// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.help.perl.searcher;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.talend.help.perl.model.EProperty;
import org.talend.help.perl.model.EType;
import org.talend.help.perl.model.Node;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ÐÇÆÚÎå, 29 ¾ÅÔÂ 2006)
 * nrousseau $
 * 
 */
public class FunctionSearcher extends Searcher {

	public FunctionSearcher(TreeViewer viewer, Button btn, Text text) {
		super(viewer, btn, text);
	}

	/* (non-Javadoc)
	 * @see org.talend.help.perl.searcher.Searcher#processSearch()
	 */
	public void processSearch() {
		currentNode = null;
		if (LABEL_NEXT.equals(searchBtn.getText())) {
			TreeSelection selection = (TreeSelection) treeViewer.getSelection();
			currentNode = (Node) selection.getFirstElement();
			if (!isEndOfSearch(currentNode)) {
				currentNode = startSearch(nextSiblingNode, searchText.getText());
			} else {
				// TODO add a tip to the user when searching end
			}
		} else {
			if ("".equals(searchText.getText())) {
				return;
			}
			currentNode = (Node) treeViewer.getInput();
			BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

				public void run() {
					currentNode = startSearch(currentNode, searchText.getText());
				}
			});
			if (currentNode != null) {
				searchBtn.setText(LABEL_NEXT);
			}
		}
		treeViewer.setExpandedElements(getNodeArray(currentNode));
		treeViewer.refresh();
		treeViewer.setSelection(new StructuredSelection(
				new Object[] { currentNode }), true);
	}

	/**
	 * search the result which can match with searchString from the starNode.
	 * 
	 * @param startNode
	 *            the start node on the tree
	 * @param searchString
	 *            the string which need to match
	 * @return the match node on the tree
	 */
	private Node startSearch(Node startNode, String searchStr) {

		boolean searchFlag = true;
		if (searcherCache.size() == 0) {
			preSetSearchCache(startNode, searchStr);
			return firstMatchNode;
		}
		Node node = null;
		while (searchFlag && startNode != null) {
			node = getFunctionNode(startNode);
			if (node.getType() == EType.FUNCTION) {
				if (searcherCache.containsKey(node)) {
					return node;
				}
			}
			if (!isEndOfSearch(node)) {
				startNode = nextSiblingNode;
			} else {
				searchFlag = false;
			}
		}
		return null;

	}

	/* (non-Javadoc)
	 * @see org.talend.help.perl.searcher.Searcher#preSetSearchCache(org.talend.help.perl.model.Node, java.lang.String)
	 */
	protected void preSetSearchCache(Node startNode, String searchStr) {
		boolean searchFlag = true;
		firstMatchNode = null;
		Node node = startNode;
		while (searchFlag && node != null) {
			node = getFunctionNode(node);
			if (node.getType() == EType.FUNCTION) {
				String anchorLabel = node.getProperties().get(EProperty.LABEL);
				if (anchorLabel.startsWith(searchStr.toLowerCase())) {
					node.setSearchMatchFlag(true);
					firstMatchNode = firstMatchNode == null ? node
							: firstMatchNode;
					searcherCache.put(node, currentPageContent);

				}
			}
			if (!isEndOfSearch(node)) {
				node = nextSiblingNode;
			} else {
				searchFlag = false;
			}
		}
		setMatchTextFlag(false);
	}

	public String getHtmlByKey(Node node) {
		return searcherCache.get(node);
	}

}
