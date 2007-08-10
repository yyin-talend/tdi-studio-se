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
package org.talend.expressionbuilder.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.RuntimeExceptionHandler;

/**
 * Sockte server that listening the notification from the expression jobs..
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionTestServer.java 下午04:02:29 2007-6-26 +0000 (2007-6-26) yzhang $
 * 
 */
public class ExpressionTestServer implements Runnable {

    private static ExpressionTestServer testServer;

    private ServerSocket server;

    private Socket client;

    private volatile boolean stop;

    private Text resultDisplay;

    private Display display;

    /**
     * Set default constructor to be private accessed.
     * 
     * yzhang ExpressionTestServer constructor comment.
     */
    private ExpressionTestServer(Display display, Text resultDisplay) {
        this.display = display;
        this.resultDisplay = resultDisplay;

        try {
            server = new ServerSocket(6666);
        } catch (IOException e) {
            RuntimeExceptionHandler.process(e);
        }
    }

    /**
     * Return the single server.
     * 
     * yzhang Comment method "getInstance".
     * 
     * @param display
     * @param resultDisplay
     * @return
     */
    public static ExpressionTestServer getInstance(Display display, Text resultDisplay) {
        if (testServer == null) {
            testServer = new ExpressionTestServer(display, resultDisplay);
        }
        return testServer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        do {
            try {
                client = server.accept();
                if (!stop) {
                    new ExpressionTestProcess().start();
                }
            } catch (IOException e) {
                RuntimeExceptionHandler.process(e);
            }
        } while (!stop);

    }

    /**
     * Stop current server thread.
     * 
     * yzhang Comment method "stop".
     */
    public void stop() {
        stop = true;
        try {
            new Socket("localhost", 6666); //$NON-NLS-1$
        } catch (UnknownHostException e) {
            RuntimeExceptionHandler.process(e);
        } catch (IOException e) {
            RuntimeExceptionHandler.process(e);
        }

        try {
            server.close();
        } catch (IOException e) {
            RuntimeExceptionHandler.process(e);
        }
        server = null;
        testServer = null;
    }

    /**
     * To see whether thread stopped or not.
     * 
     * yzhang Comment method "stopped".
     * 
     * @return
     */
    public boolean stopped() {
        return stop;
    }

    /**
     * yzhang ExpressionTestServer class global comment. Detailled comment <br/>
     * 
     * $Id: ExpressionTestServer.java 上午10:07:44 2007-7-24 +0000 (2007-7-24) yzhang $
     * 
     */
    class ExpressionTestProcess extends Thread {

        private volatile boolean processStop;

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            if (client != null) {
                try {
                    InputStream in = client.getInputStream();
                    LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
                    while (!processStop) {
                        final String data = reader.readLine();
                        if (data == null) {
                            processStop = true;
                            reader.close();
                            in.close();
                            reader = null;
                            in = null;
                        } else {
                            display.syncExec(new Runnable() {

                                public void run() {
                                    resultDisplay.setText(data);
                                }

                            });

                        }
                    }
                } catch (final IOException e) {
                    display.syncExec(new Runnable() {

                        public void run() {
                            StringBuffer buffer = new StringBuffer("Error: " + e.getMessage() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                            buffer.append(resultDisplay.getText());
                            resultDisplay.setText(buffer.toString());

                        }

                    });

                }
            }
        }
    }
}
