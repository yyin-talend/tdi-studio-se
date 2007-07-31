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
package org.talend.expressionbuilder.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.CorePlugin;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.JavaFunctionParser;
import org.talend.expressionbuilder.test.shadow.Variable;
import org.talend.expressionbuilder.ui.CategoryComposite;
import org.talend.expressionbuilder.ui.ExpressionBuilderDialog;
import org.talend.expressionbuilder.ui.ExpressionComposite;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: JavaTestShadow.java 下午04:33:49 2007-6-29 +0000 (2007-6-29) yzhang $
 * 
 */
public class JavaTestShadow {

    private Thread serverThread;

    private ExpressionTestServer server;

    public void process(Text testResultText, TableViewer tableViewer) {

        Map<String, String> map = JavaFunctionParser.getTypePackgeMethods();

        Function function = CategoryComposite.getSelectedFunction();

        String expression = ExpressionBuilderDialog.getExpressionComposite().getExpression();

        if (function != null && expression != null && !"".equals(expression)) {

            if (serverThread == null || !serverThread.isAlive()) {
                server = ExpressionTestServer.getInstance(Display.getCurrent(), testResultText);
                serverThread = new Thread(server);
                serverThread.start();
            }

            String methodFullName = map.get(function.getTalendType().getName() + "." + function.getName());
            String[] fullClassAndMethod = methodFullName.split("\\.");

            String methodName = fullClassAndMethod[fullClassAndMethod.length - 1];

            expression = expression.replaceAll(methodName, methodFullName);

            ExpressionArguments arguments = new ExpressionArguments();
            arguments.setExpression(expression);
            arguments.setVariables((List<Variable>) tableViewer.getInput());

            ExpressionGenerator codeGenerator = new ExpressionGenerator();
            String fileContent = codeGenerator.generate(arguments);

            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IProject project = workspace.getRoot().getProject(JavaUtils.JAVA_PROJECT_NAME);

            IFile file = project.getFile(new Path("src/routines/ExpressionVariableTest.java"));
            IFile classFile = project.getFile(new Path("classes/routines/ExpressionVariableTest.class"));

            InputStream in = new ByteArrayInputStream(fileContent.getBytes());
            try {
                if (!file.exists()) {
                    file.create(in, true, null);
                } else {
                    file.delete(true, null);
                    classFile.delete(true, null);
                    file.create(in, true, null);
                }
                while (!classFile.exists()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        RuntimeExceptionHandler.process(e1);
                    }
                }
            } catch (CoreException e1) {
                RuntimeExceptionHandler.process(e1);
            }

            try {
                in.close();
                in = null;
            } catch (IOException e2) {
                RuntimeExceptionHandler.process(e2);
            }

            String javaInterpreter = CorePlugin.getDefault().getPreferenceStore().getString(
                    ITalendCorePrefConstants.JAVA_INTERPRETER);

            IWorkspaceRoot workSpaceRoot = ResourcesPlugin.getWorkspace().getRoot();
            IResource javaProject = workSpaceRoot.findMember(".Java");
            IPath path = javaProject.getLocation().append("classes");

            String[] str = new String[] { javaInterpreter, "-cp", path.toOSString(), "routines.ExpressionVariableTest" };

            InputStreamReader reader = null;

            try {
                Process exec = Runtime.getRuntime().exec(str);
                reader = new InputStreamReader(exec.getErrorStream());

                char[] buffer = new char[1024];
                int i = 0;
                StringBuffer bufferString = new StringBuffer();

                while ((i = reader.read(buffer)) != -1) {
                    bufferString.append(buffer, 0, i);
                }

                reader.close();
                if (bufferString.length() > 0) {
                    testResultText.setText(bufferString.toString());
                }
            } catch (IOException e1) {
                RuntimeExceptionHandler.process(e1);
            } finally {
                reader = null;
            }
        }
    }

    public void stop() {
        if (server != null && !server.stopped()) {
            server.stop();
        }
    }
}
