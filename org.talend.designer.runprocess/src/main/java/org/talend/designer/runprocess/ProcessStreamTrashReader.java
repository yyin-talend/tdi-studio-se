// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.runprocess;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

/**
 * Read streams of a process. <br/>
 * 
 * $Id$
 * 
 */
public final class ProcessStreamTrashReader {

    /**
     * Constructs a new ProcessStreamTrashReader.
     */
    private ProcessStreamTrashReader() {
        super();
    }

    public static void readAndForget(final Process process) {
        try {
            new Thread() {

                public void run() {
                    InputStream is = process.getInputStream();
                    InputStreamReader din = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(din);
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("getInputStream " + line);
                        }
                    } catch (Exception ex) {
                        ex.getMessage();
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ex) {
                        }
                    }
                }
            }.start();

            // int len = is.available();
            // if (len > 0) {
            // byte[] data = new byte[len];
            // is.read(data);
            // }

            new Thread() {

                public void run() {
                    InputStream is = process.getErrorStream();
                    InputStreamReader din = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(din);
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("getErrorStream " + line);
                        }
                    } catch (Exception ex) {
                        ex.getMessage();
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ex) {
                        }
                    }
                }
            }.start();

            // len = is.available();
            // if (len > 0) {
            // byte[] data = new byte[len];
            // is.read(data);
            // }

            boolean stopped = false;
            while (!stopped) {
                try {
                    process.exitValue();
                    stopped = true;
                } catch (IllegalThreadStateException itse) {
                    // Do nothing
                }
            }
        } catch (Exception ioe) {//IO
            // Do nothing
        }
    }
}
