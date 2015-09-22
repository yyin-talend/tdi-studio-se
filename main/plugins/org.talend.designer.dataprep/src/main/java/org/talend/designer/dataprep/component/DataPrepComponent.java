package org.talend.designer.dataprep.component;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;

public class DataPrepComponent extends AbstractExternalNode {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#initialize()
     */
    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#open(org.talend.core.model.process.Display)
     */
    @Override
    public int open(Display display) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#open(org.talend.core.model.process.Composite)
     */
    @Override
    public int open(Composite parent) {
        /**
         * http://localhost/#/home/preparations?prepid=2ff7b182f59e0249347cd3b5299c3b389cff3e20
         **/
        IElementParameter urlParam = getElementParameter("URL"); //$NON-NLS-1$
        IElementParameter preparationId = getElementParameter("PREPARATION_ID"); //$NON-NLS-1$

        if (urlParam != null && preparationId != null) {
            String urlValue = urlParam.getValue().toString();
            // remove the extra "
            urlValue = urlValue.replace("\"", "") + "/#/home/preparations?prepid=" + preparationId.getValue().toString().replace("\"", ""); //$NON-NLS-1$//$NON-NLS-2$
            try {
                PlatformUI.getWorkbench().getBrowserSupport().createBrowser("dataprep").openURL(new URL(urlValue)); //$NON-NLS-1$
            } catch (PartInitException | MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("The dataprep component url parameter or id is not set!");
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#setExternalData(org.talend.core.model.process.IExternalData)
     */
    @Override
    public void setExternalData(IExternalData persistentData) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#renameInputConnection(java.lang.String, java.lang.String)
     */
    @Override
    public void renameInputConnection(String oldName, String newName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#renameOutputConnection(java.lang.String, java.lang.String)
     */
    @Override
    public void renameOutputConnection(String oldName, String newName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getComponentDocumentation(java.lang.String, java.lang.String)
     */
    @Override
    public IComponentDocumentation getComponentDocumentation(String componentName, String tempFolderPath) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getScreenshot()
     */
    @Override
    public ImageDescriptor getScreenshot() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getTMapExternalData()
     */
    @Override
    public IExternalData getTMapExternalData() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getExternalEmfData()
     */
    @Override
    public AbstractExternalData getExternalEmfData() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.AbstractExternalNode#renameMetadataColumnName(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    protected void renameMetadataColumnName(String conectionName, String oldColumnName, String newColumnName) {
        // TODO Auto-generated method stub

    }

}
