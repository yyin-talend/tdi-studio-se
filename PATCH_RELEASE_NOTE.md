---
version: 7.1.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-4553

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20201224\_TPS-4553\_v1-7.1.1 |
| Release Date     | 2020-12-24 |
| Target Version   | 20181026\_1147-V7.1.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fixes:

- TPS-4553 [7.1.1] tFileInputPositional inappropriately handles CR(\r) as a row separator (TDI-45260)

## Prerequisites

Consider the following requirements for your system:

- Talend Studio 7.1.1 must be installed.

## Installation
- From the Talend Studio 7.1.1 installation folder, make a copy of the following files somewhere safe:
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileInputPositional/tFileInputPositional_begin.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileInputPositional/tFileInputPositional_java.xml
- {Talend_Studio_path}/configuration/.m2/repository/org/talend/components/lib/talend_file_enhanced/1.1-patch/talend_file_enhanced-1.1-patch.jar

- Unzip content of the patch zip onto your Talend Studio 7.1.1 folder.

## Uninstallation

- Replace the files overridden by the patch by the copy you made before unzipping.

## Affected files for this patch

The following files are installed by this patch:

- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileInputPositional/tFileInputPositional_begin.javajet
- {Talend_Studio_path}/plugins/org.talend.designer.components.localprovider_7.1.1.20181026_1147/components/tFileInputPositional/tFileInputPositional_java.xml
- {Talend_Studio_path}/configuration/.m2/repository/org/talend/components/lib/talend_file_enhanced/1.1-patch/talend_file_enhanced-1.1-patch.jar