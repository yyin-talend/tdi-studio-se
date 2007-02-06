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
package org.talend.repository.ui.swt;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.talend.repository.i18n.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author cantoine
 * 
 */
public class ExpressionXPath {
    
  public static void main(String[] args) {
    try {
      DocumentBuilderFactory fabriqueDOM = 
                                    DocumentBuilderFactory.newInstance();
      DocumentBuilder analyseur = fabriqueDOM.newDocumentBuilder();
      Document document = analyseur.parse(new File("C:\\test.xml")); //$NON-NLS-1$
      XPathFactory fabriqueXPath = XPathFactory.newInstance();
      XPath xpath = fabriqueXPath.newXPath();
      String expression = "//bills/bill/line[@id='38']"; //$NON-NLS-1$
      
//      line[@id='38']
      
      Node noeud = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);
      afficherInfos(noeud);
//      expression = "ancestor::*/logiciel[3]/commentaire";
//      noeud = (Node) xpath.evaluate(
//                                    expression, noeud, XPathConstants.NODE);
//      afficherInfos(noeud);

//      XMLParserLiaison xpathSupport = new XMLParserLiaisonDefault();
//      XPathProcessor xpathParser = new XPathProcessorImpl(xpathSupport);
//      PrefixResolver prefixResolver = new PrefixResolverDefault(source.getDocumentElement());
//
//      // create the XPath and initialize it
//      XPath xp = new XPath();
//      String xpString = "//address[child::addressee[text() = '"+name+"']]";
//      xpathParser.initXPath(xp, xpString, prefixResolver);
//
//      // now execute the XPath select statement
//      XObject list = xp.execute(xpathSupport, source.getDocumentElement(), prefixResolver);

      // return the resulting node
//      return list.nodeset().item(0);
    
    
    
    
    
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
  }
  public static void afficherInfos(Node noeud) {
    String nom = noeud.getNodeName();
    String valeur = noeud.getNodeValue();
    short type = noeud.getNodeType();
    if (type == Node.ELEMENT_NODE) {
      valeur = noeud.getTextContent();
    }
    if (type == Node.ATTRIBUTE_NODE) {
        valeur = noeud.getTextContent();
    }

    System.out.println(nom + " (" + type + ") = '" + valeur + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
  
}
