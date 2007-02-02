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
package org.talend.designer.rowgenerator.utils.problems;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.Problem;
import org.talend.designer.rowgenerator.external.data.ExternalRowGeneratorData;
import org.talend.designer.rowgenerator.managers.RowGeneratorManager;

/**
 *  qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ProblemsAnalyser.java,v 1.5 2007/01/31 05:20:51 pub Exp $
 * 
 */
public class ProblemsAnalyser {

    private RowGeneratorManager generatorManager;

    private List<Problem> problems = new ArrayList<Problem>();

    /**
     *  qzhang ProblemsAnalyser constructor comment.
     */
    public ProblemsAnalyser(RowGeneratorManager generatorManager) {
        super();
        this.generatorManager = generatorManager;
    }

    public List<Problem> checkProblems(ExternalRowGeneratorData externalData) {
        problems.clear();
        return getProblems();

    }

    /**
     *  qzhang Comment method "getProblems".
     * 
     * @return
     */
    private List<Problem> getProblems() {
        // TODO Auto-generated method stub
        return new ArrayList<Problem>();
    }

    
    public RowGeneratorManager getGeneratorManager() {
        return this.generatorManager;
    }
}
