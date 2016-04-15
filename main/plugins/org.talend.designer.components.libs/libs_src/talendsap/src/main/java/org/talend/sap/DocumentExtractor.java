package org.talend.sap;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;

public class DocumentExtractor {
	
	Document doc = null;
	String function = null;
	StringBuilder sb = new StringBuilder();
	
	public DocumentExtractor(Document doc,String function) {
		this.doc = doc;
		this.function = function;
	}
	
	public String getSingleResult(String name) {
		XPath xpath = org.dom4j.DocumentHelper.createXPath(
			sb.append("/").append(function).append("/OUTPUT/").append(name)
			.append("|")
			.append("/").append(function).append("/CHANGING/").append(name)
			.toString()
		);
		sb.setLength(0);
		Node node = xpath.selectSingleNode(doc);
		if(node == null) {
			return null;
		}
		return node.getText();
	}
	
	public List<String> getStructureResult(String structureName,List<String> names) {
		XPath xpath = org.dom4j.DocumentHelper.createXPath(
			sb.append("/").append(function).append("/OUTPUT/").append(structureName)
			.append("|")
			.append("/").append(function).append("/CHANGING/").append(structureName)
			.toString()
		);
		sb.setLength(0);
		Node node = xpath.selectSingleNode(doc);
		
		if(node == null) {
			return null;
		}
		
		List<String> result = new ArrayList<String>();
		
		for(String name : names) {
			Node subNode = node.selectSingleNode(name);
			if(subNode == null) {
				result.add(null);
			} else {
				result.add(subNode.getText());
			}
		}
		
		return result;
	}
	
	public List<List<String>> getTableResult(String tableName,List<String> names) {
		List<List<String>> result = new ArrayList<List<String>>();
		
		Element functionElement = doc.getRootElement();
		if (functionElement == null) {
			return result;
		}

		Element tablesOrChangingElement = functionElement.element("TABLES");
		if (tablesOrChangingElement == null) {
			tablesOrChangingElement = functionElement.element("CHANGING");
		}
		
		if(tablesOrChangingElement == null) {
			return result;
		}

		Element tableElement = tablesOrChangingElement.element(tableName);

		if (tableElement == null) {
			return result;
		}
		
		List<Element> elements = tableElement.elements("item");
		
		for(Element element : elements) {
			List<String> row = new ArrayList<String>();
			for(String name : names) {
				Element subElement = element.element(name);
				if(subElement == null) {
					row.add(null);
				} else {
					row.add(subElement.getText());
				}
			}
			result.add(row);
		}
		
		return result;
	}
}
