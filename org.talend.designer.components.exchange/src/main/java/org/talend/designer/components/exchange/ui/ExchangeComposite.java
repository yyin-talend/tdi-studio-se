// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.exchange.ui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.properties.tab.IDynamicProperty;
import org.talend.designer.components.exchange.ExchangeConstants;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.util.ExchangeUtils;
import org.talend.designer.core.IMultiPageTalendEditor;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public abstract class ExchangeComposite extends Composite implements IDynamicProperty {

    private static Logger log = Logger.getLogger(ExchangeComposite.class);

    protected Element element;

    protected Composite parent;

    protected EComponentCategory section;

    protected TabbedPropertySheetWidgetFactory widgetFactory;

    protected static final SimpleDateFormat FORMATTER = new SimpleDateFormat();

    Map<String, ComponentExtension> fDownloadedExtensions = new HashMap<String, ComponentExtension>();

    Map<String, ComponentExtension> fInstalledExtensions = new HashMap<String, ComponentExtension>();

    Map<String, ComponentExtension> fContributedExtensions = new HashMap<String, ComponentExtension>();

    private static final String RELEASE_DATE_FORMAT = "yyyy-MM-dd"; //$NON-NLS-1$

    protected DateFormat formatter = new SimpleDateFormat(RELEASE_DATE_FORMAT);

    private ComponentExtension selected = null;

    protected static final Image exchangeImageMissing = ImageProvider.getImage(ECoreImage.EXCHNAGEIMAGEMISSING);

    /**
     * DOC hcyi ExchangeComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ExchangeComposite(Composite parent, int styles, TabbedPropertySheetWidgetFactory widgetFactory) {
        super(parent, styles);
        this.parent = parent;
        this.widgetFactory = widgetFactory;
    }

    /**
     * Load downloaded components information from file.
     */
    public void loadDownloadedExtensionsFromFile() {
        try {
            List<ComponentExtension> extensions = ExchangeUtils.loadDownloadedComponents(ExchangeConstants.COMPONENT_MODEL_FILE);
            for (ComponentExtension ext : extensions) {
                fDownloadedExtensions.put(ext.getIdExtension(), ext);
            }
        } catch (Throwable e) {
            // do nothing, the file may not exist because this is the first time
            // we use this view and we haven't
            // installed any extensions
        }
    }

    /**
     * Save downloaded components information to file.
     */
    public void saveDownloadedExtensionsToFile() {
        try {
            ExchangeUtils.saveDownloadedComponents(ExchangeConstants.COMPONENT_MODEL_FILE, getDownloadedExtensions());
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
    }

    public List<ComponentExtension> getDownloadedExtensions() {
        return new ArrayList<ComponentExtension>(fDownloadedExtensions.values());
    }

    public Boolean addDownloadedExtension(ComponentExtension extension) {
        fDownloadedExtensions.put(extension.getIdExtension(), extension);
        return true;
    }

    /**
     * Getter for selected.
     * 
     * @return the selected
     */
    public ComponentExtension getSelectedExtension() {
        return this.selected;
    }

    /**
     * Sets the selected.
     * 
     * @param selected the selected to set
     */
    public void setSelectedExtension(ComponentExtension selected) {
        this.selected = selected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getHashCurControls()
     */
    public BidiMap getHashCurControls() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getElement()
     */
    public Element getElement() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getPart()
     */
    public IMultiPageTalendEditor getPart() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getSection()
     */
    public EComponentCategory getSection() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getComposite()
     */
    public Composite getComposite() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#setCurRowSize(int)
     */
    public void setCurRowSize(int i) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getCurRowSize()
     */
    public int getCurRowSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getTableIdAndDbTypeMap()
     */
    public Map<String, String> getTableIdAndDbTypeMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getTableIdAndDbSchemaMap()
     */
    public Map<String, String> getTableIdAndDbSchemaMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#refresh()
     */
    public void refresh() {

        Display.getDefault().syncExec(new Runnable() {

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Runnable#run()
             */
            public void run() {
                getParent().layout();
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.properties.tab.IDynamicProperty#getRepositoryAliasName(org.talend.core.properties.tab.ConnectionItem
     * )
     */
    public String getRepositoryAliasName(ConnectionItem connectionItem) {
        // TODO Auto-generated method stub
        return null;
    }

}
