package org.talend.repository.ui.wizards.exportjob.scriptsmanager.esb;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.utils.EmfModelUtils;
import org.talend.repository.utils.FileCopyUtils;
import org.talend.repository.utils.ZipFileUtils;

public class WSDLExporter {

    private WSDLExporter() {
    }

    public static List<URL> exportWsdlsForProcess(ProcessItem processItem, String tempFolderPath) {

        List<URL> wsdlUrls = new ArrayList<URL>();

        // get all tESBConsumer components
        Collection<NodeType> consumerComponents = EmfModelUtils.getComponentsByName(processItem,
                "tESBConsumer"); //$NON-NLS-1$

        for (NodeType consumerComponent : consumerComponents) {

            // retrieve WSDL content (compressed-n-encoded)
            String wsdl_content = EmfModelUtils.computeTextElementValue(
                    "WSDL_CONTENT", consumerComponent); //$NON-NLS-1$

            if (null == wsdl_content || wsdl_content.trim().isEmpty()) {
                continue;
            }

            String componentUniqueName = EmfModelUtils.computeTextElementValue(
                    "UNIQUE_NAME", consumerComponent); //$NON-NLS-1$

            // configure decoding and uncompressing
            InputStream wsdlStream = ZipFileUtils.decodeAndUncompress(new ByteArrayInputStream(
                    wsdl_content.getBytes()));

            // write WSDL content into temporary file
            File wsdlFile = new File(tempFolderPath + componentUniqueName + ".wsdl");
            try {
                FileOutputStream wsdlFileOutputStream = null;
                try {
                    wsdlFileOutputStream = new FileOutputStream(wsdlFile);
                    FileCopyUtils.copyStreams(wsdlStream, wsdlFileOutputStream);
                } finally {
                    if (wsdlFileOutputStream != null) {
                        wsdlFileOutputStream.close();
                    }
                }
                wsdlUrls.add(wsdlFile.toURI().toURL());
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        // process sub-jobs
        Set<JobInfo> subJobInfos = ProcessorUtilities.getChildrenJobInfo(processItem);
        for (JobInfo subJobInfo : subJobInfos) {
            wsdlUrls.addAll(exportWsdlsForProcess(subJobInfo.getProcessItem(), tempFolderPath));
        }

        return wsdlUrls;
    }

}
