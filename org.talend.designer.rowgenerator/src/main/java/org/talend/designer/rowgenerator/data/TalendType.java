// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.rowgenerator.data;

import java.util.List;

/**
 * class global comment. Detailled comment <br/> $Id: TalendType.java,v 1.3 2007/01/31 05:20:52 pub Exp $
 */
public class TalendType {

    /**
     * @uml.property name="name"
     */
    private String name = "";

    /**
     * Getter of the property <tt>name</tt>.
     * 
     * @return Returns the name.
     * @uml.property name="name"
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter of the property <tt>name</tt>.
     * 
     * @param name The name to set.
     * @uml.property name="name"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @uml.property name="functions"
     * @uml.associationEnd multiplicity="(1 -1)" ordering="true"
     * inverse="talendType:org.talend.designer.rowgenerator.data.Function"
     */
    private List functions = new java.util.ArrayList();

    /**
     * Getter of the property <tt>functions</tt>.
     * 
     * @return Returns the functions.
     * @uml.property name="functions"
     */
    public List getFunctions() {
        return this.functions;
    }

    /**
     * Returns the element at the specified position in this list.
     * 
     * @param index index of element to return.
     * @return the element at the specified position in this list.
     * @see java.util.List#get(int)
     * @uml.property name="functions"
     */
    public Function getFunctions(int i) {
        return (Function) this.functions.get(i);
    }




    /**
     * Returns an array containing all of the elements in this list in proper sequence.
     * 
     * @return an array containing all of the elements in this list in proper sequence.
     * @see java.util.List#toArray()
     * @uml.property name="functions"
     */
    @SuppressWarnings("unchecked")
    public Function[] functionsToArray() {
        return (Function[]) this.functions.toArray(new Function[this.functions.size()]);
    }



    /**
     * Appends the specified element to the end of this list (optional operation).
     * 
     * @param element element to be appended to this list.
     * @return <tt>true</tt> (as per the general contract of the <tt>Collection.add</tt> method).
     * @see java.util.List#add(Object)
     * @uml.property name="functions"
     */
    @SuppressWarnings("unchecked")
    public boolean addFunctions(Function function) {
        return this.functions.add(function);
    }



    /**
     * Setter of the property <tt>functions</tt>.
     * 
     * @param functions the functions to set.
     * @uml.property name="functions"
     */
    public void setFunctions(List functions) {
        this.functions = functions;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Type name " + name).append("\n").append("    ");
        Function[] f = functionsToArray();

        for (int i = 0; i < f.length; i++) {
            Function function = f[i];
            sb.append(function).append("\n").append("    ");
        }
        return sb.toString();
    }
}
