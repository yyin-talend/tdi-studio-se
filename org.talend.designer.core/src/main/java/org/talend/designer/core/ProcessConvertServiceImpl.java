package org.talend.designer.core;

import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.ui.editor.process.Process;

public class ProcessConvertServiceImpl implements IProcessConvertService {

    public ProcessConvertServiceImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public IProcess getProcessFromItem(Item item, boolean loadScreenshots) {
        if (ProcessItem.class.equals(item.getClass())) {
            Process process = null;
            process = new Process(item.getProperty());
            process.loadXmlFile(loadScreenshots);
            return process;
        }
        return null;
    }

}
