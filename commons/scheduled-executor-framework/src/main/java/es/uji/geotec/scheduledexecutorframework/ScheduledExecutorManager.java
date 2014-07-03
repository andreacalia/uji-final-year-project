package es.uji.geotec.scheduledexecutorframework;

import java.util.List;

import es.uji.geotec.scheduledexecutorframework.message.ErrorMessages;
import es.uji.geotec.scheduledexecutorframework.task.AbstractScheduledTask;

/**
 * Simply provide a static access to an Executor. It's mandatory to call init before any other method.
 * A good moment to do that is at the servlet startup (in case of Java EE project). 
 * It uses Initialization-on-demand holder idiom [ http://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom ]
 *
 */
public final class ScheduledExecutorManager {
	
	private static boolean isInit = false;
	private static ScheduledExecutor executor = null;
	
	private synchronized static ScheduledExecutor getInstance() {
		assertInit();
		return executor;
	}
	
	public synchronized static void init(final List<AbstractScheduledTask> tasks) {
		executor = new ScheduledExecutor(tasks);
		isInit = true;
	}
	
	public synchronized static void destroy() {
		getInstance().destroy();
		executor = null;
		isInit = false;
	}
	
	public synchronized static void startTasks() {
		getInstance().startTasks();
	}
	
	public synchronized static void startServices() {
		getInstance().startServices();
	}
	
	public synchronized static void stopTasks() {
		getInstance().stopTasks();
	}
	
	public synchronized static String getStatus() {
		return getInstance().getStatus();
	}
	
	public synchronized static void stopServices() {
		getInstance().stopServices();
	}
	
	public synchronized static void changeTaskPeriod(final String taskName, final long delay) {
		getInstance().changeTaskPeriod(taskName, delay);
	}
	
	private static void assertInit() {
		if( ! isInit )
			throw new RuntimeException(ErrorMessages.SCHEDULED_TASK_MANAGER_NOT_INIT);
	}

}
