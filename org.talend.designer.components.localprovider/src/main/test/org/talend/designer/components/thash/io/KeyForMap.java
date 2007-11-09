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
    /**
     * 
     * DOC amaumont  class global comment. Detailled comment
     * <br/>
     *
     * 
     *
     */
    class KeyForMap {

        int dbIdBean;

        int hashcode;

        /**
         * DOC amaumont KeyForMap constructor comment.
         */
        public KeyForMap(int dbIdBean, int hashcode) {
            super();
            this.dbIdBean = dbIdBean;
            this.hashcode = hashcode;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return this.hashcode;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final KeyForMap other = (KeyForMap) obj;
            if (this.dbIdBean != other.dbIdBean)
                return false;
            return true;
        }

    }
