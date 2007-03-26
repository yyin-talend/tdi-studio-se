<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />	
			<img src="{/project/@logo}"/>
			<xsl:value-of select="/project/@title"/>
			<br/>
			<br />
			 Project Description:
			 <br />
			<TABLE border="1" width='100%'>			
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
			Job Description:
			<br />
			<TABLE border="1" width='100%'>			
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
      <xsl:variable name="jobPreviewPicture" select="$job/preview/@picture"/>
			<xsl:if test="string-length($jobPreviewPicture)!=0">
			Job Preview Picture:<br/>
				<img src="{$jobPreviewPicture}"/>
                       </xsl:if>
      <br/>
      <br/>                
      Component List:
      <TABLE border="1" width="100%">
                   		 <TR>
                   		 <TH>Component Name</TH>
                   		 <TH>Component Type</TH>
                   		 </TR>
                   		   <xsl:for-each select="$job/componentList/componentItem">                  
                           <TR>
                           		<xsl:if test="position() mod 2 != 0">
                                <xsl:attribute name="bgcolor">#e0edff</xsl:attribute>
                          		</xsl:if> 

                              <TD><A href="#{@link}">
				<xsl:value-of select="@name"/></A></TD>
                              <TD><xsl:value-of select="@type"/></TD>                           
                           </TR>
                   		 </xsl:for-each>
      </TABLE>                 
                       
      <br/>
      Components Description:
      <br/>
      					
			<xsl:for-each select="$job/component">
					<br/>
					<img src="{@icon}"/>&#160;&#160;&#160;&#160;&#160;
	      		Unique Name:&#160;&#160;	      		
	      			<A name="{@uniqueName}">
				</A>
	      		<xsl:value-of select="@uniqueName"/>&#160;&#160;&#160;&#160;&#160;
	      		Label:&#160;&#160;<xsl:value-of select="@label"/>
	      		<br/>
      
	      		
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
						
						
	      &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
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
	      <br/>
	      <br/>
	      
	      Component:&#160;&#160;<xsl:value-of select="componentType"/>	
	      <br/>
		<xsl:variable name="previewPicture" select="@preview"/>
			<xsl:if test="string-length($previewPicture)!=0">
				<img src="{$previewPicture}"/>
                       </xsl:if> 
	      <br/>
	      <br/>
	      
	      Component Parameters:<br/>
	      			
	      			<!--  <xsl:variable name="rowNumber" select="parameters/parameter[last()]/@row"/>
	      				row number:<xsl:value-of select="parameters/parameter[last()]/@row"/>-->
	      				
	      				
	      				<TABLE width="100%" BORDER="0"  CELLPADDING="0" CELLSPACING="0">
	      			     <xsl:for-each select="parameters/parameter">         			     			
													<TR>
 														<TD>
	 														<TABLE width="100%" BORDERCOLOR="#000000" BORDER="1" CELLPADDING="0" CELLSPACING="0">
	 															<TR>
	 															<xsl:for-each select="column">			 														
											  						<TD><xsl:value-of select="@name"/>: &#160;&#160;<xsl:value-of select="text()"/></TD>															
																</xsl:for-each>
																</TR>
									 						</TABLE>
 														</TD> 														
													</TR>
										 </xsl:for-each>
									</TABLE>
	      
	      <br/>
	      <br/>
	      Schema:<br/>
                		<TABLE border="1" width="100%">
                   		 <TR>
                   		 		<TH>Column</TH><TH>Key</TH>
                   		 		<TH>Type</TH><TH>Length</TH>
                   		 		<TH>Precision</TH><TH>Nullable</TH>
                   		 		<TH>Comment</TH>
                   		 </TR>
                   		 <xsl:for-each select="schema/column">                  
                           <TR>
                           		<xsl:if test="position() mod 2 != 0">
                                <xsl:attribute name="bgcolor">#e0edff</xsl:attribute>
                          		</xsl:if> 

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
		</html>
	</xsl:template>
</xsl:stylesheet>