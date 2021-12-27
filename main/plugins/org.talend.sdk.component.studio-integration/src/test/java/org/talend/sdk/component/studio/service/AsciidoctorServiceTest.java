package org.talend.sdk.component.studio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
//import org.osgi.framework.Bundle;
//import org.osgi.framework.BundleContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.talend.sdk.component.studio.SdkComponentPlugin;

class AsciidoctorServiceTest {

    @Test void init() {


        SdkComponentPlugin plugin = SdkComponentPlugin.getDefault();

        Bundle bundle = plugin.getBundle();
        BundleContext context = bundle.getBundleContext();
        final AsciidoctorService instance = new AsciidoctorService();
        String result = instance.convert("= Header");
        assertEquals("<h1>Header</h1>",result.trim());
    }

}
