<?xml version="1.0" encoding="utf-8"?>
<!-- edited with XMLSpy v2007 sp1 (http://www.altova.com) by  () -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
			</head>
			<body>
				<xsl:variable name="rootTable" select="externalNode/mapperTable"/>
				<xsl:variable name="subRootTable" select="externalNode/mapperTable/rootTable/table"/>
				<br/>
				 <xsl:variable name="componentPreviewPic" select="$rootTable/@preview"></xsl:variable>
				     <xsl:if test="string-length($componentPreviewPic)!=0">
				        <div class="FONTSTYLE" align="left">
							<img src="{$componentPreviewPic}" usemap="#jobmap" alt="No image available" class="bordercolor"/>
					</div> 				     
				</xsl:if>  
				<xsl:for-each select="$rootTable">					
					<b class="FONTSTYLE">Mapper table for <xsl:value-of select="/externalNode/@name"/>(<xsl:value-of select="@type"/>):</b>
					<TABLE class="cols" border="1" width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" frame="box">
						<TH align="middle" width="30%" class="TABLECOLUMNSTYLE">Properties</TH>
						<TH align="left" class="TABLECOLUMNSTYLE">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;Values</TH>
						<TR>
							<TD class="FONTSTYLE" align="middle">Name</TD>
							<TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="table/@name"/>
							</TD>
						</TR>
						<TR>
							<TD class="FONTSTYLE" align="middle">Table Name</TD>
							<TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="table/@tableName"/>
							</TD>
						</TR>
						<TR>
							<TD class="FONTSTYLE" align="middle">Alias</TD>
							<TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="table/@alias"/>
							</TD>
						</TR>
						<TR>
							<TD class="FONTSTYLE" align="middle">Join Type</TD>
							<TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="table/@joinType"/>
							</TD>
						</TR>
						
						<TR>
							<TD class="FONTSTYLE" align="middle">isMinimized</TD>
							<TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="table/@isMinimized"/>
							</TD>
						</TR>
					</TABLE>
					<br/>
					<br/>
				<b class="FONTSTYLE">Metadata Table Entries:</b>
				<br/>
					<TABLE class="cols" width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" frame="box">
						<TR class="profont">
							<TH class="TABLECOLUMNSTYLE" width="25%">Name</TH>
							<TH class="TABLECOLUMNSTYLE" width="25%">Expression</TH>
							<TH class="TABLECOLUMNSTYLE" width="25%">Operator</TH>
							<TH class="TABLECOLUMNSTYLE" width="25%">isJoin</TH>
						</TR>
						<xsl:for-each select="table/metadataTableEntries/entry">
							<TR class="FONTSTYLE">
								<TD align="middle">
									<xsl:value-of select="@name"/>
								</TD>
								<TD align="middle">
									<xsl:value-of select="@expression"/>
								</TD>
								<TD align="middle">
									<xsl:value-of select="@operator"/>
								</TD>
								<TD align="middle">
									<xsl:value-of select="@isJoin"/>
								</TD>
							</TR>
						</xsl:for-each>
					</TABLE>
					    <br/>
						<b class="FONTSTYLE">Custom Conditions Entries:</b>
					<TABLE class="cols" width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" frame="box">
						<TR class="profont">
							<TH class="TABLECOLUMNSTYLE" width="25%">Name</TH>
							<TH class="TABLECOLUMNSTYLE" width="25%">Expression</TH>
							<TH class="TABLECOLUMNSTYLE" width="25%">Operator</TH>
							<TH class="TABLECOLUMNSTYLE" width="25%">isJoin</TH>
						</TR>
						<xsl:for-each select="table/customConditionsEntries/entry">
							<TR class="FONTSTYLE">
								<TD align="middle">
									<xsl:value-of select="@name"/>
								</TD>
								<TD align="middle">
									<xsl:value-of select="@expression"/>
								</TD>
								<TD align="middle">
									<xsl:value-of select="@operator"/>
								</TD>
								<TD align="middle">
									<xsl:value-of select="@isJoin"/>
								</TD>
							</TR>
						</xsl:for-each>
					</TABLE>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
