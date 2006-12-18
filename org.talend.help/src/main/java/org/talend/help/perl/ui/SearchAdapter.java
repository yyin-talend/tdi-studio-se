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
package org.talend.help.perl.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.help.perl.model.EProperty;
import org.talend.help.perl.model.EType;
import org.talend.help.perl.model.Node;
import org.talend.help.perl.reader.DocParser;

/**
 * DOC admin class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (Fri, 29 Sep 2006) nrousseau $
 * 
 */
public class SearchAdapter extends SelectionAdapter {

    private TreeViewer treeViewer = null;

    private Button searchBtn = null;

    private Text searchText = null;

    private boolean matchTextFlag = false;

    private static final String LABEL_NEXT = "next";

    private String currentPageContent = "";

    public SearchAdapter(TreeViewer viewer, Button btn, Text text) {
        this.treeViewer = viewer;
        this.searchBtn = btn;
        this.searchText = text;
    }

    public void widgetSelected(SelectionEvent e) {
        Node currentNode = null;
        if (LABEL_NEXT.equals(searchBtn.getText())) {
            TreeSelection selection = (TreeSelection) treeViewer.getSelection();
            currentNode = (Node) selection.getFirstElement();
            if (!isEndOfSearch(currentNode)) {
                currentNode = search(nextSiblingNode, searchText.getText());
            } else {
                // TODO add a tip to the user when searching end
                System.out.println("search end");
            }
        } else {
            currentNode = (Node) treeViewer.getInput();
            currentNode = search(currentNode, searchText.getText());
            if (currentNode != null) {
                searchBtn.setText(LABEL_NEXT);
            }
        }
        treeViewer.setExpandedElements(getNodeArray(currentNode));
        treeViewer.refresh();
        treeViewer.setSelection(new StructuredSelection(new Object[] { currentNode }), true);

    }

    /**
     * search the result which can match with searchString from the starNode.
     * 
     * @param startNode the start node on the tree
     * @param searchString the string which need to match
     * @return the match node on the tree
     */
    private Node search(Node startNode, String searchString) {
        boolean searchFlag = true;
        if (searcherCache.size() == 0) {
            preSetSearchCache(startNode, searchString);
            setMatchTextFlag(firstMatchNode == null ? false : true);
            return firstMatchNode;
        }
        Node node = null;
        while (searchFlag && startNode != null) {
            node = getFunctionNode(startNode);
            if (node.getType() == EType.FUNCTION) {
                // firstSelPageContent = searcherCache.get(node);
                if (searcherCache.containsKey(node)) {
                    setMatchTextFlag(true);
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
        // boolean searchFlag = true;
        // Node node = startNode;
        // String htmlFileContent = null;
        // while (searchFlag && node != null) {
        // node = getFunctionNode(node);
        // if (node.getType() == EType.FUNCTION) {
        // Node anchorNode = node.getChildren().get(0);
        // String anchor = anchorNode.getProperties().get(EProperty.VALUE);
        // try {
        // htmlFileContent = DocParser.getInstance().getDoc(anchor);
        // if (htmlFileContent.contains(searchString)) {
        // currentHtmlContent = getHighLightStr(htmlFileContent,
        // searchString);
        // /*
        // * if the currentHtmlContent.equals(htmlFileContent)is
        // * true, the htmlFileContent is unchanged,because the
        // * searchString is key of html tag
        // */
        // if (!currentHtmlContent.equals(htmlFileContent)) {
        // setMatchTextFlag(true);
        // return anchorNode.getParent();
        // }
        // }
        //
        // } catch (IOException e) {
        // openError(e);
        // }
        // }
        // if (!isEndOfSearch(node)) {
        // node = nextSiblingNode;
        // } else {
        // searchFlag = false;
        // }
        // }
        // setMatchTextFlag(false);
        // return null;
    }

    private Map<Node, String> searcherCache = new HashMap<Node, String>();

    private Node firstMatchNode = null;

    private void preSetSearchCache(Node startNode, String searchStr) {
        boolean searchFlag = true;
        firstMatchNode = null;
        Node node = startNode;
        String htmlFileContent = null;
        // Node tempNode = null;
        while (searchFlag && node != null) {
            node = getFunctionNode(node);
            if (node.getType() == EType.FUNCTION) {
                Node anchorNode = node.getChildren().get(0);
                String anchor = anchorNode.getProperties().get(EProperty.VALUE);
                try {
                    htmlFileContent = DocParser.getInstance().getDoc(anchor);
                    if (htmlFileContent.contains(searchStr)) {
                        currentPageContent = getHighLightStr(htmlFileContent, searchStr);
                        /*
                         * if the currentHtmlContent.equals(htmlFileContent)is true, the htmlFileContent is
                         * unchanged,because the searchString is key of html tag
                         */
                        if (!currentPageContent.equals(htmlFileContent)) {
                            // setMatchTextFlag(true);
                            node.setSearchMatchFlag(true);
                            firstMatchNode = firstMatchNode == null ? node : firstMatchNode;
                            searcherCache.put(node, currentPageContent);
                        }

                    }

                } catch (IOException e) {
                    openError(e);
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

    public String getHtmlByKey(Object node) {
        return searcherCache.get(node);
    }

    public void clearSearchCache() {
        Set<Node> nodeList = searcherCache.keySet();
        for (Node node : nodeList) {
            node.setSearchMatchFlag(false);
        }
        searcherCache.clear();
    }

    private Node nextSiblingNode = null;

    /**
     * judge the search wether achive the end.
     * 
     * @param node the node on the tree
     * @return the search achive the end,return true;else,return false
     */
    private boolean isEndOfSearch(Node node) {
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

    private static void openError(Exception e1) {
        Display workbenchDisplay = PlatformUI.getWorkbench().getDisplay();
        ErrorDialog.openError(workbenchDisplay.getActiveShell(), "Error occured", e1.getMessage(), null);
    }

    private static final String HEAD_REGEX = ">(([^>^<])*)(";

    private static final String TAIL_REGEX = ")+(([^>^<])*)<";

    private static final String HEAD_REPLACE = ">$1<span style=\"background:#ff9000\">";

    private static final String TAIL_REPLACE = "</span>$4<";

    /**
     * replace to the match htmlFileContent with the form of <span style=\"background:#ff9000\">htmlFileContent</span>
     * via the regex. For example: String str = "<a name="item_index">index STR,SUBSTR,POSITION</a>" String str2 =
     * getHighLightStr(str,index); and the str2 result is: "<a name="item_index"><span
     * style="background:#ff9000">index</span> STR,SUBSTR,POSITION</a>" *
     * 
     * @param htmlFileContent
     * @param searchStr
     * @return the string with highLine tag
     */
    private String getHighLightStr(String htmlFileContent, String searchStr) {
        String regexStr = HEAD_REGEX + searchStr + TAIL_REGEX;
        String replaceStr = HEAD_REPLACE + searchStr + TAIL_REPLACE;
        String resultStr = htmlFileContent.replaceAll(regexStr, replaceStr);
        return resultStr;
    }

    /**
     * according the given node to get the function type node,if the parent node hasn't children which is function tye
     * and the return value is null.
     * 
     * @param typeNode the given node
     * @return function type node
     */
    private Node getFunctionNode(Node typeNode) {
        Node childrenNode = null;
        if (typeNode.getType() != EType.FUNCTION) {
            if (typeNode.hasChildren()) {
                childrenNode = typeNode.getChildren().get(0);
                return getFunctionNode(childrenNode);
            }
        }
        return typeNode;
    }

    /*
     * private TreePath computeTreePath(Node node) { Node parent = node; LinkedList<Node> elements = new LinkedList<Node>();
     * while (parent != null) { elements.addFirst(parent); parent = parent.getParent(); } return new
     * TreePath(elements.toArray()); }
     */

    // public String getCurrentPageContent() {
    // return currentPageContent;
    // }
    private Object[] getNodeArray(Node node) {
        Node parent = node;
        LinkedList<Node> elements = new LinkedList<Node>();
        while (parent != null) {
            elements.addFirst(parent);
            parent = parent.getParent();
        }
        return elements.toArray();
    }

    /**
     * Sets the matchTextFlag.
     * 
     * @param matchTextFlag the matchTextFlag to set
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
