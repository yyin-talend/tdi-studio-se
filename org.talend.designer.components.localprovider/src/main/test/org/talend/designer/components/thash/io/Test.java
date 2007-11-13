//============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
//============================================================================
package org.talend.designer.components.thash.io;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 *
 */
public class Test {

    public static void main(String[] args) {
        
        System.out.println(Long.SIZE);
        System.out.println(Integer.SIZE);
        System.out.println(Short.SIZE);
        long intAddressable = ((long)Integer.MAX_VALUE) - ((long)Integer.MIN_VALUE);
        
        
        
        System.out.println("intAddressable / 121 = " + intAddressable/121);
        System.out.println(intAddressable);
        System.out.println(Integer.MAX_VALUE/8);
        System.out.println(intAddressable/8 );
        int shortAddressable = ((int)Short.MAX_VALUE) - ((int)Short.MIN_VALUE);
        System.out.println("short=" +shortAddressable );
        System.out.println("short2=" +shortAddressable/8 );
        
        System.out.println(24 % 100);
        
        
    }
    
}
