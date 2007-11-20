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
import java.util.Observable;
import java.util.Observer;

/**
 * DOC slanglois  class global comment. Detailled comment
 */
public class ReadPointerCollection implements Observer {

    ObservableRandomAccessFile[] array = null;
    
    long[] initPositions = null;

    int pointers = 0;

    // private int toIndex;

    // private int fromIndex;

    public ReadPointerCollection(String container, int pointers) throws IOException {

        array = new ObservableRandomAccessFile[pointers];
        initPositions = new long[pointers];
        this.pointers = pointers;

        File file = new File(container);
//        System.out.println("File size: "+file.length());
        long offsetBetweenPointer = (long) ((float) file.length() / (float) (pointers));

        for (int i = 0; i < pointers; i++) {
            RandomAccessFile raf = new RandomAccessFile(container, "r");
            long position = (offsetBetweenPointer) * (i + 1) - offsetBetweenPointer / 2;
            initPositions[i] = position;
            raf.seek(position);
            array[i] = new ObservableRandomAccessFile(raf, file.length(), position);
            array[i].addObserver(this);
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        // Arrays.sort(array, fromIndex, toIndex);
        Arrays.sort(array);
    }

    public ObservableRandomAccessFile getPointer(long position) {
        ObservableRandomAccessFile key = new ObservableRandomAccessFile();
        key.position = position;
        int index = Arrays.binarySearch(array, key);

        if (index < 0) {
            index = -1 - index;
            if (index == 0) {
                // index =0
            } else if (index == pointers) {
                index = pointers - 1;
            } else {
                if (array[index].compareTo(key) > array[index - 1].compareTo(key)) {
                    index = index - 1;
                }
            }
        }
      //choose the last pointer in array when there are more than one nearest pointers available in array.
//        while (index < pointers - 1 && array[index].compareTo(array[index + 1]) == 0) {
//            index++;
//        }

        // fromIndex = index;
        // while(fromIndex > 0 && array[fromIndex].compareTo(array[fromIndex - 1]) == 0){
        // fromIndex --;
        // }
        // toIndex = index + 1;
        // while(toIndex < pointers && array[toIndex].compareTo(array[toIndex - 1]) == 0){
        // toIndex ++;
        // }
//        System.out.println("Get index: " + index + "  " + array[index].position);
//        System.out.println("Before: " + toString());
        return array[index];
    }

    public void close() throws IOException {
        for (int i = 0; i < pointers; i++) {
            array[i].close();
        }
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < pointers; i++) {
            result += array[i].position + "    ";
        }
        return result;
    }
    
    public void scatterPointers() throws IOException{
        for(int i=0; i<pointers; i++){
            array[i].raf.seek(initPositions[i]);
            array[i].position = initPositions[i];
        }
    }
}
