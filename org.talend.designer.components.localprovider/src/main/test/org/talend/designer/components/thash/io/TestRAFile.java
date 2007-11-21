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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * DOC slanglois class global comment. Detailled comment <br/>
 * 
 */
public class TestRAFile {

    /**
     * 
     */
    private static final String D_20071109TEMP = "/tmp/20071109temp";

    private static final String D_CURSORPOSITION = "/tmp/cursorPosition";

    private static void initForGet(int loop) throws IOException {
        MultiplePointerSimpleHashFile hashFile = MultiplePointerSimpleHashFile.getInstance();

        hashFile.initPut(D_20071109TEMP);
        long[] cursors = new long[loop];
        for (int i = 0; i < loop; i++) {
            InternalSmallBean bean = new InternalSmallBean(i, "test" + i);
            cursors[i] = hashFile.put("", bean);
        }
        hashFile.endPut();

        File f = new File(D_CURSORPOSITION);
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(cursors);
        oos.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, InterruptedException {
        File f1 = new File(D_20071109TEMP);
        File f2 = new File(D_CURSORPOSITION);
        if (!f1.exists() || !f2.exists()) {
            System.out.println("write 20000000 items to file will take long time, please waite...");
            initForGet(20000000);
            System.out.println("write 20000000 items successful");
        }
        System.out.println("File size: " + f1.length());
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(D_CURSORPOSITION));
        long[] cursors = (long[]) ois.readObject();
        System.out.println(cursors.length + " items in file.");
        ois.close();

        // MultiplePointerSimpleHashFile hashFile = MultiplePointerSimpleHashFile.getInstance();
        NewMultiplePointerSimpleHashFile hashFile = NewMultiplePointerSimpleHashFile.getInstance();

        System.out.println("Use " + hashFile.getClass().getSimpleName() + ": ");
        int size = 1000000;
        long[] randomCursors = new long[size];
        for (int j = 0; j < size; j++) {
            randomCursors[j] = cursors[(int) (Math.random() * cursors.length)];
        }

        long end = 0;
        long start = java.util.Calendar.getInstance().getTimeInMillis();
        hashFile.initGet(D_20071109TEMP);
        for (int j = 0; j < size; j++) {
            InternalSmallBean bean = (InternalSmallBean) hashFile.get("", randomCursors[j], -1);
            // System.out.println(bean.primitiveInt+": "+bean.name);
        }

        hashFile.endGet(D_20071109TEMP);
        end = java.util.Calendar.getInstance().getTimeInMillis();
        System.out.println("Use " + hashFile.getClass().getSimpleName() + ": " + (end - start) + " milliseconds for " + size
                + " objects to get.");
        
        
//        SimpleHashFile hashFile = SimpleHashFile.getInstance();
//        
//        hashFile.initPut(D_20071109TEMP);
//
//        List<Long> cursorPositionList = new ArrayList<Long>();
//
//        int loop = 1000000;
//        long end = 0;
//        long start = java.util.Calendar.getInstance().getTimeInMillis();
//        for (int i = 0; i < loop; i++) {
//            InternalSmallBean bean = new InternalSmallBean(i, "test" + i);
//            // KeyForMap keyForMap = new KeyForMap(id, bean.hashCode());
//            cursorPositionList.add(hashFile.put("", bean));
//        }
//        hashFile.endPut();
//        end = java.util.Calendar.getInstance().getTimeInMillis();
//        System.out.println((end - start) + " milliseconds for " + loop + " objects to store.");
//
//        start = java.util.Calendar.getInstance().getTimeInMillis();
//        hashFile.initGet(D_20071109TEMP);
//        int size = cursorPositionList.size();
//        for (int i = 0; i < size; i++) {
//            InternalSmallBean bean = (InternalSmallBean) hashFile.get("", cursorPositionList.get(i), -1);
////            System.out.println(bean.primitiveInt + "  " + bean.name);
//        }
//        hashFile.endGet(D_20071109TEMP);
//
//        end = java.util.Calendar.getInstance().getTimeInMillis();
//        System.out.println((end - start) + " milliseconds for " + loop + " objects to get.");
    }
}
/*
results by slanglois's PC:

File size: 2868888890
20000000 items in file.
Use NewMultiplePointerSimpleHashFile:
Total time to get 1000 items from disk=10031 ms
Total time to get 1000 items from disk=10703 ms
Total time to get 1000 items from disk=10234 ms
Total time to get 1000 items from disk=10219 ms
Total time to get 1000 items from disk=11422 ms
Total time to get 1000 items from disk=9891 ms
Total time to get 1000 items from disk=10579 ms
Total time to get 1000 items from disk=10187 ms
Total time to get 1000 items from disk=10172 ms
Total time to get 1000 items from disk=9843 ms
Total time to get 1000 items from disk=11156 ms
Total time to get 1000 items from disk=10282 ms
Total time to get 1000 items from disk=16109 ms
Total time to get 1000 items from disk=43469 ms
Total time to get 1000 items from disk=10171 ms
Total time to get 1000 items from disk=10219 ms
Total time to get 1000 items from disk=10439 ms
Total time to get 1000 items from disk=10344 ms
Total time to get 1000 items from disk=10625 ms
Total time to get 1000 items from disk=10047 ms
Total time to get 1000 items from disk=10234 ms
Total time to get 1000 items from disk=10485 ms
Total time to get 1000 items from disk=10110 ms
Total time to get 1000 items from disk=9844 ms
Total time to get 1000 items from disk=10984 ms
Total time to get 1000 items from disk=10656 ms
Total time to get 1000 items from disk=10358 ms
Total time to get 1000 items from disk=10343 ms
Total time to get 1000 items from disk=10282 ms
Total time to get 1000 items from disk=11047 ms
Total time to get 1000 items from disk=16939 ms
Total time to get 1000 items from disk=40812 ms
Total time to get 1000 items from disk=10890 ms
Total time to get 1000 items from disk=10110 ms
Total time to get 1000 items from disk=10469 ms
Total time to get 1000 items from disk=10000 ms
Total time to get 1000 items from disk=9937 ms
Total time to get 1000 items from disk=10830 ms
Total time to get 1000 items from disk=9938 ms
Total time to get 1000 items from disk=9859 ms
Total time to get 1000 items from disk=10376 ms
Total time to get 1000 items from disk=10188 ms
Total time to get 1000 items from disk=10032 ms
Total time to get 1000 items from disk=11032 ms
Total time to get 1000 items from disk=9892 ms
Total time to get 1000 items from disk=10328 ms
Total time to get 1000 items from disk=10155 ms
Total time to get 1000 items from disk=10078 ms
Total time to get 1000 items from disk=10375 ms
Total time to get 1000 items from disk=42266 ms
Total time to get 1000 items from disk=13561 ms
Total time to get 1000 items from disk=10766 ms
Total time to get 1000 items from disk=9968 ms
Total time to get 1000 items from disk=10266 ms
Total time to get 1000 items from disk=10030 ms
Total time to get 1000 items from disk=10298 ms
Total time to get 1000 items from disk=9906 ms
Total time to get 1000 items from disk=10827 ms
......
......



//////////////////////////////////////////////////////
File size: 2868888890
20000000 items in file.
Use MultiplePointerSimpleHashFile:
Total time to get 1000 items from disk=9500 ms
Total time to get 1000 items from disk=10015 ms
Total time to get 1000 items from disk=10470 ms
Total time to get 1000 items from disk=12187 ms
Total time to get 1000 items from disk=35796 ms
Total time to get 1000 items from disk=25031 ms
Total time to get 1000 items from disk=30282 ms
Total time to get 1000 items from disk=13688 ms
Total time to get 1000 items from disk=49343 ms
Total time to get 1000 items from disk=26219 ms
Total time to get 1000 items from disk=9922 ms
Total time to get 1000 items from disk=11765 ms
Total time to get 1000 items from disk=10077 ms
Total time to get 1000 items from disk=10439 ms
Total time to get 1000 items from disk=10344 ms
Total time to get 1000 items from disk=11187 ms
Total time to get 1000 items from disk=10328 ms
Total time to get 1000 items from disk=13359 ms
Total time to get 1000 items from disk=10204 ms
Total time to get 1000 items from disk=9859 ms
Total time to get 1000 items from disk=10969 ms
Total time to get 1000 items from disk=10000 ms
Total time to get 1000 items from disk=10140 ms
Total time to get 1000 items from disk=11703 ms
Total time to get 1000 items from disk=10188 ms
Total time to get 1000 items from disk=9860 ms
Total time to get 1000 items from disk=36109 ms
Total time to get 1000 items from disk=20296 ms
Total time to get 1000 items from disk=10986 ms
Total time to get 1000 items from disk=10218 ms
Total time to get 1000 items from disk=9922 ms
Total time to get 1000 items from disk=9986 ms
Total time to get 1000 items from disk=10890 ms
Total time to get 1000 items from disk=10282 ms
Total time to get 1000 items from disk=10874 ms
Total time to get 1000 items from disk=10406 ms
Total time to get 1000 items from disk=10313 ms
Total time to get 1000 items from disk=9875 ms
Total time to get 1000 items from disk=10328 ms
Total time to get 1000 items from disk=10390 ms
Total time to get 1000 items from disk=10875 ms
Total time to get 1000 items from disk=10187 ms
Total time to get 1000 items from disk=10282 ms
Total time to get 1000 items from disk=10202 ms
Total time to get 1000 items from disk=27171 ms
Total time to get 1000 items from disk=33125 ms
Total time to get 1000 items from disk=10812 ms
Total time to get 1000 items from disk=11875 ms
Total time to get 1000 items from disk=10500 ms
Total time to get 1000 items from disk=11891 ms
Total time to get 1000 items from disk=10859 ms
Total time to get 1000 items from disk=11031 ms
Total time to get 1000 items from disk=10343 ms
Total time to get 1000 items from disk=10969 ms
Total time to get 1000 items from disk=10782 ms
Total time to get 1000 items from disk=13375 ms
Total time to get 1000 items from disk=10859 ms
Total time to get 1000 items from disk=10406 ms
Total time to get 1000 items from disk=11110 ms
Total time to get 1000 items from disk=10203 ms
Total time to get 1000 items from disk=10234 ms
Total time to get 1000 items from disk=14391 ms
....
....
 */
