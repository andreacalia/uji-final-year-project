
function hAddr(address, locality, region, lon, lat) {
	return "<span class='h-addr'>" +
				"<span class='p-street-address'>"+address+"</span>" +
				", <span class='p-locality'>"+locality+"</span>"+
				" (<span class='p-region'>"+region+"</span>)" +
				"<data class='h-geo'><data class='p-latitude' value='"+lat+"' /><data class='p-longitude' value='"+lon+"' /></data>" +
			"</span>";
}