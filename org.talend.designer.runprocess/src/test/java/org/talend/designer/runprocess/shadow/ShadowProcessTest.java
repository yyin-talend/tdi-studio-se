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
package org.talend.designer.runprocess.shadow;

import junit.framework.TestCase;

import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.runprocess.ProcessorException;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ShadowProcessTest extends TestCase {

    private static final int FIELDS_COUNT = 3;

    private static final int ROW_COUNT = 5;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        Project project = new Project("ShadowProcessTest");
        project.setLanguage(ECodeLanguage.PERL);

        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY);
        repositoryContext.setProject(project);
        super.setUp();
    }

    /**
     * Test method for {@link org.talend.designer.runprocess.shadow.ShadowProcess#run()}.
     */
    @SuppressWarnings("unchecked")
    public void testRun() {

        System.out.print("Waiting for JET to initialize.");
        // CodeGeneratorPlugin.getDefault().initialize(ECodeLanguage.PERL);
        // synchronized(this) {
        // try {
        // for (int i = 0; i < 36; i++) {
        // wait(1000);
        // System.out.print(".");
        // }
        // }
        // catch (InterruptedException ie) {
        // // Do nohting
        // }
        // }
        System.out.println("\nJet is normally up.");

        try {
//            ProcessDescription desc = new ProcessDescription();
//            desc.setFilepath("D:\\tlData\\data1.csv");
//            desc.setRowSeparator("\"\\n\"");
//            desc.setFieldSeparator("';'");
//
//            ShadowProcess shadowProcess = new ShadowProcess(desc, EShadowProcessType.FILE_DELIMITED);
//            XmlArray array = shadowProcess.run();
//            assertNotNull("No XML generated.", array);
//            assertEquals("Wrong row count.", array.getRows().size(), ROW_COUNT);
//            assertEquals("Wrong col count.", array.getRows().get(0).getFields().size(), FIELDS_COUNT);
        } catch (Exception pe) {
            System.out.println("Be carefull of preferences : PERL interpreter.");

            pe.printStackTrace();
            fail(pe.getMessage());
        }
    }

}
