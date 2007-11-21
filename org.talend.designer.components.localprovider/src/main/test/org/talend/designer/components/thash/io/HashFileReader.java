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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * DOC slanglois class global comment. Detailled comment
 */
public class HashFileReader {

    RandomAccessFileWithPosition[] array = null;

    long[] initPositions = null;

    int pointers = 1;

    long fileSize;

    boolean threaded = true;

    public HashFileReader(String container, int pointers) throws IOException {

        array = new RandomAccessFileWithPosition[pointers];
        initPositions = new long[pointers];
        this.pointers = pointers;

        File file = new File(container);
        fileSize = file.length();
        long offsetBetweenPointer = (long) ((float) fileSize / (float) (pointers));

        for (int i = 0; i < pointers; i++) {
            RandomAccessFile raf = new RandomAccessFile(container, "r");
            long position = (long) (offsetBetweenPointer) * (i + 1) - (long) offsetBetweenPointer / 2;
            initPositions[i] = position;
            raf.seek(position);
            array[i] = new RandomAccessFileWithPosition(raf, position);
        }
    }

    public byte[] read(long position) throws IOException {
        RandomAccessFileWithPosition key = new RandomAccessFileWithPosition(null, position);

        int index = Arrays.binarySearch(array, key);

        // here get the nearest pointer and seek to the position, index >= 0 indicate that the index's position is the
        // key's position.
        if (index < 0) {
            index = -1 - index;
            if (index == 0) {
                // index = 0;
            } else if (index == pointers) {
                index = pointers - 1;
            } else {
                if (array[index].position - position > position - array[index - 1].position) {
                    index = index - 1;
                }
            }
            array[index].raf.seek(position);
        }

        byte[] result = new byte[array[index].raf.readInt()];
        array[index].raf.read(result);
        array[index].position = position + result.length + 4;

        // determine the from index and to index for sorting.
        if (array[index].position == fileSize && index != pointers - 1) {
            array[index].raf.seek(0);
            array[index].position = 0;
            Arrays.sort(array, 0, index + 1);
        } else {
            int fromIndex = index;
            int toIndex = index + 1;
            while (fromIndex > 0 && array[fromIndex - 1].position > array[index].position) {
                fromIndex--;
            }
            while (toIndex < pointers && array[toIndex].position < array[index].position) {
                toIndex++;
            }
            Arrays.sort(array, fromIndex, toIndex);
        }

        return result;

    }

    public void close() throws IOException {
        for (int i = 0; i < pointers; i++) {
            array[i].close();
        }
    }

    /**
     * DOC slanglois Comment method "scatterPointers". Scatter pointers to their initial positions.
     * 
     * @throws IOException
     */
    public void scatterPointers() throws IOException {
        for (int i = 0; i < pointers; i++) {
            array[i].raf.seek(initPositions[i]);
            array[i].position = initPositions[i];
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "";
        for (int i = 0; i < pointers; i++) {
            result += array[i].position + "    ";
        }
        return result;
    }

}

/**
 * DOC slanglois class global comment. Detailled comment
 * 
 */
class RandomAccessFileWithPosition implements Comparable<RandomAccessFileWithPosition> {

    long position;

    RandomAccessFile raf = null;

    RandomAccessFileWithPosition(RandomAccessFile raf, long position) {
        super();
        this.raf = raf;
        this.position = position;
    }

    @Override
    public int compareTo(RandomAccessFileWithPosition o) {
        return (int) (this.position - o.position);
    }

    void close() throws IOException {
        if (raf != null) {
            raf.close();
        }

    }
}
