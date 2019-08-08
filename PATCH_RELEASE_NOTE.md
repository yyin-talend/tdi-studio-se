---
version: 7.2.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-3315

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20190808_TPS-3315\_v1-7.2.1 |
| Release Date     | 2019-08-08 |
| Target Version   | Talend-Studio-20190620\_1446-V7.2.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fixes:

- TPS-3315 [7.2.1] Additional JDBC Parameter exposes the password when using a context variable of password type (TDI-42721)

## Prerequisites

Consider the following requirements for your system:

- Talend Studio 7.2.1 must be installed.


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

- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tELTMSSqlMap/tELTMSSqlMap\_main.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tELTMysqlMap/tELTMysqlMap\_main.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tELTOracleMap/tELTOracleMap\_main.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/templates/DB/CheckContextPassword.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMSSqlBulkExec/tMSSqlBulkExec\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMSSqlConnection/tMSSqlConnection\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMSSqlInput/tMSSqlInput\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMSSqlOutput/tMSSqlOutput\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMSSqlRow/tMSSqlRow\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMSSqlSCD/tMSSqlSCD\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMSSqlSP/tMSSqlSP\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMysqlBulkExec/tMysqlBulkExec\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMysqlConnection/tMysqlConnection\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMysqlInput/tMysqlInput\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMysqlOutput/tMysqlOutput\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMysqlRow/tMysqlRow\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMysqlSCD/tMysqlSCD\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMysqlSCDELT/tMysqlSCDELT\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMysqlSP/tMysqlSP\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOracleBulkExec/tOracleBulkExec\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOracleConnection/tOracleConnection\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOracleInput/tOracleInput\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOracleOutput/tOracleOutput\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOracleRow/tOracleRow\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOracleSCD/tOracleSCD\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOracleSCDELT/tOracleSCDELT\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOracleSP/tOracleSP\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tRedshiftBulkExec/tRedshiftBulkExec\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tRedshiftConnection/tRedshiftConnection\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tRedshiftInput/tRedshiftInput\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tRedshiftOutput/tRedshiftOutput\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tRedshiftRow/tRedshiftRow\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tRedshiftUnload/tRedshiftUnload\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tdqprovider\_7.2.1.20190614\_0245/components/tMSSqlInvalidRows/tMSSqlInvalidRows\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tdqprovider\_7.2.1.20190614\_0245/components/tMSSqlValidRows/tMSSqlValidRows\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tdqprovider\_7.2.1.20190614\_0245/components/tMySQLInvalidRows/tMySQLInvalidRows\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tdqprovider\_7.2.1.20190614\_0245/components/tMySQLValidRows/tMySQLValidRows\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tdqprovider\_7.2.1.20190614\_0245/components/tOracleInvalidRows/tOracleInvalidRows\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tdqprovider\_7.2.1.20190614\_0245/components/tOracleValidRows/tOracleValidRows\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tisprovider\_7.2.1.20190614\_0242/components/tMSSqlCDC/tMSSqlCDC\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tisprovider\_7.2.1.20190614\_0242/components/tMysqlCDC/tMysqlCDC\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tisprovider\_7.2.1.20190614\_0242/components/tOracleCDC/tOracleCDC\_begin.javajet
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.tisprovider\_7.2.1.20190614\_0242/components/tOracleCDCOutput/tOracleCDCOutput\_begin.javajet