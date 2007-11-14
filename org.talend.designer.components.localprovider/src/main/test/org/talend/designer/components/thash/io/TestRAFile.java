// ============================================================================//// Copyright (C) 2006-2007 Talend Inc. - www.talend.com//// This source code is available under agreement available at// %InstallDIR%featuresorg.talend.rcp.branding.%PRODUCTNAME%%PRODUCTNAME%license.txt//// You should have received a copy of the  agreement// along with this program; if not, write to Talend SA// 9 rue Pages 92150 Suresnes, France//// ============================================================================
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
public class TestRAFile {

    /**
     * 
     */
    private static final String D_20071109TEMP = "/tmp/20071109temp";

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        SimpleHashFile hashFile = SimpleHashFile.getInstance();        
        hashFile.initPut(D_20071109TEMP);

        List<Long> cursorPositionList = new ArrayList<Long>();

        int loop = 1000000;
        long end = 0;
        long start = java.util.Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < loop; i++) {
            InternalSmallBean bean = new InternalSmallBean(i, "test" + i);
            // KeyForMap keyForMap = new KeyForMap(id, bean.hashCode());
            cursorPositionList.add(hashFile.put("", bean));
        }
        hashFile.endPut();
        end = java.util.Calendar.getInstance().getTimeInMillis();
        System.out.println((end - start) + " milliseconds for " + loop + " objects to store.");

        start = java.util.Calendar.getInstance().getTimeInMillis();
        hashFile.initGet(D_20071109TEMP);
        int size = cursorPositionList.size();
        for (int i = 0; i < size; i++) {
            InternalSmallBean bean = (InternalSmallBean) hashFile.get("", cursorPositionList.get(i), -1);
//            System.out.println(bean.primitiveInt + "  " + bean.name);
        }
        hashFile.endGet(D_20071109TEMP);

        end = java.util.Calendar.getInstance().getTimeInMillis();
        System.out.println((end - start) + " milliseconds for " + loop + " objects to get.");

    }
}
