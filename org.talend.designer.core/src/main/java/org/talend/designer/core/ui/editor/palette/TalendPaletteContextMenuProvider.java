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
package org.talend.designer.core.ui.editor.palette;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Shell;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.action.ComponentSearcher;


/**
 * DOC hcw  class global comment. Detailled comment
 */
public class TalendPaletteContextMenuProvider extends PaletteContextMenuProvider {

    /**
     * DOC hcw TalendPaletteContextMenuProvider constructor comment.
     * 
     * @param palette
     */
    public TalendPaletteContextMenuProvider(PaletteViewer palette) {
        super(palette);
    }

    @Override
    public void buildContextMenu(IMenuManager menu) {
        super.buildContextMenu(menu);
        menu
                .appendToGroup(GEFActionConstants.MB_ADDITIONS, new SearchComponentAction(getPaletteViewer()));
    }

    class SearchComponentAction extends Action {

        private Shell shell;
        private PaletteViewer paletteViewer;

        /**
         * DOC hcw SearchComponentAction constructor comment.
         * 
         * @param paletteViewer
         */
        public SearchComponentAction(PaletteViewer paletteViewer) {
            this.paletteViewer = paletteViewer;
            shell = paletteViewer.getControl().getShell();
            setText(Messages.getString("SearchComponentInJobs.Title"));
            setToolTipText(Messages.getString("SearchComponentInJobs.Tooltip"));
            setDescription(Messages.getString("SearchComponentInJobs.Tooltip"));
        }
        
        @Override
        public void run() {       
            List list = paletteViewer.getSelectedEditParts();
            if (!list.isEmpty()) {
                PaletteEntry selection = (PaletteEntry) ((EditPart) list.get(0)).getModel();
                if (!(selection instanceof PaletteRoot)) {
                    System.out.println(selection.getLabel());
                    ComponentSearcher searcher = new ComponentSearcher(selection.getLabel(), paletteViewer.getControl()
                            .getShell());
                    searcher.run();
                }
            }           
        }
      
    }
}
