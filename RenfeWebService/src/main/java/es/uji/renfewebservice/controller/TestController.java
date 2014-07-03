package es.uji.renfewebservice.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class TestController {
	
	@GET
	public String test() {
		return "Si";
	}
	
}
