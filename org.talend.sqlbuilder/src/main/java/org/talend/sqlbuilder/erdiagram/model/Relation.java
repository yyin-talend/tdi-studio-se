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
package org.talend.sqlbuilder.erdiagram.model;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: Relation.java 1 2006-12-25 下午02:57:25 +0000 (ææäº, 29 ä¹æ 2006) yzhang $
 * 
 */
public class Relation extends ErElement {

    private static final String PROP_SOURCE = "source";

    private static final String PROP_TARGET = "target";

    private Column source;

    private Column target;

    /**
     * DOC yzhang Relation constructor comment.
     */
    public Relation(Column source, Column target) {
        this.source = source;
        this.target = target;
        source.addOutput(this);
        target.addInput(this);
    }

    /**
     * DOC yzhang Comment method "getSource".
     * 
     * @return
     */
    public Column getSource() {
        return this.source;
    }

    /**
     * DOC yzhang Comment method "setSource".
     * 
     * @param source
     */
    public void setSource(Column source) {
        this.source = source;
        fireStructureChange(PROP_SOURCE, source);

    }

    /**
     * DOC yzhang Comment method "getTarget".
     * 
     * @return
     */
    public Column getTarget() {
        return this.target;
    }

    /**
     * DOC yzhang Comment method "setTarget".
     * 
     * @param target
     */
    public void setTarget(Column target) {
        this.target = target;
        fireStructureChange(PROP_TARGET, source);
    }

}
