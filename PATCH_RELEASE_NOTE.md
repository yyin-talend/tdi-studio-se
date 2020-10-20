---
version: 7.1.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-4435

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20201020\_TPS-4435\_v1-7.1.1 |
| Release Date     | 2020-10-20 |
| Target Version   | 20181026\_1147-7.1.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fixes:

- TPS-4435 [7.1.1] Logger doesnt take the full query as single string in toraclerow component (TDI-44935)

## Prerequisites <!-- mandatory -->

Consider the following requirements for your system:

- Talend Studio 7.1.1 must be installed.


## Installation

### Installing the patch using Software update <!-- if applicable -->

1) Logon TAC and switch to Configuration->Software Update, then enter the correct values and save referring to the documentation: https://help.talend.com/reader/f7Em9WV\_cPm2RRywucSN0Q/j9x5iXV~vyxMlUafnDejaQ

2) Switch to Software update page, where the new patch will be listed. The patch can be downloaded from here into the nexus repository.

3) On Studio Side: Logon Studio with remote mode, on the logon page the Update button is displayed: click this button to install the patch.

### Installing the patch using Commandline <!-- if applicable -->

Execute the following commands:

1. Talend-Studio-win-x86\_64.exe -nosplash -application org.talend.commandline.CommandLine -consoleLog -data commandline-workspace startServer -p 8002 --talendDebug
2. initRemote {tac\_url} -ul {TAC login username} -up {TAC login password}
3. checkAndUpdate -tu {TAC login username} -tup {TAC login password}


## Affected files for this patch <!-- if applicable -->

The following files are installed by this patch:

- plugins/org.talend.designer.components.localprovider\_7.1.1.20181026\_1147/components/tOracleRow/tOracleRow\_main.javajet
- plugins/org.talend.designer.components.localprovider\_7.1.1.20181026\_1147/components/templates/Log4j/Log4jDBConnUtil.javajet
- plugins/org.talend.designer.components.localprovider\_7.1.1.20181026\_1147/components/tAmazonOracleRow/tAmazonOracleRow\_main.javajet
- plugins/org.talend.designer.components.localprovider\_7.1.1.20181026\_1147/components/templates/DB/Input/AbstractDBInputBegin.javajet
- plugins/org.talend.designer.components.localprovider\_7.1.1.20181026\_1147/components/templates/DB/Row/AbstractDBRowMain.javajet
