<?xml version="1.0" encoding="UTF-8"?>
<!-- 

	extensionsStats.xsl
	
	XSL Stylesheet that takes a SAML 2.0 metadata file and extract
	key elements to be used to discover the institutions included within 
	the various entities, running calculations and stats for analysis purposes
	
	Author: Christian R. F. Pruvost <christianpruvost@gmail.com> 
	
	Output: text/HTML, with UTF-8 and mobile display support
	
	Requires: JQuery mobile libraries for rendering
	
-->

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata"
	xmlns:mdui="urn:oasis:names:tc:SAML:metadata:ui"
	xmlns:mdrpi="urn:oasis:names:tc:SAML:metadata:rpi"
	xmlns:shibmd="urn:mace:shibboleth:metadata:1.0" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	exclude-result-prefixes="ds md mdrpi shibmd xsi mdui">
	
<!-- review https://met.refeds.org/met/entity/https%253A%252F%252Fsdauth.sciencedirect.com%252F/ -->

<!-- Output is text/html with UTF-8 support for foreign languages -->
<xsl:output version="1.0" encoding="UTF-8" indent="yes" media-type="text/html" method="html"/>
<xsl:param name="MAXLENGTH">50</xsl:param><!-- used to restrict the length of entityID on screen -->
<xsl:template match="md:EntitiesDescriptor">
<html>
   <head><title>MetadataXML Info List</title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1"/> 
	<link rel="stylesheet" href="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css"/>
	<link rel="stylesheet" href="css/style1.css"/>
	<script src="https://code.jquery.com/jquery-2.1.1.min.js"/>
	<script src="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"/>
   </head>
   <body>
<div data-role="page" id="pageone" style="width: 100%; overflow: auto">
  <div data-role="header">
    <h1>MetadataXML Info Page</h1>
  </div> 
   <h2>MetadataXML Info List</h2>
   <!-- General information about the Federation -->
   <pre>
   --- SignedInfo ---
   SignatureMethod: <xsl:value-of select="ds:Signature/ds:SignedInfo/ds:SignatureMethod/@Algorithm"/>
   Reference: <xsl:value-of select="ds:Signature/ds:SignedInfo/ds:Reference/@URI"/>
   DigestMethod: <xsl:value-of select="ds:Signature/ds:SignedInfo/ds:Reference/ds:DigestMethod/@Algorithm"/>
   --- KeyInfo ---
   X509Certificate: <xsl:value-of select="ds:Signature/ds:KeyInfo/ds:X509Data/ds:X509Certificate"/>
   </pre>
   <!-- Entities list and details -->
   <table data-role="table" data-mode="columntoggle" class="ui-responsive" data-column-btn-text="Columns..." id="myTable">
    <thead>
    <tr bgcolor="#9acd32">
      <th data-priority="6">ID / registrationAuthority</th>
      <th>entityID</th>
      <th data-priority="5">SP/IDP</th>
      <th data-priority="4">md:Extensions</th>
      <th data-priority="3">OrgName</th>
      <th data-priority="2">Display Name</th>
      <th data-priority="1">Contact Person</th>
    </tr>
    </thead>
    <tbody>
    <xsl:for-each select="//md:EntityDescriptor">
       
    <xsl:sort select="md:Organization/md:OrganizationName[@xml:lang='en']"/>
    <tr>
     <td><xsl:value-of select="@ID"/><br/><xsl:value-of select="md:Extensions/mdrpi:RegistrationInfo/@registrationAuthority"/></td>
     <td id="entityID"><xsl:call-template name="entityIDValue"/></td>
     <td><xsl:call-template name="idpORsp"/></td>
     <td><xsl:call-template name="mdui:UIInfo"><xsl:with-param name="position"><xsl:value-of select="position()"/></xsl:with-param></xsl:call-template><xsl:call-template name="mdui:DiscoHints"><xsl:with-param name="position"><xsl:value-of select="position()"/></xsl:with-param></xsl:call-template>
     <xsl:call-template name="shibmd:Scope"><xsl:with-param name="position"><xsl:value-of select="position()"/></xsl:with-param></xsl:call-template></td>
     <td><xsl:value-of select="md:Organization/md:OrganizationName[@xml:lang='en']"/></td>
     <td><xsl:value-of select="md:Organization/md:OrganizationDisplayName[@xml:lang='en']"/></td>
     <td><a><xsl:attribute name="href"><xsl:value-of select="md:ContactPerson/md:EmailAddress"/></xsl:attribute>-<xsl:value-of select="md:ContactPerson/md:GivenName"/>,<xsl:value-of select="md:ContactPerson/md:SurName"/>-</a></td>
    </tr>
    </xsl:for-each>
    </tbody>
   </table>

   <div data-role="footer">
   <h1>Copyright © 2017 Crispy-snippets except for content provided by third parties.</h1>
  </div>
</div>
   </body>
 </html>   
</xsl:template>

<xsl:template name="entityIDValue">
<xsl:param name="idvalue" select="@entityID"/>
<xsl:choose>
	<xsl:when test="string-length($idvalue) &gt; $MAXLENGTH ">
		<xsl:value-of select="substring( $idvalue ,0, $MAXLENGTH )"/>...
	</xsl:when>
	<xsl:otherwise>
		<xsl:value-of select="$idvalue"/>
	</xsl:otherwise>
</xsl:choose>
</xsl:template>

<xsl:template name="idpORsp">
	<xsl:variable name="spdesc" select="md:SPSSODescriptor/@protocolSupportEnumeration"/>
    <xsl:variable name="idpdesc" select="md:IDPSSODescriptor/@protocolSupportEnumeration"/>
	<xsl:choose>
		<xsl:when test="$spdesc!=''">SP</xsl:when>
		<xsl:when test="$idpdesc!=''">IDP</xsl:when>
		<xsl:otherwise>?</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<!-- processing of the Extensions (UI Info) -->
<xsl:template name="mdui:UIInfo">
<xsl:param name="position"/>
<!-- Only displayed if there is something in it for the IDP or SP -->
<xsl:if test="child::md:IDPSSODescriptor/md:Extensions/mdui:UIInfo != '' or child::md:SPSSODescriptor/md:Extensions/mdui:UIInfo != ''">

	<xsl:if test="child::md:SPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:Description or child::md:IDPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:Description">
	<ol class="mduiHeader">mdui:Description
	<xsl:for-each select="*/md:Extensions/mdui:UIInfo/mdui:Description">
	<li class="uiinfo"><xsl:choose><xsl:when test="./@xml:lang">lang=<span class="red"><xsl:value-of select="./@xml:lang"/></span></xsl:when>
	<xsl:otherwise>lang=<span class="highlight bold">None</span></xsl:otherwise></xsl:choose>: <xsl:value-of select="."/></li>
	</xsl:for-each>
	</ol></xsl:if>
	
	<xsl:if test="child::md:SPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:DisplayName or child::md:IDPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:DisplayName">
	<ol class="mduiHeader">mdui:DisplayName
	<xsl:for-each select="*/md:Extensions/mdui:UIInfo/mdui:DisplayName">
	<li class="uiinfo"><xsl:choose><xsl:when test="./@xml:lang">lang=<span class="red"><xsl:value-of select="./@xml:lang"/></span></xsl:when>
	<xsl:otherwise>lang=<span class="highlight bold">None</span></xsl:otherwise></xsl:choose>: <xsl:value-of select="."/></li>
	</xsl:for-each>
	</ol></xsl:if>
	
	<xsl:if test="child::md:SPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:InformationURL|child::md:IDPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:InformationURL">
	<ol class="mduiHeader">mdui:InformationURL
	<xsl:for-each select="*/md:Extensions/mdui:UIInfo/mdui:InformationURL">
	<li class="uiinfo"><xsl:choose><xsl:when test="./@xml:lang">lang=<span class="red"><xsl:value-of select="./@xml:lang"/></span></xsl:when>
	<xsl:otherwise>lang=<span class="highlight bold">None</span></xsl:otherwise></xsl:choose>: <xsl:value-of select="."/></li>
	</xsl:for-each>
	</ol>
	</xsl:if>
	
	<xsl:if test="child::md:SPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:Keywords|child::md:IDPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:Keywords">
	<ol class="mduiHeader">mdui:Keywords
	<xsl:for-each select="*/md:Extensions/mdui:UIInfo/mdui:Keywords">
	<li class="uiinfo"><xsl:choose><xsl:when test="./@xml:lang">lang=<span class="red"><xsl:value-of select="./@xml:lang"/></span></xsl:when>
	<xsl:otherwise>lang=<span class="highlight bold">None</span></xsl:otherwise></xsl:choose>: <xsl:value-of select="."/></li>
	</xsl:for-each>
	</ol>
	</xsl:if>
	
	<xsl:if test="child::md:SPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:Logo|child::md:IDPSSODescriptor/md:Extensions/mdui:UIInfo/mdui:Logo">
	<ol class="mduiHeader">mdui:Logo
	<xsl:for-each select="*/md:Extensions/mdui:UIInfo/mdui:Logo">
	<xsl:variable name="logoinfo" select="."/>
	<li class="uiinfo"><xsl:choose>
	<xsl:when test="./@xml:lang">lang=<span class="red"><xsl:value-of select="./@xml:lang"/></span></xsl:when>
	<xsl:otherwise>lang=<span class="highlight bold">None</span></xsl:otherwise></xsl:choose>: <xsl:choose>
	<xsl:when test="starts-with($logoinfo, 'data:image')">data:image =&gt;<img><xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute></img></xsl:when>
	<xsl:otherwise><xsl:value-of select="."/></xsl:otherwise></xsl:choose></li>
	</xsl:for-each>
	</ol>
	</xsl:if>
</xsl:if>
</xsl:template>

<!-- processing of the Extensions (Discovery Hints) -->
<xsl:template name="mdui:DiscoHints">
<xsl:param name="position"/>
<!-- Only displayed if there is something in it for the IDP or SP -->
<xsl:if test="child::md:IDPSSODescriptor/md:Extensions/mdui:DiscoHints != '' or child::md:SPSSODescriptor/md:Extensions/mdui:DiscoHints != ''">
<a href="#popDiscoHints{$position}" data-rel="popup" class="ui-btn ui-corner-all ui-shadow ui-btn-inline" style="background:yellow;">mdui:DiscoHints</a>
<div data-role="popup" id="popDiscoHints{$position}" class="ui-content" style="max-width:800px">
    <a href="#" data-rel="back" class="ui-btn ui-corner-all ui-shadow ui-btn-z ui-icon-delete ui-btn-icon-notext ui-btn-right">Close</a>
	<xsl:if test="child::md:SPSSODescriptor/md:Extensions/mdui:DiscoHints/mdui:DomainHint or child::md:IDPSSODescriptor/md:Extensions/mdui:DiscoHints/mdui:DomainHint">
	<ol class="mduiHeader">mdui:DomainHint
	<xsl:for-each select="*/md:Extensions/mdui:DiscoHints/mdui:DomainHint">
	<li class="uiinfo"><xsl:value-of select="."/></li>
	</xsl:for-each>
	</ol></xsl:if>
	<xsl:if test="child::md:SPSSODescriptor/md:Extensions/mdui:DiscoHints/mdui:GeolocationHint or child::md:IDPSSODescriptor/md:Extensions/mdui:DiscoHints/mdui:GeolocationHint">
	<ol class="mduiHeader">mdui:GeoLocationHint
	<xsl:for-each select="*/md:Extensions/mdui:DiscoHints/mdui:GeolocationHint">
	<li class="uiinfo"><xsl:value-of select="."/></li>
	</xsl:for-each>
	</ol></xsl:if>
	<xsl:if test="child::md:SPSSODescriptor/md:Extensions/mdui:DiscoHints/mdui:IPHint or child::md:IDPSSODescriptor/md:Extensions/mdui:DiscoHints/mdui:IPHint">
	<ol class="mduiHeader">mdui:IPHint
	<xsl:for-each select="*/md:Extensions/mdui:DiscoHints/mdui:IPHint">
	<li class="uiinfo"><xsl:value-of select="."/></li>
	</xsl:for-each>
	</ol></xsl:if>
</div>
</xsl:if>
</xsl:template>

<!-- processing of the Extensions (shibmd:Scope) -->
<xsl:template name="shibmd:Scope">
<xsl:param name="position"/>
<xsl:if test="child::md:IDPSSODescriptor/md:Extensions/shibmd:Scope != '' or child::md:SPSSODescriptor/md:Extensions/shibmd:Scope != ''">
<a href="#Scope{$position}" data-rel="popup" class="ui-btn ui-corner-all ui-shadow ui-btn-inline" style="background:#57FF57;">shibmd:Scope</a>
<div data-role="popup" id="Scope{$position}" class="ui-content" style="max-width:800px">
    <a href="#" data-rel="back" class="ui-btn ui-corner-all ui-shadow ui-btn-z ui-icon-delete ui-btn-icon-notext ui-btn-right">Close</a>
	<xsl:if test="child::md:SPSSODescriptor/md:Extensions/shibmd:Scope or child::md:IDPSSODescriptor/md:Extensions/shibmd:Scope">
	<ol class="mduiHeader">shibmd:Scope
	<xsl:for-each select="*/md:Extensions/shibmd:Scope">
	<li class="uiinfo"><xsl:value-of select="."/></li>
	</xsl:for-each>
	</ol></xsl:if>
</div>
</xsl:if>
</xsl:template>
</xsl:stylesheet>