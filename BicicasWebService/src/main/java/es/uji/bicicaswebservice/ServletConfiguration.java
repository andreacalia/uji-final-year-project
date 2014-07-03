package es.uji.bicicaswebservice;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import es.uji.bicicaswebservice.task.BicicasScraperTask;
import es.uji.bicicaswebservice.task.StatisticsWriterTask;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.commonutils.reflection.ObjectBuilder;
import es.uji.geotec.jerseyutils.ClientIPTrackerFilter;
import es.uji.geotec.jerseyutils.RequestEventListenerLog;
import es.uji.geotec.jerseyutils.model.StatisticInfo;
import es.uji.geotec.scheduledexecutorframework.task.AbstractScheduledTask;

/**
 * OJO: Documentación aquí: https://jersey.java.net/documentation/latest/deployment.html
 *
 */
@ApplicationPath("/ws/")
public class ServletConfiguration extends ResourceConfig {
	
	private static final String TAG = ServletConfiguration.class.getName();

    public ServletConfiguration() {
        packages("es.uji.bicicaswebservice.controller");
        
        register(ClientIPTrackerFilter.class);
        register(buildApplicationListener());
        
        property(ServerProperties.MONITORING_STATISTICS_MBEANS_ENABLED, true);
        
//        register(RolesAllowedDynamicFeature.class);
        
        // Enable Jackson
        packages("org.glassfish.jersey.examples.jackson");
        register(JacksonFeature.class);
    }

    private ApplicationListener buildApplicationListener() {
		try {
			// Queue that store the statistics info of the requests
			final LinkedBlockingQueue<StatisticInfo> statisticsQueue = new LinkedBlockingQueue<>();
			// Constructor to call that listen the requests
			final Constructor<RequestEventListenerLog> constructor = RequestEventListenerLog.class.getConstructor(BlockingQueue.class);
			// The object builder that will instantiate the listener on each request
			final ObjectBuilder<RequestEventListenerLog> reqEventListenerBuilder = new ObjectBuilder<RequestEventListenerLog>(constructor, statisticsQueue);
			
			// The list of tasks that are performed in background
			final List<AbstractScheduledTask> tasks = new ArrayList<>();
			// Add the task that will write the statistics information on a database
			tasks.add(new StatisticsWriterTask(statisticsQueue));
			tasks.add(new BicicasScraperTask());
			
			return new ApplicationListener(tasks, reqEventListenerBuilder);
			
		} catch (NoSuchMethodException e) {
			Log.e(TAG, e, "This should never happen");
		} catch (SecurityException e) {
			Log.e(TAG, e, "This should never happen");
		}
		
		throw new RuntimeException("This should never happen");
    }
}
