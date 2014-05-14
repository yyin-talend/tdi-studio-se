package org.talend.azurestorage.utils;

import java.io.File;
import java.util.HashMap;

public class AzureStorageUtils {

	public java.util.Map<String, String> genAzureObjectList(File file,
			String keyParent) {
		java.util.Map<String, String> map = new java.util.HashMap<String, String>();
		if (file.isDirectory()) {
			if (keyParent != null && !"".equals(keyParent)) {
				if (keyParent.trim().lastIndexOf("/") != keyParent.trim()
						.length() - 1) {
					keyParent = keyParent + "/";
				}
			}
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					map.putAll(genAzureObjectList(f, keyParent + f.getName() + "/"));
				} else {
					map.put(f.getAbsolutePath(), keyParent + f.getName());
					
				}
			}
		} else {
			map.put(file.getAbsolutePath(), keyParent);
		}

		return map;
	}

	public java.util.Map<String, String> genFileFilterList(
			java.util.List<java.util.Map<String, String>> list,
			String localdir, String remotedir) {
		if (remotedir != null && !"".equals(remotedir)) {
			if (remotedir.trim().lastIndexOf("/") != remotedir.trim().length() - 1) {
				remotedir = remotedir + "/";
			}
		}
		java.util.Map<String, String> fileMap = new HashMap<String, String>();
		for (java.util.Map<String, String> map : list) {
			java.util.Set<String> keySet = map.keySet();
			for (String key : keySet) {
				String tempdir = localdir;
				String filemask = key;
				String dir = null;
				String mask = filemask.replaceAll("\\\\", "/");
				int i = mask.lastIndexOf('/');
				if (i != -1) {
					dir = mask.substring(0, i);
					mask = mask.substring(i + 1);
				}
				if (dir != null && !"".equals(dir))
					tempdir = tempdir + "/" + dir;
				mask = mask.replaceAll("\\.", "\\\\.").replaceAll("\\*", ".*");
				final String finalMask = mask;
				java.io.File[] listings = null;
				java.io.File file = new java.io.File(tempdir);
				if (file.isDirectory()) {
					listings = file.listFiles(new java.io.FileFilter() {
						public boolean accept(java.io.File pathname) {
							boolean result = false;
							if (pathname != null && pathname.isFile()) {
								result = java.util.regex.Pattern
										.compile(finalMask)
										.matcher(pathname.getName()).find();
							}
							return result;
						}
					});
				}
				if (listings == null || listings.length <= 0) {
					System.err.println("No match file(" + key + ") exist!");
				} else {
					String localFilePath = "";
					String newObjectKey = "";
					for (int m = 0; m < listings.length; m++) {
						if (listings[m].getName().matches(mask)) {
							localFilePath = listings[m].getAbsolutePath();
							if (map.get(key) != null
									&& map.get(key).length() > 0) {
								newObjectKey = remotedir + map.get(key);
							} else {
								newObjectKey = remotedir
										+ listings[m].getName();
							}
							fileMap.put(localFilePath, newObjectKey);
						}
					}

				}
			}
		}
				
		return fileMap;
	}
}
