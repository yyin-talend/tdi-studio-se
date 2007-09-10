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
package org.talend.expressionbuilder.ui;

import java.util.List;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.data.Parameter;
import org.talend.expressionbuilder.i18n.Messages;
import org.talend.expressionbuilder.ui.proposal.ExpressionBuilderProposalProvider;
import org.talend.expressionbuilder.ui.proposal.ExpressionBuilderTextContentAdapter;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionComposite.java 上午10:12:38 2007-7-24 +0000 (2007-7-24) yzhang $
 * 
 */
public class ExpressionComposite extends Composite {

    private final Text text;

    private String replacedText;

    /**
     * yzhang ExpressionComposite class global comment. Detailled comment <br/>
     * 
     * $Id: ExpressionComposite.java 下午04:23:35 2007-8-8 +0000 (2007-8-8) yzhang $
     * 
     */
    class ButtonListener extends MouseAdapter {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
         */
        @Override
        public void mouseUp(MouseEvent e) {
            if (e.getSource() instanceof Button) {
                Button button = (Button) e.getSource();
                String buttonType = button.getText();
                if (buttonType.equals("+")) { //$NON-NLS-1$
                    text.insert("+"); //$NON-NLS-1$
                } else if (buttonType.equals("-")) { //$NON-NLS-1$
                    text.insert("-"); //$NON-NLS-1$
                } else if (buttonType.equals("*")) { //$NON-NLS-1$
                    text.insert("*"); //$NON-NLS-1$
                } else if (buttonType.equals("/")) { //$NON-NLS-1$
                    text.insert("/"); //$NON-NLS-1$
                } else if (buttonType.equals("==")) { //$NON-NLS-1$
                    text.insert("=="); //$NON-NLS-1$
                } else if (buttonType.equals(">")) { //$NON-NLS-1$
                    text.insert("<"); //$NON-NLS-1$
                } else if (buttonType.equals("<")) { //$NON-NLS-1$
                    text.insert(">"); //$NON-NLS-1$
                } else if (buttonType.equals("<>")) { //$NON-NLS-1$
                    text.insert("<>"); //$NON-NLS-1$
                } else if (buttonType.equals(">=")) { //$NON-NLS-1$
                    text.insert(">="); //$NON-NLS-1$
                } else if (buttonType.equals("<=")) { //$NON-NLS-1$
                    text.insert("<="); //$NON-NLS-1$
                } else if (buttonType.equals("not")) { //$NON-NLS-1$
                    text.insert("!"); //$NON-NLS-1$
                } else if (buttonType.equals("and")) { //$NON-NLS-1$
                    text.insert("&&"); //$NON-NLS-1$
                } else if (buttonType.equals("or")) { //$NON-NLS-1$
                    text.insert("||"); //$NON-NLS-1$
                } else if (buttonType.equals("(")) { //$NON-NLS-1$
                    text.insert("("); //$NON-NLS-1$
                } else if (buttonType.equals(")")) { //$NON-NLS-1$
                    text.insert(")"); //$NON-NLS-1$
                }

            }
        }
    }

    /**
     * Create the composite
     * 
     * @param parent
     * @param style
     */
    public ExpressionComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new FillLayout());

        final Group expressionGroup = new Group(this, SWT.NONE);
        GridLayout groupLayout = new GridLayout();
        expressionGroup.setLayout(groupLayout);
        expressionGroup.setText(Messages.getString("ExpressionComposite.expression")); //$NON-NLS-1$

        final Composite upperOperationButtonBar = new Composite(expressionGroup, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 8;
        gridLayout.numColumns = 3;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        upperOperationButtonBar.setLayout(gridLayout);
        upperOperationButtonBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));
        upperOperationButtonBar.setData("nsd", null); //$NON-NLS-1$

        // final Button undoButton = new Button(upperOperationButtonBar, SWT.NONE);
        // undoButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        // undoButton.setText("Undo(Ctrl + Z)");
        //
        // final Button wrapButton = new Button(upperOperationButtonBar, SWT.NONE);
        // wrapButton.setText("Wrap");

        final Button clearButton = new Button(upperOperationButtonBar, SWT.NONE);
        clearButton.setText(Messages.getString("ExpressionComposite.clear")); //$NON-NLS-1$

        clearButton.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                text.setText(""); //$NON-NLS-1$
            }
        });

        text = new Text(expressionGroup, SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Composite lowerOperationButtonBar = new Composite(expressionGroup, SWT.NONE);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        lowerOperationButtonBar.setLayoutData(gridData);
        lowerOperationButtonBar.setLayout(new RowLayout(SWT.HORIZONTAL));

        ButtonListener buttonListener = new ButtonListener();
        final Button plusButton = new Button(lowerOperationButtonBar, SWT.NONE);
        plusButton.setText("+"); //$NON-NLS-1$
        plusButton.addMouseListener(buttonListener);

        final Button minusButton = new Button(lowerOperationButtonBar, SWT.NONE);
        minusButton.setText("-"); //$NON-NLS-1$
        minusButton.addMouseListener(buttonListener);

        final Button multiplyButton = new Button(lowerOperationButtonBar, SWT.NONE);
        multiplyButton.setText("*"); //$NON-NLS-1$
        multiplyButton.addMouseListener(buttonListener);

        final Button divideButton = new Button(lowerOperationButtonBar, SWT.NONE);
        divideButton.setText("/"); //$NON-NLS-1$
        divideButton.addMouseListener(buttonListener);

        final Label label = new Label(lowerOperationButtonBar, SWT.NONE);
        final RowData rowData = new RowData();
        rowData.width = 20;
        label.setLayoutData(rowData);

        final Button eqButton = new Button(lowerOperationButtonBar, SWT.NONE);
        eqButton.setText("=="); //$NON-NLS-1$
        eqButton.addMouseListener(buttonListener);

        final Button gButton = new Button(lowerOperationButtonBar, SWT.NONE);
        gButton.setText(">"); //$NON-NLS-1$
        gButton.addMouseListener(buttonListener);

        final Button gebutton = new Button(lowerOperationButtonBar, SWT.NONE);
        gebutton.setText(">="); //$NON-NLS-1$
        gebutton.addMouseListener(buttonListener);

        final Button nebutton = new Button(lowerOperationButtonBar, SWT.NONE);
        nebutton.setText("<>"); //$NON-NLS-1$
        nebutton.addMouseListener(buttonListener);

        final Button lebutton = new Button(lowerOperationButtonBar, SWT.NONE);
        lebutton.setText("<="); //$NON-NLS-1$
        lebutton.addMouseListener(buttonListener);

        final Button lButton = new Button(lowerOperationButtonBar, SWT.NONE);
        lButton.setText("<"); //$NON-NLS-1$
        lButton.addMouseListener(buttonListener);

        final Label label1 = new Label(lowerOperationButtonBar, SWT.NONE);
        final RowData rowData1 = new RowData();
        rowData1.width = 20;
        label1.setLayoutData(rowData1);

        final Button andButton = new Button(lowerOperationButtonBar, SWT.NONE);
        andButton.setText("and"); //$NON-NLS-1$
        andButton.addMouseListener(buttonListener);

        final Button orButton = new Button(lowerOperationButtonBar, SWT.NONE);
        orButton.setText("or"); //$NON-NLS-1$
        orButton.addMouseListener(buttonListener);

        final Button notButton = new Button(lowerOperationButtonBar, SWT.NONE);
        notButton.setText("not"); //$NON-NLS-1$
        notButton.addMouseListener(buttonListener);

        final Label label2 = new Label(lowerOperationButtonBar, SWT.NONE);
        final RowData rowData2 = new RowData();
        rowData2.width = 20;
        label2.setLayoutData(rowData2);

        final Button leftBracketButton = new Button(lowerOperationButtonBar, SWT.NONE);
        leftBracketButton.setText("("); //$NON-NLS-1$
        leftBracketButton.addMouseListener(buttonListener);

        final Button rightBracketbutton = new Button(lowerOperationButtonBar, SWT.NONE);
        rightBracketbutton.setText(")"); //$NON-NLS-1$
        rightBracketbutton.addMouseListener(buttonListener);

    }

    public static final String PERL_FUN_PREFIX = "sub{"; //$NON-NLS-1$

    public static final String PERL_FUN_SUFFIX = ")}"; //$NON-NLS-1$

    public static final String FUN_PARAM_SEPARATED = ","; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.ui.ExpressionController#setExpression(org.talend.designer.rowgenerator.data.Function)
     */
    public void setExpression(Function f) {
        String newValue = PERL_FUN_PREFIX;
        if (f != null) {

            final List<Parameter> parameters = f.getParameters();
            if (FunctionManager.isJavaProject()) {
                String fullName = f.getName();
                newValue = fullName + "("; //$NON-NLS-1$
                for (Parameter pa : parameters) {
                    newValue += pa.getValue() + FUN_PARAM_SEPARATED; //$NON-NLS-1$
                }
                if (!parameters.isEmpty()) {
                    newValue = newValue.substring(0, newValue.length() - 1);
                }
                newValue += ")"; //$NON-NLS-1$

            } else {
                newValue += f.getName() + "("; //$NON-NLS-1$
                for (Parameter pa : parameters) {
                    newValue += pa.getValue() + FUN_PARAM_SEPARATED; //$NON-NLS-1$
                }
                newValue = newValue.substring(0, newValue.length() - 1);
                newValue += PERL_FUN_SUFFIX; //$NON-NLS-1$
            }
        }
        text.setText(newValue);
    }

    /**
     * yzhang Comment method "getExpression".
     * 
     * @return
     */
    public String getExpression() {
        if (text != null) {
            return text.getText();
        }
        return null;
    }

    /**
     * yzhang Comment method "setExpression".
     * 
     * @param expression
     */
    public void setExpression(String expression, boolean append) {
        if (append) {
            text.insert(expression);
        } else {
            text.setText(expression);
        }
    }

    /**
     * yzhang Comment method "setPropersoal".
     */
    public void configProposal() {
        try {
            KeyStroke stroke = KeyStroke.getInstance("Ctrl+Space"); //$NON-NLS-1$
            IControlContentAdapter contorlContentAdapter = new ExpressionBuilderTextContentAdapter();
            ExpressionBuilderProposalProvider contentProposalProvider = new ExpressionBuilderProposalProvider();
            ContentProposalAdapter proposal = new ContentProposalAdapter(text, contorlContentAdapter,
                    contentProposalProvider, stroke, new char[] { '+', '.' });

        } catch (ParseException e) {
            RuntimeExceptionHandler.process(e);
        }

    }

    /**
     * Sets the replacedText.
     * 
     * @param replacedText the replacedText to set
     */
    public void setReplacedText(String replacedText) {
        this.replacedText = replacedText;
    }

    /**
     * yzhang Comment method "replacedContent".
     * 
     * @param content
     * @param position
     */
    public void replacedContent(String content, Point position) {
        if (replacedText.startsWith("*")) { //$NON-NLS-1$
            text.setSelection(position.x, position.x);
        } else {
            text.setSelection(position.x - replacedText.length(), position.x);
        }
        text.insert(content);
    }
}
