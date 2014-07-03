
//var baseURL = "http://192.168.1.131:8090";
//var baseURL = "http://192.168.1.167:8090/TripPlanner";
var baseURL = "http://inti.init.uji.es:8080/TripPlanner";

function nearestBicycleURL(lon, lat) {
	return baseURL+"/ws/ajax/location/bicicas/nearest/?lon="+lon+"&lat="+lat+"&testMode="+( testMode ? "on" : "off" );
}

function trainStationLocationURL() {
	return baseURL+"/ws/ajax/location/trainstation";
}

function routeURL(flon, flat, tlon, tlat, v) {
	var URL = baseURL+"/ws/ajax/route/?flat="+flat+"&flon="+flon+"&tlat="+tlat+"&tlon="+tlon;
	v !== undefined ? URL = URL +"&v="+v : null;
	return URL;
}

function trainTimeTableURL(millisFrom, millisTo) {
	return baseURL+"/ws/ajax/train/timetable/?millisFrom="+millisFrom+"&millisTo="+millisTo;
}

function nominatimOpenStreetMapURL(lon, lat) {
	// Doc: http://wiki.openstreetmap.org/wiki/Nominatim
	return "http://nominatim.openstreetmap.org/reverse?format=json&lat="+lat+"&lon="+lon+"&zoom=18&addressdetails=1";
}