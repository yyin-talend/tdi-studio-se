package org.talend.webhcat.launcher.fs;

import java.io.InputStream;

public interface FileSystem {
	boolean exists(String file);
	InputStream open(String file);
	void delete(String folder);
	void mkdir(String folder);
	void copyFromLocal(String localFile, String target);
	String getFileSystemPrefix();
}
