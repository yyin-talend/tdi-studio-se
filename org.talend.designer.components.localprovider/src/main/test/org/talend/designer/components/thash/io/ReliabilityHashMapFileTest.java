// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.components.thash.io;

import gnu.trove.THashMap;
import gnu.trove.TObjectHashingStrategy;

import java.util.Map;
import java.util.Random;

import org.talend.designer.components.thash.Sizeof;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 *
 *
 * 
 * 
 * 
 * 
 */
public class ReliabilityHashMapFileTest {

    public static MultipleHashFile hashFile;

    public static void main(String[] args) throws Exception {

//         int loop = 1;
//        int loop = 10000;
//        int loop = 100000;    //   100 000 
//        int loop = 1000000;   //  1 000 000
        int loop = 10000000;  // 10 000 000
//        int loop = 25000000;
//        int loop = 42000000;
//        int loop = 53000000;
//        int loop = 60000000;
//        int loop = 70000000;
//        int loop = 80000000;

        
        // change also in Bean.equals(...) class
//        hashFile = SimpleHashFile.getInstance();
        hashFile = MultipleHashFile.getInstance();

        
        String filePath = "/tmp/talend_hash";
        
        System.out.println("Starting with " + loop + " items ");

        boolean randomRead = true;
        
        boolean readonly = false;
        
        boolean readAfterStore = true;

        Sizeof.runGC();
        long heap1 = Sizeof.usedMemory(); // Take a before heap snapshot

        long time1 = System.currentTimeMillis();

        TObjectHashingStrategy objectHashingStrategy = new TObjectHashingStrategy() {

            /*
             * (non-Javadoc)
             * 
             * @see gnu.trove.TObjectHashingStrategy#computeHashCode(java.lang.Object)
             */
            public int computeHashCode(Object o) {
                return o == null ? 0 : o.hashCode();
            }

            /*
             * (non-Javadoc)
             * 
             * @see gnu.trove.TObjectHashingStrategy#equals(java.lang.Object, java.lang.Object)
             */
            public boolean equals(Object o1, Object o2) {
                return o2 == null ? o1 == null : o2.equals(o1);
            }

        };

        // Tests for 5 000 000 items

        // ################################################################################
        // Do not compare bytes number in this class with bytes number MemoryHashMapTest,
        // the purpose of this test class is not to measure heap memory !
        // ################################################################################

        // Map hashMap = new HashMap(); // 142 bytes, all=94 s
//         Map hashMap = new HashMap(loop, 1f); // ??
//         Map hashMap = new HashMap(10000, 1f); // ??
        // Map hashMap = new THashMap(objectHashingStrategy); // 132 bytes, 25 s
        // Map hashMap = new THashMap(loop, 0.1f, objectHashingStrategy); // ??
        // Map hashMap = new THashMap(loop, 1f, objectHashingStrategy); // ??
        // Map hashMap = new THashMap(loop + 10000, 1.0f, objectHashingStrategy); // ??
        // Map hashMap = new THashMap(loop + 10000, 0.99f, objectHashingStrategy); // ??
        // Map hashMap = new THashMap(10000, 1.0f, objectHashingStrategy); // ??
        
        
        // Data in Memory, 119 bytes , all=34 s
        // Data in Sqlite DB, ?? bytes , all=?? s, write=2550 s (42 min), read=???? s (?? min)
        // Data in File, ?? bytes , all=?? s, write=?? s (?? min), read=???? s (?? min)
        
        
         Map hashMap = new THashMap(loop, 1.0f, objectHashingStrategy); // ??
//        Map hashMap = new THashMap(loop + (int)((float)loop * 0.1f), 0.1f, objectHashingStrategy);
        
        if(hashMap instanceof THashMap) {
            System.out.println(">> THashMap");
        } else if(hashMap instanceof THashMap) {
            System.out.println(">> HashMap");
        } else {
            System.out.println(">> HashMap type not found");
        }
        


        hashFile.readonly = readonly;

        System.out.println("Write step");
        long start = System.currentTimeMillis();
        
        long previousUsedMemory = Sizeof.usedMemory();
        
        hashFile.initPut(filePath);
        for (int i = 0; i < loop; i++) {
            // => bean from tFileInput for example...
            Bean bean = new Bean(i, String.valueOf(i));

            KeyForMap keyForMap = null;
            // => THash storing step
            if (readAfterStore) {
                keyForMap = new KeyForMap((int) hashFile.put("buffer", bean), bean.hashCode());
            } else {
                keyForMap = new KeyForMap(i, bean.hashCode());
            }
            hashMap.put(keyForMap, keyForMap);

            if (i % 100000 == 0) {
                System.out.println("Writing " + i);
            }
            
        }
        hashFile.endPut();
        long end = System.currentTimeMillis();
        long deltaTime = (end - start);
        System.out.println(deltaTime + " milliseconds for " + loop + " objects to STORE. "
                + (int)((float)loop / (float)deltaTime * 1000f) + " items/s ");

        
        Random rand = new Random(System.currentTimeMillis());

        
        if (readAfterStore) {
            System.out.println("Read step");
            start = System.currentTimeMillis();
            long lastTime = start;
            hashFile.initGet(filePath);
            for (int i = 0; i < loop; i++) {
                if (i % 100000 == 0) {
                    System.out.println("Reading " + i + ", time since last display"
                            + (System.currentTimeMillis() - lastTime) / 1000 + " s");
                    lastTime = System.currentTimeMillis();
                }

                Bean bean = null;
                // => bean from main flow in tMap for example...
                if(randomRead) {
                    int v = rand.nextInt(loop);
                    bean = new Bean(v, String.valueOf(v));
                } else {
                    bean = new Bean(i, String.valueOf(i));
                }

                // => search properties of bean in lookup for example...
                KeyForMap keyForMap = (KeyForMap) hashMap.get(bean);

                // validity test
                if (keyForMap == null) {
                    throw new RuntimeException("keyForMap not found with id " + i);
                }

                // => bean found from DB
                Bean beanFromDB = (Bean) hashFile.get("buffer", keyForMap.cursorPosition, keyForMap.hashcode);

                // validity test
                if (beanFromDB == null) {
                    throw new RuntimeException("Bean not found with cursorPosition " + keyForMap.cursorPosition);
                }
                // validity test
                if (!beanFromDB.name.equals(bean.name) || beanFromDB.primitiveInt != bean.primitiveInt) {
                    throw new RuntimeException("Values of beans are different with cursorPosition "
                            + keyForMap.cursorPosition);
                }
            }
            hashFile.endGet(filePath);
            end = System.currentTimeMillis();
            
            deltaTime = (end - start);
            System.out.println(deltaTime + " milliseconds for " + loop + " objects to READ. "
                    + (int)((float)loop / (float)deltaTime * 1000f) + "  items/s ");

        }

        long time2 = System.currentTimeMillis();

        System.out.println("countReturnFalse1=" + Bean.countReturnFalse1);
        System.out.println("waiting for garbage collector...");
        Sizeof.runGC();
        long heap2 = Sizeof.usedMemory(); // Take a before heap snapshot

        final int size = Math.round(((float) (heap2 - heap1)) / loop);
        System.out.println("'before' heap: " + heap1 + " bytes, 'after' heap: " + heap2 + " bytes "); // not needed
        // here
        System.out.println("heap delta: " + (heap2 - heap1) + " bytes ");
        System.out.println("size by item: " + size + " bytes ");
        System.out.println("Number of loops: " + loop);
        System.out.println("Number of items: " + hashMap.size());
        if (loop != hashMap.size()) {
            System.out.println("WARNING: loops number is different of items number !");
        }
        System.out.println("Time: " + ((time2 - time1) / 1000) + " s");

    }

}
