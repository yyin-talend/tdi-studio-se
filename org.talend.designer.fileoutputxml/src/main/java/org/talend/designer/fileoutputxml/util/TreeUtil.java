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
        if (node == null) {
            return false;
        }

        if (node instanceof Attribute) {
            return false;
        }

        Element e = (Element) node;

        if (e.getParent() == null) {
            return false;
        }

        if (e.isGroup()) {
            return true;
        }

        boolean isSubOfGroup = false;
        Element temp = e;
        while (temp != null) {
            if (temp.isGroup()) {
                isSubOfGroup = true;
                break;
            }
            temp = (Element) temp.getParent();
        }

        Element father = (Element) e.getParent();

        if (isSubOfGroup) {
            do {
                if (father.getElementChildren().size() > 1) {
                    return false;
                }
                father = (Element) father.getParent();
            } while (father != null && !father.isGroup());
        } else {

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
        }

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
     * 
     * @param node
     */
    public static void setAsLoopNode(FOXTreeNode node) {
        if (checkTreeLoopNode(node)) {
            node.setLoop(true);
        }
    }

    /**
     * DOC ke Comment method "clearLoopNode".
     * 
     * @param root
     */
    public static void clearLoopNode(FOXTreeNode root) {
        if (root instanceof Element) {
            Element e = (Element) root;
            if (e.isLoop()) {
                e.setLoop(false);
            } else {
                for (FOXTreeNode child : e.getElementChildren()) {
                    clearLoopNode(child);
                }
            }
        }
    }

    /**
     * DOC ke Comment method "getLoopNode".
     * 
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
     * DOC ke Comment method "getGroupNode".
     * 
     * @param root
     * @return
     */
    public static FOXTreeNode getGroupNode(FOXTreeNode root) {
        if (root instanceof Element) {
            Element e = (Element) root;
            do {
                if (e.isLoop()) {
                    return null;
                }
                if (e.isGroup()) {
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
     * 
     * @param root
     * @return
     */
    public static boolean refreshTree(FOXTreeNode root) {
        List<FOXTreeNode> groupList = new ArrayList<FOXTreeNode>();
        getGroupList(root, groupList);
        boolean noGroup = (groupList.size() == 0);
        if (noGroup) {
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
        } else {
            FOXTreeNode group = groupList.get(0);
            if (!checkTreeGroupNode(group)) {
                group.setGroup(false);
                clearLoopNode(root);
                guessAndSetLoopNode(root);
                return true;
            } else if (!checkFromGroupNode(group)) {
                List<FOXTreeNode> loopList = new ArrayList<FOXTreeNode>();
                getLoopList(group, loopList);
                if (loopList.size() > 0) {
                    loopList.get(0).setLoop(false);
                }
                guessLoopWithGroup(group);
                return true;
            }
        }
        return false;
    }

    private static void getGroupList(FOXTreeNode root, List<FOXTreeNode> groupList) {
        if (root instanceof Element) {
            if (root.isGroup()) {
                groupList.add(root);
            } else {
                for (FOXTreeNode ch : ((Element) root).getElementChildren()) {
                    getGroupList(ch, groupList);
                }
            }
        }
    }

    private static boolean checkFromGroupNode(final FOXTreeNode group) {
        Element e = (Element) group;
        while (true) {
            if (e.hasLink() && !e.isLoop()) {
                return false;
            }
            if (e.getElementChildren().size() == 0) {
                return true;
            }
            if (e.isLoop()) {
                return true;
            }
            if (e.getElementChildren().size() > 1) {
                return false;
            }
            e = (Element) e.getElementChildren().get(0);
        }
    }

    private static boolean checkTreeGroupNode(FOXTreeNode group) {
        if (((Element) group).getElementChildren().size() > 1) {
            return false;
        }
        Element parent = (Element) group.getParent();
        while (parent != null) {
            if (parent.getElementChildren().size() > 1) {
                return false;
            }
            for (FOXTreeNode att : parent.getAttributeChildren()) {
                if (att.hasLink()) {
                    return false;
                }
            }
            parent = (Element) parent.getParent();
        }
        return true;
    }

    /**
     * DOC ke Comment method "getLoopList".
     * 
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

    /**
     * DOC ke Comment method "betweenGroupAndLoop".
     * 
     * @param node
     * @return whether the currnt node is between the group node and loop node in the tree hierachy.
     */
    public static boolean isSubLoopNode(FOXTreeNode node) {
        if (node instanceof Attribute || node.isGroup() || node.isLoop()) {
            return false;
        }
        boolean result = false;
        FOXTreeNode temp = node;
        while (true) {
            temp = temp.getParent();
            if (temp == null) {
                break;
            }
            if (temp.isLoop()) {
                return false;
            }
        }
        temp = node;
        boolean flag = false;
        while (true) {
            temp = temp.getParent();
            if (temp == null) {
                break;
            }
            if (temp.isGroup()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            temp = node;
            while (true) {
                if (((Element) temp).getElementChildren().size() == 0) {
                    result = true;
                    break;
                }
                if (((Element) temp).getElementChildren().size() > 1) {
                    break;
                }
                temp = ((Element) temp).getElementChildren().get(0);
                if (temp.isLoop()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * DOC ke Comment method "clearSubGroupNode".
     * 
     * @param node
     */
    public static void clearSubGroupNode(FOXTreeNode node) {
        if (node instanceof Attribute) {
            return;
        }
        Element tNode = (Element) node;
        do {
            if (tNode.isGroup()) {
                tNode.setGroup(false);
                return;
            }
            if (tNode.isLoop() || tNode.getElementChildren().size() == 0 || tNode.getElementChildren().size() > 1) {
                return;
            }
            tNode = (Element) tNode.getElementChildren().get(0);
        } while (true);

    }

    /**
     * DOC ke Comment method "guessLoopWithGroup".
     * 
     * @param node
     * @return
     */
    public static boolean guessLoopWithGroup(FOXTreeNode node) {
        if (node instanceof Attribute) {
            return false;
        }
        boolean result = false;
        if (!node.isGroup()) {
            FOXTreeNode tNode = node;
            while (true) {
                if (tNode.isGroup()) {
                    node = tNode;
                    result = true;
                    break;
                }
                if (((Element) tNode).getElementChildren().size() == 0) {
                    break;
                }
                tNode = ((Element) tNode).getElementChildren().get(0);
            }
        } else {
            result = true;
        }
        if (!result) {
            return false;
        }
        Element e = (Element) node;
        do {
            if (e.getElementChildren().size() > 1 || e.hasLink()) {
                e.setLoop(true);
                return true;
            }
            if (e.getElementChildren().size() == 0) {
                return true;
            }
            e = (Element) e.getElementChildren().get(0);
        } while (true);
    }

    /**
     * DOC ke Comment method "checkTreeGoupNode".
     * 
     * @param node
     * @return
     */
    public static boolean checkTreeGoupNode(FOXTreeNode node) {
        if (node == null) {
            return false;
        }

        if (node instanceof Attribute) {
            return false;
        }

        Element e = (Element) node;

        if (e.getParent() == null) {
            return false;
        }

        if (e.getElementChildren().size() > 1) {
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
}
