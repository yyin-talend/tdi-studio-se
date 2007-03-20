// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.core.ui.editor;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.notes.NoteCreationFactory;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;

/**
 * This class creates the palette in the Gef Editor. <br/>
 * 
 * $Id$
 * 
 */
public final class TalendEditorPaletteFactory {

    /** Preference ID used to persist the palette location. */
    public static final String PALETTE_DOCK_LOCATION = "TalendEditorPaletteFactory.Location"; //$NON-NLS-1$

    /** Preference ID used to persist the palette size. */
    public static final String PALETTE_SIZE = "TalendEditorPaletteFactory.Size"; //$NON-NLS-1$

    /** Preference ID used to persist the flyout palette's state. */
    public static final String PALETTE_STATE = "TalendEditorPaletteFactory.State"; //$NON-NLS-1$

    private static PaletteRoot palette;

    /** Create the "Shapes" drawer. */
    private static void createComponentsDrawer(final IComponentsFactory compFac) {
        PaletteDrawer componentsDrawer;
        String name, longName;
        String family, prevFamily = null;

        CombinedTemplateCreationEntry component;
        Hashtable<String, PaletteDrawer> ht = new Hashtable<String, PaletteDrawer>();

        componentsDrawer = new PaletteDrawer(Messages.getString("TalendEditorPaletteFactory.Default")); //$NON-NLS-1$
        List<IComponent> componentList = compFac.getComponents();
        Collections.sort(componentList, new Comparator<IComponent>() {

            public int compare(IComponent component1, IComponent component2) {
                return component1.getTranslatedName().compareTo(component2.getTranslatedName());
            }

        });
        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();
        boolean displayTechnical = preferenceStore.getBoolean(TalendDesignerPrefConstants.DEFAULT_DISPLAY);

        for (int i = 0; i < componentList.size(); i++) {
            IComponent xmlComponent = componentList.get(i);

            if (!displayTechnical && !xmlComponent.isVisible()) {
                continue;
            }

            if (xmlComponent.isLoaded()) {
                name = xmlComponent.getTranslatedName();
                family = xmlComponent.getFamily();
                longName = xmlComponent.getLongName();

                if (!family.equals(prevFamily)) {
                    if (!ht.containsKey(family)) {
                        componentsDrawer = new PaletteDrawer(family);
                        componentsDrawer.setInitialState(loadFamilyState(family));
                        ht.put(family, componentsDrawer);
                    } else {
                        componentsDrawer = ht.get(family);
                    }
                    prevFamily = family;
                }

                ImageDescriptor imageSmall = xmlComponent.getIcon16();
                ImageDescriptor imageLarge = xmlComponent.getIcon24();

                component = new CombinedTemplateCreationEntry(name, name, Node.class, new PaletteComponentFactory(
                        xmlComponent), imageSmall, imageLarge);
                component.setDescription(longName);
                componentsDrawer.add(component);
            }
        }
        Enumeration<String> keysEnum = ht.keys();
        List<String> keysList = Collections.list(keysEnum);

        Collections.sort(keysList);

        for (String categoryName : keysList) {
            palette.add(ht.get(categoryName));
        }
    }

    /**
     * DOC nrousseau Comment method "loadFamilyState".
     * 
     * @param family
     * @return
     */
    private static int loadFamilyState(String family) {
        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();
        preferenceStore.setDefault(PALETTE_STATE + family, PaletteDrawer.INITIAL_STATE_CLOSED);
        return preferenceStore.getInt(PALETTE_STATE + family);
    }

    public static void saveFamilyState(PaletteViewer viewer) {
        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();
        for (Object o : palette.getChildren()) {
            if (o instanceof PaletteDrawer) {
                PaletteDrawer paletteItem = (PaletteDrawer) o;
                String family = paletteItem.getLabel();
                int value;
                if (viewer.isExpanded(paletteItem)) {
                    value = PaletteDrawer.INITIAL_STATE_OPEN;
                } else {
                    value = PaletteDrawer.INITIAL_STATE_CLOSED;
                }
                paletteItem.setInitialState(value);
                preferenceStore.setValue(PALETTE_STATE + family, value);
            }
        }
    }

    /**
     * Creates the PaletteRoot and adds all palette elements. Use this factory method to create a new palette for your
     * graphical editor.
     * 
     * @return a new PaletteRoot
     */
    static PaletteRoot createPalette(final IComponentsFactory compFac) {
        palette = new PaletteRoot();
        palette.add(createToolsGroup());
        createComponentsDrawer(compFac);
        return palette;
    }

    /**
     * Return a FlyoutPreferences instance used to save/load the preferences of a flyout palette.
     */
    static FlyoutPreferences createPalettePreferences() {
        return new FlyoutPreferences() {

            private IPreferenceStore getPreferenceStore() {
                return DesignerPlugin.getDefault().getPreferenceStore();
            }

            public int getDockLocation() {
                return getPreferenceStore().getInt(PALETTE_DOCK_LOCATION);
            }

            public int getPaletteState() {
                return getPreferenceStore().getInt(PALETTE_STATE);
            }

            public int getPaletteWidth() {
                return getPreferenceStore().getInt(PALETTE_SIZE);
            }

            public void setDockLocation(final int location) {
                getPreferenceStore().setValue(PALETTE_DOCK_LOCATION, location);
            }

            public void setPaletteState(final int state) {
                getPreferenceStore().setValue(PALETTE_STATE, state);
            }

            public void setPaletteWidth(final int width) {
                getPreferenceStore().setValue(PALETTE_SIZE, width);
            }
        };
    }

    /** Create the "Tools" group. */
    private static PaletteContainer createToolsGroup() {
        PaletteGroup toolGroup = new PaletteGroup(Messages.getString("TalendEditorPaletteFactory.Tools")); //$NON-NLS-1$

        // Add a selection tool to the group
        ToolEntry tool = new PanningSelectionToolEntry();
        toolGroup.add(tool);
        palette.setDefaultEntry(tool);

        // Add a marquee tool to the group
        // toolGroup.add(new MarqueeToolEntry());

        CreationToolEntry noteCreationToolEntry = new CreationToolEntry(
                Messages.getString("TalendEditorPaletteFactory.Note"), //$NON-NLS-1$
                Messages.getString("TalendEditorPaletteFactory.CreateNote"), //$NON-NLS-1$
                new NoteCreationFactory(), 
                ImageProvider.getImageDesc(ECoreImage.CODE_ICON), 
                ImageProvider.getImageDesc(ECoreImage.CODE_ICON)); 
        toolGroup.add(noteCreationToolEntry);
        
        // Add a (unnamed) separator to the group
        toolGroup.add(new PaletteSeparator());

        return toolGroup;
    }

    /** Utility class. */
    private TalendEditorPaletteFactory() {
        // Utility class
    }

}
