package es.uji.geotec.scheduledexecutorframework.task;

import java.util.concurrent.Future;

import es.uji.geotec.commonutils.Log;
import es.uji.geotec.scheduledexecutorframework.message.ErrorMessages;

/**
 * Abstract template for the tasks
 *
 */
public abstract class AbstractScheduledTask implements Runnable {

	private static final String TAG = AbstractScheduledTask.class.getName();
	
	protected static final long DEFAULT_INITIAL_DELAY = 0L;
	protected static final long DEFAULT_DELAY = 5000L;
	
	private long initialDelay = DEFAULT_INITIAL_DELAY;
	private long delay = DEFAULT_DELAY;
	private boolean isService;
	private String name;
	private Future<?> future;
	
	public AbstractScheduledTask(final String name, final boolean isService) {
		this.name = name;
		this.isService = isService;
	}
	
	public AbstractScheduledTask(final String name) {
		this(name, false);
	}
	
	@Override
	public void run() {
		final long beforeTime = System.currentTimeMillis();
		try {
			doJob();
		} catch( Exception e ) {
			Log.e(TAG, e, ErrorMessages.SCHEDULED_TASK_RUNNING_ERROR, name);
		}
		final long afterTime = System.currentTimeMillis();
		
		Log.d(TAG, "Task [ " + name + " ] executed in " + (afterTime - beforeTime) + " ms");
	}
	
	protected abstract void doJob();
	
	public Future<?> getFuture() {
		return future;
	}
	
	public void setFuture(final Future<?> future) {
		this.future = future;
	}
	
	public String getName() {
		return name;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public void setDelay(final long delay) {
		this.delay = delay;
	}
	
	public long getInitialDelay() {
		return initialDelay;
	}
	
	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}
	
	public boolean isService() {
		return isService;
	}
	
	public boolean isTask() {
		return !isService;
	}
}
