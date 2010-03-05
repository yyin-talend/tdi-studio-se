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
package org.talend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class FileCopy {

    private final static long L_SIZE = 100 * 1024 * 1024; // 100M

    private final static long M_SIZE = 10 * 1024 * 1024; // 10M

    private final static long S_SIZE = 0; // 0M

    public static void copyFile(String srcFileName, String desFileName, boolean delSrc) throws Exception {

        FileInputStream srcInputStream = new FileInputStream(srcFileName);
        long lastModified = new File(srcFileName).lastModified();
        int available = srcInputStream.available();
        if (available > L_SIZE) {// X > 100M
            copyFileL(srcFileName, srcInputStream, desFileName, delSrc);
        } else if (available > M_SIZE) {// 10M < X <100M
            copyFileM(srcFileName, srcInputStream, desFileName, delSrc);
        } else { // X < 10M
            copyFileS(srcFileName, srcInputStream, desFileName, delSrc);
        }
        // keep modification_time
        new File(desFileName).setLastModified(lastModified);
    }

    private static void copyFileS(String srcFileName, FileInputStream srcInputStream, String desFileName, boolean delSrc)
            throws IOException {
        File source = new File(srcFileName);
        File dest = new File(desFileName);

        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = srcInputStream;
            out = new FileOutputStream(dest);

            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
            if (delSrc) {
                source.delete();
            }

        } finally {

            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    private static void copyFileM(String srcFileName, FileInputStream srcInputStream, String desFileName, boolean delSrc)
            throws IOException {
        File source = new File(srcFileName);
        File dest = new File(desFileName);

        FileChannel in = null;
        FileChannel out = null;
        try {
            in = srcInputStream.getChannel();
            out = new FileOutputStream(dest).getChannel();

            in.transferTo(0, in.size(), out);

            in.close();
            out.close();

            if (delSrc) {
                source.delete();
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    private static void copyFileL(String srcFileName, FileInputStream srcInputStream, String desFileName, boolean delSrc)
            throws Exception {
        File source = new File(srcFileName);
        File dest = new File(desFileName);

        FileChannel in = null, out = null;
        try {
            in = srcInputStream.getChannel();
            out = new FileOutputStream(dest).getChannel();

            long size = in.size();
            long position = 0;
            final long MAP_SIZE = 102400000;
            MappedByteBuffer buf = null;
            while (true) {
                if (position + MAP_SIZE >= size) {
                    buf = in.map(FileChannel.MapMode.READ_ONLY, position, size - position);
                    out.write(buf);
                    if (delSrc) {
                        // here must clean first, or it can't delete
                        clean(buf);
                    }
                    break;
                } else {
                    buf = in.map(FileChannel.MapMode.READ_ONLY, position, MAP_SIZE);
                    out.write(buf);
                    if (delSrc) {
                        // here must clean first, or it can't delete
                        clean(buf);
                    }
                    position += MAP_SIZE;
                }
            }

            in.close();
            out.close();

            if (delSrc) {
                source.delete();
            }

        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void clean(final Object buffer) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
