package es.uji.geotec.httputils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import es.uji.geotec.commonutils.Log;
import es.uji.geotec.httputils.message.ErrorMessages;

public class WebExecutor {

	private static final String TAG = WebExecutor.class.getName();
	private static final ExecutorService executor = Executors.newCachedThreadPool();
	
	private static Future<String> performGetRequest(final Builder request) {
		
		if( executor.isShutdown() ) {
			Log.e(TAG, ErrorMessages.WEB_EXECUTOR_IS_SHUTTED_DOWN);
			throw new RuntimeException(ErrorMessages.WEB_EXECUTOR_IS_SHUTTED_DOWN);
		}
		
		final Callable<String> callable = new Callable<String>() {
			@Override
			public String call() throws Exception {
				try {
					
					return request.get(String.class);
					
				} catch( ResponseProcessingException e ) {
					Log.e(TAG, e, ErrorMessages.WEB_CLIENT_PROCESS_RESPONSE_FAIL);
					throw new RuntimeException(e);
				} catch( ProcessingException e ) {
					Log.e(TAG, e, ErrorMessages.WEB_CLIENT_IO_ERROR);
					throw new RuntimeException(e);
				} catch( WebApplicationException e ) {
					Log.e(TAG, e, ErrorMessages.WEB_CLIENT_RESPONSE_CODE_ERROR);
					throw new RuntimeException(e);
				}
			}
		};
		
		return executor.submit(callable);
	}
	
	private static Future<String> performPostRequest(final Builder request, final Entity<?> entity) {
		
		if( executor.isShutdown() ) {
			Log.e(TAG, ErrorMessages.WEB_EXECUTOR_IS_SHUTTED_DOWN);
			throw new RuntimeException(ErrorMessages.WEB_EXECUTOR_IS_SHUTTED_DOWN);
		}
		
		final Callable<String> callable = new Callable<String>() {
			@Override
			public String call() throws Exception {
				try {
					
					return request.post(entity, String.class);
					
				} catch( ResponseProcessingException e ) {
					Log.e(TAG, e, ErrorMessages.WEB_CLIENT_PROCESS_RESPONSE_FAIL);
					throw new RuntimeException(e);
				} catch( ProcessingException e ) {
					Log.e(TAG, e, ErrorMessages.WEB_CLIENT_IO_ERROR);
					throw new RuntimeException(e);
				} catch( WebApplicationException e ) {
					Log.e(TAG, e, ErrorMessages.WEB_CLIENT_RESPONSE_CODE_ERROR);
					throw new RuntimeException(e);
				}
			}
		};
		
		return executor.submit(callable);
	}

	// GET
	
	public static String syncGetRequest(final WebTarget target, final MediaType mediaType) {
		final Future<String> future = performGetRequest(target.request(mediaType));
		
		try {
			
			return future.get();
			
		} catch (InterruptedException | ExecutionException e) {
			Log.e(TAG, e, ErrorMessages.INTERRUPTED_ERROR);
			throw new RuntimeException(e);
		}
	}
	
	public static String syncGetRequest(final WebTarget target) {
		return syncGetRequest(target, MediaType.WILDCARD_TYPE);
	}
	
	public static String syncGetRequest(final String url) {
		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(url);
		final String response = syncGetRequest(target); 
		client.close();
		return response;
	}
	
	public static String syncGetRequest(final String url, final MediaType mediaType) {
		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(url);
		final String response = syncGetRequest(target, mediaType); 
		client.close();
		return response;
	}
	
	public static Future<String> asyncGetRequest(final WebTarget target, final MediaType mediaType) {
		return performGetRequest(target.request(mediaType));
	}
	
	public static Future<String> asyncGetRequest(final WebTarget target) {
		return asyncGetRequest(target, MediaType.WILDCARD_TYPE);
	}
	
	public static Future<String> asyncGetRequest(final String url) {
		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(url);
		return asyncGetRequest(target);
	}


	// POST
	
	public static String syncPostRequest(final WebTarget target, final Entity<?> entity, final MediaType mediaType) {
		final Future<String> future = performPostRequest(target.request(mediaType), entity);
		
		try {
			
			return future.get();
			
		} catch (InterruptedException | ExecutionException e) {
			Log.e(TAG, e, ErrorMessages.INTERRUPTED_ERROR);
			throw new RuntimeException(e);
		}
	}
	
	public static String syncPostRequest(final String url, final Form form) {
		final Client client = ClientBuilder.newClient();
		
		final WebTarget target = client.target(url);
		final Entity<?> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
		
		final String resp = syncPostRequest(target, entity, MediaType.WILDCARD_TYPE);
		
		client.close();
		return resp;
	}
	
}
