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
package org.talend.designer.core.ui.views.modules;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */

public class TestProcess {

    private StringBuffer out;

    private StringBuffer err;

    private Runtime runtime;

    private Process process = null;
    private InputStream input = null; 
    private long timeout = 0;

    public TestProcess(StringBuffer out, StringBuffer err) {
        runtime = Runtime.getRuntime();
        this.out = out;
        this.err = err;
    }

    public int execute(String[] args) throws IOException {
        int status = -1;

        /* Creation du sous-processus */
        process = runtime.exec(args, null, null);

        /* Consomme la sortie d'erreur */
        createProdConsThread(process.getErrorStream(), true, 1024).start();

        /* Consomme la sortie standard */
        createProdConsThread(process.getInputStream(), false, 1024).start();

        /*  */
        if (timeout > 0L) {
            Thread subProcess = createSubProcess(process);
            subProcess.start();

            try {
                subProcess.join(timeout);
                try {
                    status = process.exitValue();
                } catch (IllegalThreadStateException itse) {
                    process.destroy();
                    status = process.exitValue();
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        /* On attend que le sous-processus se termine */
        else if (timeout == 0L) {
            try {
                status = process.waitFor();
            } catch (InterruptedException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            }
        }

        return status;
    }

    /**
     * @return Returns the timeout.
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout The timeout to set.
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    private Thread createProdConsThread(final InputStream input, final boolean isError, final int bufferSize) {
        Thread thread = new Thread() {

            public void run() {
                try {
                    BufferedInputStream outStreamProcess = new BufferedInputStream(input);
                    byte[] buffer = new byte[bufferSize];
                    int read;

                    while ((read = outStreamProcess.read(buffer, 0, buffer.length)) != -1) {
                        if (isError) {
                            err.append(buffer);
                        } else {
                            out.append(new String(buffer));
                        }
                    }
                    outStreamProcess.close();
                } catch (IOException ioe) {
                    // TODO Auto-generated catch block
                    ioe.printStackTrace();
                }
            }
        };
        return thread;
    }

    private Thread createSubProcess(final Process process) {
        return new Thread() {

            public void run() {
                try {
                    process.waitFor();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        };
    }
}
