// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%featuresorg.talend.rcp.branding.%PRODUCTNAME%%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.thash.io;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class Bean implements Serializable {

    int primitiveInt;

    String name;

    transient int hashcode = -1;
    
    /**
     * DOC amaumont Bean constructor comment.
     * 
     * @param primitiveInt
     * @param name
     */
    public Bean(int primitiveInt, String name) {
        super();
        this.primitiveInt = primitiveInt;
        this.name = name;
    }
    
    public Bean(){
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if(hashcode == -1) {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
            result = prime * result + this.primitiveInt;
            hashcode = result;
        }
        return hashcode;
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
        final KeyForMap other = (KeyForMap) obj;

        if(this.hashCode() != other.hashcode) {
            return false;
        }
        
        Object o = null;
        try {
            o = ReliabilityHashMapFileTest.hashFile.get("buffer", (long)other.cursorPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (o == null) {
            return false;
        }
        Bean bean = (Bean) o;

        if (this.name == null) {
            if ((String) bean.name != null)
                return false;
        } else if (!this.name.equals((String) bean.name))
            return false;
        if (this.primitiveInt != bean.primitiveInt)
            return false;
        return true;
    }

    
    public int getPrimitiveInt() {
        return primitiveInt;
    }

    
    public void setPrimitiveInt(int primitiveInt) {
        this.primitiveInt = primitiveInt;
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

}
