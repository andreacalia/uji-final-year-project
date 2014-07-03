<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes" />

<xsl:template match="*">
  <xsl:copy>
    <xsl:apply-templates/>
  </xsl:copy>
</xsl:template>

<xsl:template match="timeTable/trips/trip">
	<xsl:for-each select=".">
		<trip class="h-event">
			<name class="p-name"><xsl:value-of select="./name" /></name>
			<category class="p-category">Train trip</category>
			<departureDateTime class="dt-start"><xsl:value-of select="departureDateTime" /></departureDateTime>
			<arrivalDateTime class="dt-end"><xsl:value-of select="arrivalDateTime" /></arrivalDateTime>
			<tripTime class="dt-duration"><xsl:value-of select="./tripTime" /></tripTime>
			<line><xsl:value-of select="./line" /></line>
			<civis><xsl:value-of select="./civis" /></civis>
		</trip>
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>