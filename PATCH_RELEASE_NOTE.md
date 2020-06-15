---
version: 7.2.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-4066

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20200615_TPS-4066\_v1-7.2.1 |
| Release Date     | 2020-06-15 |
| Target Version   | 20190620\_1446-V7.2.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fixes:

- TPS-4066 [7.2.1] BigQueryBulkExec Component fails when DATE type column has empty fields (TDI-44165)

## Prerequisites

Consider the following requirements for your system:

- Talend Studio 7.2.1 must be installed.
- TPS-3294 - [7.2.1] USAF Case/Jira Status- Case 00144616 (TUP-23087).
- TPS-3356 - [7.2.1][20190927] tBigQueryInput does not support backtick in query in 7.2.1 (TBD-9137).
- TPS-3406 - [7.2.1][Azure] Add Azure datacenter part of the studio connection
- TPS-3592 - [7.2.1][20191206] tBigQueryBulkExec loading data from Google Cloud Storage to BigQuery failed after setting header
- TPS-3681 - 7.2.1 Cumulative Patch - 20200228

## Installation
- From the Talend Studio 7.2.1 installation folder, make a copy of the following files somewhere safe:
    - {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryBulkExec/tBigQueryBulkExec_begin.javajet
    - {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryBulkExec/tBigQueryBulkExec_java.xml
    - {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryBulkExec/tBigQueryBulkExec_messages.properties
    - {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryOutputBulk/tBigQueryOutputBulk_java.xml
    - {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryOutputBulk/tBigQueryOutputBulk_main.javajet
    - {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryOutputBulk/tBigQueryOutputBulk_messages.properties
- Unzip content of the patch zip onto your Talend Studio 7.2.1 folder.

## Uninstallation

- Replace the files overriden by the patch by the copy you made before unzipping.

## Affected files for this patch

The following files are installed by this patch:

- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryBulkExec/tBigQueryBulkExec_begin.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryBulkExec/tBigQueryBulkExec_java.xml
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryBulkExec/tBigQueryBulkExec_messages.properties
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryOutputBulk/tBigQueryOutputBulk_java.xml
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryOutputBulk/tBigQueryOutputBulk_main.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.2.1.20190614_0309/components/tBigQueryOutputBulk/tBigQueryOutputBulk_messages.properties
