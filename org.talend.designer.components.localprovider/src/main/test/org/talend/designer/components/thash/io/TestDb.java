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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * DOC slanglois class global comment. Detailled comment <br/>
 * 
 */
public class TestDb {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        
        int loop = 1000000;
        
        System.out.println("Test result using java.io:");
        DB.setMode(DB.IO);
        
        DB.initPut("D:/20071109temp");

        List<Integer> lookup = new ArrayList<Integer>();

        long end = 0;
        long start = java.util.Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < loop; i++) {
            Bean bean = new Bean(i, "test" + i);
            int id = (int) DB.put("", bean);
//             KeyForMap keyForMap = new KeyForMap(id, bean.hashCode());
            lookup.add(id);
        }
        DB.endPut();
        end = java.util.Calendar.getInstance().getTimeInMillis();
        System.out.println((end - start) + " milliseconds for " + loop + " objects to store.");

        start = java.util.Calendar.getInstance().getTimeInMillis();
        DB.initGet("D:/20071109temp");
        Iterator<Integer> ite = lookup.iterator();
        while (ite.hasNext()) {
            Bean bean = (Bean) DB.get("", ite.next());
//            System.out.println(bean.primitiveInt + "  " + bean.name);
        }
        DB.endGet("D:/20071109temp");

        end = java.util.Calendar.getInstance().getTimeInMillis();
        System.out.println((end - start) + " milliseconds for " + loop + " objects to get.");
        
        /////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("////////////////////////////////////////////////////////////////");
        System.out.println("Test result using java.nio:");
        DB.setMode(DB.NIO);
        
        DB.initPut("D:/20071109temp");

        lookup = new ArrayList<Integer>();

        start = java.util.Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < loop; i++) {
            Bean bean = new Bean(i, "test" + i);
            int id = (int) DB.put("", bean);
            // KeyForMap keyForMap = new KeyForMap(id, bean.hashCode());
            lookup.add(id);
        }
        DB.endPut();
        end = java.util.Calendar.getInstance().getTimeInMillis();
        System.out.println((end - start) + " milliseconds for " + loop + " objects to store.");

        start = java.util.Calendar.getInstance().getTimeInMillis();
        DB.initGet("D:/20071109temp");
        ite = lookup.iterator();
        while (ite.hasNext()) {
            Bean bean = (Bean) DB.get("", ite.next());
//            System.out.println(bean.primitiveInt + "  " + bean.name);
        }
        DB.endGet("D:/20071109temp");

        end = java.util.Calendar.getInstance().getTimeInMillis();
        System.out.println((end - start) + " milliseconds for " + loop + " objects to get.");

    }
}
