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
package org.talend.designer.components.thash.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

/**
 * 
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 */
class DB {
   
    static BufferedOutputStream bw = null;
    
    static long position = 0;
    
    static RandomAccessFile ra = null;
    
    public static Object get(String container, long id) throws IOException, ClassNotFoundException{
        ra.seek(id);
        byte[] byteArray = new byte[10];
        ra.read(byteArray);
        ObjectInput oi = new ObjectInputStream(new ByteArrayInputStream(byteArray));
        int length = oi.readInt();
        
        byteArray = new byte[length];
        ra.read(byteArray);
        oi = new ObjectInputStream(new ByteArrayInputStream(byteArray));
        return oi.readObject();
    }

    public static long put(String container, Object bean) throws IOException {
        
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(bean);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }            
            }
        }
        
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeInt(byteArray.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }            
            }
        }
        
        byte[] byteArray2 = byteArrayOutputStream.toByteArray();
        
        byteArrayOutputStream.close();
        
        bw.write(byteArray2);
        
        bw.write(byteArray);
        
        long returnPosition = position;
        
        position+=(10+byteArray.length);
        
        return returnPosition;
    }
    
    public static void initPut(String container) throws FileNotFoundException {
        File file = new File(container);
        position = 0;
        bw = new BufferedOutputStream(new FileOutputStream(file));
    }
    
    public static void endPut() throws IOException { 
        bw.flush();
        bw.close();
    }
    
    public static void initGet(String container) throws FileNotFoundException {
        ra = new RandomAccessFile(container, "r");
    }
    
    
    public static void endGet(String container) throws IOException { 
        if (ra != null) { 
            ra.close();
        }
        File file = new File(container);
        file.delete();
    }
    
}
