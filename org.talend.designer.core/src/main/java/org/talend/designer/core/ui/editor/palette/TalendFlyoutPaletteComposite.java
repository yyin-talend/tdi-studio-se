// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.PaletteMessages;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;
import org.talend.repository.ui.actions.ShowFavoriteAction;
import org.talend.repository.ui.actions.ShowStandardAction;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class TalendFlyoutPaletteComposite extends FlyoutPaletteComposite {

    // pruo static final FontManager FONT_MGR = new FontManager();

    /**
     * DOC Administrator TalendFlyoutPaletteComposite constructor comment.
     * 
     * @param parent
     * @param style
     * @param page
     * @param pvProvider
     * @param preferences
     */
    public TalendFlyoutPaletteComposite(Composite parent, int style, IWorkbenchPage page, PaletteViewerProvider pvProvider,
            FlyoutPreferences preferences) {
        super(parent, style, page, pvProvider, preferences);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Control createTitle(Composite parent, boolean isHorizontal) {
        // TODO Auto-generated method stub
        return new TalendTitleCanvas(parent, isHorizontal);
    }

    // @Override
    // protected void updateState(IWorkbenchPage page) {
    // IViewReference view = page.findViewReference(TalendPaletteView.ID);
    // if (view == null && isInState(STATE_HIDDEN))
    // setState(prefs.getPaletteState());
    // if (view != null && !isInState(STATE_HIDDEN))
    // setState(STATE_HIDDEN);
    // }

    //
    private class TalendTitleCanvas extends TitleCanvas {

        // private LightweightSystem lws;

        /**
         * DOC Administrator TalendTitleCanvas constructor comment.
         * 
         * @param parent
         * @param horizontal
         */
        public TalendTitleCanvas(Composite parent, boolean horizontal) {
            super(parent, horizontal);
            init(horizontal);
        }

        @Override
        protected void init(boolean isHorizontal) {
            // TODO Auto-generated method stub
            // super.init(isHorizontal);

            final IFigure contents = new TitleLabel(true);
            contents.setRequestFocusEnabled(true);
            contents.setFocusTraversable(true);
            contents.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent fe) {
                    fe.gainer.repaint();
                }

                public void focusLost(FocusEvent fe) {
                    fe.loser.repaint();
                }
            });

            lws = new LightweightSystem();
            lws.setControl(this);
            lws.setContents(contents);
            setCursor(SharedCursors.SIZEALL);
            FONT_MGR.register(this);
            new TitleDragManager(this);
            final MenuManager manager = new MenuManager();
            MenuManager mgr = new MenuManager(PaletteMessages.DOCK_LABEL);
            mgr.add(new ChangeDockAction(PaletteMessages.LEFT_LABEL, PositionConstants.WEST));
            mgr.add(new ChangeDockAction(PaletteMessages.RIGHT_LABEL, PositionConstants.EAST));
            manager.add(new ResizeAction());
            ShowStandardAction showSt = ShowStandardAction.getInstance();
            ShowFavoriteAction showSf = ShowFavoriteAction.getInstance();
            showSt.setShowF(showSf);
            showSf.setShowS(showSt);
            manager.add(showSt);
            manager.add(showSf);
            showSt.doSetEnable();
            manager.add(mgr);
            setMenu(manager.createContextMenu(this));
            mgr.addMenuListener(new IMenuListener() {

                public void menuAboutToShow(IMenuManager menuMgr) {
                    IContributionItem[] items = menuMgr.getItems();
                    for (int i = 0; i < items.length; i++) {
                        ((ActionContributionItem) items[i]).update();
                    }
                }
            });

            addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    FONT_MGR.unregister(TalendTitleCanvas.this);
                    manager.dispose();
                }
            });

        }
    }

}
