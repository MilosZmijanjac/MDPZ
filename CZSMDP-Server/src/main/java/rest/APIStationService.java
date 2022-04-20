package rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Station;

@Path("/stations")
public class APIStationService {
	
	StationService service;

	public APIStationService() {
		super();
		service=new StationService();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String,Station> getAll() {
		return service.getStations();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") String id) {
		Station station = service.getById(id);
		if (station != null) {
			return Response.status(200).entity(station).build();
		} else {
			return Response.status(404).build();
		}
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(Station station) {
		if (service.add(station)) {
			return Response.status(200).entity(station).build();
		} else {
			return Response.status(500).entity("Greska pri dodavanju").build();
		}
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response edit(Station station, @PathParam("id") String id) {
		if (service.update(station, id)) {
			return Response.status(200).entity(station).build();
		}
		return Response.status(404).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") String id) {
		if (service.remove(id)) {
			return Response.status(200).build();
		}
		return Response.status(404).build();
	}

}
