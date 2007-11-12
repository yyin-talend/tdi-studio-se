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
package org.talend.expressionbuilder.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.common.snippets.core.ISnippetsEntry;
import org.eclipse.wst.common.snippets.internal.Logger;
import org.eclipse.wst.common.snippets.internal.PluginRecord;
import org.eclipse.wst.common.snippets.internal.palette.SnippetPaletteItem;
import org.eclipse.wst.common.snippets.internal.ui.SnippetsView;
import org.eclipse.wst.common.snippets.ui.DefaultSnippetInsertion;
import org.eclipse.wst.common.snippets.ui.ISnippetInsertion;
import org.osgi.framework.Bundle;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 */
public class SnippetsDialogTrayView extends SnippetsView {

    private IEditorPart editorPart;

    /**
     * DOC bqian SnippetsDialogTrayView constructor comment.
     */
    public SnippetsDialogTrayView() {
        super();
        insertionHelper = new InsertionHelper();
    }

    /**
     * DOC bqian Comment method "setEditor".
     * 
     * @param editorPart
     */
    public void setEditor(IEditorPart editorPart) {
        this.editorPart = editorPart;
    }

    /**
     * DOC bqian SnippetsDialogTrayView class global comment. Detailled comment <br/>
     * 
     */
    class InsertionHelper {

        protected InsertionHelper() {
            super();
        }

        protected ISnippetInsertion getInsertion(SnippetPaletteItem item) {
            ISnippetInsertion insertion = null;
            String className = item.getClassName();

            PluginRecord record = null;
            if (item.getSourceType() == ISnippetsEntry.SNIPPET_SOURCE_PLUGINS)
                record = (PluginRecord) item.getSourceDescriptor();
            // ignore the version
            if (record != null && record.getPluginName() != null && className != null) {
                Class theClass = null;
                Bundle bundle = Platform.getBundle(record.getPluginName());
                try {
                    if (className != null && className.length() > 0)
                        theClass = bundle.loadClass(className);
                } catch (ClassNotFoundException e) {
                    try { // maybe it's local???
                        theClass = Class.forName(className);
                    } catch (ClassNotFoundException f) {
                        Logger.logException("Could not load Insertion class", e); //$NON-NLS-1$
                    }
                    if (theClass == null)
                        Logger.logException("Could not load Insertion class", e); //$NON-NLS-1$
                }
                if (theClass != null) {
                    try {
                        insertion = (ISnippetInsertion) theClass.newInstance();
                    } catch (IllegalAccessException e) {
                        Logger.logException("Could not access Insertion class", e); //$NON-NLS-1$
                    } catch (InstantiationException e) {
                        Logger.logException("Could not instantiate Insertion class", e); //$NON-NLS-1$
                    }
                }
            }
            if (insertion == null) {
                insertion = new DefaultSnippetInsertion();
            }
            return insertion;
        }

        public boolean insert(SnippetPaletteItem item, IEditorPart editorPart) {
            ISnippetInsertion insertion = getInsertion(item);
            if (insertion != null) {
                insertion.setItem(item);
                insertion.insert(editorPart);
                return true;
            }
            return false;
        }
    }

    private InsertionHelper insertionHelper;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.common.snippets.internal.ui.SnippetsView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.common.snippets.internal.ui.SnippetsView#insert(org.eclipse.wst.common.snippets.internal.palette.SnippetPaletteItem)
     */
    @SuppressWarnings("restriction")
    public void insert(SnippetPaletteItem item) {
        insertionHelper.insert(item, editorPart);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.common.snippets.internal.ui.SnippetsView#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
    }

}
