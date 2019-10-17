---
version: 7.2.1
module: https://talend.poolparty.biz/coretaxonomy/42
product:
- https://talend.poolparty.biz/coretaxonomy/183

---

# TPS-3495 <!-- mandatory -->

| Info             | Value |
| ---------------- | ---------------- |
| Patch Name       | Patch_20191017_TPS-3495_v1-7.2.1 |
| Release Date     | 2019-10-17 |
| Target Version    | 20190620\_1446-7.2.1 |
| Product affected | Talend Studio |

## Introduction <!-- mandatory -->

This patch is cumulative. It includes all previous generally available patches for Talend Studio 7.2.1.

**NOTE**: To download this patch, liaise with your Support contact at Talend.

## Fixed issues <!-- mandatory -->

This patch contains the following fixes:

- TPS-3495 [7.2.1] [tck:ADLSgen2] Short term solution for columns selector (TDI-43010)

This patch also includes the following patches:
- TPS-3489 [7.2.1] Build error with tRunJob on a spark job (TUP-24784)
- TPS-3476 [7.2.1] [20191018] Spark job fails on databricks when 'trim all' option is selected in the advanced settings of tFileInputDelimited component (TBD-9334)
- TPS-3460 [7.2.1] [20191101] "Invalid signature file digest for Manifest main attributes" Error with HD Insight Cluster (TBD-9289)
- TPS-3421 [7.2.1] Following TDI-42674, update dependency for Redshift database metadata (TUP-23960)
- TPS-3401 [7.2.1] JDBCInput component not working as expected when values as provided using context (TUP-24392)
- TPS-3449 [7.2.1] Route persists error even after resolving the issue(TESB-26300)
- TPS-3459 [7.2.1] Change Databricks API calls for Databricks 5.4 (TBD-9316)
- TPS-3413 [7.2.1] Default Context environment appended after migrating to 7.2.1 (TUP-24355)
- TPS-3429 [7.2.1][20190919] - ES6 support on tMatchIndex/tMatchIndexPredict (TDQ-16220)
- TPS-3336 [7.2.1] Improve Tck integration/installation/update in 7.2.1 (TUP-23106,TUP-23967,TUP-23264,TUP-23536,TUP-24226,TUP-24084)
- TPS-3402 [7.2.1] Build silently fails if Route calls a Routelet of different versions (TESB-26578)
- TPS-3422 [7.2.1] "java.lang.NoClassDefFoundError: org/talend/repository/services/model/services/ServiceItem" in studio(TESB-26856)
- TPS-3386 [7.2.1] Path in Textbox of "To archive file" is incomplete when building job (TUP-22646)
- TPS-3389 [7.2.1] Projects are switched to read only mode while reconnecting the session (TUP-24272)
- TPS-3387 [7.2.1] keyword "__TABLE__" not working for tJDBCxxxx components (TUP-24326)
- TPS-3406 [7.2.1] [Azure] Add Azure datacenter part of the studio connection
- TPS-3359 [7.2.1] JDBC metadata connection for Redshift when exported as context, unable to retrieve schema (TUP-24232)
- TPS-3323 [7.2.1] Error with cSplitter and jsonpath langage (TESB-26549)
- TPS-3383 [7.2.1] Jobs with tWindow not compiling in Spark Streaming Jobs - Studio 7.2.1 (TBD-9096)
- TPS-3374 [7.2.1] ERROR when Job in MDM project calling Job in Reference DI project:import routines.DataQuality cannot be resolved (TUP-23743)
- TPS-3291 [7.2.1] Rest Dataservice with Microservice build type fails with error LoggerFactory is not a Logback (TESB-26574)
- TPS-3303 [7.2.1] ESB - Docker Support for Microservices - CI Part (TESB-24597)
- TPS-3340 [7.2.1] Problem with Data Service Rest publishing (maven plugin) (TESB-26711)
- TPS-3334 [7.2.1] Snowflake components issue with cloud licence (TUP-24130)
- TPS-3243 [7.2.1] Backporting the support of Databricks 5.x and ADLS Gen 2 (TBD-7856) + Spark Batch fixes (TBD-8836, TBD-8850) + Configurable poll interval for Databricks job status (TBD-9006)
- TPS-3294 [7.2.1] Change security encryption of nexus (TUP-23087)
- TPS-3318 [7.2.1] Issue with nexus setup with cloud (TUP-24046)
- TPS-3287 [7.2.1] Cannot add more than 5 columns in tAggregateRow (TBD-8860)
- TPS-3273 [7.2.1] Error in calling spark job from trunjob job inside standard job (TUP-23950)
- TPS-3249 [7.2.1] Regression caused by TUP-21532 for ESB use case using tRunJob (TUP-23755)
- TPS-3263 [7.2.1] Studio changes in column order are not saved (no propagate changes dialog) (TUP-23809)
- TPS-3268 [7.2.1] ClassNotFoundException of the class of the job called by tRunJob in Routes (TESB-26048)
- TPS-3269 [7.2.1]  [Java 11] Failures when publishing to cloud using CI (maven plugin) (TESB-26461)
- TPS-3270 [7.2.1] Duplicated libraries in private & import packages of the build manifest file (TESB-26293)

## Prerequisites <!-- mandatory -->

Consider the following requirements for your system:

- Talend Studio 7.2.1 must be installed.
- To make TPS-3340 or TPS-3269 totally work, please replace the cloudpublisher-maven-plugin-7.2.1.jar into "{Studio_Home}/configuration/.m2/repository/org/talend/ci/cloudpublisher-maven-plugin/7.2.1" from "repository/org/talend/ci/cloudpublisher-maven-plugin/7.2.1" in patch zip.
- To make TPS-3243 or TPS-3459
- In the "{Studio_Home}/configuration/config.ini" file, add the following entry: 
    ,org.talend.hadoop.distribution.dbr540@start
- Clean the libraries installed on the Databricks cluster. To do this, 
   1. On the cluster side, click the "Libraries" tab and then select the check box to select all the libraries.
   2. Click "Uninstall".
   3. Restart the cluster.
- To make TPS-3269 totally work, please replace the cloudpublisher-maven-plugin-7.2.1.jar into "{Studio_Home}/configuration/.m2/repository/org/talend/ci/cloudpublisher-maven-plugin/7.2.1" from "repository/org/talend/ci/cloudpublisher-maven-plugin/7.2.1" in patch zip.
- To make TPS-3429 totally work when installing the patch using Commandline,  Before starting the studio, need to delete the folder Configuration/org.eclipse.osgi

## Installation <!-- mandatory -->

<!--
- Detailed installation steps for the customer.
- If any files need to be backed up before installation, it should be mentioned in this section.
- Two scenarios need to be considered for the installation:
 1. The customer has not yet installed any patch before => provide instructions for this
 2. The customer had installed one previous cumulative patch => provide instructions for this
-->

### Installing the patch for Cloud Studio

- If you want to fetch license from cloud Azure - USA West (for TPS-3406)

    1) Open "{your_patch_zip}/plugins".
    
    2) Open "{Studio_home}/configuration/org.talend.configurator/bundles.info".
    
    3) CHANGE the version of org.talend.license.gui and org.talend.license.gui.talend in bundles.info(in the last two lines) TO the version of the jars in plugins.
    
    Example：
    
    The jars in plugin folder of the patch zip are "org.talend.license.gui_7.2.1.20190909_1200_patch.jar" and "org.talend.license.gui.talend_7.2.1.20190909_1200_patch.jar",
    MODIFY them in bundles.info file to "org.talend.license.gui,7.2.1.20190909_1200_patch,plugins/org.talend.license.gui_7.2.1.20190909_1200_patch.jar,4,false" and 
    "org.talend.license.gui.talend,7.2.1.20190909_1200_patch,plugins/org.talend.license.gui.talend_7.2.1.20190909_1200_patch.jar,4,false"
    
### Installing the patch using Software update <!-- if applicable -->

1) Logon TAC and switch to Configuration->Software Update, then enter the correct values and save referring to the documentation: https://help.talend.com/reader/f7Em9WV_cPm2RRywucSN0Q/j9x5iXV~vyxMlUafnDejaQ

2) Switch to Software update page, where the new patch will be listed. The patch can be downloaded from here into the nexus repository.

3) On Studio Side: Logon Studio with remote mode, on the logon page the Update button is displayed: click this button to install the patch.

### Installing the patch using Talend Studio <!-- if applicable -->

1) Create a folder named "patches" under your studio installer directory and copy the patch .zip file to this folder.

2) Restart your studio: a window pops up, then click OK to install the patch, or restart the commandline and the patch will be installed automatically.

### Installing the patch using Commandline <!-- if applicable -->

Execute the following commands:

1. Talend-Studio-win-x86_64.exe -nosplash -application org.talend.commandline.CommandLine -consoleLog -data commandline-workspace startServer -p 8002 --talendDebug
2. initRemote {tac_url} -ul {TAC login username} -up {TAC login password}
3. checkAndUpdate -tu {TAC login username} -tup {TAC login password}
