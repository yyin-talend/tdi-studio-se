// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.fileoutputxml.util;

import java.util.ArrayList;
import java.util.List;

import org.talend.designer.fileoutputxml.data.Attribute;
import org.talend.designer.fileoutputxml.data.Element;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;

/**
 * DOC ke class global comment. Detailled comment <br/>
 * 
 */
public class TreeUtil {

    /**
     * DOC ke Comment method "checkLoopNode".
     * 
     * @param node
     * @return
     */
    public static boolean checkTreeLoopNode(FOXTreeNode node) {
        if (node == null) {// ???
            return true;
        }

        if (node instanceof Attribute) {
            return false;
        }

        Element e = (Element) node;

        if (e.getParent() == null) {
            return false;
        }

        Element father = (Element) e.getParent();
        do {
            if (father.hasLink()) {
                return false;
            }

            for (FOXTreeNode att : father.getAttributeChildren()) {
                if (att.hasLink()) {
                    return false;
                }
            }

            if (father.getElementChildren().size() > 1) {
                return false;
            }

            father = (Element) father.getParent();
        } while (father != null);

        return true;

    }

    /**
     * DOC ke Comment method "guessAndSetLoopNode".
     * 
     * @param root
     */
    public static FOXTreeNode guessAndSetLoopNode(FOXTreeNode root) {
        FOXTreeNode loopNode = root;
        Element node = (Element) loopNode;
        if (node.getElementChildren().size() > 1) {
            node.setLoop(true);
            return node;
        }
        for (FOXTreeNode att : node.getAttributeChildren()) {
            if (att.getColumn() != null) {
                node.setLoop(true);
                return node;
            }
        }
        if (node.getElementChildren().size() == 0) {
            return null;
        }
        do {
            node = (Element) node.getElementChildren().get(0);
            if (node.getColumn() != null) {
                node.setLoop(true);
                return node;
            }
            if (node.getElementChildren().size() > 1) {
                node.setLoop(true);
                return node;
            }
            for (FOXTreeNode att : node.getAttributeChildren()) {
                if (att.getColumn() != null) {
                    node.setLoop(true);
                    return node;
                }
            }
            if (node.getElementChildren().size() == 0) {
                return null;
            }
        } while (true);
    }

    /**
     * DOC ke Comment method "setAsLoopNode".
     * @param node
     */
    public static void setAsLoopNode(FOXTreeNode node) {
        if (checkTreeLoopNode(node)) {
            node.setLoop(true);
        }
    }

    /**
     * DOC ke Comment method "clearLoopNode".
     * @param root
     */
    public static void clearLoopNode(FOXTreeNode root) {
        if (root instanceof Element) {
            Element e = (Element) root;
            if (e.isLoop()) {
                e.setLoop(false);
            }
            for (FOXTreeNode child : e.getElementChildren()) {
                clearLoopNode(child);
            }
        }
    }

    /**
     * DOC ke Comment method "getLoopNode".
     * @param root
     * @return
     */
    public static FOXTreeNode getLoopNode(FOXTreeNode root) {
        if (root instanceof Element) {
            Element e = (Element) root;
            do {
                if (e.isLoop()) {
                    return e;
                }
                if (e.getElementChildren().size() < 1) {
                    break;
                }
                e = (Element) e.getElementChildren().get(0);
            } while (true);
        }
        return null;
    }

    /**
     * DOC ke Comment method "refreshTree".
     * @param root
     * @return
     */
    public static boolean refreshTree(FOXTreeNode root) {
        List<FOXTreeNode> loopList = new ArrayList<FOXTreeNode>();
        getLoopList(root, loopList);
        if (loopList.size() == 0) {
            guessAndSetLoopNode(root);
            return true;
        }
        FOXTreeNode loop = loopList.get(0);
        if (!checkTreeLoopNode(loop)) {
            loop.setLoop(false);
            guessAndSetLoopNode(root);
            return true;
        }
        return false;
    }

    /**
     * DOC ke Comment method "getLoopList".
     * @param node
     * @param loopList
     */
    private static void getLoopList(FOXTreeNode node, List<FOXTreeNode> loopList) {
        if (node instanceof Element) {
            if (node.isLoop()) {
                loopList.add(node);
            }
            for (FOXTreeNode ch : ((Element) node).getElementChildren()) {
                getLoopList(ch, loopList);
            }
        }
    }

    // public static FOX
}
