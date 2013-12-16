package com.talend.compress.zip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Zip {

	private String sourceDir;
	private String targetZip;
	private boolean overwriteExistTargetZip = true;
	private boolean makeTargetDir = false;
	private int compressLevel = 4;
	private String encoding = "ISO-8859-15";

	private boolean allFiles = true;
	private String namePatternFilter = null;
	private boolean containSubDir = true;

	private boolean isEncrypted = false;
	private String password = null;

	private String zip64Mode = null;

	private boolean useAesEncryption = false;

	public void setUseAesEncryption(boolean useAesEncryption) {
		this.useAesEncryption = useAesEncryption;
	}

	public void setZip64Mode(String zip64Mode) {
		this.zip64Mode = zip64Mode;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}

	public void setContainSubDir(boolean containSubDir) {
		this.containSubDir = containSubDir;
	}

	public void setAllFiles(boolean allFiles) {
		this.allFiles = allFiles;
	}

	public void setNamePatternFilter(String namePatternFilter) {
		this.namePatternFilter = namePatternFilter;
	}

	public void setCompressLevel(int compressLevel) {
		this.compressLevel = compressLevel;
	}

	public void setMakeTargetDir(boolean makeTargetDir) {
		this.makeTargetDir = makeTargetDir;
	}

	public Zip(String sourceDir, String targetZip) {
		this.sourceDir = sourceDir;
		this.targetZip = targetZip;
	}

	public void setOverwriteExistTargetZip(boolean overwriteExistTargetZip) {
		this.overwriteExistTargetZip = overwriteExistTargetZip;
	}

	public void doZip() throws Exception {
		File target = new java.io.File(targetZip);
		if (target.exists()) {
			if(!overwriteExistTargetZip) {
				throw (new java.lang.Exception("File already exist!"));
			} else {
				target.delete();
			}
		}

		if (makeTargetDir) {
			String zipDir = targetZip.contains("\\") ? targetZip.substring(0,
					targetZip.lastIndexOf("\\")) : targetZip.substring(0,
					targetZip.lastIndexOf("/"));
			File zDir = new java.io.File(zipDir);
			zDir.mkdirs();
		}

		final File source = new File(sourceDir);
		final List<File> list = new ArrayList<File>();
		source.listFiles(new java.io.FilenameFilter() {
			public boolean accept(java.io.File dir, String name) {
				java.io.File file = new java.io.File(dir, name);
				if (file.isFile()) {
					if (allFiles || name.matches(namePatternFilter)) {
						list.add(file);
						return true;
					}
				} else if (containSubDir) {
					file.listFiles(this);
				}
				return false;
			}
		});

		if (list.size() < 1) {
			return;
		}

		if (isEncrypted && useAesEncryption) {
			doZip2(source, list);
		} else {
			doZip1(source, list);
		}
	}

	// apache common compress impl
	private void doZip1(final File source, final List<File> list)
			throws Exception {
		int beginIndex = source.getPath().length() + 1;

		org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream out = null;
		java.io.OutputStream output_stream = new java.io.FileOutputStream(
				targetZip);
		if (isEncrypted && !"".equals(password)) {
			output_stream = new javax.crypto.CipherOutputStream(output_stream,
					org.talend.archive.IntegrityUtil.createCipher(
							javax.crypto.Cipher.ENCRYPT_MODE, password));
		}
		out = new org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream(
				new java.io.BufferedOutputStream(output_stream));
		out.setLevel(compressLevel);
		// set filename & comment encoding
		out.setEncoding(encoding);
		if ("ALWAYS".equals(zip64Mode)) {
			out.setUseZip64(org.apache.commons.compress.archivers.zip.Zip64Mode.Always);
		} else if ("NEVER".equals(zip64Mode)) {
			out.setUseZip64(org.apache.commons.compress.archivers.zip.Zip64Mode.Never);
		}

		for (int i = 0; i < list.size(); i++) {
			java.io.BufferedInputStream in = new java.io.BufferedInputStream(
					new java.io.FileInputStream(list.get(i)));
			org.apache.commons.compress.archivers.zip.ZipArchiveEntry entry = new org.apache.commons.compress.archivers.zip.ZipArchiveEntry(
					list.get(i).getPath().substring(beginIndex));
			entry.setTime(list.get(i).lastModified());
			out.putArchiveEntry(entry);

			int readLen;
			byte[] buf = new byte[1024];
			while ((readLen = in.read(buf, 0, 1024)) != -1) {
				out.write(buf, 0, readLen);
			}
			out.closeArchiveEntry();
			out.flush();
			in.close();
		}

		if (out != null) {
			out.close();
		}
	}

	// zip4j impl
	private void doZip2(final File source, final List<File> list)
			throws Exception {

		ZipFile zipFile = new ZipFile(targetZip);
		if("UTF-8".equalsIgnoreCase(encoding)) {
			encoding = "UTF8";
		}
		zipFile.setFileNameCharset(encoding);

		ZipParameters params = new ZipParameters();
		params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		params.setCompressionLevel(compressLevel);

		if (isEncrypted && !"".equals(password)) {
			params.setEncryptFiles(true);
			params.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			params.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			params.setPassword(password);
		}

		params.setDefaultFolderPath(source.getAbsoluteFile().getPath());
		zipFile.addFiles((ArrayList) list, params);
	}
}
