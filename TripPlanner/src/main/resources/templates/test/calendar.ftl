<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="Web Servicies integration prototype">

<link rel="shortcut icon" href="/static/favicon.ico">

<title>Google Calendar Test</title>

<!-- Libs -->
<link href="/static/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="/static/lib/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
<link href="/static/lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom -->

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="https://apis.google.com/js/client.js"></script>

<script type="text/javascript">

	function insertCalendarEvent() {
		
		function doAuth() {
			var config = {
				'client_id' : '173591869008.apps.googleusercontent.com',
				'scope' : 'https://www.googleapis.com/auth/calendar'
			};
			gapi.auth.authorize(config, authCallback);
		}
		
		function authCallback(authResult) {
			if (authResult && !authResult.error) {
				// Auth ok
				callCalendarAPI();
			} else {
				// Auth fail
				authResult === null ? console.log("Null result") : console.log(authResult.error);
			}
		}
		
		function callCalendarAPI() {
			
			var gcalendarInsertCallback = function(jsonResp, rawResp) {
				console.log(rawResp);
				
				if( jsonResp && jsonResp.result.status === 'confirmed' ) {
					console.log("Event succesfully added");
				} else {
					console.log("Event add error");
				}
			}
			
			var gcalendarListCallback = function(jsonResp, rawResp) {
				console.log(rawResp);
				
				if( !jsonResp || !jsonResp.result ) {
					console.log("Calendar API request error");
					return;
				}
				
				var primaryCalendarId = null;
				
				var calendars = jsonResp.items;
				for( i = 0; i < calendars.length; ++i ) {
					if( calendars[i].primary ) {
						primaryCalendarId = calendars[i].id;
						console.log("Primary calendar ID is " + primaryCalendarId);
					}
				}
				
				if( primaryCalendarId === null ) {
					console.log("Primary calendar not found");
					return;
				}
				
				gapi.client.calendar.events.insert({
					'calendarId': primaryCalendarId,
					'resource': {
						'start': {
							'dateTime': '2014-04-01T15:00:00.000',
							'timeZone': 'Europe/Madrid'
						},
						'end': {
							'dateTime': '2014-04-01T18:00:00.000',
							'timeZone': 'Europe/Madrid'
						},
						'summary': 'Train trip',
						'source': {
							'title': 'BICICAS PROTOTIPO',
							'url': 'http://URLDESPLEGADA.uji.es'
						}
					}
				}).execute(gcalendarInsertCallback);
			}; 
			
			var apiLoadedCallback = function() {
				console.log("Calendar API loaded");
				
				gapi.client.calendar.calendarList.list({}).execute(gcalendarListCallback);
			};
			
			gapi.client.load('calendar', 'v3', apiLoadedCallback);
		}
		
		doAuth();
	}
	
	
</script>
</head>

<body role="document">

	<div class="container">
		<button class="btn btn-default" onclick="insertCalendarEvent();">Authorize</button>
	</div>
	
	<div id="content"></div>
</body>
</html>
