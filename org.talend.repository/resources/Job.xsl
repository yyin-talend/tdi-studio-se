<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>		
		<head>
		<!-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />	 -->
			<style>
					SPAN.special {
						font:12pt black;
					}
					TABLE.properties {
						width:95%;
					}
					TD.propname {
						width:30%;
						font:bold;
					}
					TR.profont{
					font:bold;
					}
					
					TD.propval {
						width:70%;
					}
					TD.dependtype {
						width:20%;
					}
					TD.dependloc {
						width:60%;
					}
					TABLE.cols {
						width:90%;
					}
					TD.constraint {
						width:20%;
						font:bold;
					}
					H3.hand {
						cursor:hand;
					}	
					img.bordercolor
					{
					border-color:#AFCA00;
					}
	</style>  

	
			<img src="{/project/@logo}"/>
			</head>
			<body>
			<table border="0" cellspacing="0" width="100%">
          <tr valign="top">
	          <td align="left">
	            <H2><xsl:value-of select="/project/@title"/></H2>
	            <H4>Report generated on <I><xsl:value-of select="/project/@generatedDate"/></I></H4>
	          </td>
          </tr>
      </table>
      
      <DIV><B><A href="#ProjectDescription">Project Description</A></B></DIV>
      <DIV><B><A href="#JobDescription">Job Description</A></B></DIV>
      <DIV><B><A href="#JobPreviewPicture">Job Preview Picture</A></B></DIV>
      <DIV><B><A href="#ComponentList">Component List</A></B></DIV> 
      <DIV><B><A href="#ComponentsDescription">Components Description</A></B></DIV>
			<br/>
			<br />
			<!-- Project Description-->
      <!--HR-->
      <H2><A id="ProjectDescription" name="#ProjectDescription">Project Description</A></H2>
			 <br />
			<TABLE class="cols" border="2" cellspacing="0" cellpadding="4" frame="box">		
         <TR>
            <TD COLSPAN='2'>Name:&#160;&#160;<xsl:value-of select="/project/@name"/></TD>
         </TR>
         <TR>
            <TD COLSPAN='2'>Language:&#160;&#160;<xsl:value-of select="/project/@language"/></TD>
         </TR>
          <TR>
            <TD COLSPAN='2'>Description:&#160;&#160;<xsl:value-of select="/project/@description"/></TD>
         </TR>
         </TABLE>
		  <br/> 
		  <br/>
			<xsl:variable name="job" select="/project/job"/>
			<H2><A id="JobDescription" name="#JobDescription">Job Description</A></H2>
			<br />
			<TABLE class="cols" border="2" cellspacing="0" cellpadding="4" frame="box">		
         <TR>
            <TD COLSPAN='2'>Name:&#160;&#160;<xsl:value-of select="$job/@name"/></TD>
         </TR>
         <TR>
            <TD>Author:&#160;&#160;<xsl:value-of select="$job/@author"/></TD>
            <TD>Version:&#160;&#160;<xsl:value-of select="$job/@version"/></TD>
         </TR>
         <TR>
            <TD>Purpose:&#160;&#160;<xsl:value-of select="$job/@purpose"/></TD>
            <TD>Status:&#160;&#160;<xsl:value-of select="$job/@status"/></TD>
         </TR>
         <TR>
            <TD COLSPAN='2'>Description:&#160;&#160;<xsl:value-of select="$job/@description"/></TD>
         </TR>
         <TR>
            <TD>Creation:&#160;&#160;<xsl:value-of select="$job/@creation"/></TD>
            <TD>Modification:&#160;&#160;<xsl:value-of select="$job/@modification"/></TD>
         </TR>  
      </TABLE>
      <br/>
      <!-- Project Description-->
      <!--HR-->
      <xsl:variable name="jobPreviewPicture" select="$job/preview/@picture"/>
			<xsl:if test="string-length($jobPreviewPicture)!=0">
			     <H2><A id="JobPreviewPicture" name="#JobPreviewPicture">Job Preview Picture</A></H2><br/>
				   <div align="center"><img src="{$jobPreviewPicture}" usemap="#jobmap" alt="No image available" class="bordercolor"></img></div>
       </xsl:if>
      <br/>
      <br/>                
      <!-- Component List-->
      <!--HR-->
      <H2><A id="ComponentList" name="#ComponentList">Component List</A></H2>
      <TABLE class="cols" border="2" cellspacing="0" cellpadding="4" frame="box">
                   		 <TR>
                   		 <TH>Component Name</TH>
                   		 <TH>Component Type</TH>
                   		 </TR>
                   		   <xsl:for-each select="$job/componentList/componentItem">                  
                           <TR>
                           <TD><A href="#{@link}">
				<xsl:value-of select="@name"/></A></TD>
                              <TD><xsl:value-of select="@type"/></TD>                           
                           </TR>
                   		 </xsl:for-each>
      </TABLE>                 
                       
      <br/>
      <!-- Components Description-->
      <!--HR-->
      <H2><A id="ComponentsDescription" name="#ComponentsDescription">Components Description</A></H2>
      					
			<xsl:for-each select="$job/component">
			<TABLE BORDER="0" CELLSPACING="0" WIDTH="90%">
			<br/>
			<br/>
			<TR BGCOLOR="#C0C0C0"><B>Component:&#160;&#160;<xsl:value-of select="componentType"/></B></TR>
			</TABLE>
			<TABLE BORDER="0" CELLSPACING="0" WIDTH="40%">
			<TR>
					<td>
					<img src="{@icon}"/>&#160;&#160;&#160;&#160;&#160;
	      		Unique Name:&#160;&#160;	      		
	      			<A name="{@uniqueName}"></A>
	      		<xsl:value-of select="@uniqueName"/>&#160;&#160;&#160;&#160;&#160;
	      		</td>
	      		
	      		<td valign="bottom">Label:&#160;&#160;<xsl:value-of select="@label"/>
	      		</td>
	     </TR>
	      		<TR>
	      		<td>	
						Inputs:&#160;&#160;
         		<xsl:for-each select="input">         				
         				<xsl:choose>
        						<xsl:when test="text() = 'none'">
              				<xsl:value-of select="text()"/>
        						</xsl:when>
        						<xsl:otherwise>
              				 <A href="#{@link}">
                       	 <xsl:value-of select="text()"/>
                    	 </A>
                    	 <xsl:if test="position()!= last()">
                         <xsl:text>,&#160;&#160;</xsl:text>
                       </xsl:if> 
        						</xsl:otherwise>
								</xsl:choose>                   
         		</xsl:for-each>
         		</td>
						
						
	      <td>
	      Outputs:&#160;&#160;
         		<xsl:for-each select="output">         				
         				<xsl:choose>
        						<xsl:when test="text() = 'none'">
              				<xsl:value-of select="text()"/>
        						</xsl:when>
        						<xsl:otherwise>
              				 <A href="#{@link}">
                       	 <xsl:value-of select="text()"/>
                    	 </A>
                    	 <xsl:if test="position()!= last()">
                         <xsl:text>,&#160;&#160;</xsl:text>
                       </xsl:if> 
        						</xsl:otherwise>
								</xsl:choose>                   
         		</xsl:for-each>
         		</td>
         		</TR>
         		</TABLE>

		<xsl:variable name="previewPicture" select="@preview"/>
			<xsl:if test="string-length($previewPicture)!=0">
			  <br/>
				<div><img src="{$previewPicture}" usemap="#jobmap" alt="No image available" class="bordercolor"></img></div>
                       </xsl:if> 
	      <br/>
  
	      <b>Component Parameters:</b><br/>
	      				<TABLE class="cols" border="2" cellspacing="0" cellpadding="4" frame="box">
	      				<TR>
	      				<TH>Name</TH>
	      				<TH>Value</TH>
	      				</TR>
	      			     <xsl:for-each select="parameters/column">
	      			                 <TR>      			     			
															<TD><xsl:value-of select="@name"/></TD>
															<TD><xsl:value-of select="text()"/></TD>																		 														 	
															</TR>
										 </xsl:for-each>
									 </TABLE> 
	      
	      <br/>
	      <b>Schema:</b><br/>
                		<TABLE class="cols" border="2" cellspacing="0" cellpadding="4" frame="box">
                   		 <TR class="profont">
                   		 		<TH>Column</TH>
                   		 		<TH>Key</TH>
                   		 		<TH>Type</TH>
                   		 		<TH>Length</TH>
                   		 		<TH>Precision</TH>
                   		 		<TH>Nullable</TH>
                   		 		<TH>Comment</TH>
                   		 </TR>
                   		 <xsl:for-each select="schema/column">                  
                           <TR>
                              <TD><xsl:value-of select="@name"/></TD>
                              <TD><xsl:value-of select="@key"/></TD> 
                              <TD><xsl:value-of select="@type"/></TD> 
                              <TD><xsl:value-of select="@length"/></TD>
                              <TD><xsl:value-of select="@precision"/></TD>
                              <TD><xsl:value-of select="@nullable"/></TD>
                              <TD><xsl:value-of select="@comment"/></TD>                             
                           </TR>
                   		 </xsl:for-each>
                		</TABLE>                
     </xsl:for-each>
     </body>	 
		</html>
	</xsl:template>
</xsl:stylesheet>