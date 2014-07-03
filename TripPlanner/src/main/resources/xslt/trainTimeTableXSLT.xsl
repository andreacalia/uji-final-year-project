<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/TR/xpath-functions"
	>
	
<xsl:output method="xml" indent="yes" />

<xsl:template match="/">
	<div class="table-responsive">
		<table class="table table-striped trainTimeTable">
			<thead>
				<tr>
					<th>Line</th>
					<th>Departure time</th>
					<th>Arrival time</th>
					<th>Duration</th>
					<th>Civis</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="timeTable/trips" />
			</tbody>
		</table>
	</div>
</xsl:template>

<xsl:template match="timeTable/trips">
	<xsl:for-each select="trip">
	
		<xsl:variable name="pos" select="position() - 1" />
		<xsl:variable name="arrivalDateTime" select="./arrivalDateTime/text()" />
		<xsl:variable name="departureDateTime" select="./departureDateTime/text()" />
		
		<tr class="{./@class}" id="trip-{$pos}">
			<td>
				<xsl:value-of select="./line" />
				<span class="{./name/@class}" hidden="hidden"><xsl:value-of select="./name" /></span> 
				<span class="{./category/@class}" hidden="hidden"><xsl:value-of select="./category" /></span> 
			</td>
			<td>
				<time class="{./departureDateTime/@class}" datetime="{$departureDateTime}">
					<!-- HH:MM (DAY-MONTH-YEAR) -->
					<abbr title="{substring($departureDateTime, 9, 2)}-{substring($departureDateTime, 6, 2)}-{substring($departureDateTime, 1, 4)}">
						<xsl:value-of select="substring($departureDateTime, 12, 5)" />
					</abbr>
				</time>
			</td>
			<td>
				<time class="{./arrivalDateTime/@class}" datetime="{$arrivalDateTime}">
					<!-- HH:MM (DAY-MONTH-YEAR) -->
					<abbr title="{substring($arrivalDateTime, 9, 2)}-{substring($arrivalDateTime, 6, 2)}-{substring($arrivalDateTime, 1, 4)}">
						<xsl:value-of select="substring($arrivalDateTime, 12, 5)" />
					</abbr>
				</time>
			</td>
			<td>
				<time class="{./tripTime/@class}" datetime="{./tripTime/text()}">
					<xsl:value-of select="./tripTime" />
				</time>
			</td>
			<td class="">
				<xsl:choose>
					<xsl:when test="./civis/text() = 'true'">
						<span class="text-success">
							<i class="fa fa-check"></i>
						</span>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<button type="button" class="btn btn-default bt-sm"
					onclick="addEventToAgenda('trip-{$pos}')">Add to calendar</button>
			</td>
		</tr>
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>