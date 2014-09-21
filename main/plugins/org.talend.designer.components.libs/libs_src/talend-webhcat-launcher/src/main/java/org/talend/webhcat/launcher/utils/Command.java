package org.talend.webhcat.launcher.utils;

public enum Command {
	LIBJARS("-libjars"), USERNAME("-username"), NAMENODE("-namenode"), STATUSFOLDER(
			"-status"), REMOTEFOLDER("-remote"), LOCALFOLDER("-local"), JAR(
			"-jar"), CLASS("-class"), ENDPOINT("-webhcat"), FILE("-file"), AZURE_STORAGE_NAME(
			"-azureStorageAccount"), AZURE_STORAGE_PASSWORD(
			"-azureStoragePassword"), AZURE_STORAGE_ADDRESS(
			"-azureStorageAddress"), AZURE_STORAGE_CONTAINER(
			"-azureStorageContainer"), REMOTE("-remote"), STATUS("-status"), LOCAL(
			"-local"), WEBHCAT_USERNAME("-webhcatUsername"), HDINSIGHT_USERNAME(
			"-hdInsightUsername"), HDINSIGHT_PASSWORD("-hdInsightPassword");

	private String command;

	Command(String command) {
		this.command = command;
	}

	public String toString() {
		return this.command;
	}

	public boolean equals(String s) {
		if (s == null)
			return false;
		else
			return s.equals(this.command);
	}

}
