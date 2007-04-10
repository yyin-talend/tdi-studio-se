<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
	<xsl:variable name="ucase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
  <xsl:variable name="lcase" select="'abcdefghijklmnopqrstuvwxyz'"/>

		<html>		
		<head>
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
					.FONTSTYLE {font-family: Arial, Helvetica, sans-serif;}
					.LINKSTYLE {TEXT-DECORATION:none}a:hover{TEXT-DECORATION:underline}
					.TITLESTYLE {font-size: 26px;	color: #818181;}
					.TABLECOLUMNSTYLE {font-family: Arial, Helvetica, sans-serif; color: #818181; background-color: #E6E6E6;align: middle}

	</style>  
			</head>
			<body>
			<br/>
<table border="0" cellspacing="0" rowspacing="0" width="90%" class="FONTSTYLE">
<tr valign="top">
<!--<td width="15%" rowspan="2" align="middle"></td> -->
<td width="26%" rowspan="2" align="middle"><a href="#{project/@link}"><img src="{project/@logo}" border="0" align="absbottom"></img></a></td>
<!-- <td width="1%" rowspan="2" align="middle"></td> -->
<td width="58%"><strong><font size="200" color="#B5DC10">Job&#160;</font><font size="200" color="#818181">documentation</font></strong></td>

</tr>
<tr valign="top">
  <td height="20" align="left" valign="top" class="TITLESTYLE"><xsl:value-of select="/project/@title"/></td>
</tr>
</table>
			<br/>
			<br/>
			<table border="1"  width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" class="FONTSTYLE">
          <tr valign="top">
	          <td align="middle" width="25%" class="TABLECOLUMNSTYLE">
	            PROJECT NAME
	          </td>
	          <td align="middle" width="25%" class="FONTSTYLE"><xsl:value-of select="/project/@name"/>
	          </td>
	          <td align="middle" width="25%" class="TABLECOLUMNSTYLE">GENERATION DATE
	          </td>
	          <td align="middle" width="25%"><xsl:value-of select="/project/@generatedDate"/>
	          </td>
	          <tr>
	          <td align="middle" width="25%" class="TABLECOLUMNSTYLE">AUTHOR
	          </td>
	          <td align="middle" width="25%"><xsl:value-of select="/project/job/@author"/>
	          </td>
	            <td align="middle" width="25%" class="TABLECOLUMNSTYLE">T.O.S VERSION
	          </td>
	          <td align="middle" width="25%" class="FONTSTYLE"> <xsl:value-of select="/project/@version"/>
	          </td>
	          </tr>
          </tr>
      </table>
       <H2 class="FONTSTYLE">Summary</H2>
      <DIV class="FONTSTYLE"><B><A href="#ProjectDescription">Project Description</A></B></DIV>
      <DIV class="FONTSTYLE"><B><A href="#JobDescription">Job Description</A></B></DIV>
      <DIV class="FONTSTYLE"><B><A href="#JobPreviewPicture">Job Preview Picture</A></B></DIV>
      <DIV class="FONTSTYLE"><B><A href="#ComponentList">Component List</A></B></DIV> 
      <DIV class="FONTSTYLE"><B><A href="#ComponentsDescription">Components Description</A></B></DIV>
			<br/>
			<br />
			<!-- Project Description-->
      <!--HR-->
      <H2 class="FONTSTYLE"><A id="ProjectDescription" name="#ProjectDescription">Project Description</A></H2>
			 <br />
			<TABLE border="1"  width="90%" class="FONTSTYLE" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111"> 		
			   <TH align="middle" width="30%" class="TABLECOLUMNSTYLE">Properties</TH>
			   <TH align="left" class="TABLECOLUMNSTYLE">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;Values</TH>
			
         <TR>
            <TD class="FONTSTYLE" align="middle">Name</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="/project/@name"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">Language</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="/project/@language"/></TD>
         </TR>
          <TR>
            <TD class="FONTSTYLE" align="middle">Description</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="/project/@description"/></TD>
         </TR>
         </TABLE>
		  <br/> 
		  <br/>
			<xsl:variable name="job" select="/project/job"/>
			<H2 class="FONTSTYLE"><A id="JobDescription" name="#JobDescription">Job Description</A></H2>
			<br />
			<TABLE class="cols"  border="1"  width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111"  frame="box">
			 <TH align="middle" width="30%" class="TABLECOLUMNSTYLE">Properties</TH>
			   <TH align="left" class="TABLECOLUMNSTYLE">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;Values</TH>		
         <TR>
            <TD class="FONTSTYLE" align="middle">Name</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@name"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">Author</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@author"/></TD>
         </TR>
         <TR>
         <TD class="FONTSTYLE" align="middle">Version</TD>
         <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@version"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">Purpose</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@purpose"/></TD>
         </TR>
         <TR>
         <TD class="FONTSTYLE" align="middle">Status</TD>
         <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@status"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">Description</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@description"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">Creation</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@creation"/></TD>
         </TR>
         <TR>
          <TD class="FONTSTYLE" align="middle">Modification</TD>
          <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@modification"/></TD>
         </TR>  
      </TABLE>
      <br/>
      <!-- Project Description-->
      <!--HR-->
      <xsl:variable name="jobPreviewPicture" select="$job/preview/@picture"/>
			<xsl:if test="string-length($jobPreviewPicture)!=0">
			     <H2 class="FONTSTYLE"><A id="JobPreviewPicture" name="#JobPreviewPicture">Job Preview Picture</A></H2><br/>
				   <div class="FONTSTYLE" align="center"><img src="{$jobPreviewPicture}" usemap="#jobmap" alt="No image available" class="bordercolor"></img></div>
       </xsl:if>
      <br/>
      <br/>                
      <!-- Component List-->
      <!--HR-->
      <H2 class="FONTSTYLE"><A id="ComponentList" name="#ComponentList">Component List</A></H2>
      <TABLE  border="1"  width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111"  frame="box">
                   		 <TR>
                   		 <TH width="50%" align="middle" class="TABLECOLUMNSTYLE">Component Name</TH>
                   		 <TH width="50%" align="middle" class="TABLECOLUMNSTYLE">Component Type</TH>
                   		 </TR>
                   		 
                   		 
                   		 <map id ="jobmap" name="jobmap">
                   		   <xsl:for-each select="$job/componentList/componentItem"> 
													<area shape ="rect" coords ="{@leftTopX},{@leftTopY},{@rightBottomX},{@rightBottomY}"
                          href ="#{@link}" alt="{@name}" />
                          </xsl:for-each>
                          </map>
                          <xsl:for-each select="$job/componentList/componentItem">
                           <TR>
                           <TD class="FONTSTYLE" align="middle"><A href="#{@link}" class="LINKSTYLE">
				<xsl:value-of select="@name"/></A></TD>
                              <TD class="FONTSTYLE" align="middle"><xsl:value-of select="@type"/></TD>                           
                           </TR>
                   		 </xsl:for-each>
      </TABLE>                 
                       
      <br/>
      <!-- Components Description-->
      <!--HR-->
      <H2 class="FONTSTYLE"><A id="ComponentsDescription" name="#ComponentsDescription">Components Description</A></H2>
      					
			<xsl:for-each select="$job/component">
			<A name="{@uniqueName}"></A>
			<TABLE border="0"  width="90%" class="FONTSTYLE" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111">
			<br/>
			<br/>
			<TR bgcolor="#E6E6E6"><B class="FONTSTYLE">Component:&#160;&#160;<xsl:value-of select="componentType"/></B></TR>
			</TABLE>
			<BR/>
			<TABLE border="1"  width="90%" class="FONTSTYLE" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" >
			<TR>
					<td class="cols" align="center" rowspan="2" width="10%"><img src="{@icon}"/>&#160;&#160;&#160;&#160;&#160;</td>
					<td align="middle" class="TABLECOLUMNSTYLE" width="15%">
	      		UNIQUE NAME
	      	</td>	      		
	      			<td class="FONTSTYLE" align="middle" width="15%">
	      		<xsl:value-of select="@uniqueName"/>
	      		</td>
	      		<td align="middle" class="TABLECOLUMNSTYLE" width="15%">	
						INPUT(S)
						</td>
						<td class="FONTSTYLE" align="middle">
         		<xsl:for-each select="input">         				
         				<xsl:choose>
        						<xsl:when test="text() = 'none'">
              				<xsl:value-of select="text()"/>
        						</xsl:when>
        						<xsl:otherwise>
              				 <A href="#{@link}" style="LINKSTYLE">
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
	     <TR>
	      <td align="middle" class="TABLECOLUMNSTYLE">LABEL</td>
	      <td align="middle"><xsl:value-of select="@label"/>
	      </td>		
	      <td align="middle" class="TABLECOLUMNSTYLE">
	      OUTPUT(S)
	      </td>
	      <td class="FONTSTYLE" align="middle">
         		<xsl:for-each select="output">         				
         				<xsl:choose>
        						<xsl:when test="text() = 'none'">
              				<xsl:value-of select="text()"/>
        						</xsl:when>
        						<xsl:otherwise>
              				 <A href="#{@link}" style="LINKSTYLE">
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
				<div class="FONTSTYLE"><img src="{$previewPicture}" usemap="#jobmap" alt="No image available" class="bordercolor"></img></div>
                       </xsl:if> 
	      <br/>
  
	      <b class="FONTSTYLE">Component Parameters:</b><br/>
	      				<TABLE width="90%" class="FONTSTYLE" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" frame="box">
	      				<TR>
	      				<TH align="middle" width="30%" class="TABLECOLUMNSTYLE">Properties</TH>
	      				<TH align="left" width="70%" class="TABLECOLUMNSTYLE">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;Values</TH>
	      				</TR>
	      			     <xsl:for-each select="parameters/column">
	      			                 <TR>      			     			
															<TD class="FONTSTYLE" align="middle">
															  <xsl:variable name="propname" select="@name"/>
                                <xsl:value-of select="translate($propname, $lcase, $ucase)"/>
															</TD>
															<TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="text()"/></TD>																		 														 	
															</TR>
										 </xsl:for-each>
									 </TABLE> 
	      
	      <br/>
	      <xsl:for-each select="schemas/schema">
			
	      <b class="FONTSTYLE">Schema for <xsl:value-of select="@name"/>:</b><br/>
                		<TABLE class="cols" width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" frame="box">
                   		 <TR class="profont">
                   		 		<TH class="TABLECOLUMNSTYLE" width="14%">Column</TH>
                   		 		<TH class="TABLECOLUMNSTYLE" width="14%">Key</TH>
                   		 		<TH class="TABLECOLUMNSTYLE" width="14%">Type</TH>
                   		 		<TH class="TABLECOLUMNSTYLE" width="14%">Length</TH>
                   		 		<TH class="TABLECOLUMNSTYLE" width="14%">Precision</TH>
                   		 		<TH class="TABLECOLUMNSTYLE" width="14%">Nullable</TH>
                   		 		<TH class="TABLECOLUMNSTYLE" width="16%">Comment</TH>
                   		 </TR>
                   		 <xsl:for-each select="column">                  
                           <TR class="FONTSTYLE">
                              <TD align="middle"><xsl:value-of select="@name"/></TD>
                              <TD align="middle"><xsl:value-of select="@key"/></TD> 
                              <TD align="middle"><xsl:value-of select="@type"/></TD> 
                              <TD align="middle"><xsl:value-of select="@length"/></TD>
                              <TD align="middle"><xsl:value-of select="@precision"/></TD>
                              <TD align="middle"><xsl:value-of select="@nullable"/></TD>
                              <TD align="middle"><xsl:value-of select="@comment"/></TD>                             
                           </TR>
                   		 </xsl:for-each>
                		</TABLE>
                </xsl:for-each>                
     </xsl:for-each>
     </body>	 
		</html>
	</xsl:template>
</xsl:stylesheet>