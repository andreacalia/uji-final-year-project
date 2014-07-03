
/**
 * Inserts an h-event in Google Calendar
 */
function addEventToAgenda(parentId) {
	
	var parent = $('#'+parentId);
	var startDateTime = parent.find('.dt-start').attr('datetime');
	var endDateTime = parent.find('.dt-end').attr('datetime');
	var summary = parent.find('.p-category').html(); // This is the title of the event
	var description = parent.find('.p-name').html();
		
	function doAuth() {
		var config = {
			'client_id' : '569144674017-3p7g2ll8cssitj9lpsteue8ho3m0v7v9.apps.googleusercontent.com',
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
				showInfo("Event succesfully added to Google Calendar");
				parent.find('button').prop('disabled', 'disabled').removeClass('btn-default').addClass('btn-success');
			} else {
				showError("Event insert error");
			}
		};
		
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
						'dateTime': startDateTime,
						'timeZone': 'Europe/Madrid'
					},
					'end': {
						'dateTime': endDateTime,
						'timeZone': 'Europe/Madrid'
					},
					'summary': summary,
					'description': description
					/*,
					'source': {
						'title': 'BICICAS PROTOTIPO',
						'url': 'http://URLDESPLEGADA.uji.es'
					}
					*/
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