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

    private int filePointersNumber;

    private RandomAccessFile[] bwArray = null;

    private int[] bwPositionArray = null;

    boolean readonly;

    private int hashFilesNumber;

    private byte numberOfChars;

    private MultiReadPointersFileHandler[] fileHandlersArray = null;

    private Object[] lastRetrievedObjectArray = null;

    private long[] lastRetrievedCursorPositionArray = null;

    private int countUniqueGet;

    private MultiPointersMultiHashFiles(int hashFilesNumber) {
        super();
        this.hashFilesNumber = hashFilesNumber;
        System.out.println("hashFilesNumber = " + hashFilesNumber);
        this.numberOfChars = (byte) (String.valueOf(hashFilesNumber).length() - 1);
    }

    /**
     * getInstance.
     * 
     * @param filesNumber number of hash files generated 10,100
     * @param filePointersNumber number of file pointers by file, number greater than 0
     * 
     * @return the instance if this project handler
     */
    public static synchronized MultiPointersMultiHashFiles createInstance(int filesNumber) {
        return new MultiPointersMultiHashFiles(filesNumber);
    }

    public Object get(String container, long cursorPosition, int hashcode) throws IOException, ClassNotFoundException {

        int fileNumber = getFileNumber(hashcode);

        RandomAccessFile ra = fileHandlersArray[fileNumber].getPointer(cursorPosition);

        if (cursorPosition != lastRetrievedCursorPositionArray[fileNumber]) {
            ++countUniqueGet;
            ra.seek(cursorPosition);
            byte[] byteArray = new byte[ra.readInt()];
            ra.read(byteArray);
            lastRetrievedObjectArray[fileNumber] = new ObjectInputStream(new ByteArrayInputStream(byteArray))
                    .readObject();
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
    private int getFileNumber(int hashcode) {
        int index = Math.abs(hashcode) % hashFilesNumber;
        return index;
    }

    public long put(String container, Object bean) throws IOException {

        int fileNumber = getFileNumber(bean.hashCode());

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
            bwArray = new RandomAccessFile[hashFilesNumber];
            bwPositionArray = new int[hashFilesNumber];
            for (int i = 0; i < hashFilesNumber; i++) {
                File file = new File(container + i);
                file.delete();
                bwArray[i] = new RandomAccessFile(container + i, "rw");
            }
        }
    }

    public void endPut() {
        if (!readonly) {
            for (int i = 0; i < hashFilesNumber; i++) {
                try {
                    if (bwArray[i] != null) {
                        bwArray[i].close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initGet(String container) throws IOException {
        fileHandlersArray = new MultiReadPointersFileHandler[hashFilesNumber];
        lastRetrievedCursorPositionArray = new long[hashFilesNumber];
        lastRetrievedObjectArray = new Object[hashFilesNumber];
        for (int i = 0; i < hashFilesNumber; i++) {
            fileHandlersArray[i] = new MultiReadPointersFileHandler(container + i, filePointersNumber);
            lastRetrievedCursorPositionArray[i] = -1;
        }
    }

    public void endGet(String container) throws IOException {
        if (!readonly) {
            for (int i = 0; i < hashFilesNumber; i++) {
                MultiReadPointersFileHandler ra = fileHandlersArray[i];
                if (ra != null) {
                    ra.close();
                }
                File file = new File(container + i);
                // file.delete();
            }
        }

        System.out.println("countUniqueGet = " + countUniqueGet);
    }

    class MultiReadPointersFileHandler {

        private String containerFilePath;

        private RandomAccessFile[] pointersArray;

        private int filePointersNumber;

        private int offsetBetweenPointer;

        private long fileSize;

        /**
         * DOC amaumont FileHandler constructor comment.
         * 
         * @param filePointersNumber
         * @throws IOException
         */
        public MultiReadPointersFileHandler(String containerFilePath, int filePointersNumber) throws IOException {
            super();
            this.containerFilePath = containerFilePath;
            this.filePointersNumber = filePointersNumber;

            init();
        }

        /**
         * DOC amaumont Comment method "init".
         * 
         * @throws IOException
         */
        private void init() throws IOException {
            pointersArray = new RandomAccessFile[filePointersNumber];
            File file = new File(containerFilePath);
            fileSize = file.length();

            offsetBetweenPointer = (int) ((float) fileSize / (float) (filePointersNumber));

            for (int i = 0; i < filePointersNumber; i++) {
                pointersArray[i] = new RandomAccessFile(containerFilePath, "r");
                pointersArray[i].seek((offsetBetweenPointer) * (i + 1) - offsetBetweenPointer / 2);
            }

        }

        public RandomAccessFile getPointer(long cursorPosition) {
            int index = (int) (cursorPosition / offsetBetweenPointer);
            // System.out.println(index);
            if (index >= filePointersNumber) {
                index = filePointersNumber - 1;
            }
            return pointersArray[index];
        }

        public void close() throws IOException {
            for (int i = 0; i < filePointersNumber; i++) {
                RandomAccessFile ra = pointersArray[i];
                if (ra != null) {
                    ra.close();
                }
            }

        }

    }

    /**
     * Getter for filePointersNumber.
     * 
     * @return the filePointersNumber
     */
    public int getFilePointersNumber() {
        return this.filePointersNumber;
    }

    /**
     * Sets the filePointersNumber.
     * 
     * @param filePointersNumber the filePointersNumber to set
     */
    public void setFilePointersNumber(int filePointersNumber) {
        this.filePointersNumber = filePointersNumber;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.components.thash.io.MapHashFile#getTotalSize()
     */
    @Override
    public long getTotalSize() {
        for (int i = 0; i < hashFilesNumber; i++) {
            File file = new File(container + i);
            file.length();
        }
        return 0;
    }

}
