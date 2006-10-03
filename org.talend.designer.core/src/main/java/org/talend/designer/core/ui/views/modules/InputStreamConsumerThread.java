// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class InputStreamConsumerThread extends Thread {

    private InputStream _in;

    private OutputStream _out;

    public InputStreamConsumerThread(ThreadGroup group, String name, InputStream in, OutputStream out) {
        super(group, name);
        setDaemon(true);
        if (in == null) {
            throw new IllegalArgumentException("InputStream argument cannot be null");
        }
        _in = in;
        _out = out;
    }

    public InputStreamConsumerThread(String name, InputStream in, OutputStream out) {
        this(null, name, in, out);
    }

    public void run() {
        byte[] buf = new byte[512];
        int count = 0;
        try {
            while ((count = _in.read(buf)) != -1) {
                if (_out != null) {
                    _out.write(buf, 0, count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
