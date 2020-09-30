---
version: 7.1.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-4399

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20200930\_TPS-4399\_v1-7.1.1 |
| Release Date     | 2020-09-30 |
| Target Version   | 20181026\_1147-V7.1.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fixes:

- TPS-4399 [7.1.1] tBigQueryInput cannot execute query within public dataset (TDI-43025)
- TPS-4434 [7.1.1] Unable to write into Bigquery table when user defined delimiter is used

## Prerequisites

Consider the following requirements for your system:

- Talend Studio 7.1.1 must be installed.
- TPS-2912 - [7.1.1][20190222] Use Regional Location for BigQuery Datasets (TBD-8301)
- TPS-2958 - [7.1.1][20190628] BigQuery fixes for Service Accounts (TBD-8356, TBD-8484, TBD-8496, TBD-7153).
- TPS-3137 - [7.1.1][20190607] [Standard - DI] tBigQueryInput component doesn't contain field Result Size when the authentication type is Service account(TBD-8649)
- TPS-3161 - [7.1.1][20190823] Create table option if it doesn't exist for service account authentication type - tbigqueryoutput ( TBD-8708 )
- TPS-3225 - [7.1.1][20190726] tBigQueryBulkExec get error when run with big file (TBD-8853)
- TPS-3584 - [7.1.1][20191213] Header field is ignored in Service account mode tBigQueryBulkExec (TBD-8951)
- TPS-3809 - [7.1.1] tSSH component hangs if Command takes time to complete (TDI-43670)
- TPS-3939 - [ER] 7.1.1 Cumulative Patch - 20200410

## Installation
- From the Talend Studio 7.1.1 installation folder, make a copy of the following files somewhere safe:
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryBulkExec/tBigQueryBulkExec_begin.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryBulkExec/tBigQueryBulkExec_java.xml
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryInput/BigQueryInputQueryHelper.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryInput/tBigQueryInput_begin.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryInput/tBigQueryInput_end.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryInput/tBigQueryInput_java.xml
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryOutput/tBigQueryOutput_java.xml
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryOutputBulk/tBigQueryOutputBulk_main.javajet
- Unzip content of the patch zip onto your Talend Studio 7.1.1 folder.

## Uninstallation

- Replace the files overridden by the patch by the copy you made before unzipping.

## Affected files for this patch

The following files are installed by this patch:

- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryBulkExec/tBigQueryBulkExec_begin.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryBulkExec/tBigQueryBulkExec_java.xml
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryInput/BigQueryInputQueryHelper.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryInput/tBigQueryInput_begin.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryInput/tBigQueryInput_end.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryInput/tBigQueryInput_java.xml
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryOutput/tBigQueryOutput_java.xml
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tBigQueryOutputBulk/tBigQueryOutputBulk_main.javajet
