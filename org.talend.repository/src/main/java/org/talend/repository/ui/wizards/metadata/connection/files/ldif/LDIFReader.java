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
package org.talend.repository.ui.wizards.metadata.connection.files.ldif;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.talend.repository.i18n.Messages;

/**
 * @author cantoine.
 * 
 */
public class LDIFReader {

    private String lastDN = null;

    private BufferedReader reader = null;

    public LDIFReader(BufferedReader r) {
        reader = r;
    }

    /**
     * add.
     * 
     * @param entry
     * @param attribute
     * @param value
     * @param type
     * @throws IOException
     */
    private void add(Attributes entry, String attribute, String value, int type) throws IOException {
        Attribute vals = entry.get(attribute);
        if (vals == null) {
            vals = new BasicAttribute(attribute);
        } else if (type == 1) {
            vals.add(value);
        } else if (type == 3) {
            URL url = null;
            try {
                url = new URL(value);
            } catch (MalformedURLException ex) {
                throw new IOException(String.valueOf(ex) + Messages.getString("LDIFReader.ioException.cannotConstrauctURL") + value); //$NON-NLS-1$
            }
            if (!url.getProtocol().equalsIgnoreCase("file")) { //$NON-NLS-1$
                throw new IOException(Messages.getString("LDIFReader.ioException.protocolNotSupported") + url.getProtocol()); //$NON-NLS-1$
            }
            File file = new File(url.getFile());
            byte[] b = new byte[(int) file.length()];
            FileInputStream fi = new FileInputStream(file);
            fi.read(b);
            vals.add(b);
        } else {
            throw new IOException(Messages.getString("LDIFReader.ioException.unknow")); //$NON-NLS-1$
        }
        entry.put(vals);
    }

    /**
     * @author cantoine
     * @return Dn
     * 
     */
    public String getDN() {
        return lastDN;
    }

    /**
     * @author cantoine
     * @param dn
     * @param type
     * @throws IOException
     * @return Dn
     */
    private String getDN(StringBuffer dn, int type) throws IOException {
        if (dn == null) {
            return null;
        }
        String rdn = dn.toString();
        // if(type == 2)
        // rdn = new String(Base64.decode(rdn.getBytes()), "UTF8");
        return rdn;
    }

    /**
     * @author cantoine
     * 
     */
    public Attributes getNext() throws IOException {
        int type = 1;
        boolean encoded = false;
        Attributes entry = null;
        int linenr = 0;
        entry = null;
        StringBuffer dn;
        StringBuffer value = dn = null;
        String line;
        String attribute;
        String sDN;
        lastDN = line = attribute = sDN = null;
        while ((line = reader.readLine()) != null) {
            int len = line.length();
            linenr++;
            if (len <= 0 || line.charAt(0) != '#') {
                if (len > 0 && line.charAt(0) == ' ') {
                    if (value == null) {
                        lastDN = null;
                    }
                    value.append(line.substring(1));
                } else if (len == 0) {
                    if (attribute != null && value != null) {
                        if (entry == null) {
                            entry = new BasicAttributes(true);
                        }
                        add(entry, attribute, value.toString(), type);
                        attribute = null;
                        value = null;
                    }
                    if (dn != null && lastDN == null) {
                        lastDN = getDN(dn, type);
                    }
                    if (entry != null) {
                        return entry;
                    }
                } else {
                    int pos = line.indexOf(":"); //$NON-NLS-1$
                    if (pos == -1) {
                        lastDN = null;
                    }
                    if (dn != null && lastDN == null) {
                        lastDN = getDN(dn, type);
                    }
                    if (attribute != null && value != null) {
                        if (entry == null) {
                            entry = new BasicAttributes(true);
                        }
                        add(entry, attribute, value.toString(), type);
                    }
                    int to = pos;
                    int from = pos + 1;
                    if (line.charAt(from) == ':') {
                        type = 2;
                        from++;
                    } else if (line.charAt(from) == '<') {
                        type = 3;
                        from++;
                    } else {
                        type = 1;
                    }
                    if (line.charAt(from) == ' ') {
                        from++;
                    }
                    attribute = line.substring(0, to);
                    value = new StringBuffer(line.substring(from));
                    if (dn == null) {
                        dn = value;
                        attribute = null;
                    }
                }
            }
        }
        return entry;
    }
    
}

