package es.uji.geotec.jerseyutils;

import java.util.concurrent.BlockingQueue;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import es.uji.geotec.commonutils.DateUtils;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.jerseyutils.message.ErrorMessages;
import es.uji.geotec.jerseyutils.model.StatisticInfo;

public class RequestEventListenerLog implements RequestEventListener {

	private static final String TAG = RequestEventListenerLog.class.getName();
	
	private volatile long methodStartTime;
	private volatile long methodEndTime;
	private BlockingQueue<StatisticInfo> queue;
	
	public RequestEventListenerLog(final BlockingQueue<StatisticInfo> queue) {
		this.queue = queue;
	}
	
	@Override
	public void onEvent(RequestEvent requestEvent) {

		switch (requestEvent.getType()) {
		case RESOURCE_METHOD_START:
			
			methodStartTime = System.currentTimeMillis();
			
			break;
		case RESOURCE_METHOD_FINISHED:
			
			methodEndTime = System.currentTimeMillis();
			
			break;
		case EXCEPTION_MAPPER_FOUND:
			break;
		case EXCEPTION_MAPPING_FINISHED:
			break;
		case FINISHED:

			final String clientIP = (String) requestEvent.getContainerRequest().getProperty(ClientIPTrackerFilter.CLIENT_IP_PROPERTY);
			
			if( clientIP == null )
				Log.e(TAG, ErrorMessages.NO_CLIENT_IP_PROPERTY_SET);
			
			
			final StatisticInfo info = new StatisticInfo();
			info.ms = (int) (methodEndTime - methodStartTime);
			info.URI = requestEvent.getUriInfo().getAbsolutePath().toString();
			info.success = requestEvent.isSuccess();
			info.exceptionMessage = requestEvent.getException() != null ? requestEvent.getException().getMessage() : null;
			info.httpStatus = requestEvent.getContainerResponse() != null ? requestEvent.getContainerResponse().getStatus() : null;
			info.headers = requestEvent.getContainerRequest().getRequestHeaders();
			info.date = DateUtils.nowDateES();
			info.clientIP = clientIP != null ? clientIP : "";
			
			queue.add(info);
			
			break;
		case LOCATOR_MATCHED:
			break;
		case MATCHING_START:
			break;
		case ON_EXCEPTION:
			break;
		case REQUEST_FILTERED:
			break;
		case REQUEST_MATCHED:
			break;
		case RESP_FILTERS_FINISHED:
			break;
		case RESP_FILTERS_START:
			break;
		case START:
			break;
		case SUBRESOURCE_LOCATED:
			break;
		default:
			break;
		}
	}
	
}
