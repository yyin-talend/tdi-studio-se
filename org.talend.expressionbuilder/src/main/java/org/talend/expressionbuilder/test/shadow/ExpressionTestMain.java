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
package org.talend.expressionbuilder.test.shadow;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.designer.rowgenerator.RowGeneratorComponent;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.managers.UIManager;
import org.talend.designer.rowgenerator.shadow.LogRowNode;
import org.talend.designer.rowgenerator.shadow.RowGenProcess;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.expressionbuilder.ui.ExpressionBuilderDialog;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: TestProcess.java 下午02:58:17 2007-7-17 +0000 (2007-7-17) yzhang $
 * 
 */
public class ExpressionTestMain {

    public final static String EXPRESSION_BUILDER = "Expression_builder"; //$NON-NLS-1$

    private IProcess expressionBuilderTestProcess;

    private final StyledText text;

    private Process process;

    /**
     * yzhang TestProcess constructor comment.
     */
    public ExpressionTestMain(Function function, StyledText resultText) {
        text = resultText;

        if (function != null) {
            IContext context2 = new org.talend.core.model.context.JobContext(EXPRESSION_BUILDER);

            List<IContextParameter> params = new ArrayList<IContextParameter>();
            JobContextParameter contextParameter = new JobContextParameter();
            contextParameter.setName(EXPRESSION_BUILDER);
            contextParameter.setValue(EXPRESSION_BUILDER);
            contextParameter.setType("String"); //$NON-NLS-1$
            params.add(contextParameter);
            context2.setContextParameterList(params);

            if (UIManager.isJavaProject()) {

                Property property = PropertiesFactory.eINSTANCE.createProperty();
                property.setLabel(EXPRESSION_BUILDER + "_RowGenerator2"); //$NON-NLS-1$
                property.setId(EXPRESSION_BUILDER + "_RowGenerator2"); //$NON-NLS-1$

                RowGeneratorComponent rowGeneratorNode = new VirtualRowGeneratorNode(function);
                rowGeneratorNode.setNumber("1"); //$NON-NLS-1$

                expressionBuilderTestProcess = new RowGenProcess(property, rowGeneratorNode);

            } else {
                String expression = ExpressionBuilderDialog.getExpressionComposite().getExpression();
                List<Variable> variables = ExpressionBuilderDialog.getTestComposite().getVariableList();
                for (Variable variable : variables) {
                    expression = expression.replaceAll(variable.getName(), variable.getValue());
                }

                PerlRowNode inNode = new PerlRowNode(expression);
                LogRowNode outNode = new LogRowNode("tLogRow", inNode.getMetadataList().get(0));

                expressionBuilderTestProcess = new AnyIntoLogRowProcess<PerlRowNode>(inNode, outNode);

            }

            startProcess(expressionBuilderTestProcess, context2);
        }

    }

    /**
     * yzhang Comment method "startProcess".
     * 
     * @param p
     * @param context
     */
    private void startProcess(IProcess p, IContext context) {
        IProcessor processor = ProcessorUtilities.getProcessor(p, context);

        try {
            process = processor.run(IProcessor.NO_STATISTICS, IProcessor.NO_TRACES, null);
            int counter = 0;
            while (process == null && counter < 6) {
                try {
                    Thread.sleep(500);
                    counter++;
                } catch (InterruptedException e) {
                    RuntimeExceptionHandler.process(e);
                }
            }
            feedBackTestResult();

        } catch (ProcessorException e) {
            RuntimeExceptionHandler.process(e);
        }
    }

    /**
     * yzhang Comment method "feedBackTestResult".
     */
    private void feedBackTestResult() {

        InputStreamReader reader = new InputStreamReader(process.getInputStream());
        char[] buffer = new char[512];
        int i = 0;
        StringBuffer testResult = new StringBuffer();
        try {
            while ((i = reader.read(buffer)) != -1) {
                testResult.append(String.valueOf(buffer, 0, i));
            }
            text.setText(testResult.toString());

            if (process.getErrorStream().available() > 0) {
                InputStreamReader errorReader = new InputStreamReader(process.getErrorStream());
                while ((i = errorReader.read(buffer)) != -1) {
                    testResult.append(String.valueOf(buffer, 0, i));
                }
                text.setText(testResult.toString());
                Color red = new Color(text.getDisplay(), new RGB(255, 0, 0));
                StyleRange style = new StyleRange(0, testResult.length(), red, null, SWT.NORMAL);
                text.setStyleRange(style);

            }
        } catch (IOException e) {
            RuntimeExceptionHandler.process(e);
        }
    }
}
