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
		<trip xmlns="http://rdf.data-vocabulary.org/#" typeof="Event">
			<name property="name"><xsl:value-of select="./name" /></name>
			<category property="v:eventType">Train trip</category>
			<departureDateTime property="startDate"><xsl:value-of select="departureDateTime" /></departureDateTime>
			<arrivalDateTime property="endDate"><xsl:value-of select="arrivalDateTime" /></arrivalDateTime>
			<tripTime property="duration"><xsl:value-of select="./tripTime" /></tripTime>
			<line><xsl:value-of select="./line" /></line>
			<civis><xsl:value-of select="./civis" /></civis>
		</trip>
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>