package org.talend.designer.codegen.stigma;

import java.io.File;

import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.designer.codegen.components.model.ComponentsFactory;

public class StigmaLocalComponentsProvider extends AbstractComponentsProvider {

    @Override
    protected File getExternalComponentsLocation() {
        return new File(ComponentsFactory.APPLICATION_PATH + "/components");
    }

    public String getId() {
        return "org.talend.stigma.localprovider";
    }

    public String getFolderName() {
        return "stigma-server";
    }

}
