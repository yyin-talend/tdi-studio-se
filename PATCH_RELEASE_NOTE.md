---
version: 8.0.1
module: https://talend.poolparty.biz/coretaxonomy/42
product: 
- https://talend.poolparty.biz/coretaxonomy/183
- https://talend.poolparty.biz/coretaxonomy/23
---

# TPS-5390

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch\_20221202\_TPS-5390\_v1-8.0.1 |
| Release date     | 2022-12-02 |
| Target version    | 20211109\_1610-8.0.1 |
| Product affected | Talend Studio |

## Introduction

This is a self-contained patch.

**NOTE**: For information on how to obtain this patch, reach out to your Support contact at Talend.

## Fixed issues

This patch contains the following fix:

- TDI-48617 [8.0.1] tFTPConnection : Sftp issue after R2022-08  jsch-0.2.1 library ( PubkeyAcceptedKeyTypes )



## Prerequisites

Consider the following requirements for your system:

- Must install Talend Studio 8.0.1 with the monthly released patch, Patch_20221118_R2022-11_v1-8.0.1.zip.
- Or must update the Talend Studio 8.0.1 with the URL, https://update.talend.com/Studio/8/updates/R2022-08/.

## Installation

- Installation On Studio:

1. Shut down Talend studio if it is opened.
2. Extract the zip.
3. Merge the folder "plugins"  and its content to "{studio}/plugins" and overwrite the existing files. 
4. remove the folder "{studio}/configuration/org.eclipse.osgi".
5. Start the Talend studio.
6. Rebuild your jobs.
