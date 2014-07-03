package es.uji.bicicaswebservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import es.uji.bicicasdatamodel.BicicasPoint;
import es.uji.bicicasdatamodel.Dock;
import es.uji.bicicasdatamodel.Location;
import es.uji.bicicasdatamodel.SharingPointState;
import es.uji.bicicaswebservice.dao.BicicasDAO;
import es.uji.bicicaswebservice.model.BicicasPointUtils;
import es.uji.bicicaswebservice.scraping.BicicasScraper;
import es.uji.geotec.commonutils.DateUtils;

@Path("test")
public class TestController {

	@GET
	@Path("hello")
	public Response test() {
		
		final BicicasScraper sc = new BicicasScraper("http://www.bicicas.es/estado/EstadoActual.asp");
		final List<BicicasPoint> updatedBicicas = sc.performScraping();
		
		final BicicasDAO dao = new BicicasDAO();
		dao.link();
		
		boolean result = true;
		
		for( final BicicasPoint updated : updatedBicicas )
			result &= dao.update(updated.code, updated);
		
		dao.unlink();
		
		return (result ? Response.ok() : Response.serverError()).build();
	}
	
	@GET
	@Path("dao")
	public Response testDao() {
		
		BicicasPoint point = new BicicasPoint();
		
		point.code = 1;
		point.bicycleNumber = 25;
		point.location = new Location();
		point.location.lat = (double) 4;
		point.location.lon = (double) 2;
		point.name = "TEST";
		point.currentState = new SharingPointState();
		point.currentState.bicycleAvailable = 12;
		point.currentState.online = true;
		point.currentState.updated = DateUtils.newDateES();
		point.currentState.docks = new ArrayList<Dock>();
		point.currentState.docks.add(new Dock(7));
		point.currentState.docks.add(new Dock(7));
		point.currentState.docks.add(new Dock(7));
		
		BicicasPoint point1 = new BicicasPoint();
		
		point1.code = 1;
		point1.bicycleNumber = 25;
		point1.location = new Location();
		point1.location.lat = (double) 4;
		point1.location.lon = (double) 2;
		point1.name = "TEST";
		point1.currentState = new SharingPointState();
		point1.currentState.bicycleAvailable = 12;
		point1.currentState.online = true;
		point1.currentState.updated = DateUtils.newDateES();
		point1.currentState.docks = new ArrayList<Dock>();
		point1.currentState.docks.add(new Dock(8));
		point1.currentState.docks.add(new Dock(8));
		point1.currentState.docks.add(new Dock(7));

		assert BicicasPointUtils.hasDockListStatusChanged(point, point1);
		
		point1.currentState.docks.set(0, new Dock(7));
		point1.currentState.docks.set(1, new Dock(7));
		
		assert ! BicicasPointUtils.hasDockListStatusChanged(point, point1);
		
		point.currentState.docks = Arrays.asList(new Dock(1), new Dock(-1),new Dock(2));
		point1.currentState.docks = Arrays.asList(new Dock(-1), new Dock(-1),new Dock(2));
		
		assert BicicasPointUtils.hasDockListStatusChanged(point, point1);
		assert BicicasPointUtils.getIngoingBikes(point, point1).equals(new ArrayList<Dock>());
		assert BicicasPointUtils.getOutgoingBikes(point, point1).equals(Arrays.asList(new Dock(1)));
		
		point.currentState.docks = new ArrayList<Dock>();
		point1.currentState.docks = new ArrayList<Dock>();
		
		assert ! BicicasPointUtils.hasDockListStatusChanged(point, point1);
		
		return Response.ok().build();
	}
	
}
