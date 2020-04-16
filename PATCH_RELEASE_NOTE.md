---
version: 7.2.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-3235

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20200416\_TPS-3928\_v1-7.2.1 |
| Release Date     | 2020-04-16 |
| Target Version   | Talend-Studio-20190620\_1446-V7.2.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fixes:

- TPS-3928 [7.2.1] Exceptions thrown by tDotNETRow crash the JVM (TDI-43929)

## Prerequisites

Consider the following requirements for your system:

- Talend Studio 7.2.1 must be installed.

## Installation

### Manual installation 
It needs manual installation because the file janet-win64.dll needs to be updated.
1) Unpack "plugins" folder from the archive into studio's root folder. So that the files 
    "tDotNETInstantiate\_java.xml", "tDotNETRow\_java.xml", "tMSAXInput\_java.xml", "tMSAXOutput\_java.xml", "tOleDbInput\_java.xml", "tOleDbOutput\_java.xml", "tOleDbRow\_java.xml"
	will be replaced. 
2) Find the local file "janet-win64.dll". 
   It should be in: 
   a) the folder where "java.library.path" variable points out
   b) or in the %WINDIR%\System32 folder (e.g. "C:\Windows\System32")

   Replace the file with one of the files either from "janet-dlls_35" or "janet-dlls_40" folder. The folders are in the patch's root.
   Use "janet-dlls_35" for .NET 3.5 or "janet-dlls_40" for .NET 4.0 and higher. 

## Uninstallation
Backup the Affected files list below. Uninstall the patch by restore the backup files.

## Affected files for this patch

The following files are installed by this patch:

- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tDotNETInstantiate/tDotNETInstantiate\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tDotNETRow/tDotNETRow\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMSAXInput/tMSAXInput\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tMSAXOutput/tMSAXOutput\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOleDbInput/tOleDbInput\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOleDbOutput/tOleDbOutput\_java.xml
- {Talend\_Studio\_path}/plugins/org.talend.designer.components.localprovider\_7.2.1.20190614\_0309/components/tOleDbRow/tOleDbRow\_java.xml
- janet-win64.dll (the file is in the place where java.library.path variable points out)