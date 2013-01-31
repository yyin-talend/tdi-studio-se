package org.talend.designer.core;

import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.impl.ProcessItemImpl;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.designer.core.ui.editor.process.Process;

public class ProcessConvertServiceImpl implements IProcessConvertService {

    public ProcessConvertServiceImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public IProcess getProcessFromItem(Item item, boolean loadScreenshots) {
        if (ProcessItemImpl.class == item.getClass()) {
            Process process = null;
            process = new Process(item.getProperty());
            process.loadXmlFile(loadScreenshots);
            return process;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IProcessConvertService#doConvert(org.talend.core.model.properties.Item,
     * org.talend.core.model.repository.IRepositoryViewObject)
     */
    @Override
    public Item doConvert(Item item, IRepositoryViewObject repViewObject) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IProcessConvertService#isOriginalItemDeleted()
     */
    @Override
    public boolean isOriginalItemDeleted() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.IProcessConvertService#isNewItemCreated()
     */
    @Override
    public boolean isNewItemCreated() {
        // TODO Auto-generated method stub
        return false;
    }

}
