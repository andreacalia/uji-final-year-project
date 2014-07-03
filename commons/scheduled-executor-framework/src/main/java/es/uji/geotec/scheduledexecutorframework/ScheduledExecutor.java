package es.uji.geotec.scheduledexecutorframework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import es.uji.geotec.scheduledexecutorframework.message.ErrorMessages;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.scheduledexecutorframework.task.AbstractScheduledTask;

/**
 * This class provides a executor that encapsulates a ScheduledThreadPoolExecutor.
 * This class IS thread-safe. Multiple threads can use the same instance.
 *
 */
public final class ScheduledExecutor {
	
	// TODO TASKS: actualizar el estado de las bicicas, calcular las rutas, etc...
	
	private static final String TAG = ScheduledExecutor.class.getName();
	private static final long AWAIT_TERMINATION_TIMEOUT = 20000; // 20 sec

	private final List<AbstractScheduledTask> tasks = new ArrayList<AbstractScheduledTask>();
	private final Object lock = new Object();
	
	private final ThreadFactory threadFactory = new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			final Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		}
	};

	private ScheduledThreadPoolExecutor executor;
	
	public ScheduledExecutor(final List<AbstractScheduledTask> inputTasks) {
		
		// Add here new tasks
		tasks.addAll(inputTasks);
		
		// The pool size is min(numTasks, cores*2)
		executor = new ScheduledThreadPoolExecutor(Math.min(tasks.size(), Runtime.getRuntime().availableProcessors() * 2), threadFactory);
		// This will stop tasks after calling the shutdown method
		executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		// Will remove the task after calling the cancel method on the linked Future object
		executor.setRemoveOnCancelPolicy(true);
		
		Log.i(TAG, "Executor init ok");
	}
	
	private void startTask(final AbstractScheduledTask task) {
		if( task.getFuture() != null )
			return;
		
		final Future<?> future = executor.scheduleWithFixedDelay(task, task.getInitialDelay(), task.getDelay(), TimeUnit.MILLISECONDS);
		task.setFuture(future);
	}
	
	public void startServices() {
		synchronized (lock) {
			int cont = 0;
			// Start the tasks
			for( final AbstractScheduledTask task : tasks ) {
				if( task.isService() ) {
					startTask(task);
					cont ++;
				}
			}
			
			Log.i(TAG, cont + " services added to executor");
		}
	}
	
	public void startTasks() {
		synchronized (lock) {
			int cont = 0;
			// Start the tasks
			for( final AbstractScheduledTask task : tasks ) {
				if( task.isTask() ) {
					startTask(task);
					cont ++;
				}
			}
			
			Log.i(TAG, cont + " tasks added to executor");
		}
	}
	
	public void stopServices() {
		synchronized (lock) {
			int cont = 0;
			// Attempt to cancel the tasks
			for( final AbstractScheduledTask task : tasks ) {
				if( task.isService() ) {
					syncStopTask(task);
					cont ++;
				}
			}
			Log.i(TAG, cont + " services cancelled");
		}
	}
	
	public void stopTasks() {
		synchronized (lock) {
			int cont = 0;
			for( final AbstractScheduledTask task : tasks ) {
				if( task.isTask() ) {
					syncStopTask(task);
					cont ++;
				}
			}
			Log.i(TAG, cont + " tasks cancelled");
		}
	}
	
	private void asyncStopTask(final AbstractScheduledTask task) {
		synchronized (lock) {
			final Future<?> future = task.getFuture();
			
			if( future == null )
				return;
			
			// Cancel the future
			future.cancel(false);
			// Clean future
			task.setFuture(null);
		}
	}
	
	private void syncStopTask(final AbstractScheduledTask task) {
		synchronized (lock) {
			// Attempt to cancel the task
			asyncStopTask(task);
			// Make sure the task has stopped
			waitTaskTermination(task);
		}
	}
	
	private void waitTaskTermination(final AbstractScheduledTask task) {
		
		if( task.getFuture() == null || task.getFuture().isDone() )
			return;
		
		if( ! task.getFuture().isCancelled() )
			throw new RuntimeException("Waiting the termination of a not cancelled task");
		
		// Wait termination of selected task
		final long before = System.currentTimeMillis();
		while( executor.getQueue().contains(task.getFuture()) )
			if( System.currentTimeMillis() - before > AWAIT_TERMINATION_TIMEOUT )
				throw new RuntimeException("This should not happen. " + task.getName() + " did not stop in " + AWAIT_TERMINATION_TIMEOUT + " seconds");
			else
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Log.e(TAG, e,ErrorMessages.INTERRUPTED_ERROR);
				}
	}
	
	public String getStatus() {
		synchronized (lock) {
			return String.format(
					"Executor: taskCount[ %d ], poolSize[ %d ], tasksQueued[ %d ]",
					executor.getTaskCount(),
					executor.getPoolSize(),
					executor.getQueue().size());
		}
	}
	
	public int getQueueSize() {
		synchronized (lock) {
			return executor.getQueue().size();
		}
	}
	
	private AbstractScheduledTask getTaskByName(final String name) {
		for( final AbstractScheduledTask task : tasks)
			if( task.getName().equals(name))
				return task;
		return null;
	}
	
	public void changeTaskPeriod(final String taskName, final long delay) {
		synchronized (lock) {
			final AbstractScheduledTask task = getTaskByName(taskName);
			
			if( task == null )
				throw new IllegalArgumentException("Task not found");

			final boolean running = executor.getQueue().contains(task.getFuture());
			
			if( running )
				syncStopTask(task);
			
			task.setDelay(delay);
			
			if( running )
				startTask(task);
		}
	}

	
	public void destroy() {
		synchronized (lock) {
			stopServices();
			stopTasks();
			executor.purge();
			executor.shutdownNow();
		}
	}
}
