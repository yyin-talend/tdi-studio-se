<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="ucase"
			select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />
		<xsl:variable name="lcase"
			select="'abcdefghijklmnopqrstuvwxyz'" />
		<html>
			<head>
				<title>Generated documentation</title>
				<style type="text/css">
					@page { size: letter; } 
					SPAN.special { font:12pt black; } 
					TABLE.properties { width:95%; } 
					TD.propname { width:30%; font:bold; } 
					TR.profont { font:bold; }
					TD.propval { width:70%; } 
					TD.dependtype { width:20%;} 
					TD.dependloc { width:60%; } 
					TABLE.cols {width:90%; } 
					TD.constraint { width:20%; font:bold; }
					H3.hand { cursor:hand; } 
					img.bordercolor {border-color:#AFCA00; } 
					.FONTSTYLE {font-family:Arial, Helvetica, sans-serif;} 
					.LINKSTYLE {TEXT-DECORATION:none}
					a:hover {TEXT-DECORATION:underline}
					.TITLESTYLE {font-size: 26px; color: #818181;}
					.TOPTITLESTYLE {font-size: 40px; color: #818181;}
					.TABLECOLUMNSTYLE {font-family: Arial, Helvetica,sans-serif; color: #818181; background-color:#E6E6E6;align: center} 
					tr {page-break-inside: avoid}
				</style>
			</head>
			<body>
				<br />
				<table border="0" cellspacing="0" width="90%"
					class="FONTSTYLE" summary="Job&#160;documentation">
					<tr valign="top">
						<!--<td width="15%" rowspan="2" align="center"></td> -->
						<td  align="center">
							<a href="{project/@link}">
								<img src="{project/@logo}" border="0"
									align="bottom" alt="" />
							</a>
						</td>
						<!-- <td width="1%" rowspan="2" align="center"></td> -->
						<td class="TOPTITLESTYLE">
							<xsl:value-of select="/project/@docType" />
						</td>
					</tr>
					<tr valign="top">
						<td height="20" align="center" valign="top" class="TITLESTYLE">
							<xsl:value-of select="/project/@company" />
						</td>
						<td height="20" align="left" valign="top"
							class="TITLESTYLE">
							<xsl:value-of select="/project/@title" />
						</td>
					</tr>
				</table>
				<br />
				<br />
				<table border="1" width="90%" cellpadding="0"
					cellspacing="0" style="border-collapse: collapse"
					bordercolor="#111111" class="FONTSTYLE" summary="">
					<tr valign="top">
						<td align="center" width="25%"
							class="TABLECOLUMNSTYLE">
							PROJECT NAME
						</td>
						<td align="center" width="25%"
							class="FONTSTYLE">
							<xsl:value-of select="/project/@name" />
						</td>
						<td align="center" width="25%"
							class="TABLECOLUMNSTYLE">
							GENERATION DATE
						</td>
						<td align="center" width="25%">
							<xsl:value-of
								select="/project/@generatedDate" />
						</td>
					</tr>
					<tr>
						<td align="center" width="25%"
							class="TABLECOLUMNSTYLE">
							AUTHOR
						</td>
						<td align="center" width="25%">
							<xsl:value-of select="/project/job/@author" />
						</td>
						<td align="center" width="25%"
							class="TABLECOLUMNSTYLE">
							<xsl:value-of
								select="/project/@versionName" />
						</td>
						<td align="center" width="25%"
							class="FONTSTYLE">
							<xsl:value-of select="/project/@version" />
						</td>
					</tr>
				</table>
				<h2 class="FONTSTYLE">Summary</h2>
				<div class="FONTSTYLE">
					<b>
						<a href="#ProjectDescription">
							Project Description
						</a>
					</b>
				</div>
				<div class="FONTSTYLE">
					<b>
						<a href="#JobDescription">Job Description</a>
					</b>
				</div>
				<div class="FONTSTYLE">
					<b>
						<a href="#JobPreviewPicture">
							Job Preview Picture
						</a>
					</b>
				</div>
				<div class="FONTSTYLE">
					<b>
						<a href="#Job Settings">Job Settings</a>
					</b>
				</div>
				<div class="FONTSTYLE">
					<b>
						<a href="#Context List">Context List</a>
					</b>
				</div>
				<div class="FONTSTYLE">
					<b>
						<a href="#ComponentList">Component List</a>
					</b>
				</div>
				<div class="FONTSTYLE">
					<b>
						<a href="#ComponentsDescription">
							Components Description
						</a>
					</b>
				</div>
				<xsl:variable name="job" select="/project/job" />
				<xsl:variable name="code" select="$job/sourcecodes"></xsl:variable>
				<xsl:if test="$code">
				<div class="FONTSTYLE">
					<b>
						<a href="#SourceCode">Source Code</a>
					</b>
				</div>
				</xsl:if>
				<br />
				<br />
				<!-- Project Description-->
				<!--HR-->
				<h2 class="FONTSTYLE">
					<a name="#ProjectDescription">
						Project Description
					</a>
				</h2>
				<br />
				<table border="1" width="90%" class="FONTSTYLE"
					cellpadding="0" cellspacing="0"
					style="border-collapse: collapse; padding-left:10mm;"
					bordercolor="#111111" summary="">
					<tr>
						<th align="left" width="30%"
							class="TABLECOLUMNSTYLE">
							Properties
						</th>
						<th align="left" class="TABLECOLUMNSTYLE">
							Values
						</th>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">Name</td>
						<td class="FONTSTYLE" align="left">
							<xsl:value-of select="/project/@name" />
						</td>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">Language</td>
						<td class="FONTSTYLE" align="left">
							<xsl:value-of select="/project/@language" />
						</td>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">
							Description
						</td>
						<td class="FONTSTYLE" align="left">
							<pre>
								<xsl:value-of
									select="/project/pro-description" disable-output-escaping="yes" />
							</pre>
						</td>
					</tr>
				</table>
				<br />
				<br />
				<h2 class="FONTSTYLE">
					<a name="#JobDescription">Job Description</a>
				</h2>
				<br />
				<table border="1" width="90%" cellpadding="0"
					cellspacing="0"
					style="border-collapse: collapse; padding-left:10mm;"
					bordercolor="#111111" frame="box" summary="">
					<tr>
						<th align="left" width="30%"
							class="TABLECOLUMNSTYLE">
							Properties
						</th>
						<th align="left" class="TABLECOLUMNSTYLE">
							Values
						</th>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">Name</td>
						<td class="FONTSTYLE" align="left">
							<xsl:value-of select="$job/@name" />
						</td>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">Author</td>
						<td class="FONTSTYLE" align="left">
							<xsl:value-of select="$job/@author" />
						</td>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">Version</td>
						<td class="FONTSTYLE" align="left">
							<xsl:value-of select="$job/@version" />
						</td>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">Purpose</td>
						<td class="FONTSTYLE" align="left">
							<xsl:value-of select="$job/@purpose" />
						</td>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">Status</td>
						<td class="FONTSTYLE" align="left">
							<xsl:value-of select="$job/@status" />
						</td>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">
							Description
						</td>
						<td class="FONTSTYLE" align="left">
								<pre>
								<xsl:value-of select="$job/description"  disable-output-escaping="yes"/>
								</pre>
							
						</td>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">Creation</td>
						<td class="FONTSTYLE" align="left">
							<xsl:value-of select="$job/@creation" />
						</td>
					</tr>
					<tr>
						<td class="FONTSTYLE" align="left">
							Modification
						</td>
						<td class="FONTSTYLE" align="left">
							<xsl:value-of select="$job/@modification" />
						</td>
					</tr>
				</table>
				<br />
				<!-- Project Description-->
				<!--HR-->
				<xsl:variable name="jobPreviewPicture"
					select="$job/preview/@picture" />
				<xsl:if test="string-length($jobPreviewPicture)!=0">
					<h2 class="FONTSTYLE">
						<a name="#JobPreviewPicture">
							Job Preview Picture
						</a>
					</h2>
					<br />
					<div class="FONTSTYLE" align="center">
						<img src="{$jobPreviewPicture}" usemap="#jobmap"
							alt="No image available" class="bordercolor" />
					</div>
					<map id="jobmap" name="jobmap">
						<xsl:for-each
							select="$job/componentList/componentItem">
							<area shape="rect"
								coords="{@leftTopX},{@leftTopY},{@rightBottomX},{@rightBottomY}"
								href="#{@link}" alt="{@name}" />
						</xsl:for-each>
					</map>
				</xsl:if>
				<br />
				<br />
				<!-- Job settings -->
				<!--HR-->
				<h2 class="FONTSTYLE">
					<a name="#Job settings">Job settings</a>
				</h2>
				<table border="0" width="80%" class="FONTSTYLE"
					cellpadding="0" cellspacing="0" style="border-collapse: collapse"
					bordercolor="#111111" summary="">
					<tr bgcolor="#E6E6E6" class="FONTSTYLE">
						<td class="FONTSTYLE">
							<b>Extra settings</b>
						</td>
					</tr>
				</table>
				<br />
				<xsl:variable name="extra"
					select="$job/jobSetting/extra">
				</xsl:variable>
				<table width="80%" border="1" cellpadding="0"
					cellspacing="0"
					style="border-collapse: collapse; padding-left:10mm;"
					bordercolor="#111111" frame="box" summary="">
					<tr>
						<th width="50%" align="left"
							class="TABLECOLUMNSTYLE">
							Name
						</th>
						<th width="50%" align="left"
							class="TABLECOLUMNSTYLE">
							Value
						</th>
					</tr>
					<xsl:for-each select="$extra/jobParameter">
						<tr>
							<td class="FONTSTYLE" align="left">
								<xsl:value-of select="@name" />
							</td>
							<td class="FONTSTYLE" align="left">
								<xsl:value-of select="@value" />
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<br />
				<br />
				<table border="0" width="80%" class="FONTSTYLE"
					cellpadding="0" cellspacing="0" style="border-collapse: collapse"
					bordercolor="#111111" summary="">
					<tr bgcolor="#E6E6E6" class="FONTSTYLE">
						<td class="FONTSTYLE">
							<b>Stats &amp; Logs</b>
						</td>
					</tr>
				</table>
				<br />
				<xsl:variable name="statsAndLog"
					select="$job/jobSetting/Statslogs">
				</xsl:variable>
				<table width="80%" border="1" cellpadding="0"
					cellspacing="0"
					style="border-collapse: collapse; padding-left:10mm;"
					bordercolor="#111111" frame="box" summary="">
					<tr>
						<th width="50%" align="left"
							class="TABLECOLUMNSTYLE">
							Name
						</th>
						<th width="50%" align="left"
							class="TABLECOLUMNSTYLE">
							Value
						</th>
					</tr>
					<xsl:for-each select="$statsAndLog/jobParameter">
						<tr>
							<td class="FONTSTYLE" align="left">
								<xsl:value-of select="@name" />
							</td>
							<td class="FONTSTYLE" align="left">
								<xsl:value-of select="@value" />
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<br />
				<br />
				<!-- Context List-->
				<!--HR-->
				<h2 class="FONTSTYLE">
					<a name="#Context List">Context List</a>
				</h2>
				<xsl:for-each select="$job/contextList/context">
					<table border="0" width="80%" class="FONTSTYLE"
						cellpadding="0" cellspacing="0" style="border-collapse: collapse"
						bordercolor="#111111" summary="">
						<tr bgcolor="#E6E6E6" class="FONTSTYLE">
							<td class="FONTSTYLE">
								<b>
									Context :
									<xsl:value-of select="@name" />
								</b>
							</td>
						</tr>
					</table>
					<br />
					<table width="80%" border="1" cellpadding="0"
						cellspacing="0"
						style="border-collapse: collapse; padding-left:10mm;"
						bordercolor="#111111" frame="box" summary="">
						<tr>
							<th width="20%" align="left"
								class="TABLECOLUMNSTYLE">
								Name
							</th>
							<th width="25%" align="left"
								class="TABLECOLUMNSTYLE">
								Prompt
							</th>
							<th width="20%" align="left"
								class="TABLECOLUMNSTYLE">
								Need Prompt?
							</th>
							<th width="10%" align="left"
								class="TABLECOLUMNSTYLE">
								Type
							</th>
							<th width="15%" align="left"
								class="TABLECOLUMNSTYLE">
								Value
							</th>
							<th width="10%" align="left"
								class="TABLECOLUMNSTYLE">
								Source
							</th>
						</tr>
						<xsl:for-each
							select="contextParameter">
							<tr>
								<td class="FONTSTYLE" align="left">
									<xsl:value-of select="@name" />
								</td>
								<td class="FONTSTYLE" align="left">
									<xsl:value-of select="@prompt" />
								</td>
								<td class="FONTSTYLE" align="left">
									<xsl:value-of
										select="@promptNeeded" />
								</td>
								<td class="FONTSTYLE" align="left">
									<xsl:value-of select="@type" />
								</td>
								<td class="FONTSTYLE" align="left">
									<xsl:value-of select="@value" />
								</td>
								<td class="FONTSTYLE" align="left">
									<xsl:value-of select="@source" />
								</td>
							</tr>
						</xsl:for-each>
					</table>
					<br />
					<br />
				</xsl:for-each>
				<br />
				<br />
				<!-- Component List-->
				<!--HR-->
				<h2 class="FONTSTYLE">
					<a name="#ComponentList">Component List</a>
				</h2>
				<table width="60%" border="1" cellpadding="0"
					cellspacing="0"
					style="border-collapse: collapse; padding-left:10mm;"
					bordercolor="#111111" frame="box" summary="">
					<tr>
						<th width="50%" align="left"
							class="TABLECOLUMNSTYLE">
							Component Name
						</th>
						<th width="50%" align="left"
							class="TABLECOLUMNSTYLE">
							Component Type
						</th>
					</tr>
					<xsl:for-each
						select="$job/componentList/componentItem">
						<tr>
							<td class="FONTSTYLE" align="left">
								<a href="#{@link}" class="LINKSTYLE">
									<xsl:value-of select="@name" />
								</a>
							</td>
							<td class="FONTSTYLE" align="left">
								<xsl:value-of select="@type" />
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<br />
				<!-- Components Description-->
				<!--HR-->
				<h2 class="FONTSTYLE">
					<a name="#ComponentsDescription">
						Components Description
					</a>
				</h2>
				<xsl:for-each
					select="$job/internalNodeComponents/component">
					<a name="{@uniqueName}" />
					<table border="0" width="90%" class="FONTSTYLE"
						cellpadding="0" cellspacing="0" style="border-collapse: collapse"
						bordercolor="#111111" summary="">
						<tr bgcolor="#E6E6E6" class="FONTSTYLE">
							<td class="FONTSTYLE">
								<b>
									Component:&#160;&#160;
									<xsl:value-of
										select="componentType" />
								</b>
							</td>
						</tr>
					</table>
					<BR />
					<table border="1" width="90%" class="FONTSTYLE"
						cellpadding="0" cellspacing="0" style="border-collapse: collapse"
						bordercolor="#111111" summary="">
						<tr>
							<td class="cols" align="center" rowspan="2"
								width="10%">
								<img src="{@icon}" alt="" />
								&#160;&#160;&#160;&#160;&#160;
							</td>
							<td align="center" class="TABLECOLUMNSTYLE"
								width="15%">
								UNIQUE NAME
							</td>
							<td class="FONTSTYLE" align="center"
								width="15%">
								<xsl:value-of select="@uniqueName" />
							</td>
							<td align="center" class="TABLECOLUMNSTYLE"
								width="15%">
								INPUT(S)
							</td>
							<td class="FONTSTYLE" align="center">
								<xsl:for-each select="input">
									<xsl:choose>
										<xsl:when
											test="text() = 'none'">
											<xsl:value-of
												select="text()" />
										</xsl:when>
										<xsl:otherwise>
											<a href="#{@link}"
												style="LINKSTYLE">
												<xsl:value-of
													select="text()" />
											</a>
											<xsl:if
												test="position()!= last()">
												<xsl:text>,&#160;&#160;</xsl:text>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
							</td>
						</tr>
						<tr>
							<td align="center"
								class="TABLECOLUMNSTYLE">
								LABEL
							</td>
							<td align="center">
								<xsl:value-of select="@label" />
							</td>
							<td align="center"
								class="TABLECOLUMNSTYLE">
								OUTPUT(S)
							</td>
							<td class="FONTSTYLE" align="center">
								<xsl:for-each select="output">
									<xsl:choose>
										<xsl:when
											test="text() = 'none'">
											<xsl:value-of
												select="text()" />
										</xsl:when>
										<xsl:otherwise>
											<a href="#{@link}"
												style="LINKSTYLE">
												<xsl:value-of
													select="text()" />
											</a>
											<xsl:if
												test="position()!= last()">
												<xsl:text>,&#160;&#160;</xsl:text>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
							</td>
						</tr>
					</table>
					<xsl:variable name="previewPicture"
						select="@preview" />
					<xsl:if test="string-length($previewPicture)!=0">
						<br />
						<div class="FONTSTYLE">
							<img src="{$previewPicture}"
								usemap="#jobmap" alt="No image available" class="bordercolor" />
						</div>
					</xsl:if>
					<br />
					
					<b class="FONTSTYLE">Component Parameters:</b>
					<br />
					<table class="cols" width="90%" border="1"
						cellpadding="0" cellspacing="0"
						style="border-collapse: collapse; padding-left:10mm;"
						bordercolor="#111111" frame="box" summary="">
						<tr>
							<th align="left" width="30%"
								class="TABLECOLUMNSTYLE">
								Properties
							</th>
							<th align="left" width="70%"
								class="TABLECOLUMNSTYLE">
								Values
							</th>
						</tr>
						<xsl:for-each select="parameters/column">
							<tr>
								<td class="FONTSTYLE" align="left">
									<xsl:variable name="propname"
										select="@name" />
									<xsl:value-of select="$propname" />
								</td>
								<td class="FONTSTYLE" align="left">
									<xsl:value-of select="text()" />
								</td>
							</tr>
						</xsl:for-each>
					</table>
					<br />
					<xsl:for-each select="schemas/schema">
						<b class="FONTSTYLE">
							Schema for
							<xsl:value-of select="@name" />
							:
						</b>
						<br />
						<table class="cols" width="90%" border="1"
							cellpadding="0" cellspacing="0" style="border-collapse: collapse"
							bordercolor="#111111" frame="box" summary="">
							<tr class="profont">
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Column
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Key
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Type
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Length
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Precision
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Nullable
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="16%">
									Comment
								</th>
							</tr>
							<xsl:for-each select="column">
								<tr class="FONTSTYLE">
									<td align="center">
										<xsl:value-of select="@name" />
									</td>
									<td align="center">
										<xsl:value-of select="@key" />
									</td>
									<td align="center">
										<xsl:value-of select="@type" />
									</td>
									<td align="center">
										<xsl:value-of select="@length" />
									</td>
									<td align="center">
										<xsl:value-of
											select="@precision" />
									</td>
									<td align="center">
										<xsl:value-of
											select="@nullable" />
									</td>
									<td align="center">
										<xsl:value-of select="@comment" />
									</td>
								</tr>
							</xsl:for-each>
						</table>
					</xsl:for-each>
					<br />
					<b class="FONTSTYLE">Original Function Parameters:</b>
					<br/>
					<xsl:comment><xsl:value-of select="@uniqueName" />ended</xsl:comment>
				</xsl:for-each>
				<xsl:for-each
					select="$job/externalNodeComponents/component">
					<a name="{@uniqueName}" />
					<table border="0" width="90%" class="FONTSTYLE"
						cellpadding="0" cellspacing="0" style="border-collapse: collapse"
						bordercolor="#111111" summary="">
						<tr bgcolor="#E6E6E6">
							<td class="FONTSTYLE">
								<b>
									Component:&#160;&#160;
									<xsl:value-of
										select="componentType" />
								</b>
							</td>
						</tr>
					</table>
					<BR />
					<table border="1" width="90%" class="FONTSTYLE"
						cellpadding="0" cellspacing="0" style="border-collapse: collapse"
						bordercolor="#111111" summary="">
						<tr>
							<td class="cols" align="center" rowspan="2"
								width="10%">
								<img src="{@icon}" alt="" />
								&#160;&#160;&#160;&#160;&#160;
							</td>
							<td align="center" class="TABLECOLUMNSTYLE"
								width="15%">
								UNIQUE NAME
							</td>
							<td class="FONTSTYLE" align="center"
								width="15%">
								<xsl:value-of select="@uniqueName" />
							</td>
							<td align="center" class="TABLECOLUMNSTYLE"
								width="15%">
								INPUT(S)
							</td>
							<td class="FONTSTYLE" align="center">
								<xsl:for-each select="input">
									<xsl:choose>
										<xsl:when
											test="text() = 'none'">
											<xsl:value-of
												select="text()" />
										</xsl:when>
										<xsl:otherwise>
											<a href="#{@link}"
												style="LINKSTYLE">
												<xsl:value-of
													select="text()" />
											</a>
											<xsl:if
												test="position()!= last()">
												<xsl:text>,&#160;&#160;</xsl:text>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
							</td>
						</tr>
						<tr>
							<td align="center"
								class="TABLECOLUMNSTYLE">
								LABEL
							</td>
							<td align="center">
								<xsl:value-of select="@label" />
							</td>
							<td align="center"
								class="TABLECOLUMNSTYLE">
								OUTPUT(S)
							</td>
							<td class="FONTSTYLE" align="center">
								<xsl:for-each select="output">
									<xsl:choose>
										<xsl:when
											test="text() = 'none'">
											<xsl:value-of
												select="text()" />
										</xsl:when>
										<xsl:otherwise>
											<a href="#{@link}"
												style="LINKSTYLE">
												<xsl:value-of
													select="text()" />
											</a>
											<xsl:if
												test="position()!= last()">
												<xsl:text>,&#160;&#160;</xsl:text>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
							</td>
						</tr>
					</table>
					<xsl:variable name="previewPicture"
						select="@preview" />
					<xsl:if test="string-length($previewPicture)!=0">
						<br />
						<div class="FONTSTYLE">
							<img src="{$previewPicture}"
								usemap="#jobmap" alt="No image available" class="bordercolor" />
						</div>
					</xsl:if>
					<br />
					<xsl:for-each select="parameters">
					<b class="FONTSTYLE">Component Parameters:</b>
					<br />
					<table width="90%" class="FONTSTYLE" border="1"
						cellpadding="0" cellspacing="0" style="border-collapse: collapse"
						bordercolor="#111111" frame="box" summary="">
						<tr>
							<th align="center" width="30%"
								class="TABLECOLUMNSTYLE">
								Properties
							</th>
							<th align="left" width="70%"
								class="TABLECOLUMNSTYLE">
								&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;Values
							</th>
						</tr>
						<xsl:for-each select="parameters/column">
							<tr>
								<td class="FONTSTYLE" align="center">
									<xsl:variable name="propname"
										select="@name" />
									<xsl:value-of select="$propname" />
								</td>
								<td class="FONTSTYLE" align="left">
									&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
									<xsl:value-of select="text()" />
								</td>
							</tr>
						</xsl:for-each>
					</table>
					</xsl:for-each>
					<br />
					<xsl:for-each select="schemas/schema">
						<b class="FONTSTYLE">
							Schema for
							<xsl:value-of select="@name" />
							:
						</b>
						<br />
						<table class="cols" border="1" width="90%"
							cellpadding="0" cellspacing="0" style="border-collapse: collapse"
							bordercolor="#111111" frame="box" summary="">
							<tr class="profont">
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Column
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Key
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Type
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Length
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Precision
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="14%">
									Nullable
								</th>
								<th class="TABLECOLUMNSTYLE"
									width="16%">
									Comment
								</th>
							</tr>
							<xsl:for-each select="column">
								<tr class="FONTSTYLE">
									<td align="center">
										<xsl:value-of select="@name" />
									</td>
									<td align="center">
										<xsl:value-of select="@key" />
									</td>
									<td align="center">
										<xsl:value-of select="@type" />
									</td>
									<td align="center">
										<xsl:value-of select="@length" />
									</td>
									<td align="center">
										<xsl:value-of
											select="@precision" />
									</td>
									<td align="center">
										<xsl:value-of
											select="@nullable" />
									</td>
									<td align="center">
										<xsl:value-of select="@comment" />
									</td>
								</tr>
							</xsl:for-each>
						</table>
					</xsl:for-each>
					<br />
					<xsl:comment><xsl:value-of select="@uniqueName" />ended</xsl:comment>
					<xsl:text /><!--before: $job/externalNodeComponents/component/@uniqueName -->
				</xsl:for-each>
					<!-- Source Code-->
				<!--HR-->
				<xsl:if test="$code">
				<h2 class="FONTSTYLE">
					<a name="#SourceCode">Source Code</a>
				</h2>
				<table width="90%" border="1" cellpadding="0"
					cellspacing="0"
					style="border-collapse: collapse; padding-left:10mm;"
					bordercolor="#111111" frame="box" summary="">
					<tr>
						<th width="50%" align="center"
							class="TABLECOLUMNSTYLE">
							content
						</th>
					</tr>
					<tr>
					<td bgcolor="gray" style="word-break:break-all;word-wrap:break-word;">
						<xsl:for-each select="$code/code" >
							<xsl:value-of select="@content" />
							<br />
						</xsl:for-each></td>
					</tr>
				</table>
				</xsl:if>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
