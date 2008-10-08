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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.palette.PaletteDrawer;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.utils.threading.ExecutionLimiter;
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

    private ThreadPoolExecutor executor;

    private final ExecutionLimiter expandLimiter = new ExecutionLimiter(500, true) {

        @Override
        public void execute(final boolean isFinalExecution, Object data) {
            final Text text = (Text) data;
            text.getDisplay().asyncExec(new Runnable() {

                public void run() {
                    ExpandPaletteRunnable runnable = (ExpandPaletteRunnable) executor.getQueue().poll();
                    if (runnable != null) {
                        runnable.stopExpand();
                    }
                    filterPalette(text);
                    executor.execute(new ExpandPaletteRunnable());
                }
            });
        }
    };

    public TalendPaletteViewer(EditDomain graphicalViewerDomain) {
        setEditDomain(graphicalViewerDomain);
        setKeyHandler(new PaletteViewerKeyHandler(this));
        setEditPartFactory(new TalendPaletteEditPartFactory());
        executor = new ThreadPoolExecutor(1, 2, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3));
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
                if (text.getText().equals(SEARCH_COMPONENT)) {
                    text.setText("");
                }
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
                expandLimiter.resetTimer();
                expandLimiter.startIfExecutable(true, text);
            }

        });
    }

    /**
     * yzhang TalendPaletteViewer class global comment. Detailled comment
     */
    private class ExpandPaletteRunnable implements Runnable {

        private volatile boolean stop;

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            Display display = Display.getDefault();
            if (display == null) {
                return;
            }
            display.syncExec(new Runnable() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.lang.Runnable#run()
                 */
                public void run() {
                    List children = ComponentUtilities.getPaletteRoot().getChildren();
                    int counter = 0;
                    for (Object obj : children) {
                        if (stop || counter > 3) {
                            return;
                        }
                        if (obj instanceof PaletteDrawer) {
                            ((PaletteDrawer) obj).setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
                            counter++;
                            PaletteDrawer subDrawer = getSubDrawer((PaletteDrawer) obj);
                            if (subDrawer != null) {
                                subDrawer.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
                                counter++;
                            }
                        }

                    }
                }
            });

        }

        public void stopExpand() {
            stop = true;
        }
    }

    private PaletteDrawer getSubDrawer(PaletteDrawer parent) {
        for (Object obj : parent.getChildren()) {
            if (obj instanceof PaletteDrawer) {
                return (PaletteDrawer) obj;
            }
        }
        return null;
    }

    private void filterPalette(Text text) {
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
}
