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
package org.talend.designer.core.ui.editor.connections;

import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.talend.commons.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.commons.utils.workbench.preferences.GlobalConstant;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ConnectionTraceFigure extends Figure {

    private static final String FIELD_SEP = "|"; //$NON-NLS-1$

    private static final String FIELD_EQUAL = "="; //$NON-NLS-1$

    private static final Color BACKGROUND = new Color(null, 220, 220, 220);

    private static final int MAX_VARIABLE_WIDTH = 70;

    private static final int MAX_VALUE_WIDTH = 100;

    private Connection connection;

    private boolean maximized;

    private ConnectionTraceFigure tooltip = null;

    public ConnectionTraceFigure(Connection connection, boolean maximized) {
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);
        // setBorder(new SimpleRaisedBorder());
        this.connection = connection;
        this.maximized = maximized;
        if (maximized) {
            tooltip = new ConnectionTraceFigure(connection, false);
            this.setToolTip(tooltip);
            tooltip.setVisible(false);
        }
    }

    @Override
    public void paint(Graphics graphics) {
        // see bug 2074
        if (GlobalConstant.generatingScreenShoot) {
            return;
        }
        super.paint(graphics);
    }

    public void setTraceData(String data) {
        if (data != null) {
            List childrens = this.getChildren();
            childrens.clear();
            boolean noVarNameDefined = false;

            int sepIndex = data.indexOf(FIELD_SEP); // index separator for row name

            String dataWithoutRowName = data.substring(sepIndex + 1);
            sepIndex = dataWithoutRowName.indexOf(FIELD_SEP);
            String lineNumber = dataWithoutRowName.substring(0, sepIndex);
            SimpleHtmlFigure titleFigure;
            titleFigure = new SimpleHtmlFigure();
            titleFigure.setText(""); //$NON-NLS-1$
            titleFigure.setText("<font color='#0000FF'> <b> " + connection.getConnectionLabel().getLabelText() //$NON-NLS-1$
                    + "</b></font>            <font color='#808080'>Current row : " + lineNumber + "</font>"); //$NON-NLS-1$ //$NON-NLS-2$
            if (tooltip != null) {
                titleFigure.setBackgroundColor(ColorConstants.white);
                titleFigure.setOpaque(true);
                titleFigure.setBorder(new LineBorder(ColorConstants.darkGray, SWT.LEFT | SWT.RIGHT | SWT.TOP | SWT.BOTTOM));
            }
            titleFigure.getPreferredSize().expand(0, 3);
            add(titleFigure);
            Dimension size = titleFigure.getPreferredSize().getCopy();

            int variableWidth = 0;
            int valueWidth = 0;
            String lineInfo = dataWithoutRowName.substring(sepIndex + 1);
            StringTokenizer st = new StringTokenizer(lineInfo, FIELD_SEP);
            while (st.hasMoreTokens()) {
                String str = st.nextToken();
                int valueStart = str.indexOf(FIELD_EQUAL);
                if (valueStart != -1) {
                    String formatedVariable = "<font color='#000000'>  <b>" + str.substring(0, valueStart) //$NON-NLS-1$
                            + "</b></font>"; //$NON-NLS-1$
                    String formatedValue = "<font color='#FF8040'> <b>" + str.substring(valueStart + 1) + "</b></font>"; //$NON-NLS-1$ //$NON-NLS-2$
                    SimpleHtmlFigure var = new SimpleHtmlFigure();
                    var.setText(formatedVariable);
                    SimpleHtmlFigure value = new SimpleHtmlFigure();
                    value.setText(formatedValue);
                    Dimension varSize = var.getPreferredSize();
                    varSize.expand(0, 3);
                    var.setPreferredSize(varSize);
                    if (varSize.width > variableWidth) {
                        variableWidth = varSize.width;
                    }
                    Dimension valueSize = value.getPreferredSize();
                    valueSize.expand(0, 3);
                    value.setPreferredSize(valueSize);
                    if (valueSize.width > valueWidth) {
                        valueWidth = valueSize.width;
                    }
                    size.expand(0, varSize.height);
                } else {
                    noVarNameDefined = true;
                    String formatedValue = "<font color='#FF8040'> <b>" + str + "</b></font>"; //$NON-NLS-1$ //$NON-NLS-2$
                    SimpleHtmlFigure value = new SimpleHtmlFigure();
                    value.setText(formatedValue);
                    Dimension valueSize = value.getPreferredSize();
                    if (valueSize.width > valueWidth) {
                        valueWidth = valueSize.width;
                    }
                    size.expand(0, valueSize.height);
                }
            }
            variableWidth += 10;
            valueWidth += 10;

            if (maximized) {
                if (variableWidth > MAX_VARIABLE_WIDTH) {
                    variableWidth = MAX_VARIABLE_WIDTH;
                }

                if (valueWidth > MAX_VALUE_WIDTH) {
                    valueWidth = MAX_VALUE_WIDTH;
                }
            }

            if ((variableWidth + valueWidth) < titleFigure.getPreferredSize().width) {
                valueWidth = titleFigure.getPreferredSize().width - variableWidth;
            }
            if (noVarNameDefined) {
                if (titleFigure.getPreferredSize().width > valueWidth) {
                    valueWidth = titleFigure.getPreferredSize().width;
                }
            }

            st = new StringTokenizer(lineInfo, FIELD_SEP);
            int nbVar = 0;
            Figure variableFigure = null;
            while (st.hasMoreTokens()) {
                String str = st.nextToken();
                int valueStart = str.indexOf(FIELD_EQUAL);
                if (valueStart != -1) {
                    String formatedVariable = "<font color='#000000'>  <b>" + str.substring(0, valueStart) //$NON-NLS-1$
                            + "</b></font>"; //$NON-NLS-1$
                    String formatedValue = "<font color='#FF8040'> <b>" + str.substring(valueStart + 1) + "</b></font>"; //$NON-NLS-1$ //$NON-NLS-2$
                    SimpleHtmlFigure var = new SimpleHtmlFigure();
                    var.setText(formatedVariable);
                    SimpleHtmlFigure value = new SimpleHtmlFigure();
                    value.setText(formatedValue);
                    Dimension valueSize = value.getPreferredSize();
                    valueSize.expand(0, 3);
                    value.setPreferredSize(valueSize);
                    value.setPreferredSize(valueWidth, valueSize.height);
                    var.setBorder(new LineBorder(ColorConstants.darkGray, SWT.RIGHT));
                    Dimension varSize = var.getPreferredSize();
                    varSize.expand(0, 3);
                    var.setPreferredSize(varSize);
                    var.setPreferredSize(variableWidth, varSize.height);

                    ToolbarLayout variableLayout = new ToolbarLayout(true);
                    variableFigure = new Figure();
                    variableFigure.setLayoutManager(variableLayout);
                    variableFigure.add(var);
                    variableFigure.add(value);
                    add(variableFigure);
                } else {
                    String formatedValue = "<font color='#FF8040'> <b> " + str + "</b></font>"; //$NON-NLS-1$ //$NON-NLS-2$
                    SimpleHtmlFigure value = new SimpleHtmlFigure();
                    value.setText(formatedValue);

                    Dimension valueSize = value.getPreferredSize();
                    valueSize.expand(0, 3);
                    value.setPreferredSize(valueSize);
                    value.setPreferredSize(valueWidth, valueSize.height);

                    ToolbarLayout variableLayout = new ToolbarLayout(true);
                    variableFigure = new Figure();
                    variableFigure.setLayoutManager(variableLayout);
                    variableFigure.add(value);

                    add(variableFigure);
                }
                if (tooltip != null) {
                    variableFigure.setBorder(new LineBorder(ColorConstants.darkGray, SWT.LEFT | SWT.RIGHT));
                }
                variableFigure.setOpaque(true);
                if ((nbVar % 2) != 0) {
                    if (tooltip != null) {
                        variableFigure.setBackgroundColor(ColorConstants.white);
                    }
                } else {
                    variableFigure.setBackgroundColor(BACKGROUND);
                }

                nbVar++;
            }
            if (tooltip != null) {
                if (variableFigure != null) {
                    variableFigure.setBorder(new LineBorder(ColorConstants.darkGray, SWT.LEFT | SWT.BOTTOM | SWT.RIGHT));
                }
            }
            if (noVarNameDefined) {
                size.width = valueWidth;
            } else {
                size.width = variableWidth + valueWidth;
            }

            if (size.width < titleFigure.getPreferredSize().width) {
                size.width = titleFigure.getPreferredSize().width;
            }
            size.expand(5, 3);
            setPreferredSize(size);
            setVisible(true);
        } else {
            setPreferredSize(0, 0);
            setVisible(false);
        }
        if (tooltip != null) {
            tooltip.setTraceData(data);
        }
    }

    /**
     * DOC nrousseau ConnectionTraceFigure class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    public class LineBorder extends AbstractBorder {

        private Color color;

        private int orientation;

        private int leftOffset;

        private int rightOffset;

        private int topOffset;

        private int bottomOffset;

        public LineBorder(Color color, int orientation) {
            this.color = color;
            this.orientation = orientation;
            rightOffset = -1;
            bottomOffset = -1;
        }

        public void setLeftOffset(int leftOffset) {
            this.leftOffset = leftOffset;
        }

        public void setRightOffset(int rightOffset) {
            this.rightOffset = rightOffset;
        }

        public void setTopOffset(int topOffset) {
            this.topOffset = topOffset;
        }

        public void setBottomOffset(int bottomOffset) {
            this.bottomOffset = bottomOffset;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
         */
        public Insets getInsets(IFigure figure) {
            return new Insets(0, 0, 0, 0);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics,
         * org.eclipse.draw2d.geometry.Insets)
         */
        public void paint(IFigure figure, Graphics graphics, Insets insets) {
            graphics.setForegroundColor(color);
            if ((orientation & SWT.TOP) != 0) {
                graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft().translate(0, topOffset), tempRect.getTopRight()
                        .translate(0, topOffset));
            }
            if ((orientation & SWT.LEFT) != 0) {
                graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft().translate(leftOffset, 0), tempRect
                        .getBottomLeft().translate(leftOffset, 0));
            }
            if ((orientation & SWT.RIGHT) != 0) {
                graphics.drawLine(getPaintRectangle(figure, insets).getTopRight().translate(rightOffset, 0), tempRect
                        .getBottomRight().translate(rightOffset, 0));
            }
            if ((orientation & SWT.BOTTOM) != 0) {
                graphics.drawLine(getPaintRectangle(figure, insets).getBottomLeft().translate(0, bottomOffset), tempRect
                        .getBottomRight().translate(0, bottomOffset));
            }
        }
    }
}
