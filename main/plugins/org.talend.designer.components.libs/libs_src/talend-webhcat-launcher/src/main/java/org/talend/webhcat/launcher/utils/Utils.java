package org.talend.webhcat.launcher.utils;

public class Utils {

	public static String removeFirstSlash(String string) {
		if (string != null && string.length() > 1 && string.charAt(0) == '/') {
			string = string.substring(1);
		}
		return string;
	}
}
