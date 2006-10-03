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

public class LaunchProcess {

    private StringBuffer out;

    private StringBuffer err;

    private Runtime runtime;

    private Process process = null;

    public LaunchProcess(StringBuffer out, StringBuffer err) {
        runtime = Runtime.getRuntime();
        this.out = out;
        this.err = err;
    }

    public int execute(String[] args) throws IOException {
        int status = -1;

        process = runtime.exec(args);

        createProdConsThread(process.getErrorStream(), true, 1024).start();

        createProdConsThread(process.getInputStream(), false, 1024).start();

        try {
            status = process.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        
        return status;
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
                             System.out.println(new String(buffer));
                            out.append(new String(buffer));
                        }
                    }
                    outStreamProcess.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        return thread;
    }
}
