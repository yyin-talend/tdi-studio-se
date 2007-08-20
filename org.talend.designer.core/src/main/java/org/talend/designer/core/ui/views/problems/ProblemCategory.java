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
package org.talend.designer.core.ui.views.problems;

import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.views.markers.internal.MarkerMessages;
import org.talend.core.model.process.Problem;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ProblemCategory extends Problem {

    ProblemList problems;

    public ProblemCategory(ProblemList problems) {
        this.problems = problems;
    }

    /**
     * Getter for problems.
     * 
     * @return the problems
     */
    public ProblemList getProblems() {
        return this.problems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.Problem#isConcrete()
     */
    @Override
    public boolean isConcrete() {
        return false;
    }

    public String name;

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.Problem#getProblemName()
     */
    @Override
    public String getProblemResource() {
        return Problem.EMPTY_STRING;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.Problem#getDescription()
     */
    @Override
    public String getDescription() {
        int size = getChildren().length;
        if (size == 1) {
            return NLS.bind(MarkerMessages.Category_One_Item_Label, new Object[] { name });
        }
        return NLS.bind(MarkerMessages.Category_Label, new Object[] { name, String.valueOf(size) });
    }
}
