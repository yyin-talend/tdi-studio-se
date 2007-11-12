// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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

        if (function != null && expression != null && !"".equals(expression)) { //$NON-NLS-1$

            if (serverThread == null || !serverThread.isAlive()) {
                server = ExpressionTestServer.getInstance(Display.getCurrent(), testResultText);
                serverThread = new Thread(server);
                serverThread.start();
            }

            String methodFullName = map.get(function.getTalendType().getName() + "." + function.getName()); //$NON-NLS-1$
            String[] fullClassAndMethod = methodFullName.split("\\."); //$NON-NLS-1$

            String methodName = fullClassAndMethod[fullClassAndMethod.length - 1];

            expression = expression.replaceAll(methodName, methodFullName);

            ExpressionArguments arguments = new ExpressionArguments();
            arguments.setExpression(expression);
            arguments.setVariables((List<Variable>) tableViewer.getInput());

            ExpressionGenerator codeGenerator = new ExpressionGenerator();
            String fileContent = codeGenerator.generate(arguments);

            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IProject project = workspace.getRoot().getProject(JavaUtils.JAVA_PROJECT_NAME);

            IFile file = project.getFile(new Path("src/routines/ExpressionVariableTest.java")); //$NON-NLS-1$
            IFile classFile = project.getFile(new Path("classes/routines/ExpressionVariableTest.class")); //$NON-NLS-1$

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
            IResource javaProject = workSpaceRoot.findMember(".Java"); //$NON-NLS-1$
            IPath path = javaProject.getLocation().append("classes"); //$NON-NLS-1$

            String[] str = new String[] { javaInterpreter, "-cp", path.toOSString(), "routines.ExpressionVariableTest" }; //$NON-NLS-1$ //$NON-NLS-2$

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
