---
version: 7.1.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-4667

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20210129\_TPS-4667\_v1-7.1.1 |
| Release Date     | 2021-01-29 |
| Target Version   | 20181026\_1147-V7.1.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fixes:

- TPS-4667 [7.1.1] Unable to create table using tBigqueryoutput component for SA Auth(TDI-43025). Fix previous TPS-4399
- TPS-4434 [7.1.1] Unable to write into Bigquery table when user defined delimiter is used
- TBD-9137 tBigQueryInput does not support backtick in query

## Prerequisites

Consider the following requirements for your system:

- Talend Studio 7.1.1 must be installed.

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
