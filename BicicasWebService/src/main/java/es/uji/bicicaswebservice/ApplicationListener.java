package es.uji.bicicaswebservice;

import java.util.List;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import es.uji.geotec.commonutils.Log;
import es.uji.geotec.commonutils.reflection.ObjectBuilder;
import es.uji.geotec.scheduledexecutorframework.ScheduledExecutorManager;
import es.uji.geotec.scheduledexecutorframework.task.AbstractScheduledTask;

/**
 * Listener of Jersey webapp events.
 * https://blogs.oracle.com/mira/entry/monitoring_of_restful_jersey_applicaiton
 *
 */
public class ApplicationListener implements ApplicationEventListener {

	private static final String TAG = ApplicationListener.class.getName();
	
	private List<AbstractScheduledTask> tasks;
	private ObjectBuilder<? extends RequestEventListener> requestEventListenerBuilder;

	public ApplicationListener(final List<AbstractScheduledTask> tasks, final ObjectBuilder<? extends RequestEventListener> requestEventListenerBuilder) {
		this.tasks = tasks;
		this.requestEventListenerBuilder = requestEventListenerBuilder;
	}
	
	public void onEvent(final ApplicationEvent applicationEvent) {
		switch (applicationEvent.getType()) {
		case INITIALIZATION_FINISHED:
			
			initScheduledExecutorManager();
			
			Log.i(TAG, "Web app init");
			break;
		case DESTROY_FINISHED:
			
			destroyScheduledExecutorManager();
			Log.i(TAG, "Web app destroyed");
			break;
		case INITIALIZATION_APP_FINISHED:
			break;
		case INITIALIZATION_START:
			break;
		case RELOAD_FINISHED:
			break;
		default:
			break;
		}
	}
	
	private void initScheduledExecutorManager() {
		ScheduledExecutorManager.init(tasks);
		ScheduledExecutorManager.startServices();
	}
	
	private void destroyScheduledExecutorManager() {
		ScheduledExecutorManager.destroy();
	}

	@Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
		return requestEventListenerBuilder.newInstance();
    }

}