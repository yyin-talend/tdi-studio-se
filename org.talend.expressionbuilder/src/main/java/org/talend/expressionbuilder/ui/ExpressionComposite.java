package org.talend.expressionbuilder.ui;

import java.util.List;

import org.eclipse.swt.SWT;
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
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.data.JavaFunctionParser;
import org.talend.designer.rowgenerator.data.Parameter;

public class ExpressionComposite extends Composite implements ExpressionController {

    private static Text text;

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
        expressionGroup.setLayout(new GridLayout());
        expressionGroup.setText("Expression");

        final Composite upperOperationButtonBar = new Composite(expressionGroup, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 8;
        gridLayout.numColumns = 3;
        upperOperationButtonBar.setLayout(gridLayout);
        upperOperationButtonBar.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
        upperOperationButtonBar.setData("nsd", null);

        final Button undoButton = new Button(upperOperationButtonBar, SWT.NONE);
        undoButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        undoButton.setText("Undo(Ctrl + Z)");

        final Button wrapButton = new Button(upperOperationButtonBar, SWT.NONE);
        wrapButton.setText("Wrap");

        final Button clearButton = new Button(upperOperationButtonBar, SWT.NONE);
        clearButton.setText("Clear");

        text = new Text(expressionGroup, SWT.V_SCROLL | SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Composite lowerOperationButtonBar = new Composite(expressionGroup, SWT.NONE);
        final GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        lowerOperationButtonBar.setLayoutData(gridData);
        lowerOperationButtonBar.setLayout(new RowLayout(SWT.HORIZONTAL));

        final Button plusButton = new Button(lowerOperationButtonBar, SWT.NONE);
        plusButton.setText("+");

        final Button minusButton = new Button(lowerOperationButtonBar, SWT.NONE);
        minusButton.setText("-");

        final Button multiplyButton = new Button(lowerOperationButtonBar, SWT.NONE);
        multiplyButton.setText("*");

        final Button divideButton = new Button(lowerOperationButtonBar, SWT.NONE);
        divideButton.setText("/");

        final Label label = new Label(lowerOperationButtonBar, SWT.NONE);
        final RowData rowData = new RowData();
        rowData.width = 20;
        label.setLayoutData(rowData);

        final Button eqButton = new Button(lowerOperationButtonBar, SWT.NONE);
        eqButton.setText("==");

        final Button gButton = new Button(lowerOperationButtonBar, SWT.NONE);
        gButton.setText(">");

        final Button gebutton = new Button(lowerOperationButtonBar, SWT.NONE);
        gebutton.setText(">=");

        final Button nebutton = new Button(lowerOperationButtonBar, SWT.NONE);
        nebutton.setText("<>");

        final Button lebutton = new Button(lowerOperationButtonBar, SWT.NONE);
        lebutton.setText("<=");

        final Button lButton = new Button(lowerOperationButtonBar, SWT.NONE);
        lButton.setText("<");

        final Label label_1 = new Label(lowerOperationButtonBar, SWT.NONE);
        final RowData rowData_1 = new RowData();
        rowData_1.width = 20;
        label_1.setLayoutData(rowData_1);

        final Button andButton = new Button(lowerOperationButtonBar, SWT.NONE);
        andButton.setText("and");

        final Button orButton = new Button(lowerOperationButtonBar, SWT.NONE);
        orButton.setText("or");

        final Button notButton = new Button(lowerOperationButtonBar, SWT.NONE);
        notButton.setText("not");

        final Label label_2 = new Label(lowerOperationButtonBar, SWT.NONE);
        final RowData rowData_2 = new RowData();
        rowData_2.width = 20;
        label_2.setLayoutData(rowData_2);

        final Button button_9 = new Button(lowerOperationButtonBar, SWT.NONE);
        button_9.setText("(");

        final Button rightBracketbutton = new Button(lowerOperationButtonBar, SWT.NONE);
        rightBracketbutton.setText(")");
    }

    public static final String PERL_FUN_PREFIX = "sub{";

    public static final String PERL_FUN_SUFFIX = ")}";

    public static final String FUN_PARAM_SEPARATED = ",";

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.ui.ExpressionController#setExpression(org.talend.designer.rowgenerator.data.Function)
     */
    public void setExpression(Function f) {
        String newValue = PERL_FUN_PREFIX;
        if (f != null) {

            final List<Parameter> parameters = (List<Parameter>) f.getParameters();
            if (FunctionManager.isJavaProject()) {
                String fullName = f.getName();
                newValue = fullName + "(";
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.ui.ExpressionController#getExpression()
     */
    public static String getExpression() {
        if (text != null) {
            return text.getText();
        }
        return null;
    }
}
