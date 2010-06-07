// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.talend.xml.sax.commons.ISAXLooper;
import org.talend.xml.sax.simpleparser.model.XMLNode;
import org.talend.xml.sax.simpleparser.model.XMLNodes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class SimpleSAXLooper extends Thread implements ISAXLooper {

    private XMLNodes nodes = new XMLNodes();

    private DataBufferCache bcache;

    public SimpleSAXLooper(String loopPath, String[] nodePaths, boolean[] asXMLs) {

        for (int i = 0; i < nodePaths.length; i++) {
            nodes.addNode(new XMLNode(loopPath, nodePaths[i], null, asXMLs[i]));
        }
        initLoopEntry();
    }

    public SimpleSAXLooper(String rootPath, String[] arrLoopPath, String[][] arrNodePaths) {

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
        start();
    }

    private java.io.InputStream is;

    public void parse(java.io.InputStream is, String charset) {
        this.is = is;
        this.charset = charset;
        start();
    }

    public void run() {
        try {

            DefaultHandler hd = new SimpleSAXLoopHandler(nodes, bcache);
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();

            if (fileURL != null) {
                org.xml.sax.InputSource inSource = new org.xml.sax.InputSource(new java.io.InputStreamReader(
                        new java.io.FileInputStream(fileURL), this.charset));
                saxParser.parse(inSource, hd);
            } else {
                org.xml.sax.InputSource inSource = new org.xml.sax.InputSource(is);
                inSource.setEncoding(this.charset);
                saxParser.parse(is, hd);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {

            Runtime.getRuntime().gc();
            long start = Runtime.getRuntime().freeMemory();
            long startall = Runtime.getRuntime().maxMemory();
            long timeStart = System.currentTimeMillis();

            String file = "./libs_src/TalendSAX/org/talend/xml/sax/dol_login.xml";
            // String file = "D:/test/outMain.xml";
            String[] query = new String[] { "@swid", "@login_date", "@login_type_name", "@affiliate_id" };
            boolean[] asXMLs = new boolean[] { false, false, false, false };
            String loopPath = "/Table/Row";

            SimpleSAXLooper looper = new SimpleSAXLooper(loopPath, query, asXMLs);
            looper.parse(file, "UTF-8");
            DataBufferCache cache = DataBufferCache.getInstance();
            long num = 0;
            while (cache.hasData()) {
                num++;
                Map<String, String> map = cache.readData();
                for (int i = 0; i < query.length; i++) {
                    System.out.print(map.get(query[i]) + "#");
                }
                System.out.println();
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

}
