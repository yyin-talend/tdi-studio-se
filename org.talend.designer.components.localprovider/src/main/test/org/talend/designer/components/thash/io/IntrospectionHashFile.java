// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend â€?www.talend.com
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
package org.talend.designer.components.thash.io;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.WrapDynaBean;
import org.talend.designer.components.thash.Sizeof;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 * 
 * 
 * 
  74829 milliseconds for 5000000 objects to STORE using IntrospectionHashFile. 66000 items/s. 10 bytes per item in storage. 
  94422 milliseconds for 5000000 objects to STORE using SimpleHashFile. 52000 items/s. 125 bytes per item in storage.
  163735 milliseconds for 5000000 objects to STORE using DoubleHashFile. 30000 items/s.
 * 
 * 
 * 
 */
class IntrospectionHashFile {

    private static IntrospectionHashFile instance;

    private IntrospectionHashFile() {
    }

    /**
     * getInstance.
     * 
     * @return the instance if this project handler
     */
    public static synchronized IntrospectionHashFile getInstance() {
        if (instance == null) {
            instance = new IntrospectionHashFile();
        }
        return instance;
    }

    RandomAccessFile bw = null;

    boolean readonly;

    RandomAccessFile ra = null;

    Object lastRetrievedObject;

    long lastRetrievedCursorPosition = -1;

    String[] names = null;

    int[] types = null;

    public Object get(String container, long cursorPosition) throws IOException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    public long put(String container, Object bean) throws IOException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        long returnPosition = bw.getFilePointer();
        if (!readonly) {
            for (int i = 0; i < names.length; i++) {
                write(org.apache.commons.beanutils.PropertyUtils.getProperty(bean, names[i]), types[i]);
            }
        }
        return returnPosition;
    }

    public void initPut(String container) throws IOException {
        if (!readonly) {
            File file = new File(container);
            file.delete();
            bw = new RandomAccessFile(container, "rw");

        }
    }

    public void endPut() throws IOException {
        if (!readonly) {
            bw.close();
        }
    }

    public void initGet(String container) throws FileNotFoundException {
        ra = new RandomAccessFile(container, "r");
    }

    public void endGet(String container) throws IOException {
        if (!readonly) {
            if (ra != null) {
                ra.close();
            }
            File file = new File(container);
            file.delete();
        }
    }

    public void init(Class beanClass) {
        PropertyDescriptor[] propertyDescriptors = org.apache.commons.beanutils.PropertyUtils
                .getPropertyDescriptors(beanClass);
        names = new String[propertyDescriptors.length - 1];
        types = new int[propertyDescriptors.length - 1];
        for (int i = 0, j = 0; i < propertyDescriptors.length; i++) {
            if (propertyDescriptors[i].getName().equals("class")) {
                continue;
            }
            names[j] = propertyDescriptors[i].getName();
            String type = propertyDescriptors[i].getPropertyType().getSimpleName();
            if (type.equals("boolean")) {
                types[j] = 0;
            } else if (type.equals("byte")) {
                types[j] = 1;
            } else if (type.equals("short")) {
                types[j] = 2;
            } else if (type.equals("int")) {
                types[j] = 3;
            } else if (type.equals("long")) {
                types[j] = 4;
            } else if (type.equals("float")) {
                types[j] = 5;
            } else if (type.equals("double")) {
                types[j] = 6;
            } else if (type.equals("char")) {
                types[j] = 7;
            } else if (type.equals("String")) {
                types[j] = 8;
            } else if (type.equals("byte[]")) {
                types[j] = 9;
            } else if (type.equals("Date")) {
                types[j] = 10;
            } else {
                types[j] = 11;
            }
            j++;
        }
    }

    public void write(Object value) throws IOException {
        if (value == null) {
            bw.writeInt((int) 0);
            return;
        }

        if (value instanceof Boolean) {
            bw.writeBoolean((Boolean) value);
            return;
        }
        if (value instanceof Byte) {
            bw.writeByte((Byte) value);
            return;
        }
        if (value instanceof Short) {
            bw.writeShort((Short) value);
            return;
        }
        if (value instanceof Integer) {
            bw.writeInt((Integer) value);
            return;
        }
        if (value instanceof Long) {
            bw.writeLong((Long) value);
            return;
        }
        if (value instanceof Float) {
            bw.writeFloat((Float) value);
            return;
        }
        if (value instanceof Double) {
            bw.writeDouble((Double) value);
            return;
        }
        if (value instanceof Character) {
            bw.writeChar((Character) value);
            return;
        }
        if (value instanceof String) {
            bw.write(((String) value).getBytes());
            return;
        }
        if (value instanceof byte[]) {
            bw.write((byte[]) value);
            return;
        }
        if (value instanceof Date) {
            bw.writeLong(((Date) value).getTime());
            return;
        }

        bw.write(value.toString().getBytes());
    }

    public void write(Object value, int type) throws IOException {
        if (value == null) {
            bw.writeInt((int) 0);
            return;
        }

        switch (type) {
        case 0:
            bw.writeBoolean((Boolean) value);
            return;
        case 1:
            bw.writeByte((Byte) value);
            return;
        case 2:
            bw.writeShort((Short) value);
            return;
        case 3:
            bw.writeInt((Integer) value);
            return;
        case 4:
            bw.writeLong((Long) value);
            return;
        case 5:
            bw.writeFloat((Float) value);
            return;
        case 6:
            bw.writeDouble((Double) value);
            return;
        case 7:
            bw.writeChar((Character) value);
            return;
        case 8:
            bw.write(((String) value).getBytes());
            return;
        case 10:
            bw.write((byte[]) value);
            return;
        case 11:
            bw.writeLong(((Date) value).getTime());
            return;
        default:
            bw.write(value.toString().getBytes());
        }
    }

    public static void main(String[] args) throws IOException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        int loop = 5000000;

        IntrospectionHashFile ihf = IntrospectionHashFile.getInstance();
        long start = System.currentTimeMillis();

        ihf.init(Bean.class);
        ihf.initPut("D:/cache");
        for(int i = 0; i < loop; i++){
            Bean bean = new Bean(i, String.valueOf(i));
            ihf.put("", bean);
        }
        ihf.endPut();

        long end = System.currentTimeMillis();
        long deltaTime = (end - start);
        System.out.print(deltaTime + " milliseconds for " + loop + " objects to STORE using IntrospectionHashFile. "
                + (loop / deltaTime * 1000) + " items/s. ");
        File file = new File("D:/cache");
        System.out.println((file.length() / loop) + " bytes per item in storage.");

        SimpleHashFile shf = SimpleHashFile.getInstance();
        start = System.currentTimeMillis();

        shf.initPut("D:/cache2");
        for(int i = 0; i < loop; i++){
            Bean bean = new Bean(i, String.valueOf(i));
            shf.put("", bean);
        }
        ihf.endPut();

        end = System.currentTimeMillis();
        deltaTime = (end - start);
        System.out.print(deltaTime + " milliseconds for " + loop + " objects to STORE using SimpleHashFile. "
                + (loop / deltaTime * 1000) + " items/s. ");
        file = new File("D:/cache2");
        System.out.println((file.length() / loop) + " bytes per item in storage.");

        DoubleHashFile dhf = DoubleHashFile.getInstance();
        start = System.currentTimeMillis();

        dhf.initPut("D:/cache3");
        for (int i = 0; i < loop; i++) {
            Bean bean = new Bean(i, String.valueOf(i));
            dhf.put("", bean);
        }
        dhf.endPut();

        end = System.currentTimeMillis();
        deltaTime = (end - start);
        System.out.print(deltaTime + " milliseconds for " + loop + " objects to STORE using DoubleHashFile. "
                + (loop / deltaTime * 1000) + " items/s. ");
    }

}
