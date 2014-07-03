
function step6GatherInfo() {
	showInfo("Step 6 gathering info");
	// Set the basic info
	$("#bicicasPointName").html(nearestBicicas.name);
	$("#bicicasAvailableBikes").html(nearestBicicas.currentState.bicycleAvailable+" / "+nearestBicicas.bicycleNumber);
	$("#travelTotalDistance").html(( distanceUserBicicas + distanceBicicasTrain ).toFixed(2));
	
	// Call the webservice to find the user and the bicicas address
	var URLBicicasAddress = nominatimOpenStreetMapURL(nearestBicicas.location.lon, nearestBicicas.location.lat);
	var URLUserAddress = nominatimOpenStreetMapURL(user.lon, user.lat);
	
	var userAddressResult = null;
	var bicicasAddressResult = null;
	var successfulCalls = 0;
	
	$.when(
			$.getJSON(URLBicicasAddress).done(function(json) {
				bicicasAddressResult = json;
				successfulCalls ++;
			}),
			$.getJSON(URLUserAddress).done(function(json) {
				userAddressResult = json;
				successfulCalls ++;
			})
	).then(function() {
		// Ajax error
		if( successfulCalls != 2 ) {
			console.log("userAddressResult == null: "+(userAddressResult == null));
			console.log("bicicasAddressResult == null: "+(bicicasAddressResult == null));
			showError("Error retrieving user and bicicas point address");
			return;
		}
		
		bicicasAddress.html(hAddr(
				bicicasAddressResult.address.road,
				bicicasAddressResult.address.city,
				bicicasAddressResult.address.state, nearestBicicas.location.lon, nearestBicicas.location.lat));
		userAddress.html(hAddr(
				userAddressResult.address.road,
				userAddressResult.address.city,
				userAddressResult.address.state, user.lon, user.lat));
		
		endProcess();
		
	});
	
}

function step5GetTrainTimeTable() {
	showInfo("Step 5 Getting Train Timetable");
	
	var totalDistance = distanceUserBicicas + distanceBicicasTrain;
	var totalTraveltime = traveltimeUserBicicas + traveltimeBicicasTrain;
	
	// Estimated time arrival at train station
	var etaMillis = Date.now() + totalTraveltime * 1000;
	
	// Find 00:00 of the next day in millis 
	var tempArrivalDate = new Date(etaMillis + 24 * 60 * 60 * 1000);
	tempArrivalDate.setHours(0, 0, 0, 0);
	var arrivalMillis = tempArrivalDate.getTime();
	
	console.log("estMillis: "+etaMillis+", arrivalMillis: "+arrivalMillis);
	
	function ajaxOk(html) {
		$("#timetable").html(html);
		step6GatherInfo();
	}
	
	function ajaxFail() {
		showError("Connection error getting train timetable");
	}
	
	var URL = trainTimeTableURL(etaMillis, arrivalMillis);
	$.ajax({url: URL, dataType: "html"}).done(ajaxOk).fail(ajaxFail);
}

function step4CalculateDistance() {
	showInfo("Step 4 Calculating Distance");
	
	var routeUserBicicas = null;
	var routeBicicasTrain = null;
	var successfulCalls = 0;
	
	// Parallel ajax requests
	var URLUserBicicas = routeURL(user.lon, user.lat, nearestBicicas.location.lon, nearestBicicas.location.lat);
	var URLBicicasTrain = routeURL(nearestBicicas.location.lon, nearestBicicas.location.lat, trainStation.lon, trainStation.lat);
	$.when(
			$.getJSON(URLUserBicicas).done(function(jsonRoute) {
				routeUserBicicas = jsonRoute;
				successfulCalls ++;
			}),
			$.getJSON(URLBicicasTrain).done(function(jsonRoute) {
				routeBicicasTrain = jsonRoute;
				successfulCalls ++;
			})
	).then(function() {
		// Ajax error
		if( successfulCalls != 2 ) {
			console.log("routeUserBicicas == null: "+(routeUserBicicas == null));
			console.log("routeBicicasTrain == null: "+(routeBicicasTrain == null));
			showError("Error retrieving routes data");
			return;
		}
		
		distanceUserBicicas = parseFloat(routeUserBicicas.properties.distance);
		traveltimeUserBicicas = parseFloat(routeUserBicicas.properties.travelTime);
		
		distanceBicicasTrain = parseFloat(routeBicicasTrain.properties.distance);
		traveltimeBicicasTrain = parseFloat(routeBicicasTrain.properties.travelTime);
		
		renderGeoJSONLineString(routeUserBicicas.geometry);
		renderGeoJSONLineString(routeBicicasTrain.geometry);
		
		moveScene(user.lat, user.lon, trainStation.lat, trainStation.lon);
		
		step5GetTrainTimeTable();
	});

}

function step3GeoLocateTrainStation() {
	showInfo("Step 3 Geolocating TrainStation");
	
	function ajaxOk(jsonGeoPoint) {
		trainStation.lat = jsonGeoPoint.coordinates[0];
		trainStation.lon = jsonGeoPoint.coordinates[1];
		
		console.log("Train station at lat[ "+trainStation.lat+" ] lon[ "+trainStation.lon+" ]");
		
		var text = "Castellon's train station";
		renderMarkerOnGeoJSONPoint(trainStation, {icon: trainMarker, popupText:text});
		
		step4CalculateDistance();
	}
	
	function ajaxFail() {
		showError("Connection error geolocating train station");
	}
	
	var URL = trainStationLocationURL();
	$.getJSON(URL).done(ajaxOk).fail(ajaxFail);
}

function step2GeoLocateNearestBicicas() {
	showInfo("Step 2 Geolocating Nearest Bicicas");
	
	function ajaxOk(jsonBicicas) {
		nearestBicicas = jsonBicicas;
		
		console.log("Nearest bicicas at lat[ "+nearestBicicas.location.lat+" ] lon[ "+nearestBicicas.location.lon+" ]");
		
		var text = "Nearest bicicas point with availables bikes";
		renderMarkerOnGeoJSONPoint(nearestBicicas.location, {icon: bicycleMarker, popupText:text});
		
		moveScene(user.lat, user.lon, nearestBicicas.location.lat, nearestBicicas.location.lon);
		
		step3GeoLocateTrainStation();
	}
	
	function ajaxFail() {
		showError("Connection error geolocating nearest bicicas point");
	}
	
	var URL = nearestBicycleURL(user.lon, user.lat);
	$.getJSON(URL).done(ajaxOk).fail(ajaxFail);
}

function step1GeoLocateUser() {
	showInfo("Step 1 Geolocating User");
	// https://developer.mozilla.org/en-US/docs/Web/API/Geolocation.getCurrentPosition
	
	function callback() {
		console.log("User at lat[ "+user.lat+" ] lon[ "+user.lon+" ]");
		
		var text = "You are within " + user.accuracy + " meters from this point";
		renderMarker(user.lat, user.lon, {icon: userMarker, popupText:text});
		renderCircle(user.lat, user.lon, {radius: user.accuracy});
		
		moveScene(user.lat, user.lon, user.lat, user.lon);
		
		step2GeoLocateNearestBicicas();
	}
	
	// If in testmode, user position is fixed
	if( testMode ) {
		user.lat = 39.985177;
		user.lon = -0.024723;
		user.accuracy = 3;
		
		return callback();
	}
	
	// If it continues, geolocation is performed
	
	function locationSuccess(position) {
		// https://developer.mozilla.org/en-US/docs/Web/API/Position
		user.lat = position.coords.latitude;
		user.lon = position.coords.longitude;
		user.accuracy = position.coords.accuracy;

		callback();
	}

	function locationError(error) {
		// https://developer.mozilla.org/en-US/docs/Web/API/PositionError
		showError("Geolocation error: " + error.message);
	}

	if( !"geolocation" in navigator ) {
		showError("Browser does not support geolocation");
		return;
	}

	var options = {
		enableHighAccuracy: true,
		timeout: 20000,
		maximumAge: 500
	};
	
	navigator.geolocation.getCurrentPosition(locationSuccess, locationError, options);
}