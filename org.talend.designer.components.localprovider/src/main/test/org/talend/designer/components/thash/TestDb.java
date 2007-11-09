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

import java.io.IOException;
import java.sql.SQLException;


/**
 * 
 * DOC slanglois  class global comment. Detailled comment
 * <br/>
 *
 */
public class TestDb {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        DB.connect("/tmp/talend.db");
        DB.createTable("buffer");
        DB.commit();
        
        // 48s for 100 000, 0,48 s/bean
        
        int loop = 100000000;
        long end = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            Bean bean = new Bean(i, "test" + i);
            DB.put("buffer", bean);
        }
        DB.commit();
        end = System.currentTimeMillis();
        System.out.println((end - start) + " milliseconds for " + loop + " objects to store.");
        
        // 17s for 100 000,  0,17 s/bean
        
        start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            Bean bean = (Bean)DB.get("buffer", loop);
        }
        end = System.currentTimeMillis();
        System.out.println((end - start) + " milliseconds for " + loop + " objects to get.");
        
        DB.close();
        
    }
}

