<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes" />

<xsl:template match="*">
  <xsl:copy>
    <xsl:apply-templates/>
  </xsl:copy>
</xsl:template>

<xsl:template match="//location">
	<xsl:for-each select=".">
		<location class="h-geo">
			<lat class="p-latitude"><xsl:value-of select="./lat" /></lat>
			<lon class="p-longitude"><xsl:value-of select="./lon" /></lon>
		</location>
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>