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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
class DB {

    static BufferedOutputStream bw = null;

    static long position = 0;

    static RandomAccessFile ra = null;

    static FileChannel finalChannel = null;

    static final boolean IO = false;

    static final boolean NIO = true;

    private static boolean mode = IO;

    public static Object get(String container, long id) throws IOException, ClassNotFoundException {
        if (mode == NIO) {
            finalChannel.position(id);
            ByteBuffer bb = ByteBuffer.allocate(10);
            finalChannel.read(bb);
            ObjectInput oi = new ObjectInputStream(new ByteArrayInputStream(bb.array()));
            int length = oi.readInt();

            bb = ByteBuffer.allocate(length);
            finalChannel.read(bb);
            oi = new ObjectInputStream(new ByteArrayInputStream(bb.array()));
            return oi.readObject();
        } else {

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
    }

    public static long put(String container, Object bean) throws IOException {
        if (mode == NIO) {
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

            long returnPosition = finalChannel.position();

            finalChannel.write(ByteBuffer.wrap(byteArray2));

            finalChannel.write(ByteBuffer.wrap(byteArray));

            return returnPosition;
        } else {

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

            position += (10 + byteArray.length);

            return returnPosition;

        }
    }

    public static void initPut(String container) throws FileNotFoundException {
        if (mode == NIO) {
            File file = new File(container);
            finalChannel = new FileOutputStream(file).getChannel();
        } else {
            File file = new File(container);
            position = 0;
            bw = new BufferedOutputStream(new FileOutputStream(file));
        }
    }

    public static void endPut() throws IOException {
        if (mode == NIO) {
            finalChannel.close();
        } else {
            bw.flush();
            bw.close();
        }
    }

    public static void initGet(String container) throws FileNotFoundException {
        if (mode == NIO) {
            File file = new File(container);
            finalChannel = new FileInputStream(file).getChannel();
        } else {
            ra = new RandomAccessFile(container, "r");
        }
    }

    public static void endGet(String container) throws IOException {
        if (mode == NIO) {
            if (finalChannel != null) {
                finalChannel.close();
            }
        } else {
            if (ra != null) {
                ra.close();
            }
        }
        File file = new File(container);
        file.delete();
        if(mode == IO){
            System.out.println("Using io");
        }else{
            System.out.println("Using nio");
        }
    }

    public static void setMode(boolean myMode) {
        mode = myMode;
    }

}
