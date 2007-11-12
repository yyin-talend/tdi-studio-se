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

import gnu.trove.THashMap;
import gnu.trove.TObjectHashingStrategy;

import java.util.Map;

import org.talend.designer.components.thash.Sizeof;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 *
 *

With THashMap class and WITHOUT HashFile
------------------
134151 milliseconds for 60000000 objects to STORE. 447000 items/s 
waiting for garbage collector...
'before' heap: 140168 bytes, 'after' heap: 1554156024 bytes 
heap delta: 1554015856 bytes 
size by item: 26 bytes 
Number of loops: 60000000
Number of items: 60000000
Time: 135 s

152120 milliseconds for 70000000 objects to STORE. 460000 items/s 
waiting for garbage collector...
'before' heap: 140696 bytes, 'after' heap: 1707881976 bytes 
heap delta: 1707741280 bytes 
size by item: 24 bytes 
Number of loops: 70000000
Number of items: 70000000
Time: 153 s


With THashMap class and SimpleHashFile
------------------
3856 milliseconds for 100000 objects to STORE. 25000 items/s 
3615 milliseconds for 100000 objects to READ. 27000 items/s 
'before' heap: 1274168 bytes, 'after' heap: 4977296 bytes 
heap delta: 3703128 bytes 
size by item: 37 bytes 
Number of loops: 100000
Number of items: 100000
Time: 7 s

279767 milliseconds for 10000000 objects to STORE. 35000 items/s 
302748 milliseconds for 10000000 objects to READ. 33000 items/s 
'before' heap: 1274168 bytes, 'after' heap: 325257696 bytes 
heap delta: 323983528 bytes 
size by item: 32 bytes 
Number of loops: 10000000
Number of items: 10000000
Time: 582 s

1401670 milliseconds for 42000000 objects to STORE. 29000 items/s
1561101 milliseconds for 42000000 objects to READ. 26000 items/s 
'before' heap: 140168 bytes, 'after' heap: 1344255912 bytes 
heap delta: 1344115744 bytes 
size by item: 32 bytes 
Number of loops: 42000000
Number of items: 42000000
Time: 2962 s

With THashMap class and DoubleHashFile
------------------
2358244 milliseconds for 42000000 objects to STORE. 17811 items/s 
1715513 milliseconds for 42000000 objects to READ. 24489 items/s 
waiting for garbage collector...
'before' heap: 140168 bytes, 'after' heap: 1008256048 bytes 
heap delta: 1008115880 bytes 
size by item: 24 bytes 
Number of loops: 42000000
Number of items: 42000000
Time: 4073 s

3942866 milliseconds for 70000000 objects to STORE. 17000 items/s 
4086505 milliseconds for 70000000 objects to READ. 17000 items/s 
waiting for garbage collector...
'before' heap: 140168 bytes, 'after' heap: 1707900576 bytes 
heap delta: 1707760408 bytes 
size by item: 24 bytes 
Number of loops: 70000000
Number of items: 70000000
Time: 8030 s


 * 
 * 
With HashMap class
------------------
284282 milliseconds for 10000000 objects to READ. 35000 items/s 
35 items/s 
waiting for garbage collector...
'before' heap: 1274872 bytes, 'after' heap: 548383120 bytes 
heap delta: 547108248 bytes 
size by item: 55 bytes 
Number of loops: 10000000
Number of items: 10000000
Time: 366 s


693379 milliseconds for 25000000 objects to STORE. 36000 items/s 
794806 milliseconds for 25000000 objects to READ. 31000 items/s 
31 items/s 
waiting for garbage collector...
'before' heap: 140696 bytes, 'after' heap: 1334371592 bytes 
heap delta: 1334230896 bytes 
size by item: 53 bytes 
Number of loops: 25000000
Number of items: 25000000
Time: 1488 s




 * 
 * 
 * 
 * 
 */
public class ReliabilityHashMapFileTest {

    public static DoubleHashFile hashFile;

    public static void main(String[] args) throws Exception {

//         int loop = 2;
//        int loop = 100000;
//        int loop = 1000000;
        int loop = 10000000;
//        int loop = 25000000;
//        int loop = 42000000;
//        int loop = 53000000;
//        int loop = 60000000;
//        int loop = 70000000;
//        int loop = 80000000;

        
        // change also in Bean.equals(...) class
//        hashFile = SimpleHashFile.getInstance();
        hashFile = DoubleHashFile.getInstance();

        
        String filePath = "/tmp/talend_hash";
        
        System.out.println("Starting with " + loop + " items ");

        
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
        
        
        Map hashMap = new THashMap(loop, 1.0f, objectHashingStrategy);
        
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
                + (loop / deltaTime * 1000) + " items/s ");

        
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

                // => bean from main flow in tMap for example...
                Bean bean = new Bean(i, String.valueOf(i));

                // => search properties of bean in lookup for example...
                KeyForMap keyForMap = (KeyForMap) hashMap.get(bean);

                // validity test
                if (keyForMap == null) {
                    throw new RuntimeException("keyForMap not found with id " + i);
                }

                // => bean found from DB
                Bean beanFromDB = (Bean) hashFile.get("buffer", keyForMap.cursorPosition);

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
                    + (loop / deltaTime * 1000) + " items/s ");

        }

        long time2 = System.currentTimeMillis();

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
