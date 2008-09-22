// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.palette;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.PaletteViewerKeyHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.components.ComponentUtilities;

/**
 * 
 */
public class TalendPaletteViewer extends PaletteViewer {

    private static final String TOOL_TIP = "Enter component prefix or pattern (*, ?)";

    private static final String SEARCH_COMPONENT = "search component...";

    private static List<Text> filters = new ArrayList<Text>();

    private static Text setTextOnly;

    private static String currentFilterText;

    public TalendPaletteViewer(EditDomain graphicalViewerDomain) {
        setEditDomain(graphicalViewerDomain);
        setKeyHandler(new PaletteViewerKeyHandler(this));
        setEditPartFactory(new TalendPaletteEditPartFactory());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.ScrollingGraphicalViewer#creatToolControl(org.eclipse.swt.widgets.Composite)
     */
    public Control creatToolControl(Composite parent) {
        Text text = new Text(parent, SWT.BORDER);
        filters.add(text);
        initFilterTextControl(text);
        return text;
    }

    /**
     * yzhang Comment method "initFilterTextControl".
     * 
     * @param control
     */
    private void initFilterTextControl(Text filterText) {
        if (currentFilterText != null) {
            filterText.setText(currentFilterText);
        } else {
            filterText.setText(SEARCH_COMPONENT);
        }

        filterText.setToolTipText(TOOL_TIP);
        configListeners(filterText);

    }

    /**
     * yzhang Comment method "configListeners".
     * 
     * @param text
     */
    private void configListeners(final Text text) {
        text.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {

            }

            public void mouseDown(MouseEvent e) {
                text.selectAll();
            }

            public void mouseUp(MouseEvent e) {

            }

        });

        text.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {

            }

            public void focusLost(FocusEvent e) {
                if (text.getText() == "") {
                    text.setText(SEARCH_COMPONENT);
                }

            }

        });

        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (setTextOnly == text) {
                    return;
                }

                List<Text> disposed = new ArrayList<Text>();
                for (Text otherText : filters) {

                    if (text == otherText) {
                        continue;
                    }

                    if (otherText.isDisposed()) {
                        disposed.add(otherText);
                        continue;
                    }
                    setTextOnly = otherText;
                    otherText.setText(text.getText());
                    setTextOnly = null;
                }

                currentFilterText = text.getText();
                if (!currentFilterText.equals(SEARCH_COMPONENT)) {
                    ComponentUtilities.filterPalette(currentFilterText.trim());
                }

                filters.removeAll(disposed);
            }

        });
    }
}
