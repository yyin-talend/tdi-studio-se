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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.proposal.xpath.XPathContentProposal;
import org.talend.commons.xml.NodeRetriever;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * ContentProposalProvider which proposes child nodes. <br/>
 * 
 * $Id: ExpressionProposalProvider.java 311 2006-11-03 07:00:19 +0000 (ven., 03 nov. 2006) amaumont $
 * 
 */
public class XPathProposalProvider implements IContentProposalProvider {

    public static final String EMPTY_STRING = "";

    public static final String SLASH = "/";

    private static final String PIPE = "|";

    private IContentProposalProvider[] otherContentProposalProviders;

    private XmlToXPathLinker linker;

    private boolean isRelativeTable;

    /**
     * Constructs a new ProcessProposalProvider.
     * 
     * @param linker
     * 
     * @param tables
     * @param control
     */
    public XPathProposalProvider(XmlToXPathLinker linker, boolean isRelative) {
        super();
        this.linker = linker;
        this.isRelativeTable = isRelative;
    }

    public void init() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposalProvider#getProposals(java.lang.String, int)
     */
    public IContentProposal[] getProposals(String contents, int position) {

        int nodeFieldMax = 500;
        int nodeLoopMax = 3000;
        int nodeLoopNumberLimit = 10;
        int nodeFieldNumberLimit = 10;
        int currentNodeNumber = 0;

        List<IContentProposal> proposals = new ArrayList<IContentProposal>();

        // isRelative = false;

        String beforeCursorExp = null;
        boolean isRelativeExpression = !contents.trim().startsWith(SLASH);
        if (isRelativeTable && isRelativeExpression) {
            // beforeCursorExp = linker.getCurrentLoopXPath() + SLASH + contents;
            beforeCursorExp = contents;
        } else {
            beforeCursorExp = contents.substring(0, position);
        }
        int lastIndexSlash = beforeCursorExp.lastIndexOf(SLASH);
        int lastIndexPipe = beforeCursorExp.lastIndexOf(PIPE);

        String currentExpr = null;
        if (isRelativeTable && isRelativeExpression) {
            currentExpr = beforeCursorExp;
        }
        if (lastIndexSlash == -1 || lastIndexSlash < lastIndexPipe && lastIndexPipe != -1) {
            currentExpr = EMPTY_STRING;
        } else if (lastIndexPipe < lastIndexSlash && lastIndexPipe != -1) {
            currentExpr = beforeCursorExp.substring(lastIndexPipe + 1, lastIndexSlash + 1);
        } else if (lastIndexSlash != -1) {
            currentExpr = beforeCursorExp.substring(0, lastIndexSlash + 1);
            // currentExpr = beforeCursorExp;
        } else {
            currentExpr = beforeCursorExp;
        }

        currentExpr = currentExpr.trim();

        String currentWord = extractLastWord(beforeCursorExp);

        // String xPathExpression =
        //            
        // // + " | " +
        // createXPathExpression(beforeCursorExp)
        // ;
        // System.out.println("#############################");
        // System.out.println("currentExpr='" + currentExpr + "'");
        // System.out.println("beforeCursorExp='"+beforeCursorExp+"'");
        // System.out.println("currentWord='"+currentWord+"'");
        // System.out.println("1");
        List<Node> list = new ArrayList<Node>(0);

        boolean estimationError = false;
        // NodeList nodesList = null;
        // try {
        // nodesList = this.linker.getNodeRetriever().retrieveNodeList(currentExpr);
        // } catch (XPathExpressionException e) {
        // ExceptionHandler.process(e);
        // estimationError = true;
        // }

        // if (true || linker.getAllLoopNodes().size() > 500 || !estimationError && nodesList.getLength() > 500) {

        ArrayList<Node> allLoopNodes = linker.getAllLoopNodes();

        // if (isRelative && expressionIsRelative && allLoopNodes.size() < nodeNumberLimit) {
        //
        // // System.out.println("nodesList.getLength():" + nodesList.getLength());
        // System.out.println("nodesList.getLength():" + linker.getAllLoopNodes().size());
        // XPathContentProposal contentProposal = new XPathContentProposal("<< Too many nodes ! >>");
        // proposals.add(contentProposal);
        //
        // } else
        boolean resultsMayBeIncomplete = false;
        boolean breakAll = false;

        Set<String> alreadyAdded = new HashSet<String>();

        // ///////////////////////////////////////////////////////////////////////////////////////////////
        // ///////////////////////////////////////////////////////////////////////////////////////////////
        // XPath requests for relative XPath
        //
        if (isRelativeTable) {
            int allNodesLoopSize = allLoopNodes.size();

            // System.out.println("nodeLoop size list : " + allNodesLoopSize);

            currentNodeNumber += allNodesLoopSize;

            if (allNodesLoopSize > nodeLoopMax) {

                resultsMayBeIncomplete = true;

            } else {

                int nodeLoopNumberOfLoop = allNodesLoopSize;
                if (allNodesLoopSize > nodeLoopNumberLimit) {
                    nodeLoopNumberOfLoop = nodeLoopNumberLimit;
                    resultsMayBeIncomplete = true;
                }

                for (int i = 0; i < nodeLoopNumberOfLoop; i++) {
                    Node nodeLoop = allLoopNodes.get(i);

                    // System.out.println("nodeLoop : " + i);

                    NodeList nodeList = null;

                    try {

                        // list =
                        // this.linker.getNodeRetriever().retrieveListOfNodes(createXPathExpression(currentExpr));
                        nodeList = this.linker.getNodeRetriever().retrieveNodeListFromNode(
                                modifyXpathToSearchAllChildren(currentExpr, true), nodeLoop);
                    } catch (XPathExpressionException e) {
                        ExceptionHandler.process(e);
                    }
                    if (nodeList != null && nodeList.getLength() == 0) {
                        try {
                            nodeList = linker.getNodeRetriever().retrieveNodeListFromNode(
                                    modifyXpathToSearchAllChildren(beforeCursorExp, true), nodeLoop);
                        } catch (XPathExpressionException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                    // System.out.println("nodeList.size()="+nodeList.size());

                    if (nodeList != null) {

                        int allNodesFieldsLoopSize = nodeList.getLength();
                        currentNodeNumber += allNodesFieldsLoopSize;
                        if (allNodesFieldsLoopSize > nodeFieldMax) {

                            resultsMayBeIncomplete = true;
                            breakAll = true;

                        } else {

                            int nodeFieldNumberOfLoop = allNodesFieldsLoopSize;
                            if (allNodesFieldsLoopSize > nodeFieldMax) {
                                nodeFieldNumberOfLoop = nodeFieldNumberLimit;
                                resultsMayBeIncomplete = true;
                            }

                            for (int j = 0; j < nodeFieldNumberOfLoop; ++j) {
                                // System.out.println("nodeField : " + j);
                                Node node = nodeList.item(j);
                                String nodeName = node.getNodeName();
                                String absoluteXPathFromNode = NodeRetriever.getAbsoluteXPathFromNode(node);
                                if ((currentWord.length() > 0 && nodeName.startsWith(currentWord) || currentWord.length() == 0 || currentWord
                                        .equals("/"))
                                        && !alreadyAdded.contains(absoluteXPathFromNode)) {
                                    // System.out.println(absoluteXPathFromNode);
                                    XPathContentProposal contentProposal = new XPathContentProposal(node);
                                    if (isRelativeTable && isRelativeExpression) {
                                        contentProposal.setRelative(isRelativeTable);
                                        contentProposal.setFirstRelativeNode(contents.indexOf(SLASH) == -1);
                                    }
                                    proposals.add(contentProposal);
                                    alreadyAdded.add(absoluteXPathFromNode);
                                }

                            } // for (int j = 0; j < nodeListLength; ++j) {

                        }
                        if (breakAll) {
                            break;
                        }

                    } // if (nodeList != null) {

                } // for (int i = 0; i < lstSize; i++) {

            } // } else {

        } // if (!estimationError) {
        // ///////////////////////////////////////////////////////////////////////////////////////////////
        // ///////////////////////////////////////////////////////////////////////////////////////////////

        // ///////////////////////////////////////////////////////////////////////////////////////////////
        // ///////////////////////////////////////////////////////////////////////////////////////////////
        // XPath requests for absolute XPath
        //
        boolean expressionIsEmpty = currentExpr.trim().length() == 0;

        NodeList nodeList = null;

        if (!expressionIsEmpty) {
            try {

                nodeList = this.linker.getNodeRetriever().retrieveNodeList(modifyXpathToSetFirstAscendant(currentExpr));
            } catch (XPathExpressionException e) {
                ExceptionHandler.process(e);
            }
        }

        if (nodeList != null || expressionIsEmpty) {
            if (!expressionIsEmpty && nodeList.getLength() > nodeLoopMax) {

                resultsMayBeIncomplete = true;

            } else {
                try {
                    nodeList = this.linker.getNodeRetriever().retrieveNodeList(modifyXpathToSearchAllChildren(currentExpr, false));
                } catch (XPathExpressionException e) {
                    ExceptionHandler.process(e);
                }
            }

            if (nodeList != null) {

                for (int j = 0; j < nodeList.getLength(); ++j) {
                    // System.out.println("nodeField : " + j);
                    Node node = nodeList.item(j);
                    String nodeName = node.getNodeName();
                    String absoluteXPathFromNode = NodeRetriever.getAbsoluteXPathFromNode(node);
                    if ((currentWord.length() > 0 && nodeName.startsWith(currentWord) || currentWord.length() == 0 || currentWord
                            .equals("/"))
                            && !alreadyAdded.contains(absoluteXPathFromNode)) {
                        // System.out.println(absoluteXPathFromNode);
                        XPathContentProposal contentProposal = new XPathContentProposal(node);
                        proposals.add(contentProposal);
                        alreadyAdded.add(absoluteXPathFromNode);
                    }

                } // for (int j = 0; j < nodeListLength; ++j) {

            } // if (nodeList != null) {

        }
        // ///////////////////////////////////////////////////////////////////////////////////////////////
        // ///////////////////////////////////////////////////////////////////////////////////////////////

        if (resultsMayBeIncomplete) {
            addTooManyNodesContentProposal(proposals);
        }

        IContentProposal[] res = new IContentProposal[proposals.size()];

        res = proposals.toArray(res);
        return res;
    }

    /**
     * DOC amaumont Comment method "modifyXpathToSetFirstAscendant".
     * 
     * @param currentExpr
     * @return
     */
    private String modifyXpathToSetFirstAscendant(String currentExpr) {

        if (currentExpr.trim().length() > 0) {

            do {

                currentExpr = currentExpr.trim().substring(0, currentExpr.length() - 1);

            } while (currentExpr.trim().endsWith("/"));
        }

        return currentExpr;
    }

    /**
     * DOC amaumont Comment method "addTooManyNodesContentProposal".
     * 
     * @param proposals
     */
    private void addTooManyNodesContentProposal(List<IContentProposal> proposals) {
        XPathContentProposal contentProposal = new XPathContentProposal("<< Too many nodes, proposed values may be incomplete ... >>");
        proposals.add(contentProposal);
    }

    /**
     * DOC amaumont Comment method "extractLastWord".
     * 
     * @param currentExpr
     * @return
     */
    private String extractLastWord(String currentExpr) {
        int size = currentExpr.length();
        for (int i = size - 1; i > 0; i--) {
            if (!("" + currentExpr.charAt(i)).matches("\\w")) {
                return currentExpr.substring(i + 1, currentExpr.length());
            }
        }
        return currentExpr.substring(0, size);
    }

    /**
     * DOC amaumont Comment method "createXPathExpression".
     * 
     * @param currentExpr
     * @param isRelative TODO
     * @param slash
     * 
     * @return
     */
    private String modifyXpathToSearchAllChildren(String currentExpr, boolean isRelative) {
        String xPathExpression;
        String slash = SLASH;

        if (currentExpr.trim().equals("") && isRelative) {
            currentExpr = ".";
        }

        if (currentExpr.endsWith(SLASH)) {
            slash = EMPTY_STRING;
        }

        xPathExpression = currentExpr + slash + "*" + " | " + currentExpr + slash + "@*";
        // System.out.println("xPathExpression='"+xPathExpression+"'");
        return xPathExpression;
    }

    /**
     * Getter for otherContentProposalProviders.
     * 
     * @return the otherContentProposalProviders
     */
    public IContentProposalProvider[] getOtherContentProposalProviders() {
        return this.otherContentProposalProviders;
    }

    /**
     * Sets the otherContentProposalProviders.
     * 
     * @param otherContentProposalProviders the otherContentProposalProviders to set
     */
    public void setOtherContentProposalProviders(IContentProposalProvider[] otherContentProposalProviders) {
        this.otherContentProposalProviders = otherContentProposalProviders;
    }

}
