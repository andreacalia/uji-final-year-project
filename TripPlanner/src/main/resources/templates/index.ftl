<!DOCTYPE html>

<#assign contextPath = "/TripPlanner">


<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="Web Servicies integration prototype">

<link rel="shortcut icon" href="/static/favicon.ico">

<title>Web Services integration prototype</title>

<!-- Libs -->
<link href="${contextPath}/static/lib/bootstrap/css/bootstrap.css" rel="stylesheet">
<!-- 
<link href="${contextPath}/static/lib/bootstrap/css/bootstrap-theme.css" rel="stylesheet">
 -->
<link href="${contextPath}/static/lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="${contextPath}/static/lib/leaflet-awesome-markers/leaflet.awesome-markers.css" rel="stylesheet">
<link href="${contextPath}/static/lib/leaflet/leaflet.css" rel="stylesheet">
<!-- Custom -->
<link href="${contextPath}/static/css/index.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
</head>

<body role="document">

<div class="container">

	<!-- START -->
	<div class="row custom-panel-no-border" id="section-start">
		
		<div class="row text-center">
			<button id="start-button" type="button" class="btn btn-primary btn-lg">Start</button>
		</div>
		<!-- CONSTROLS -->
		<div class="row">
			<input type="checkbox" onclick="toggleTestMode()" /> Test mode <small>(in case you are not in Castellón de la Plana)</small>
		</div>
	</div>
	
	<div class="row alert alert-success" id="label">
	</div>

	<!-- RESULT -->
	<div class="row custom-panel-no-border hidden" id="section-result">
		<div class="row">
			<div id="map" class="col-md-10 col-md-offset-1" tabindex="-1"></div><!-- Map container -->
		</div>
		<br/>
		<div class="row">
			<div class="col-md-10 col-md-offset-1" tabindex="-1"><!-- Map controls -->
				<button type="button" class="btn btn-sm btn-default" onClick="toggleMapDragging(this);"><i class="fa fa-lock"></i></button>
			</div>
		</div>
		<hr/>
		<div class="row">
			<div class="col-md-10 col-md-offset-1">
				<dl class="dl-horizontal">
					<dt>Departure city</dt><dd>Castellón de la Plana</dd>
					<dt>Arrival city</dt><dd>Valencia</dd>
					<dt>Your address</dt><dd id="userAddress">...</dd>
					<dt>Bicicas point name</dt><dd id="bicicasPointName">...</dd>
					<dt>Bicicas address</dt><dd id="bicicasAddress">...</dd>
					<dt>Available bikes</dt><dd><span id="bicicasAvailableBikes">...</span> bikes</dd>
					<dt>Total distance</dt><dd><span id="travelTotalDistance">...</span> km</dd>
				</dl>
			</div>
		</div>
		<hr/>
		<div class="row">
			<div id="timetable" class="col-md-10 col-md-offset-1" ></div><!-- Timetable container -->
		</div>
		<hr/>
	</div>
	
	<!-- ALERTS -->
	<div class="row custom-panel-no-border">
		<p class="alert alert-info alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<strong>Heads up!</strong> To use this application you have to <strong>allow geolocation feature</strong> for this page in the browser configuration
		</p>
		<p class="alert alert-info alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			<strong>Heads up!</strong> Your browser must support CSS3 and HTML5
		</p>
	</div>
	
</div>
<footer class="container">
<div class="row">
	<p><small class="license">Font Awesome by Dave Gandy - http://fontawesome.io</small></p>
	<p><small class="license">Leaflet: Copyright (c) 2010-2014, Vladimir Agafonkin Copyright (c) 2010-2011, CloudMade All rights reserved. - https://github.com/Leaflet/Leaflet/blob/master/LICENSE</small></p>
	<p><small class="license">Leaflet.AwesomeMarkers and colored markers are licensed under the MIT License - http://opensource.org/licenses/mit-license.html.</small></p>
	<p><small class="license">Bootstrap: Copyright (c) 2011-2014 Twitter, Inc - https://github.com/twbs/bootstrap/blob/master/LICENSE</small></p>
</div>
</footer>
	<!-- Libs -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="${contextPath}/static/lib/bootstrap/js/bootstrap.min.js"></script>
	<script src="${contextPath}/static/lib/leaflet/leaflet.js"></script>
	<script src="${contextPath}/static/lib/leaflet-awesome-markers/leaflet.awesome-markers.min.js"></script>
	<script src="https://apis.google.com/js/client.js"></script>
	<!-- Custom -->
	<script src="${contextPath}/static/js/calendar.js"></script>
	<script src="${contextPath}/static/js/URL.js"></script>
	<script src="${contextPath}/static/js/HTMLHelper.js"></script>
	<script src="${contextPath}/static/js/vars.js"></script>
	<script src="${contextPath}/static/js/map.js"></script>
	<script src="${contextPath}/static/js/steps.js"></script>
	<script src="${contextPath}/static/js/index.js"></script>
</body>
</html>
