
function startProcess() {
	console.log("Start");

	var resultSection = $("#section-result");

	!resultSection.hasClass("hidden") ? resultSection.addClass("hidden") : null;

	initVars();
	initMap();
	initHTML();
	
	resultSection.removeClass("hidden");

	beforeTime = (new Date).getTime();
	step1GeoLocateUser();
}

function endProcess() {
	afterTime = (new Date).getTime();
	showInfo("Done");
	console.log("Done in "+(afterTime-beforeTime)+" ms");
}

function initHTML() {
	// Info elements
	bicicasPointName.html("...");
	bicicasAddress.html("...");
	bicicasAvailableBikes.html("...");
	travelTotalDistance.html("...");
	userAddress.html("...");
	// Timetable
	$("#timetable").html("...");
}

function init() {
	console.log("Init");

	// Button callback
	$("#start-button").click(startProcess);

	// Markers
	bicycleMarker = L.AwesomeMarkers.icon({
		icon : "fa-location-arrow",
		prefix: "fa",
		markerColor : "green"
	});

	trainMarker = L.AwesomeMarkers.icon({
		icon : "fa-flag-o",
		prefix: "fa",
		markerColor : "blue"
	});
	
	userMarker = L.AwesomeMarkers.icon({
		icon : "fa-user",
		prefix: "fa",
		markerColor: "blue"
	});
	
	// Info elements
	bicicasPointName = $("#bicicasPointName");
	bicicasAddress = $("#bicicasAddress");
	bicicasAvailableBikes = $("#bicicasAvailableBikes");
	travelTotalDistance = $("#travelTotalDistance");
	userAddress = $("#userAddress");
}

function showError(error) {
	console.log("Error: " + error);
	window.clearTimeout(infoTimeoutID);
	
	var label = $("#label");
	label.slideDown();
	label.removeClass("alert-success");
	label.addClass("alert-danger");
	label.html("<p>"+error+"</p>");
	infoTimeoutID = window.setTimeout(function() {
		label.slideUp();
	}, 10000);
}

function showInfo(info) {
	console.log("Info: " + info);
	window.clearTimeout(infoTimeoutID);
	
	var label = $("#label");
	label.slideDown();
	label.removeClass("alert-danger");
	label.addClass("alert-success");
	label.html("<p>"+info+"</p>");
	infoTimeoutID = window.setTimeout(function() {
		label.slideUp();
	}, 10000);
}

$(document).ready(init);
