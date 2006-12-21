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

import java.io.IOException;

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
import org.talend.help.perl.reader.DocParser;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ÐÇÆÚÎå, 29 ¾ÅÔÂ 2006) nrousseau $
 * 
 */
public class PlainSearcher extends Searcher {

    /**
     * DOC Administrator PlainSearcher constructor comment.
     */
    public PlainSearcher(TreeViewer viewer, Button btn, Text text) {
        super(viewer, btn, text);
    }

    /*
     * (non-Javadoc)
     * 
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
                // currentNode = repeatToStart();
                // currentNode = startSearch(nextSiblingNode, searchText.getText());
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
        treeViewer.setSelection(new StructuredSelection(new Object[] { currentNode }), true);
    }

    /**
     * search the result which can match with searchString from the starNode.
     * 
     * @param startNode the start node on the tree
     * @param searchString the string which need to match
     * @return the match node on the tree
     */
    private Node startSearch(Node startNode, String searchStr) {

        boolean searchFlag = true;
        if (searcherCache.size() == 0) {
            preSetSearchCache(startNode, searchStr);
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
        if (searcherCache.size() != 0) {
            Node repeatNode =  repeatToStart();
            return startSearch(repeatNode, searchStr);
        }
        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.help.perl.searcher.Searcher#preSetSearchCache(org.talend.help.perl.model.Node, java.lang.String)
     */
    protected void preSetSearchCache(Node startNode, String searchStr) {
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

    private static final String HEAD_REGEX = ">(([^>^<])*)(";

    private static final String TAIL_REGEX = ")+(([^>^<])*)<";

    private static final String HEAD_REPLACE = ">$1<span style=\"background:#ff9000\">";

    private static final String TAIL_REPLACE = "</span>$4<";

    protected String getHighLightStr(String htmlFileContent, String searchStr) {
        String regexStr = HEAD_REGEX + searchStr + TAIL_REGEX;
        String replaceStr = HEAD_REPLACE + searchStr + TAIL_REPLACE;
        String resultStr = htmlFileContent.replaceAll(regexStr, replaceStr);
        return resultStr;
    }

}
