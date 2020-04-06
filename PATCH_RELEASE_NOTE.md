---
version: 7.2.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-3918

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20200403\_TPS-3918\_v1-7.2.1 |
| Release Date     | 2020-04-30 |
| Target Version   | 7.2.1.20190620_1446 |
| Product affected | Talend Studio |

## Introduction 

This is a self-contained patch.


**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues <!-- mandatory -->

This patch contains the following fixes:

- TDI-43764 : MsSQL Output possible information loss when using UTF8 and NVARCHAR with MsSQL JDBC


## Prerequisites

Consider the following requirements for your system:

- Talend Studio 7.2.1 must be installed.

## Installation
- From the Talend Studio 7.2.1 installation folder, make a copy of the following files somewhere safe:
    - plugins/org.talend.designer.components.localprovider_7.2.1.20190620_1446/components/templates/db_output_bulk.skeleton
	- plugins/org.talend.designer.components.localprovider_7.2.1.20190620_1446/components/tMSSqlConnection/tMSSqlConnection_begin.javajet
- Unzip content of the patch zip onto your Talend Studio 7.2.1 folder.

## Uninstallation

- Replace the files overriden by the patch by the copy you made before unzipping.

## Affected files for this patch

The following files are installed by this patch:

- plugins/org.talend.designer.components.localprovider_7.2.1.20190620_1446/components/templates/db_output_bulk.skeleton
- plugins/org.talend.designer.components.localprovider_7.2.1.20190620_1446/components/tMSSqlConnection/tMSSqlConnection_begin.javajet