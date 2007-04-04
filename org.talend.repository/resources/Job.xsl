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
	</style>  

				<table border="0" cellspacing="0" rowspacing="0" width="90%" class="FONTSTYLE">
          <tr valign="top">
	          <td colspan="2" align="middle" width="40%">
	           <img src="{/project/@logo}"/> 
	          </td>
	          <td>
	           <H1><font color="#AFCA00" size="150">Job</font> documentation</H1>
	           <H3><font color="#B0B0B0"><xsl:value-of select="/project/@title"/></font></H3>  
	          </td>
          </tr>
      </table>
			
			</head>
			<body>
			<br/>
			<br/>
			<table border="1"  width="90%" class="FONTSTYLE" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" >
          <tr valign="top">
	          <td align="middle" width="25%" bgcolor="#D0D0D0" class="FONTSTYLE">
	            PROJECT NAME
	          </td>
	          <td align="middle" width="25%" class="FONTSTYLE"><xsl:value-of select="/project/@name"/>
	          </td>
	          <td align="middle" width="25%" bgcolor="#D0D0D0" class="FONTSTYLE">GENERATION DATE
	          </td>
	          <td align="middle" width="25%" class="FONTSTYLE"><xsl:value-of select="/project/@generatedDate"/>
	          </td>
	          <tr>
	          <td align="middle" width="25%" bgcolor="#D0D0D0" class="FONTSTYLE">AUTHOR
	          </td>
	          <td align="middle" width="25%" class="FONTSTYLE"><xsl:value-of select="/project/job/@author"/>
	          </td>
	            <td align="middle" width="25%" bgcolor="#D0D0D0" class="FONTSTYLE">T.O.S VERSION
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
			   <TH align="middle" width="30%" bgcolor="#D0D0D0">PROPERTIES</TH>
			   <TH align="left" bgcolor="#D0D0D0">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;VALUES</TH>
			
         <TR>
            <TD class="FONTSTYLE" align="middle">NAME</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="/project/@name"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">LANGUAGE</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="/project/@language"/></TD>
         </TR>
          <TR>
            <TD class="FONTSTYLE" align="middle">DESCRIPTION</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="/project/@description"/></TD>
         </TR>
         </TABLE>
		  <br/> 
		  <br/>
			<xsl:variable name="job" select="/project/job"/>
			<H2 class="FONTSTYLE"><A id="JobDescription" name="#JobDescription">Job Description</A></H2>
			<br />
			<TABLE class="cols"  border="1"  width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111"  frame="box">
			 <TH align="middle" width="30%" bgcolor="#D0D0D0" class="FONTSTYLE">PROPERTIES</TH>
			   <TH align="left" bgcolor="#D0D0D0" class="FONTSTYLE">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;VALUES</TH>		
         <TR>
            <TD class="FONTSTYLE" align="middle">NAME</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@name"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">AUTHOR</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@author"/></TD>
         </TR>
         <TR>
         <TD class="FONTSTYLE" align="middle">VERSION</TD>
         <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@version"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">PURPOSE</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@purpose"/></TD>
         </TR>
         <TR>
         <TD class="FONTSTYLE" align="middle">STATUS</TD>
         <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@status"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">DESCRIPTION</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@description"/></TD>
         </TR>
         <TR>
            <TD class="FONTSTYLE" align="middle">CREATION</TD>
            <TD class="FONTSTYLE" align="left">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<xsl:value-of select="$job/@creation"/></TD>
         </TR>
         <TR>
          <TD class="FONTSTYLE" align="middle">MODIFICATION</TD>
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
      <TABLE class="cols" border="1"  width="90%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111"  frame="box">
                   		 <TR>
                   		 <TH class="FONTSTYLE" width="50%" align="middle" bgcolor="#D0D0D0">COMPONENT NAME</TH>
                   		 <TH class="FONTSTYLE" width="50%" align="middle" bgcolor="#D0D0D0">COMPONENT TYPE</TH>
                   		 </TR>
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
			<TR bgcolor="#D0D0D0"><B class="FONTSTYLE">Component:&#160;&#160;<xsl:value-of select="componentType"/></B></TR>
			</TABLE>
			<BR/>
			<TABLE border="1"  width="90%" class="FONTSTYLE" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" >
			<TR>
					<td class="cols" align="center" rowspan="2" width="10%"><img src="{@icon}"/>&#160;&#160;&#160;&#160;&#160;</td>
					<td class="FONTSTYLE" align="middle" bgcolor="#D0D0D0" width="15%">
	      		UNIQUE NAME
	      	</td>	      		
	      			<td class="FONTSTYLE" align="middle" width="15%">
	      		<xsl:value-of select="@uniqueName"/>
	      		</td>
	      		<td class="FONTSTYLE" align="middle" bgcolor="#D0D0D0" width="15%">	
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
	      <td class="FONTSTYLE" align="middle" bgcolor="#D0D0D0">LABEL</td>
	      <td class="FONTSTYLE" align="middle"><xsl:value-of select="@label"/>
	      </td>		
	      <td class="FONTSTYLE" align="middle" bgcolor="#D0D0D0">
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
	      				<TH class="FONTSTYLE" align="middle" width="30%" bgcolor="#D0D0D0">PROPERTIES</TH>
	      				<TH class="FONTSTYLE" align="left" width="70%" bgcolor="#D0D0D0">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;VALUES</TH>
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
                   		 		<TH class="FONTSTYLE" bgcolor="#D0D0D0" width="14%">COLUMN</TH>
                   		 		<TH class="FONTSTYLE" bgcolor="#D0D0D0" width="14%">KEY</TH>
                   		 		<TH class="FONTSTYLE" bgcolor="#D0D0D0" width="14%">TYPE</TH>
                   		 		<TH class="FONTSTYLE" bgcolor="#D0D0D0" width="14%">LENGTH</TH>
                   		 		<TH class="FONTSTYLE" bgcolor="#D0D0D0" width="14%">PRECISION</TH>
                   		 		<TH class="FONTSTYLE" bgcolor="#D0D0D0" width="14%">NULLABLE</TH>
                   		 		<TH class="FONTSTYLE" bgcolor="#D0D0D0" width="16%">COMMENT</TH>
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