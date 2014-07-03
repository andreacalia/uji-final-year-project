package es.uji.bicicaswebservice.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uji.bicicaswebservice.dao.BicicasDAO;
import es.uji.bicicaswebservice.dao.BicicasRecordsDAO;
import es.uji.bicicaswebservice.messages.ErrorMessages;
import es.uji.bicicaswebservice.utils.Paths;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.scheduledexecutorframework.ScheduledExecutorManager;

/**
 * Admin controller is used to perform administration tasks
 *
 */
@Path(Paths.URL.ADMIN_BASE)
public class AdminController {
	// TODO: Add HTTP auth
	
	private static final String TAG = AdminController.class.getName();

	@GET
	@Path(Paths.URL.CACHE_INIT)
	@Produces(MediaType.TEXT_PLAIN)
	public Response initMongoDB() {
		try {
			Log.i(TAG, "Reading starting dataset");
			final Scanner scanner = new Scanner(new File(Paths.Assets.MONGO_INITIAL_DATA_SET));
			final StringBuilder sb = new StringBuilder();
			
			while(scanner.hasNextLine())
				sb.append(scanner.nextLine());
			
			scanner.close();
			
			final String jsonDataSet = sb.toString();
			
			final BicicasDAO dao = new BicicasDAO();
			dao.link();
			
			Log.i(TAG, "Dropping collection " + BicicasDAO.BICICAS_COLLECTION);
			dao.dropCollection();
			
			Log.i(TAG, "Inserting dataset in bulk mode");
			final boolean ok = dao.insertBulkJSONString(jsonDataSet);
			
			Log.i(TAG, "Creating indexes");
			dao.ensureIndexes();
			
			dao.unlink();
			
			final BicicasRecordsDAO recordsDao = new BicicasRecordsDAO();
			recordsDao.link();
			
			Log.i(TAG, "Dropping collection " + BicicasRecordsDAO.BICICAS_RECORDS_COLLECTION);
			recordsDao.dropCollection();
			
			recordsDao.unlink();
			
			return (ok ? Response.ok("Cache should be initialized") : Response.serverError()).build();
			
		} catch (FileNotFoundException e) {
			Log.e(TAG, e, ErrorMessages.ADMIN_INITIAL_DATASET_NOT_FOUND);
		} catch (RuntimeException e) {
			Log.e(TAG, e, ErrorMessages.MONGO_INIT_ERROR);
		}
		
		return Response.serverError().build();
	}
	
	@DELETE
	@Path(Paths.URL.CACHE)
	@Produces(MediaType.TEXT_PLAIN)
	public Response dropMongoDB() {
		final BicicasDAO dao = new BicicasDAO();
		dao.link();
		
		Log.i(TAG, "Dropping collection " + BicicasDAO.BICICAS_COLLECTION);
		dao.dropCollection();
		
		dao.unlink();
		
		final BicicasRecordsDAO recordsDao = new BicicasRecordsDAO();
		recordsDao.link();
		
		Log.i(TAG, "Dropping collection " + BicicasRecordsDAO.BICICAS_RECORDS_COLLECTION);
		recordsDao.dropCollection();
		
		recordsDao.unlink();
		
		return Response.ok("Cache should be empty").build();
	}
	
	@GET
	@Path(Paths.URL.TASKS_START_TASKS)
	@Produces(MediaType.TEXT_PLAIN)
	public Response startTasks() {
		try {
			ScheduledExecutorManager.startTasks();
			return Response.ok("Tasks should start shortly").build();
		} catch( Exception e ) {
			Log.e(TAG, e, ErrorMessages.GENERIC_ERROR);
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path(Paths.URL.TASKS_START_SERVICES)
	@Produces(MediaType.TEXT_PLAIN)
	public Response startServices() {
		try {
			ScheduledExecutorManager.startServices();
			return Response.ok("Tasks should start shortly").build();
		} catch( Exception e ) {
			Log.e(TAG, e, ErrorMessages.GENERIC_ERROR);
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path(Paths.URL.TASKS_STOP_TASKS)
	@Produces(MediaType.TEXT_PLAIN)
	public Response stopTasks() {
		try {
			ScheduledExecutorManager.stopTasks();
			return Response.ok("Tasks stopped").build();
		} catch( Exception e ) {
			Log.e(TAG, e, ErrorMessages.GENERIC_ERROR);
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path(Paths.URL.TASKS_STOP_SERVICES)
	@Produces(MediaType.TEXT_PLAIN)
	public Response stopServices() {
		try {
			ScheduledExecutorManager.stopServices();
			return Response.ok("Services stopped").build();
		} catch( Exception e ) {
			Log.e(TAG, e, ErrorMessages.GENERIC_ERROR);
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path(Paths.URL.TASKS)
	@Produces(MediaType.TEXT_PLAIN)
	public Response getTasksStatus() {
		try {
			return Response.ok(ScheduledExecutorManager.getStatus()).build();
		} catch( Exception e ) {
			Log.e(TAG, e, ErrorMessages.GENERIC_ERROR);
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path(Paths.URL.TASK_PERIOD_CHANGE)
	@Produces(MediaType.TEXT_PLAIN)
	public Response changeTaskPeriod(@PathParam(value = "name") String taskName, @QueryParam(value = "period") long millis) {
		try {
			ScheduledExecutorManager.changeTaskPeriod(taskName, millis);
			Log.i(TAG, "Set new delay for task " + taskName + " [ " + millis + " ]");
			return Response.ok("Set new delay for task " + taskName + " [ " + millis + " ]").build();
		} catch( Exception e ) {
			Log.e(TAG, e, ErrorMessages.GENERIC_ERROR);
			return Response.serverError().build();
		}
	}
	
}
