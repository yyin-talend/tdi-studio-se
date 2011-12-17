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

import java.util.LinkedList;
import java.util.Map;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class DataCache {

    private LinkedList<Map<String, String>> cache = new LinkedList<Map<String, String>>();

    private boolean isReadEnd = false;

    private static DataCache instance = new DataCache();

    private long bufferSize = 10000;

    int startIndex = 0;

    private DataCache() {
    }

    public static DataCache getInstance() {
        if (instance == null)
            instance = new DataCache();
        return instance;
    }

    public void setBufferSize(long size) {
        this.bufferSize = size;
    }

    public boolean isReadEnd() {
        return isReadEnd;
    }

    public boolean hasData() {

        return !(this.isReadEnd && this.cache.size() <= 0);
    }

    public void setReadEnd(boolean isEnd) {
        synchronized (this.cache) {
            this.isReadEnd = isEnd;
            this.cache.notifyAll();
        }
    }

    public Map<String, String> readData() {
        Map<String, String> result = null;
        synchronized (this.cache) {
            if (!isReadEnd && this.cache.size() <= 0) {
                try {
                    this.cache.wait();
                } catch (InterruptedException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            if (cache.size() > 0)
                result = this.cache.remove();
            this.cache.notifyAll();
            // System.out.println(result.get("@swid") + "#" + result.get("@login_date") + "#");
            return result;
        }

        // if (cache.size() > 0) {
        // result = cache.remove();
        // if (this.isWriteWait) {
        // synchronized (this) {
        // this.notifyAll();
        // }
        // }
        // return result;
        // }
        //
        // while (!isReadEnd && cache.isEmpty()) {
        // synchronized (this) {
        // try {
        // this.isReadWait = true;
        // this.wait();
        // result = cache.remove();
        // this.isReadWait = false;
        // this.notifyAll();
        // return result;
        // } catch (Exception ex) {
        // System.err.println(ex.getMessage());
        // }
        // }
        // }
        // return result;
    }

    public void writeData(Map<String, String> map) {
        synchronized (this.cache) {
            if (this.cache.size() >= bufferSize) {
                try {
                    this.cache.wait();
                } catch (InterruptedException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            this.cache.add(map);
            this.cache.notifyAll();
        }
        // if (this.cache.size() < this.bufferSize) {
        // cache.add(map);
        // if (this.isReadWait) {
        // synchronized (this) {
        // this.notifyAll();
        // }
        // }
        // return;
        // }
        // while (this.cache.size() >= this.bufferSize) {
        // synchronized (this) {
        // try {
        // this.isReadWait = true;
        // this.wait();
        // } catch (Exception ex) {
        // System.err.println(ex.getMessage());
        // }
        // cache.add(map);
        //
        // isWriteWait = false;
        //
        // this.notifyAll();
        // }
        // }

    }
}
