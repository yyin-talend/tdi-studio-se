// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.thash.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
class MultiPointersMultiHashFiles implements MapHashFile {

    private static MultiPointersMultiHashFiles instance;

    private MultiPointersMultiHashFiles() {
    }

    /**
     * getInstance.
     * 
     * @return the instance if this project handler
     */
    public static synchronized MultiPointersMultiHashFiles getInstance() {
        if (instance == null) {
            instance = new MultiPointersMultiHashFiles();
        }
        return instance;
    }

    private RandomAccessFile[] bwArray = null;
    private int[] bwPositionArray = null;

    boolean readonly;

    private int numberFiles = 10;
    
    private byte numberOfChars = (byte)(String.valueOf(numberFiles).length() - 1);

    private FileHandler[] fileHandlersArray = null;

    private Object[] lastRetrievedObjectArray = null;

    private long[] lastRetrievedCursorPositionArray = null;
    
    private int countUniqueGet;

    public Object get(String container, long cursorPosition, int hashcode) throws IOException, ClassNotFoundException {

        byte fileNumber = getFileNumber(hashcode);
        
        RandomAccessFile ra = fileHandlersArray[fileNumber].getPointer(cursorPosition);

        if (cursorPosition != lastRetrievedCursorPositionArray[fileNumber]) {
            ++countUniqueGet;
            ra.seek(cursorPosition);
            byte[] byteArray = new byte[ra.readInt()];
            ra.read(byteArray);
            lastRetrievedObjectArray[fileNumber] = new ObjectInputStream(new ByteArrayInputStream(byteArray)).readObject();
            lastRetrievedCursorPositionArray[fileNumber] = cursorPosition;
        }
        return lastRetrievedObjectArray[fileNumber];
    }

    /**
     * DOC amaumont Comment method "getFileNumber".
     * 
     * @param hashcode
     * @return
     */
    private byte getFileNumber(int hashcode) {
        String valueOf = String.valueOf(Math.abs(hashcode));
        return Byte.parseByte(valueOf.substring(valueOf.length() - numberOfChars,valueOf.length()));
    }

    public long put(String container, Object bean) throws IOException {

        byte fileNumber = getFileNumber(bean.hashCode());
        
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

        int sizeBytes = byteArrayOutputStream.size();

        if (!readonly) {
            RandomAccessFile bw = bwArray[fileNumber];
            bw.writeInt(sizeBytes);
            bw.write(byteArrayOutputStream.toByteArray());
        }

        byteArrayOutputStream.close();

        long returnPosition = bwPositionArray[fileNumber];

        bwPositionArray[fileNumber] += (4 + sizeBytes);

        return returnPosition;
    }

    public void initPut(String container) throws IOException {
        if (!readonly) {
            bwArray = new RandomAccessFile[numberFiles];
            bwPositionArray = new int[numberFiles];
            for (int i = 0; i < numberFiles; i++) {
                File file = new File(container + i);
                file.delete();
                bwArray[i] = new RandomAccessFile(container + i, "rw");
            }
        }
    }

    public void endPut() throws IOException {
        if (!readonly) {
            for (int i = 0; i < numberFiles; i++) {
                bwArray[i].close();
            }
        }
    }

    public void initGet(String container) throws IOException {
        fileHandlersArray = new FileHandler[numberFiles];
        lastRetrievedCursorPositionArray = new long[numberFiles]; 
        lastRetrievedObjectArray = new Object[numberFiles];
        for (int i = 0; i < numberFiles; i++) {
            fileHandlersArray[i] = new FileHandler(container + i);
            lastRetrievedCursorPositionArray[i] = -1;
        }
    }

    public void endGet(String container) throws IOException {
        if (!readonly) {
            for (int i = 0; i < numberFiles; i++) {
                FileHandler ra = fileHandlersArray[i];
                if (ra != null) {
                    ra.close();
                }
                File file = new File(container + i);
//                file.delete();
            }
        }
        
        System.out.println("countUniqueGet = "+countUniqueGet);
    }

    class FileHandler {

        private String containerFilePath;
        private RandomAccessFile[] pointersArray;

        int pointersNumber = 100;

        private int offsetBetweenPointer;
        private long fileSize;

        /**
         * DOC amaumont FileHandler constructor comment.
         * @throws IOException 
         */
        public FileHandler(String containerFilePath) throws IOException {
            super();
            this.containerFilePath = containerFilePath;
            init();
        }

        /**
         * DOC amaumont Comment method "init".
         * @throws IOException 
         */
        private void init() throws IOException {
            pointersArray = new RandomAccessFile[pointersNumber];
            File file = new File(containerFilePath);
            fileSize = file.length();
            
            offsetBetweenPointer = (int)((float)fileSize / (float)(pointersNumber));
            
            for (int i = 0; i < pointersNumber; i++) {
                pointersArray[i] = new RandomAccessFile(containerFilePath, "r");
                pointersArray[i].seek((offsetBetweenPointer) * (i + 1) - offsetBetweenPointer / 2);
            }

        }
     
        public RandomAccessFile getPointer(long cursorPosition) {
            int index = (int) (cursorPosition/offsetBetweenPointer);
//            System.out.println(index);
            if(index >= pointersNumber) {
                index = pointersNumber - 1;
            }
            return pointersArray[index];
        }
        
        public void close() throws IOException {
            for (int i = 0; i < pointersNumber; i++) {
                RandomAccessFile ra = pointersArray[i];
                if (ra != null) {
                    ra.close();
                }
            }

        }
        
    }
    
}
