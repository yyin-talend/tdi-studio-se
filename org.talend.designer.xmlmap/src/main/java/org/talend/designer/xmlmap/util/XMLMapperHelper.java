// ============================================================================
package org.talend.designer.xmlmap.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class XMLMapperHelper {

    /**
     * 
     * DOC rdubois Comment method "isGeneratedAsVirtualComponent".
     * 
     * @param mapperNode
     * @return
     */
    public static boolean isGeneratedAsVirtualComponent(final INode xmlMapperNode) {
    	
    	boolean isGeneratedAsVirtualComponent = false;
    	
    	isGeneratedAsVirtualComponent = hasAggregateColumn(xmlMapperNode);
    	
        return isGeneratedAsVirtualComponent;
        
    }
    
    private static boolean hasAggregateColumn(final INode xmlMapperNode) {
    	
    	boolean hasAggregateColumn = false;
    	
    	List<? extends IConnection> outConnections = (List<? extends IConnection>)xmlMapperNode.getOutgoingConnections();
    	
    	XmlMapData xmlMapData=(XmlMapData)ElementParameterParser.getObjectValueXMLTree(xmlMapperNode);
    	
    	if(xmlMapData!=null && outConnections!=null && outConnections.size() > 0) {
    		List<OutputXmlTree> outputTables = xmlMapData.getOutputTrees();
	
	        HashMap<String, IConnection> hNameToConnection = new HashMap<String, IConnection>();
	        for (IConnection connection : outConnections) {
	             hNameToConnection.put(connection.getName(), connection);
	        }
	        
	        for(OutputXmlTree outputTable : outputTables) {
	        	String tableName = outputTable.getName();
	        	IConnection connection = hNameToConnection.get(tableName);
	        	if (connection == null) {
	        		continue;
	            }
	        	
	        	List<TreeNode> allLeaf = new ArrayList<TreeNode>();
	     
	        	for(TreeNode node  : outputTable.getNodes()) {
	        		getAllLeaf(node,allLeaf);
	        	}
	        	
	        	for(TreeNode leaf : allLeaf) {
	        		if(((OutputTreeNode)leaf).isAggregate()) {
	        			hasAggregateColumn = true;
	        			break;
	        		}
	        	} 
	        	
	        }
    	}
    	return hasAggregateColumn;
    }
    
    private static void getAllLeaf(TreeNode node,List<TreeNode> allLeaf) {
		EList<TreeNode> children = node.getChildren();
		if(children==null || children.size() == 0) {
			allLeaf.add(node);
		} else {
			for(TreeNode child : children) {
				if(child!=null) {
					getAllLeaf(child,allLeaf);
				}
			}
		}
	} 
    
    
}
