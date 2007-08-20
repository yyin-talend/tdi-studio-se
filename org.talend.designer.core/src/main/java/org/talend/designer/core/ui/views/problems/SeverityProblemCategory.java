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

import org.talend.core.model.process.Problem;

/**
 * This is subclass of ProblemCategory.
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class SeverityProblemCategory extends ProblemCategory {

    ProblemStatus status;

    /**
     * bqian SeverityProblemCategory constructor comment.
     * 
     * @param problems
     * @param group
     */
    public SeverityProblemCategory(ProblemList problems, ProblemStatus status) {
        super(problems);
        this.status = status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.Problem#getChildren()
     */
    @Override
    public Problem[] getChildren() {
        return getProblems().getProblemsBySeverity(status).toArray(new Problem[0]);
    }

}
