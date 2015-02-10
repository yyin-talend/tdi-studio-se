// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.parts.PaletteViewerKeyHandler;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.osgi.service.prefs.BackingStoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.utils.threading.ExecutionLimiter;
import org.talend.core.model.components.IComponent;
import org.talend.core.ui.component.ComponentPaletteUtilities;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.TalendEditorPaletteFactory;
import org.talend.themes.core.elements.stylesettings.TalendPaletteCSSStyleSetting;

/**
 * 
 */
public class TalendPaletteViewer extends PaletteViewer {

    private static final String TOOL_TIP = Messages.getString("TalendPaletteViewer.tooltipValue"); //$NON-NLS-1$

    private static final String SEARCH_COMPONENT = Messages.getString("TalendPaletteViewer.searchComponent"); //$NON-NLS-1$

    private static List<Text> filters = new ArrayList<Text>();

    private static Text setTextOnly;

    private static String currentFilterText;

    private ThreadPoolExecutor executor;

    protected TalendDrawerEditPart favoritesEditPart;

    protected TalendDrawerEditPart recentlyUsedEditPart;

    protected TalendPaletteEditPartFactory paletteEditPartFactory;

    protected TalendPaletteCSSStyleSetting cssStyleSetting;

    private final ExecutionLimiter expandLimiter = new ExecutionLimiter(500, true) {

        @Override
        public void execute(final boolean isFinalExecution, Object data) {
            final Text text = (Text) data;
            text.getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    ExpandPaletteRunnable runnable = (ExpandPaletteRunnable) executor.getQueue().poll();
                    if (runnable != null) {
                        runnable.stopExpand();
                    }
                    filterPalette(text);
                    if (!text.getText().equals("")) { //$NON-NLS-1$
                        executor.execute(new ExpandPaletteRunnable());
                    }
                }
            });
        }
    };

    public TalendPaletteViewer(EditDomain graphicalViewerDomain, TalendPaletteCSSStyleSetting cssStyleSetting) {
        setEditDomain(graphicalViewerDomain);
        setKeyHandler(new PaletteViewerKeyHandler(this));
        this.cssStyleSetting = cssStyleSetting;
        paletteEditPartFactory = new TalendPaletteEditPartFactory(cssStyleSetting);
        setEditPartFactory(paletteEditPartFactory);
        executor = new ThreadPoolExecutor(10, 20, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        this.enableVerticalScrollbar(true);
        setupPreferences();
    }

    private void setupPreferences() {
        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();
        final String firstTimeRunning = "org.talend.designer.core.ui.editor.palette.TalendPaletteViewer.isFirstTimeRunning"; //$NON-NLS-1$
        boolean isFirstTimeRunning = true;
        if (preferenceStore != null) {
            String value = preferenceStore.getString(firstTimeRunning);
            if (StringUtils.isNotEmpty(value)) {
                isFirstTimeRunning = Boolean.valueOf(value);
            }
        }
        if (isFirstTimeRunning) {
            PaletteViewerPreferences paletteViewerPreferences = this.getPaletteViewerPreferences();
            if (paletteViewerPreferences != null) {
                paletteViewerPreferences.setAutoCollapseSetting(PaletteViewerPreferences.COLLAPSE_NEVER);
                paletteViewerPreferences.getAutoCollapseSetting();
                if (preferenceStore != null) {
                    preferenceStore.setValue(firstTimeRunning, Boolean.FALSE.toString());
                    try {
                        InstanceScope.INSTANCE.getNode(DesignerPlugin.ID).flush();
                    } catch (BackingStoreException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
    }

    /**
     * FIXME ggu
     * 
     * Because the method createControl is final in super calss ScrollingGraphicalViewer,
     * 
     * Then, use the reflection function to move to class TalendPaletteViewerProvider.
     */
    // @Override
    // public Control createControl(Composite parent) {
    // FigureCanvas canvas = new TalendFigureCanvas(parent, getLightweightSystem(), this);
    // setControl(canvas);
    // installRootFigure(); // change the parent method to "protected".
    // return canvas;
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.ScrollingGraphicalViewer#creatToolControl(org.eclipse.swt.widgets.Composite)
     */
    public Control creatToolControl(Composite parent) {
        Composite container = new Composite(parent, SWT.BORDER);
        // GridLayout layout = new GridLayout(3, false);
        GridLayout layout = new GridLayout(2, false);
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginTop = 0;
        layout.marginBottom = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        container.setLayout(layout);
        final Text text = new Text(container, SWT.NONE);

        GridData textGridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
        textGridData.horizontalIndent = 0;
        textGridData.verticalIndent = 0;
        text.setLayoutData(textGridData);
        filters.add(text);
        initFilterTextControl(text);

        ToolBar toolbar = new ToolBar(container, SWT.NONE);
        Color searchButtonForgroundColor = cssStyleSetting.getSearchButtonBackgroundColor();
        if (searchButtonForgroundColor != null) {
            toolbar.setBackground(searchButtonForgroundColor);
        }
        GridLayout toolbarLayout = new GridLayout();
        toolbarLayout.marginLeft = 0;
        toolbarLayout.marginRight = 0;
        toolbarLayout.marginTop = 0;
        toolbarLayout.marginBottom = 0;
        toolbarLayout.marginHeight = 0;
        toolbarLayout.marginWidth = 0;
        toolbar.setLayout(toolbarLayout);

        Image findImage = cssStyleSetting.getSearchButtonImage();
        if (findImage == null) {
            findImage = ImageProvider.getImage(EImage.FIND_ICON);
        }

        ToolItem findItem = new ToolItem(toolbar, SWT.NONE);
        findItem.setImage(findImage);
        findItem.setToolTipText("Search");
        findItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                startFiltering(text);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        // Image clearImage = ImageProvider.getImage(ECoreImage.PALETTE_CLEAR_ICON);
        // final ToolItem clearItem = new ToolItem(toolbar, SWT.NONE);
        // clearItem.setImage(clearImage);
        // clearItem.addSelectionListener(new SelectionAdapter() {
        //
        // @Override
        // public void widgetSelected(SelectionEvent e) {
        //                text.setText(""); //$NON-NLS-1$
        // // Reset to default palette
        // startFiltering(text);
        // }
        // });
        return container;
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

            @Override
            public void mouseDoubleClick(MouseEvent e) {

            }

            @Override
            public void mouseDown(MouseEvent e) {
                if (text.getText().equals(SEARCH_COMPONENT)) {
                    text.setText(""); //$NON-NLS-1$
                }
            }

            @Override
            public void mouseUp(MouseEvent e) {

            }
        });

        text.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                //                text.setText(""); //$NON-NLS-1$
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (text.getText() == "") { //$NON-NLS-1$
                    text.setText(SEARCH_COMPONENT);
                }
            }
        });

        text.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                startFiltering(text);
            }

        });
    }

    private void startFiltering(final Text text) {
        String keyword = text.getText();
        refreshRecentlyUsedComponentToReference();
        if (!SEARCH_COMPONENT.equals(keyword)) {
            expandLimiter.resetTimer();
            expandLimiter.startIfExecutable(true, text);
        }
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
        @Override
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
                @Override
                public void run() {
                    List children = ComponentPaletteUtilities.getPaletteRoot().getChildren();
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
            ComponentPaletteUtilities.filterPalette(currentFilterText.trim());
        }

        filters.removeAll(disposed);
    }

    // expose getLightweightSystem to public
    public LightweightSystem getLightweightSys() {
        return getLightweightSystem();
    }

    public void setFigureCanvas(FigureCanvas canvas) {
        setControl(canvas);
        hookRootFigure();
    }

    public void addFavoritesComponent(CombinedTemplateCreationEntry component) {

        if (favoritesEditPart != null) {
            List children = favoritesEditPart.getChildren();
            int insertIndex = 0;
            boolean alreadyExist = false;
            if (children != null) {
                for (; insertIndex < children.size(); insertIndex++) {
                    TalendEntryEditPart entryEditPart = (TalendEntryEditPart) children.get(insertIndex);
                    CombinedTemplateCreationEntry entryModule = (CombinedTemplateCreationEntry) entryEditPart.getModel();
                    int compareResult = entryModule.getLabel().compareTo(component.getLabel());
                    if (0 == compareResult) {
                        alreadyExist = true;
                        break;
                    }
                    if (0 < compareResult) {
                        break;
                    }
                }
            }

            if (!alreadyExist) {
                TalendEditorPaletteFactory.storeFavoritesValue(component.getLabel(), true);
                CombinedTemplateCreationEntry newFavorite = TalendEditorPaletteFactory.createEntryFrom(component);
                newFavorite.setParent(favoritesEditPart.getDrawer());
                EditPart child = favoritesEditPart.createChild(newFavorite);
                PaletteDrawer paletteDrawer = favoritesEditPart.getDrawer();
                if (paletteDrawer != null) {
                    paletteDrawer.add(insertIndex, newFavorite);
                }
                // needn't use this, or will cause problem
                // favoritesEditPart.addChild(child, insertIndex);
            }
        }

    }

    public void removeFavoritesComponent(CombinedTemplateCreationEntry component) {

        if (favoritesEditPart != null) {
            TalendEditorPaletteFactory.storeFavoritesValue(component.getLabel(), false);
            List children = favoritesEditPart.getChildren();
            if (children != null) {
                for (Object obj : children) {
                    TalendEntryEditPart entryEditPart = (TalendEntryEditPart) obj;
                    CombinedTemplateCreationEntry entryModule = (CombinedTemplateCreationEntry) entryEditPart.getModel();
                    if (entryModule.getLabel().equals(component.getLabel())) {
                        PaletteDrawer paletteDrawer = favoritesEditPart.getDrawer();
                        if (paletteDrawer != null) {
                            paletteDrawer.remove(entryModule);
                        }
                        // needn't use this
                        // favoritesEditPart.removeChild(entryEditPart);
                        break;
                    }
                }
            }
        }

    }

    public void addRecentlyUsedComponent(IComponent component) {
        if (recentlyUsedEditPart != null) {
            List children = recentlyUsedEditPart.getChildren();
            int insertIndex = 0;
            boolean alreadyExist = false;
            if (children != null) {
                for (; insertIndex < children.size(); insertIndex++) {
                    TalendEntryEditPart entryEditPart = (TalendEntryEditPart) children.get(insertIndex);
                    CombinedTemplateCreationEntry entryModule = (CombinedTemplateCreationEntry) entryEditPart.getModel();
                    int compareResult = entryModule.getLabel().compareTo(component.getName());
                    if (0 == compareResult) {
                        alreadyExist = true;
                        break;
                    }
                    // if (0 < compareResult) {
                    // break;
                    // }
                }
            }

            // if already exist, remove first
            if (alreadyExist) {
                if (0 == insertIndex) {
                    return;
                }
                TalendEntryEditPart entryEditPart = (TalendEntryEditPart) children.get(insertIndex);
                CombinedTemplateCreationEntry entryModule = (CombinedTemplateCreationEntry) entryEditPart.getModel();
                removeRecentlyUsedComponent(entryModule);
            }

            // if (!alreadyExist) {
            TalendCombinedTemplateCreationEntry newRecently = TalendEditorPaletteFactory.createEntryFrom(component);
            newRecently.setTimestemp(new Date());
            newRecently.setParent(recentlyUsedEditPart.getDrawer());
            EditPart child = recentlyUsedEditPart.createChild(newRecently);
            PaletteDrawer paletteDrawer = recentlyUsedEditPart.getDrawer();
            if (paletteDrawer != null) {
                // paletteDrawer.add(insertIndex, newRecently);
                paletteDrawer.add(0, newRecently);
            }
            // reobtain
            children = recentlyUsedEditPart.getChildren();
            if (children != null) {
                int size = children.size();
                if (TalendEditorPaletteFactory.RECENTLY_USED_LIMIT_SIZE < size) {
                    TalendEntryEditPart entryEditPart = (TalendEntryEditPart) children
                            .get(TalendEditorPaletteFactory.RECENTLY_USED_LIMIT_SIZE);
                    CombinedTemplateCreationEntry entryModule = (CombinedTemplateCreationEntry) entryEditPart.getModel();
                    removeRecentlyUsedComponent(entryModule);
                }
            }
            // needn't use this, or will cause problem
            // recentlyUsedEditPart.addChild(child, insertIndex);
            // }

            // refreshRecentlyUsedComponentToReference();
        }

    }

    public void refreshRecentlyUsedComponentToReference() {
        if (recentlyUsedEditPart == null) {
            return;
        }
        List children = recentlyUsedEditPart.getChildren();
        if (children == null) {
            return;
        }

        List<TalendCombinedTemplateCreationEntry> recentlyUsedList = new ArrayList<TalendCombinedTemplateCreationEntry>();

        for (Object obj : children) {
            TalendEntryEditPart entryEditPart = (TalendEntryEditPart) obj;
            TalendCombinedTemplateCreationEntry entryModule = (TalendCombinedTemplateCreationEntry) entryEditPart.getModel();
            recentlyUsedList.add(entryModule);
        }

        TalendEditorPaletteFactory.storeRecentlyUsedList(recentlyUsedList);

    }

    public void removeRecentlyUsedComponent(CombinedTemplateCreationEntry component) {
        if (recentlyUsedEditPart != null) {
            List children = recentlyUsedEditPart.getChildren();
            if (children != null) {
                for (Object obj : children) {
                    TalendEntryEditPart entryEditPart = (TalendEntryEditPart) obj;
                    CombinedTemplateCreationEntry entryModule = (CombinedTemplateCreationEntry) entryEditPart.getModel();
                    if (entryModule.getLabel().equals(component.getLabel())) {
                        PaletteDrawer paletteDrawer = recentlyUsedEditPart.getDrawer();
                        if (paletteDrawer != null) {
                            paletteDrawer.remove(entryModule);
                        }
                        // needn't use this
                        // favoritesEditPart.removeChild(entryEditPart);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Getter for favoritesEditPart.
     * 
     * @return the favoritesEditPart
     */
    public TalendDrawerEditPart getFavoritesEditPart() {
        return this.favoritesEditPart;
    }

    /**
     * Sets the favoritesEditPart.
     * 
     * @param favoritesEditPart the favoritesEditPart to set
     */
    public void setFavoritesEditPart(TalendDrawerEditPart favoritesEditPart) {
        this.favoritesEditPart = favoritesEditPart;
    }

    /**
     * Getter for recentlyUsedEditPart.
     * 
     * @return the recentlyUsedEditPart
     */
    public TalendDrawerEditPart getRecentlyUsedEditPart() {
        return this.recentlyUsedEditPart;
    }

    /**
     * Sets the recentlyUsedEditPart.
     * 
     * @param recentlyUsedEditPart the recentlyUsedEditPart to set
     */
    public void setRecentlyUsedEditPart(TalendDrawerEditPart recentlyUsedEditPart) {
        this.recentlyUsedEditPart = recentlyUsedEditPart;
    }

}
