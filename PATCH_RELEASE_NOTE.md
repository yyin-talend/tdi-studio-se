---
version: 7.1.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-4636

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20210119_TPS-4636\_v1-7.1.1 |
| Release Date     | 2021-01-19 |
| Target Version   | 20181026\_1147-V7.1.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fixes:

- TPS-4636 [7.1] Not able to create excel file (.xlsx) after migrating to 7.1 from 6.3 using tFileOutputExcel component (TDI-45455)
- TDI-45404 tFileOutputExcel component gives The maximum number of cell styles was exceeded error

## Prerequisites

Consider the following requirements for your system:

- Talend Studio 7.1.1 must be installed.
- TPS-4416 - 7.1.1 Cumulative Patch - 20201211.

## Installation
- From the Talend Studio 7.1.1 installation folder, make a copy of the following files somewhere safe:
    - {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileOutputExcel/tFileOutputExcel_begin.javajet
    - {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileOutputExcel/tFileOutputExcel_java.xml
    - {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileOutputExcel/tFileOutputExcel_messages.properties
    - {Talend_Studio_path}/plugins/org.talend.repository_7.1.1.20201210_0723-patch.jar
- Unzip content of the patch zip onto your Talend Studio 7.1.1 folder.

## Uninstallation

- Replace the files overriden by the patch by the copy you made before unzipping.

## Affected files for this patch

The following files are installed by this patch:

- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileOutputExcel/tFileOutputExcel_begin.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileOutputExcel/tFileOutputExcel_java.xml
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileOutputExcel/tFileOutputExcel_messages.properties
- {Talend_Studio_path}/plugins/org.talend.repository_7.1.1.20201210_0723-patch.jar
- {Talend_Studio_path}/configuration/.m2/repository/org/talend/components/talendExcel/1.10-20210113-patched/talendExcel-1.10-20210113-patched.jar
