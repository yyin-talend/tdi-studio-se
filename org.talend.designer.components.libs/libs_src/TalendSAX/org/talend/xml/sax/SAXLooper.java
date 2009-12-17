// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.xml.sax;

import java.util.Iterator;
import java.util.Map;

import org.talend.xml.sax.commons.ISAXLooper;
import org.talend.xml.sax.simpleparser.SimpleSAXLooper;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class SAXLooper {

    // loop path in the tFileInputXML component.
    private String loopPath;

    // node paths special which tab will be read as the row value
    private String[] nodePaths;

    // add to support node.asXML()
    private boolean[] asXMLs;

    private String rootPath = null;

    // private String[] arrLoopPath = null;

    private String[][] arrNodePaths = null;

    private String[] arrOrigLoopPath = null;

    private boolean isSimpleParse = true;

    private ISAXLooper looper = null;

    public SAXLooper(String loopPath, String[] nodePaths) {
        this.loopPath = loopPath;
        this.nodePaths = nodePaths;
        this.asXMLs = new boolean[this.nodePaths.length];
        judgeIsSimple();
    }

    public SAXLooper(String loopPath, String[] nodePaths, boolean[] asXMLs) {
        this.loopPath = loopPath;
        this.nodePaths = nodePaths;
        this.asXMLs = asXMLs;
        judgeIsSimple();
    }

    public SAXLooper(String rootPath, String[] arrLoopPath, String[][] arrNodePaths) {
        this.rootPath = rootPath;

        // this.arrLoopPath = new String[arrLoopPath.length];

        this.arrOrigLoopPath = arrLoopPath;

        this.arrNodePaths = arrNodePaths;

        judegeMultiIsSimple();
    }

    private void judgeIsSimple() {
        if (this.loopPath.indexOf("..") >= 0 || this.loopPath.indexOf("*") >= 0) {
            this.isSimpleParse = false;
        }
        for (int i = 0; i < nodePaths.length; i++) {
            if (nodePaths[i].indexOf("..") >= 0 || nodePaths[i].indexOf("*") >= 0) {
                this.isSimpleParse = false;
                break;
            }
        }
        if (this.isSimpleParse) {
            looper = new SimpleSAXLooper(loopPath, nodePaths, asXMLs);
        } else {
            looper = new ComplexSAXLooper(loopPath, nodePaths, asXMLs);
        }
    }

    private void judegeMultiIsSimple() {
        if (this.rootPath.indexOf("..") >= 0 || this.rootPath.indexOf("*") >= 0) {
            this.isSimpleParse = false;
        }
        for (int i = 0; isSimpleParse && i < arrOrigLoopPath.length; i++) {
            if (arrOrigLoopPath[i].indexOf("..") >= 0 || arrOrigLoopPath[i].indexOf("*") >= 0) {
                this.isSimpleParse = false;
                break;
            }
        }
        for (int i = 0; isSimpleParse && i < arrNodePaths.length; i++) {
            for (int j = 0; j < arrNodePaths[i].length; j++) {
                if (arrNodePaths[i][j].indexOf("..") >= 0 || arrNodePaths[i][j].indexOf("*") >= 0) {
                    this.isSimpleParse = false;
                    break;
                }
            }
        }
        this.isSimpleParse = false;
        if (this.isSimpleParse) {
            looper = new SimpleSAXLooper(rootPath, arrOrigLoopPath, arrNodePaths);
        } else {
            looper = new ComplexSAXLooper(rootPath, arrOrigLoopPath, arrNodePaths);
        }
    }

    public void parse(String fileName) {
        looper.parse(fileName);
    }

    public void parse(java.io.InputStream is) {
        looper.parse(is);
    }

    public Iterator<Map<String, String>> iterator() {
        return looper.iterator();
    }

    public java.util.Iterator<java.util.Map<String, java.util.Map<String, String>>> multiIterator() {
        return looper.multiIterator();
    }

    public static void main(String args[]) {

        try {

            Runtime.getRuntime().gc();
            long start = Runtime.getRuntime().freeMemory();
            long startall = Runtime.getRuntime().maxMemory();
            long timeStart = System.currentTimeMillis();

            String file = "./libs_src/TalendSAX/org/talend/xml/sax/in_2.xml";
            // String file = "D:/test/outMain.xml";
            String[] query = new String[] { "." };
            boolean[] asXMLs = new boolean[] { false };
            String loopPath = "/root/*";

            SAXLooper looper = new SAXLooper(loopPath, query, asXMLs);
            looper.parse(file);
            Iterator<Map<String, String>> iter = looper.iterator();
            long num = 0;
            while (iter.hasNext()) {
                num++;
                Map<String, String> map = iter.next();
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
}
