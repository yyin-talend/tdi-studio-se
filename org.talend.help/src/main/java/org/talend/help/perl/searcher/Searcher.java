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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.help.perl.model.EType;
import org.talend.help.perl.model.Node;

/**
 * DOC . Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ÐÇÆÚÎå, 29 ¾ÅÔÂ 2006)
 * nrousseau $
 * 
 */
public abstract class Searcher {

	protected TreeViewer treeViewer;

	protected Button searchBtn;

	protected Text searchText;

	protected static Node currentNode = null;

	protected static final String LABEL_NEXT = "next";

	protected boolean matchTextFlag = false;

	protected String currentPageContent = "";

	protected boolean funcSearchFlag = true;

	protected Map<Node, String> searcherCache = new HashMap<Node, String>();

	protected Node firstMatchNode = null;

	protected Node nextSiblingNode = null;

	public Searcher(TreeViewer viewer, Button btn, Text text) {
		this.treeViewer = viewer;
		this.searchBtn = btn;
		this.searchText = text;
	}

	/**
	 * all the process of searching.
	 */
	public abstract void processSearch();

	
	/**get the search result,put the search result on the cache.
	 * @param startNode  the start node
	 * @param searchStr  the string which need to search
	 */
	protected abstract void preSetSearchCache(Node startNode, String searchStr);

	protected Object[] getNodeArray(Node node) {
		Node parent = node;
		LinkedList<Node> elements = new LinkedList<Node>();
		while (parent != null) {
			elements.addFirst(parent);
			parent = parent.getParent();
		}
		return elements.toArray();
	}

	/**
	 * according the given node to get the function type node,if the parent node
	 * hasn't children which is function tye and the return value is null.
	 * 
	 * @param typeNode
	 *            the given node
	 * @return function type node
	 */
	protected Node getFunctionNode(Node typeNode) {
		Node childrenNode = null;
		if (typeNode.getType() != EType.FUNCTION) {
			if (typeNode.hasChildren()) {
				childrenNode = typeNode.getChildren().get(0);
				return getFunctionNode(childrenNode);
			}
		}
		return typeNode;
	}

	/**
	 * judge the search wether achive the end.
	 * 
	 * @param node
	 *            the node on the tree
	 * @return the search achive the end,return true;else,return false
	 */
	protected boolean isEndOfSearch(Node node) {
		if (node == null) {
			return true;
		}
		Node tempNode = node.getNextSibling();
		if (node == tempNode) {
			node = node.getParent();
			return node == null ? true : isEndOfSearch(node);
		} else {
			nextSiblingNode = tempNode;
			return false;
		}

	}

	public String getHtmlByKey(Node node) {
		return searcherCache.get(node);
	}

	/**
	 * replace to the match htmlFileContent with the form of <span
	 * style=\"background:#ff9000\">htmlFileContent</span> via the regex. For
	 * example: String str = "<a name="item_index">index STR,SUBSTR,POSITION</a>"
	 * String str2 = getHighLightStr(str,index); and the str2 result is: "<a
	 * name="item_index"><span style="background:#ff9000">index</span>
	 * STR,SUBSTR,POSITION</a>" *
	 * 
	 * @param htmlFileContent
	 * @param searchStr
	 * @return the string with highLine tag
	 */
	protected String getHighLightStr(String htmlFileContent, String searchStr) {
		return "";
	}

	public void clearSearchCache() {
		Set<Node> nodeList = searcherCache.keySet();
		for (Node node : nodeList) {
			node.setSearchMatchFlag(false);
		}
		searcherCache.clear();
	}

	public static void openError(Exception e1) {
		Display workbenchDisplay = PlatformUI.getWorkbench().getDisplay();
		ErrorDialog.openError(workbenchDisplay.getActiveShell(),
				"Error occured", e1.getMessage(), null);
	}

	/**
	 * Sets the matchTextFlag.
	 * 
	 * @param matchTextFlag
	 *            the matchTextFlag to set
	 */
	public void setMatchTextFlag(boolean flag) {
		matchTextFlag = flag;
	}

	/**
	 * Getter for matchTextFlag.
	 * 
	 * @return the matchTextFlag
	 */
	public boolean getMatchTextFlag() {
		return matchTextFlag;
	}
}
