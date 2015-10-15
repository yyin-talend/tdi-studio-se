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

    private boolean useZip4jEncryption = false;

    private int encryptionMethod = Zip4jConstants.ENC_METHOD_AES;
    private int aesKeyStrength = Zip4jConstants.AES_STRENGTH_256;

    public void setUseZip4jEncryption(boolean useZip4jEncryption) {
        this.useZip4jEncryption = useZip4jEncryption;
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

    public void setEncryptionMethod(int encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }

    public void setAesKeyStrength(int aesKeyStrength) {
        this.aesKeyStrength = aesKeyStrength;
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
			if (!overwriteExistTargetZip) {
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

        if (isEncrypted && useZip4jEncryption) {
			doZip2(source, list);
		} else {
			doZip1(source, list);
		}
	}

	// apache common compress impl
	private void doZip1(final File source, final List<File> list)
			throws Exception {
		int beginIndex = source.getPath().length() + 1;

		java.io.OutputStream output_stream = null;
		try {
			output_stream = new java.io.FileOutputStream(targetZip);
			if (isEncrypted && !"".equals(password)) {
				output_stream = new javax.crypto.CipherOutputStream(
						output_stream,
						org.talend.archive.IntegrityUtil.createCipher(
								javax.crypto.Cipher.ENCRYPT_MODE, password));
			}
		} catch (Exception e) {
			if (output_stream != null) {
				output_stream.close();
			}
			throw e;
		}

		org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream out = null;
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

		try {
			for (int i = 0; i < list.size(); i++) {
				org.apache.commons.compress.archivers.zip.ZipArchiveEntry entry = new org.apache.commons.compress.archivers.zip.ZipArchiveEntry(
						list.get(i).getPath().substring(beginIndex));
				entry.setTime(list.get(i).lastModified());
				out.putArchiveEntry(entry);

				java.io.BufferedInputStream in = null;
				try {
					in = new java.io.BufferedInputStream(
							new java.io.FileInputStream(list.get(i)));
					int readLen;
					byte[] buf = new byte[1024];
					while ((readLen = in.read(buf, 0, 1024)) != -1) {
						out.write(buf, 0, readLen);
					}
				} finally {
					if (in != null) {
						in.close();
					}
				}

				out.closeArchiveEntry();
				out.flush();
			}
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	// zip4j impl
	private void doZip2(final File source, final List<File> list)
			throws Exception {

		ZipFile zipFile = new ZipFile(targetZip);
		if ("UTF-8".equalsIgnoreCase(encoding)) {
			encoding = "UTF8";
		}
		zipFile.setFileNameCharset(encoding);

		ZipParameters params = new ZipParameters();
		params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		params.setCompressionLevel(compressLevel);

		if (isEncrypted && !"".equals(password)) {
			params.setEncryptFiles(true);
            params.setEncryptionMethod(encryptionMethod);
            if (Zip4jConstants.ENC_METHOD_AES == encryptionMethod) {
                params.setAesKeyStrength(aesKeyStrength);
            }
			params.setPassword(password);
		}

		params.setDefaultFolderPath(source.getAbsoluteFile().getPath());
		zipFile.addFiles((ArrayList) list, params);
	}
}
