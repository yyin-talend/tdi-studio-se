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
package org.talend.help.perl.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.talend.help.Activator;

/**
 * DocParser.java.
 * 
 */
public class DocParser {
    
    private static DocParser docParse = new DocParser();

    private static final String PATH = "guide/perl/perlfunc.html"; //$NON-NLS-1$

    public static final String FILE = "/home/wiu/work/talend/workspace-talend-svn/org.talend.help/" + PATH; //$NON-NLS-1$

    public static DocParser getInstance() {
        return docParse;
    }
    
    public String getDoc(String anchor) throws IOException {
        InputStream stream = FileLocator.openStream(Activator.getDefault().getBundle(), new Path(PATH), false);
        try {
            return getDoc(stream, anchor);
        } finally {
            stream.close();
        }
    }

    public String getDoc(InputStream in, String anchor) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String li = "</li>"; //$NON-NLS-1$
        StringBuffer buf = null;
        String anchorString = "<dt><strong><a name=\"" + anchor + "\">"; //$NON-NLS-1$ //$NON-NLS-2$
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.indexOf(anchorString) != -1) {
                buf = new StringBuffer();
            }
            if (buf != null) {
                int index = line.indexOf(li);
                if (index == -1) {
                    buf.append(line).append("\n"); //$NON-NLS-1$
                } else {
                    buf.append(line.substring(0, index));
                    break;
                }
            }
        }
        return buf == null ? "" : buf.toString(); //$NON-NLS-1$
    }

}
