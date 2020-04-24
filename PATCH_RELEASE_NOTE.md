---
version: 7.1.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-3392

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20200424_TPS-3978\_v1-7.1.1 |
| Release Date     | 2020-04-24 |
| Target Version   | 20181026\_1147-V7.1.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fixes:

- TPS-3978 [7.1.1] HTTP Proxy for FTPS (TDI-40990)

## Prerequisites

Consider the following requirements for your system:

- Talend Studio 7.1.1 must be installed.

## Installation

### Installing the patch using Software update

1) Logon TAC and switch to Configuration->Software Update, then enter the correct values and save referring to the documentation: https://help.talend.com/reader/f7Em9WV_cPm2RRywucSN0Q/j9x5iXV~vyxMlUafnDejaQ

2) Switch to Software update page, where the new patch will be listed. The patch can be downloaded from here into the nexus repository.

3) On Studio Side: Logon Studio with remote mode, on the logon page the Update button is displayed: click this button to install the patch.

### Installing the patch using Talend Studio

1) Create a folder named "patches" under your studio installer directory and copy the patch .zip file to this folder.

2) Restart your studio: a window pops up, then click OK to install the patch, or restart the commandline and the patch will be installed automatically.

### Installing the patch using Commandline

Execute the following commands:

1. Talend-Studio-win-x86_64.exe -nosplash -application org.talend.commandline.CommandLine -consoleLog -data commandline-workspace startServer -p 8002 --talendDebug
2. initRemote {tac_url} -ul {TAC login username} -up {TAC login password}
3. checkAndUpdate -tu {TAC login username} -tup {TAC login password}

## Uninstallation
Backup the Affected files list below. Uninstall the patch by restore the backup files.

## Affected files for this patch

The following files are installed by this patch:

- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPConnection/tFTPConnection\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPConnection/tFTPConnection\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPConnection/commons-net-ftps-proxy-3.6.1-talend.jar
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPDelete/tFTPDelete\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPDelete/tFTPDelete\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPFileExist/tFTPFileExist\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPFileExist/tFTPFileExist\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPFileList/tFTPFileList\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPFileList/tFTPFileList\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPFileProperties/tFTPFileProperties\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPFileProperties/tFTPFileProperties\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPGet/tFTPGet\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPGet/tFTPGet\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPPut/tFTPPut\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPPut/tFTPPut\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPRename/tFTPRename\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPRename/tFTPRename\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPTruncate/tFTPTruncate\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFTPTruncate/tFTPTruncate\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/templates/FTP/ftpsTrust.javajet