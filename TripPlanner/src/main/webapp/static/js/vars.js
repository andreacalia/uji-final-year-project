function initVars() {
	console.log("Init Vars");
	// Map
	map !== null ? map.remove() : null;
	$("#map").html("");
	map = null;
	geoJSONLayer = null;
	// Objects
	user = {};
	nearestBicicas = {};
	trainStation = {};
	// Dinstances
	distanceUserBicicas = null;
	distanceBicicasTrain = null;
	// Traveltime
	traveltimeUserBicicas = null;
	traveltimeBicicasTrain = null;
}

function toggleTestMode() {
	testMode = !testMode;
	console.log("Test mode " + testMode);
}

//Map
var map = null;
var geoJSONLayer = null;
// Objects
var user = null;
var nearestBicicas = null;
var trainStation = null;
// Distances
var distanceUserBicicas = null;
var distanceBicicasTrain = null;
// Traveltime
var traveltimeUserBicicas = null;
var traveltimeBicicasTrain = null;
// Markers
var bicycleMarker = null;
var trainMarker = null;
var userMarker = null;
// Info elements
var bicicasPointName;
var bicicasAddress;
var bicicasAvailableBikes;
var travelTotalDistance;
var userAddress;
// Miscellaneous
var infoTimeoutID = null;
var beforeTime = null;
var afterTime = null;
var testMode = false;