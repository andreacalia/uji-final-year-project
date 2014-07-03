package es.uji.tripplanner.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;

import es.uji.geotec.commonutils.Log;
import es.uji.tripplanner.utils.Paths;

@Path(Paths.URL.ROOT)
public class PageController {

	private static final String TAG = PageController.class.getName();

	@GET
	@Path(Paths.URL.INDEX)
	@Produces(MediaType.TEXT_HTML)
	public Viewable getIndex() {
		Log.i(TAG, "Index requested");
		return new Viewable(Paths.Template.INDEX);
	}
	
}
