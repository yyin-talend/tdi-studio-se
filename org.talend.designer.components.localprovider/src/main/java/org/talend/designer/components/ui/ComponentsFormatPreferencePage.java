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
package org.talend.designer.components.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.designer.components.ComponentsLocalProviderPlugin;
import org.talend.designer.core.DesignerPlugin;

/**
 * yzhang class global comment. Detail led comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ComponentsFormatPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private Button buttonAdd, buttonRemove;

    private Text textHint, textLabel, textConnection;

    private TableViewer viewer;

    private IPreferenceStore preferenceStore;

    private Map<String, String> preferenceCach;

    private final static String PREFERENCE_TYPE_HINT = ":HINT";

    private final static String PREFERENCE_TYPE_LABEL = ":LABEL";

    private final static String PREFERENCE_TYPE_CONNECTION = ":CONNECTION";

    private static final String DEFAULT_HINT = DesignerPlugin.getDefault().getPreferenceStore().getString("defaultHint");

    private static final String DEFAULT_LABEL = DesignerPlugin.getDefault().getPreferenceStore().getString("defaultLabel");

    private static final String DEFAULT_CONNECTION = DesignerPlugin.getDefault().getPreferenceStore().getString(
            "defaultConnection");

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {

        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new GridLayout(2, false));
        panel.setLayoutData(new GridData(GridData.FILL_BOTH));

        viewer = new TableViewer(panel);
        GridData viewerGridData = new GridData(GridData.FILL_BOTH);
        viewer.getControl().setLayoutData(viewerGridData);
        configViewer(viewer);

        Composite buttonPanel = new Composite(panel, SWT.NONE);
        buttonPanel.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        buttonPanel.setLayout(new GridLayout());

        buttonAdd = new Button(buttonPanel, SWT.NONE);
        buttonAdd.setText("Add");
        buttonAdd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        buttonRemove = new Button(buttonPanel, SWT.NONE);
        buttonRemove.setText("Remove");
        buttonRemove.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite footerPanel = new Composite(panel, SWT.NONE);
        footerPanel.setLayout(new GridLayout(2, false));
        GridData footerPanelGD = new GridData(GridData.FILL_BOTH);
        footerPanelGD.horizontalSpan = 2;
        footerPanel.setLayoutData(footerPanelGD);

        Label labelHint = new Label(footerPanel, SWT.NONE);
        labelHint.setText("Format hint default:");
        textHint = new Text(footerPanel, SWT.BORDER);
        textHint.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        configTextModifyListener(textHint, PREFERENCE_TYPE_HINT);

        Label labelLabel = new Label(footerPanel, SWT.NONE);
        labelLabel.setText("Format label default:");
        textLabel = new Text(footerPanel, SWT.BORDER);
        textLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        configTextModifyListener(textLabel, PREFERENCE_TYPE_LABEL);

        Label labelConnection = new Label(footerPanel, SWT.NONE);
        labelConnection.setText("Format connection default:");
        textConnection = new Text(footerPanel, SWT.BORDER);
        textConnection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        configTextModifyListener(textConnection, PREFERENCE_TYPE_CONNECTION);

        addButtonListeners();

        return panel;
    }

    /**
     * yzhang Comment method "configTexts".
     * 
     * @param text
     * @param preferenceType
     */
    private void configTextModifyListener(final Text text, final String preferenceType) {
        text.addKeyListener(new KeyAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
             */
            @Override
            public void keyReleased(KeyEvent e) {
                Object[] objs = ((IStructuredSelection) viewer.getSelection()).toArray();
                for (Object o : objs) {
                    if (o instanceof ToolEntry) {
                        preferenceCach.put(((ToolEntry) o).getLabel() + ":ToolEntry" + preferenceType, text.getText());
                    } else if (o instanceof PaletteContainer) {
                        preferenceCach.put(((PaletteContainer) o).getLabel() + ":PaletteContainer" + preferenceType, text
                                .getText());
                    }
                }

            }
        });

    }

    /**
     * yzhang Comment method "configListViewer".
     * 
     * @param viewer
     */
    private void configViewer(TableViewer v) {

        v.setContentProvider(new IStructuredContentProvider() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
             */
            public Object[] getElements(Object inputElement) {
                return ((List) inputElement).toArray();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#dispose()
             */
            public void dispose() {

            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
             * java.lang.Object, java.lang.Object)
             */
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

        });

        v.setLabelProvider(new ILabelProvider() {

            private final Map imageCache = new HashMap();

            private Image getCachedImage(ImageDescriptor descriptor) {
                Image image = (Image) imageCache.get(descriptor);
                if (image == null) {
                    image = descriptor.createImage();
                    imageCache.put(descriptor, image);
                }
                return image;
            }

            public Image getImage(Object element) {
                PaletteEntry entry = (PaletteEntry) element;
                ImageDescriptor descriptor = entry.getSmallIcon();
                if (descriptor == null) {
                    if (entry instanceof PaletteContainer) {
                        descriptor = InternalImages.DESC_FOLDER_OPEN;
                    } else if (entry instanceof PaletteSeparator) {
                        descriptor = InternalImages.DESC_SEPARATOR;
                    } else {
                        return null;
                    }
                }
                return getCachedImage(descriptor);
            }

            public String getText(Object element) {
                return ((PaletteEntry) element).getLabel();
            }

            public void addListener(ILabelProviderListener listener) {

            }

            public void dispose() {

            }

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void removeListener(ILabelProviderListener listener) {

            }

        });

        v.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                refreshTextContent((IStructuredSelection) event.getSelection());
            }

        });

    }

    /**
     * yzhang Comment method "refreshTextContent".
     * 
     * @param selection
     */
    private void refreshTextContent(IStructuredSelection selection) {

        Object[] objs = selection.toArray();
        List<DefaultFormat> formats = new ArrayList<DefaultFormat>();

        for (Object o : objs) {

            DefaultFormat format = getDefaultPreference(getIdWithoutPreferenceType(o));

            formats.add(format);
        }

        if (formats.size() > 1) {
            checkFormatsEquals(formats);
        }

        setTextsContents(formats);
    }

    /**
     * yzhang Comment method "getLabelWithoutType".
     * 
     * @param o
     * @return
     */
    private String getIdWithoutPreferenceType(Object o) {
        if (o instanceof ToolEntry) {
            return ((ToolEntry) o).getLabel() + ":ToolEntry";
        } else if (o instanceof PaletteContainer) {
            return ((PaletteContainer) o).getLabel() + ":PaletteContainer";
        }
        return null;
    }

    /**
     * yzhang Comment method "setTextsContents".
     * 
     * @param formats
     */
    private void setTextsContents(List<DefaultFormat> formats) {
        if (formats.size() != 0) {
            DefaultFormat format = formats.get(0);

            setText(textConnection, format.getConnection(), connection);
            setText(textLabel, format.getLabel(), label);
            setText(textHint, format.getHint(), hint);
        } else {
            setText(textConnection, "", connection);
            setText(textLabel, "", label);
            setText(textHint, "", hint);
        }

        connection = true;
        label = true;
        hint = true;

    }

    /**
     * yzhang Comment method "setText".
     * 
     * @param text
     * @param content
     * @param flag
     */
    private void setText(Text text, String content, boolean flag) {
        if (flag) {
            text.setText(content);
        } else {
            text.setText("");
        }
    }

    private void checkFormatsEquals(List<DefaultFormat> formats) {
        DefaultFormat oldFormat = null;
        for (DefaultFormat format : formats) {
            if (oldFormat == null) {
                oldFormat = format;
                continue;
            }
            int status = oldFormat.equals(format);
            if (status != DefaultFormat.EQUAL) {
                switch (status) {
                case DefaultFormat.HINT:
                    hint = false;
                    break;
                case DefaultFormat.CONNECTION:
                    connection = false;
                    break;
                case DefaultFormat.LABEL:
                    label = false;
                    break;
                }
            }
        }

    }

    /**
     * DOC yzhang Comment method "initPreferenceCach".
     */
    private void initPreferenceCach() {
        List viewerContent = (List) viewer.getInput();
        for (Object o : viewerContent) {

            String cachId = getIdWithoutPreferenceType(o);

            setDefaultValueToPreferenceCach(cachId, PREFERENCE_TYPE_CONNECTION, DEFAULT_CONNECTION);
            setDefaultValueToPreferenceCach(cachId, PREFERENCE_TYPE_HINT, DEFAULT_HINT);
            setDefaultValueToPreferenceCach(cachId, PREFERENCE_TYPE_LABEL, DEFAULT_LABEL);

        }

    }

    /**
     * yzhang Comment method "setDefaultValueToPreferenceCach".
     * 
     * @param id
     * @param type
     * @param defaultValue
     */
    private void setDefaultValueToPreferenceCach(String id, String type, String defaultValue) {
        if ("".equals(preferenceStore.getString(id + type))) {
            preferenceCach.put(id + type, defaultValue);
        } else {
            preferenceCach.put(id + type, preferenceStore.getString(id + type));
        }
    }

    /**
     * yzhang Comment method "getDefaultPreference".
     * 
     * @param labelID
     * @return
     */
    private DefaultFormat getDefaultPreference(String labelID) {
        String h, l, c;

        h = preferenceCach.get(labelID + PREFERENCE_TYPE_HINT);

        l = preferenceCach.get(labelID + PREFERENCE_TYPE_LABEL);

        c = preferenceCach.get(labelID + PREFERENCE_TYPE_CONNECTION);

        return new DefaultFormat(h, l, c);

    }

    private boolean hint = true;

    private boolean label = true;

    private boolean connection = true;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
        preferenceStore = ComponentsLocalProviderPlugin.getDefault().getPreferenceStore();
        preferenceCach = new HashMap<String, String>();
    }

    /**
     * yzhang Comment method "addButtonListeners".
     */
    private void addButtonListeners() {
        buttonAdd.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                PaletteContentDialog dialog = new PaletteContentDialog(getShell(), ComponentsFormatPreferencePage.this);
                dialog.open();
            }
        });

        buttonRemove.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                StructuredSelection selection = (StructuredSelection) viewer.getSelection();
                for (Object obj : selection.toArray()) {
                    ((List) viewer.getInput()).remove(obj);
                }
                viewer.refresh();

            }
        });

    }

    /**
     * yzhang Comment method "setListViewerInput".
     * 
     * @param selections
     */
    public void setViewerInput(ISelection selections) {
        List<PaletteEntry> entryList = new ArrayList<PaletteEntry>();

        List selectionsList = ((TreeSelection) selections).toList();

        if (viewer.getInput() != null) {
            List s = (List) viewer.getInput();
            entryList.addAll(s);
        }

        for (Object paletteEntry : selectionsList) {
            boolean contained = false;
            for (PaletteEntry entry : entryList) {
                if ((((PaletteEntry) paletteEntry)).getLabel().equals(entry.getLabel())) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                entryList.add((PaletteEntry) paletteEntry);
            }

        }

        viewer.setInput(entryList);

        initPreferenceCach();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        for (String id : preferenceCach.keySet()) {
            preferenceStore.putValue(id, preferenceCach.get(id));
        }
        return super.performOk();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        StructuredSelection selections = (StructuredSelection) viewer.getSelection();
        for (Object obj : selections.toArray()) {
            String id = getIdWithoutPreferenceType(obj);
            setDefaultValueToPreferenceCach(id, PREFERENCE_TYPE_CONNECTION, DEFAULT_CONNECTION);
            setDefaultValueToPreferenceCach(id, PREFERENCE_TYPE_HINT, DEFAULT_HINT);
            setDefaultValueToPreferenceCach(id, PREFERENCE_TYPE_LABEL, DEFAULT_LABEL);
        }
        refreshTextContent((IStructuredSelection) viewer.getSelection());

        super.performDefaults();
    }

    /**
     * yzhang ComponentsFormatPreferencePage class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
     * 
     */
    private class DefaultFormat {

        private String hint, label, connection;

        public static final int EQUAL = 0;

        public static final int HINT = 1;

        public static final int LABEL = 2;

        public static final int CONNECTION = 3;

        /**
         * yzhang Comment method "equals".
         * 
         * @param obj
         * @return
         */
        public int equals(DefaultFormat obj) {

            if (!obj.getConnection().equals(connection)) {
                return CONNECTION;
            } else if (!obj.getHint().equals(hint)) {
                return HINT;
            } else if (!obj.getLabel().equals(label)) {
                return LABEL;
            } else {
                return EQUAL;
            }
        }

        /**
         * yzhang ComponentsFormatPreferencePage.DefaultFormat constructor comment.
         */
        public DefaultFormat(String h, String l, String c) {
            hint = h;
            label = l;
            connection = c;
        }

        /**
         * Getter for hint.
         * 
         * @return the hint
         */
        public String getHint() {
            return this.hint;
        }

        /**
         * Sets the hint.
         * 
         * @param hint the hint to set
         */
        public void setHint(String hint) {
            this.hint = hint;
        }

        /**
         * Getter for label.
         * 
         * @return the label
         */
        public String getLabel() {
            return this.label;
        }

        /**
         * Sets the label.
         * 
         * @param label the label to set
         */
        public void setLabel(String label) {
            this.label = label;
        }

        /**
         * Getter for connection.
         * 
         * @return the connection
         */
        public String getConnection() {
            return this.connection;
        }

        /**
         * Sets the connection.
         * 
         * @param connection the connection to set
         */
        public void setConnection(String connection) {
            this.connection = connection;
        }

    }

}
