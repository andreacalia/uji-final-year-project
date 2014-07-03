
function moveScene(flat, flon, tlat, tlon) {
	var bounds = L.latLngBounds(
				L.latLng(flat, flon),
				L.latLng(tlat, tlon)
			);
	map.fitBounds(bounds);
}

function renderGeoJSONLineString(lineString) {
	var routeStyle = {
		    "color": "#ff7800",
		    "weight": 5,
		    "opacity": 0.70
		};
	L.geoJson([lineString,], {style: routeStyle}).addTo(map);
}

function renderMarkerOnGeoJSONPoint(point, params) {
	renderMarker(point.lat, point.lon, params);
}

function renderMarker(lat, lon, params) {
	var marker = L.marker([ lat, lon ]);
	if( params !== undefined ) {
		params.icon !== undefined ? marker.setIcon(params.icon) : null;
		params.popupText !== undefined ? marker.bindPopup(params.popupText) : null;
	}
	marker.addTo(map);
}

function renderCircle(lat, lon, params) {
	var circle = L.circle([ lat, lon ]);
	if( params !== undefined ) {
		params.radius !== undefined ? circle.setRadius(params.radius) : null;
	}
	circle.addTo(map);
}

function initMap() {
	console.log("Init Map");

	// Map
	map = L.map('map', {'dragging': false});
	map.dragging ? map.dragging.disable() : null; // Disable dragging if the device supports it
	map.tap ? map.tap.disable() : null; // Allow touch scroll on mobile devices
	
	// OpenStreetMap layer
	var osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
	var osm = new L.TileLayer(osmUrl, {
		attribution : "Map data © OpenStreetMap contributors"
	});
	map.addLayer(osm);
	
	// Geo JSON Layer
	geoJSONLayer = L.geoJson();
	geoJSONLayer.addTo(map);
}

function toggleMapDragging(button) {
	if( !map.dragging ) // Device does not support dragging
		return;
	
	if( map.dragging.enabled() ) {
		// Icon with unlock
		map.dragging.disable();
		$(button).children(':first').removeClass('fa-unlock').addClass('fa-lock');
	} else {
		// Icon with lock
		map.dragging.enable();
		$(button).children(':first').removeClass('fa-lock').addClass('fa-unlock');
	}
}