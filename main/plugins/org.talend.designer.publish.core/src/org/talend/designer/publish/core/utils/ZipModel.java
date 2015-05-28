package org.talend.designer.publish.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.talend.commons.runtime.utils.io.FileCopyUtils;
import org.talend.designer.publish.core.models.BundleModel;
import org.talend.designer.publish.core.models.FeaturesModel;

public class ZipModel {

    private static final String PREFIX = "repository";

    private final ZipOutputStream output;

    public ZipModel(FeaturesModel featuresModel, File destination) throws IOException {

        // Create the parent file if not exist
        File parentDestFile = destination.getParentFile();
        if (!parentDestFile.exists()) {
            parentDestFile.mkdirs();
        }
        output = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));

        /*
         * feature file path: repository/[projectName]/[itemName]/[itemName]-feature
         * /[itemVersion]/[itemName]-[itemVersion]-feature.xml
         */
        add(PREFIX + featuresModel.getRepositoryLocation(null), featuresModel.getContent());

        /*
         * Bundle File path: repository/[projectName]/[itemName]/[itemName]
         * /[itemVersion]/[itemName]-[itemVersion].jar
         */
        for (BundleModel bundleModel : featuresModel.getBundles()) {
            // add bundle jar file
            File f = bundleModel.getFile();
            if (null == f) {
                continue;
            }
            add(PREFIX + bundleModel.getRepositoryLocation(null).toString(), f);
        }

    }

    public void add(String location, File f) throws IOException {
        ZipEntry entry = new ZipEntry(location);
        entry.setSize(f.length());
        entry.setTime(f.lastModified());
        output.putNextEntry(entry);
        write(new BufferedInputStream(new FileInputStream(f)));
    }

    public void add(String location, InputStream is) throws IOException {
        ZipEntry entry = new ZipEntry(location);
        entry.setSize(is.available());
        entry.setTime(System.currentTimeMillis());
        output.putNextEntry(entry);
        write(is);
    }

    private void write(InputStream is) throws IOException {
        try {
            FileCopyUtils.copyStreams(is, output);
        } finally {
            is.close();
        }
    }

    public void close() {
        try {
            output.flush();
        } catch (IOException e) {
        } finally {
            try {
                output.close();
            } catch (IOException e) {
            }
        }
    }
}
