// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.xml.sax.simpleparser;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.talend.xml.sax.commons.ISAXLooper;
import org.talend.xml.sax.simpleparser.model.XMLNode;
import org.talend.xml.sax.simpleparser.model.XMLNodes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class SimpleSAXLooper implements ISAXLooper,Callable {

    private XMLNodes nodes = new XMLNodes();

    private DataBufferCache bcache;

    private Thread task;
    
    private FutureTask futureTask;
    
    private boolean ignoreDTD = false;
    
    public SimpleSAXLooper(String loopPath, String[] nodePaths, boolean[] asXMLs) {
    	futureTask = new FutureTask(this);
    	task = new Thread(futureTask);
        for (int i = 0; i < nodePaths.length; i++) {
            nodes.addNode(new XMLNode(loopPath, nodePaths[i], null, asXMLs[i]));
        }
        initLoopEntry();
    }

    public SimpleSAXLooper(String rootPath, String[] arrLoopPath, String[][] arrNodePaths) {

    }
    
    /**
     * handle the exception in task.
     * FutureTask.get() is a block method waiting for the Task over and it can throw the exception in Task(Callable,Thread,Runnable)
     * @throws Exception
     */
    public void handleException() throws Exception {
		futureTask.get();
    }

    private void initLoopEntry() {

        bcache = DataBufferCache.getInstance();

        // parse the node path to loopEntry
        for (XMLNode node : nodes.getNodes().values()) {
            String column = node.originPath;
            String resultCol = node.loopPath;
            String[] splits = column.split("/");
            for (String tmp : splits) {
                if (tmp.equals(".")) {
                    node.isDot = true;
                } else {
                    resultCol += "/" + tmp;
                }
            }
            node.nodePath = resultCol;
        }
    }

    private void initLoopEntries() {

    }

    /**
     * Parse the XML file. Buffer the result in LoopEntry.
     * 
     * @param fileURL file URL
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private String fileURL = null;

    private String charset = "UTF-8";

    public void parse(String fileURL, String charset) {
        this.fileURL = fileURL;
        this.charset = charset;
        task.start();
    }

    private java.io.InputStream is;

    public void parse(java.io.InputStream is, String charset) {
        this.is = is;
        this.charset = charset;
        task.start();
    }
    
    public Object call() throws Exception {
		try {
            DefaultHandler hd = new SimpleSAXLoopHandler(nodes, bcache);
            SAXParser saxParser = null;
            if(!ignoreDTD) { //orginal code
            	saxParser = SAXParserFactory.newInstance().newSAXParser();
            } else {
	            SAXParserFactory spf = SAXParserFactory.newInstance();
	            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				saxParser = spf.newSAXParser();
            }
            if (fileURL != null) {
                org.xml.sax.InputSource inSource = new org.xml.sax.InputSource(
                        new java.io.FileInputStream(fileURL));
                inSource.setEncoding(this.charset);
                saxParser.parse(inSource, hd);
            } else {
                org.xml.sax.InputSource inSource = new org.xml.sax.InputSource(is);
                inSource.setEncoding(this.charset);
                saxParser.parse(is, hd);
            }
        } finally {
        	bcache.notifyErrorOccurred();
        }
		return null;
	}

    public static void main(String[] args) {
        try {

            Runtime.getRuntime().gc();
            long start = Runtime.getRuntime().freeMemory();
            long startall = Runtime.getRuntime().maxMemory();
            long timeStart = System.currentTimeMillis();

            String file = "C:/Documents and Settings/Administrator/桌面/in.xml";
            // String file = "D:/test/outMain.xml";
            String[] query = new String[] { "cust-vendor-num", "cust-vendor-num" + "/@xsi:nil", "cust", "cust" + "/@xsi:nil" };
            boolean[] asXMLs = new boolean[] { false, false, false, false };
            String loopPath = "/orderdata/order/header";

            SimpleSAXLooper looper = new SimpleSAXLooper(loopPath, query, asXMLs);
            looper.parse(file, "ISO-8859-15");
            Iterator<Map<String, String>> iter = looper.iterator();
            long num = 0;
            while (iter.hasNext()) {
                Map<String, String> map = iter.next();
                System.out.println(map.get("cust-vendor-num") + "\t" + map.get("cust"));
            }

            System.out.println("==Taltal==" + num);

            System.out.println("==Time=" + (System.currentTimeMillis() - timeStart));

            System.out.println("==Memory start all =" + startall);
            System.out.println("==Memory start free =" + start);
            System.out.println("==Memory end all =" + Runtime.getRuntime().maxMemory());
            System.out.println("==Memory end free =" + Runtime.getRuntime().freeMemory());
            System.out.println("==Memory=" + (start - Runtime.getRuntime().freeMemory()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.xml.sax.commons.ISAXLooper#iterator()
     */
    public Iterator<Map<String, String>> iterator() {
        // TODO Auto-generated method stub
        return new SimpleSAXIterator(this.bcache);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.xml.sax.commons.ISAXLooper#multiIterator()
     */
    public Iterator<Map<String, Map<String, String>>> multiIterator() {
        // TODO Auto-generated method stub
        return null;
    }

	public void setIgnoreDTD(boolean ignoreDTD) {
		
		this.ignoreDTD=ignoreDTD;
		
	}

}
