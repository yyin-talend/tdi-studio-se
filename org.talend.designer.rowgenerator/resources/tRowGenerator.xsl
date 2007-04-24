<?xml version="1.0" encoding="utf-8"?>
<!-- edited with XMLSpy v2007 sp1 (http://www.altova.com) by  () -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
				 <xsl:variable name="componentPreviewPic" select="externalNode/@preview"></xsl:variable>
				     <xsl:if test="string-length($componentPreviewPic)!=0">
				        <div class="FONTSTYLE" align="left">
							<img src="{$componentPreviewPic}" usemap="#jobmap" alt="No image available" class="bordercolor"/>
					</div>
					<br/> 				     
				</xsl:if>
				<b class="FONTSTYLE">Row generator information for <xsl:value-of select="/externalNode/@name"/>:</b>
				<TABLE class="cols" width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" frame="box">
							<TR class="profont">
								<TH class="TABLECOLUMNSTYLE">Column</TH>
								<TH class="TABLECOLUMNSTYLE">Key</TH>
								<TH class="TABLECOLUMNSTYLE">Type</TH>
								<TH class="TABLECOLUMNSTYLE">Nullable</TH>
								<TH class="TABLECOLUMNSTYLE">Length</TH>
								<TH class="TABLECOLUMNSTYLE">Precision</TH>
								<TH class="TABLECOLUMNSTYLE">Default</TH>
								<TH class="TABLECOLUMNSTYLE">Comment</TH>
								<TH class="TABLECOLUMNSTYLE">Functions</TH>
								<TH class="TABLECOLUMNSTYLE">Parameters</TH>
							</TR>
							<xsl:for-each select="/externalNode/column">
								<TR class="FONTSTYLE">
									<TD align="middle">
										<xsl:value-of select="@name"/>
									</TD>
									<TD align="middle">
										<xsl:value-of select="@key"/>
									</TD>
									<TD align="middle">
										<xsl:value-of select="@type"/>
									</TD>
									<TD align="middle">
										<xsl:value-of select="@nullable"/>
									</TD>
									<TD align="middle">
										<xsl:value-of select="@length"/>
									</TD>
									<TD align="middle">
										<xsl:value-of select="@precision"/>
									</TD>
										<TD align="middle">
										<xsl:value-of select="@default"/>
									</TD>
									<TD align="middle">
										<xsl:value-of select="@comment"/>
									</TD>
									<TD align="middle">
										<xsl:value-of select="@functions"/>
									</TD>
									<TD align="middle">
										<xsl:value-of select="@parameters"/>
									</TD>
								</TR>
							</xsl:for-each>
						</TABLE>
					<br/>
	</xsl:template>
</xsl:stylesheet>
