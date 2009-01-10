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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ComponentSetting;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.action.ComponentSearcher;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.ui.actions.ShowFavoriteAction;

/**
 * DOC hcw class global comment. Detailled comment
 */
public class TalendPaletteContextMenuProvider extends PaletteContextMenuProvider {

    /**
     * DOC hcw TalendPaletteContextMenuProvider constructor comment.
     * 
     * @param palette
     */
    // public static boolean state = true;
    public TalendPaletteContextMenuProvider(PaletteViewer palette) {
        super(palette);
    }

    // public TalendPaletteContextMenuProvider() {
    //
    // }

    @Override
    public void buildContextMenu(IMenuManager menu) {
        super.buildContextMenu(menu);
        menu.appendToGroup(GEFActionConstants.MB_ADDITIONS, new SearchComponentAction(getPaletteViewer()));
        if (ShowFavoriteAction.state == true) {
            menu.appendToGroup(GEFActionConstants.GROUP_COPY, new FavoriteComponentAction(getPaletteViewer()));
        } else {
            menu.appendToGroup(GEFActionConstants.GROUP_COPY, new RemoveFavoriteComponentAction(getPaletteViewer()));
        }
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
                    ComponentSearcher searcher = new ComponentSearcher(selection.getLabel(), paletteViewer.getControl()
                            .getShell());
                    searcher.run();
                }
            }
        }

    }

    class FavoriteComponentAction extends Action {

        private Shell shell;

        private PaletteViewer paletteViewer;

        /**
         * DOC hcw SearchComponentAction constructor comment.
         * 
         * @param paletteViewer
         */
        public FavoriteComponentAction(PaletteViewer paletteViewer) {
            this.paletteViewer = paletteViewer;
            shell = paletteViewer.getControl().getShell();
            setText("Add To Favorite");
            setToolTipText("Add To Favorite");
            setDescription("Add To Favorite");
        }

        @Override
        public void run() {

            List list = paletteViewer.getSelectedEditParts();
            PaletteEntry element = (PaletteEntry) ((EditPart) list.get(0)).getModel();
            Project project = null;
            if (element instanceof TalendPaletteDrawer) {

                List eleList = ((TalendPaletteDrawer) element).getChildren();
                addListNotes(eleList, project);
            } else if (element instanceof CombinedTemplateCreationEntry) {
                addNotes(element, project);

            }

        }

    }

    class RemoveFavoriteComponentAction extends Action {

        private Shell shell;

        private PaletteViewer paletteViewer;

        /**
         * DOC hcw SearchComponentAction constructor comment.
         * 
         * @param paletteViewer
         */
        public RemoveFavoriteComponentAction(PaletteViewer paletteViewer) {
            this.paletteViewer = paletteViewer;
            shell = paletteViewer.getControl().getShell();
            setText("Remove From Favorite");
            setToolTipText("Remove From Favorite");
            setDescription("Remove From Favorite");
        }

        @Override
        public void run() {

            List list = paletteViewer.getSelectedEditParts();
            PaletteEntry element = (PaletteEntry) ((EditPart) list.get(0)).getModel();
            Project project = null;
            if (element instanceof TalendPaletteDrawer) {

                List eleList = ((TalendPaletteDrawer) element).getChildren();
                removeListNotes(eleList, project);
            } else if (element instanceof CombinedTemplateCreationEntry) {
                removeNotes(element, project);

            }
            ComponentUtilities.updatePalette(true);
        }

    }

    public void addNotes(PaletteEntry element, Project project) {
        String label = element.getLabel();

        String family = ComponentsFactoryProvider.getPaletteEntryFamily(element.getParent()).replaceFirst("/", "");
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        project = repositoryContext.getProject();

        List<ComponentSetting> components = project.getEmfProject().getComponentsSettings();

        for (ComponentSetting componentSetting : components) {
            if (componentSetting.getName().equals(label) && componentSetting.getFamily().equals(family)) {

                // componentSetting.setFavoriteFlag(true);
                // );
                // family.split(ComponentsFactoryProvider.FAMILY_SEPARATOR_REGEX
                String key = componentSetting.getName() + "#" + family;
                DesignerPlugin.getDefault().getPreferenceStore().setValue(key, true);
            }

        }
    }

    public void addListNotes(List eleList, Project project) {
        for (int i = 0; i < eleList.size(); i++) {
            PaletteEntry elementLi = (PaletteEntry) eleList.get(i);
            if (elementLi instanceof TalendPaletteDrawer) {
                List list = ((TalendPaletteDrawer) elementLi).getChildren();
                addListNotes(list, project);
            } else if (elementLi instanceof CombinedTemplateCreationEntry) {
                addNotes(elementLi, project);
            }

        }
    }

    public void removeNotes(PaletteEntry element, Project project) {
        String label = element.getLabel();

        String family = ComponentsFactoryProvider.getPaletteEntryFamily(element.getParent()).replaceFirst("/", "");

        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        project = repositoryContext.getProject();

        List<ComponentSetting> components = project.getEmfProject().getComponentsSettings();

        for (ComponentSetting componentSetting : components) {
            if (componentSetting.getName().equals(label) && componentSetting.getFamily().equals(family)) {
                String key = componentSetting.getName() + "#" + family;
                // componentSetting.setFavoriteFlag(false);
                DesignerPlugin.getDefault().getPreferenceStore().setValue(key, false);
            }

        }
    }

    public void removeListNotes(List eleList, Project project) {
        for (int i = 0; i < eleList.size(); i++) {
            PaletteEntry elementLi = (PaletteEntry) eleList.get(i);
            if (elementLi instanceof TalendPaletteDrawer) {
                List list = ((TalendPaletteDrawer) elementLi).getChildren();
                removeListNotes(list, project);
            } else if (elementLi instanceof CombinedTemplateCreationEntry) {
                removeNotes(elementLi, project);
            }

        }
    }
}
