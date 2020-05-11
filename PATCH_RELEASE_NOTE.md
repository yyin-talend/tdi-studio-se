---
version: 7.2.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
- https://talend.poolparty.biz/coretaxonomy/26
- https://talend.poolparty.biz/coretaxonomy/24
---

# TPS-4044

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20200511\_TPS-4044\_v1-7.2.1 |
| Release Date     | 2020-06-12 |
| Target Version   | 7.2.1 |
| Product affected | Talend Studio |

## Introduction <!-- mandatory -->

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues <!-- mandatory -->
This patch contains this following fixe:

- TPS-4044 Fix BigQuery create table when needed (TDI-44147)

## Prerequisites <!-- mandatory -->
Consider the following requirements for your system:

- Talend Studio 7.2.1 must be installed.


## Installation <!-- mandatory -->

### Installing the patch using Software update <!-- if applicable -->

1) Logon TAC and switch to Configuration->Software Update, then enter the correct values and save referring to the documentation: https://help.talend.com/reader/f7Em9WV_cPm2RRywucSN0Q/j9x5iXV~vyxMlUafnDejaQ

2) Switch to Software update page, where the new patch will be listed. The patch can be downloaded from here into the nexus repository.

3) On Studio Side: Logon Studio with remote mode, on the logon page the Update button is displayed: click this button to install the patch.

### Installing the patch using Talend Studio <!-- if applicable -->

1) Create a folder named "patches" under your studio installer directory and copy the patch .zip file to this folder.

2) Restart your studio: a window pops up, then click OK to install the patch, or restart the commandline and the patch will be installed automatically.

### Installing the patch using Commandline <!-- if applicable -->

Execute the following commands:

1. Talend-Studio-win-x86_64.exe -nosplash -application org.talend.commandline.CommandLine -consoleLog -data commandline-workspace startServer -p 8002 --talendDebug
2. initRemote {tac_url} -ul {TAC login username} -up {TAC login password}
3. checkAndUpdate -tu {TAC login username} -tup {TAC login password}

## Affected files for this patch <!-- if applicable -->

The following files are installed by this patch:

- plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryBulkExe/tBigQueryBulkExec_begin.javajet
