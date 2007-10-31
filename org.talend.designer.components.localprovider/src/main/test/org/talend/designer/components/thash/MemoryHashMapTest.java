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
package org.talend.designer.components.thash;


import java.util.HashMap;
import java.util.Map;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 *
 * launched with these VM arguments: -Xms512m -Xmx1400m -XX:MaxPermSize=256m
 */
public class MemoryHashMapTest {

    public static void main(String[] args) throws Exception {

        /**
         * 
         * DOC amaumont MemoryHashMapTest class global comment. Detailled comment
         * <br/>
         *
         * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
         *
         */
        class KeyForMap {

            int hashcode = 0;

            int key = 0;

        }

        int loop = 10000000;
        // int LOOP = 5000000;

        Sizeof.runGC();
        Sizeof.runGC();
        Sizeof.runGC();
        long heap1 = Sizeof.usedMemory(); // Take a before heap snapshot

        long time1 = System.currentTimeMillis();

        // Tests pour 10 000 000 d'items

        // Map hashMap = new HashMap(); // 47 bytes, 12 s
        Map hashMap = new HashMap(loop, 1f); // 47 bytes, 7 s
        // Map hashMap = new THashMap(); // 37 bytes 14 s
        // Map hashMap = new THashMap(LOOP, 0.1f); // 96 bytes, 14 s
        // Map hashMap = new THashMap(LOOP, 1f); // 24 bytes, 10 s
        // Map hashMap = new THashMap(LOOP + 10000, 1.0f); // 24 bytes, 11 s
        // Map hashMap = new THashMap(LOOP + 10000, 0.99f); // 24 bytes, 10 s

        for (int i = 0; i < loop; i++) {
            KeyForMap keyForMap = new KeyForMap();
            hashMap.put(keyForMap, keyForMap);
        }

        long time2 = System.currentTimeMillis();

        Sizeof.runGC();
        Sizeof.runGC();
        Sizeof.runGC();
        long heap2 = Sizeof.usedMemory(); // Take a before heap snapshot

        final int size = Math.round(((float) (heap2 - heap1)) / loop);
        System.out.println("'before' heap: " + heap1 + " bytes, 'after' heap: " + heap2 + " bytes ");
        System.out.println("heap delta: " + (heap2 - heap1) + " bytes ");
        System.out.println("size by item: " + size + " bytes ");
        System.out.println("Number of items: " + hashMap.size());
        System.out.println("Time: " + ((time2 - time1) / 1000) + " s");

    }

    
}
