package rest;

import java.util.ArrayList;

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

import model.TimetableRow;

@Path("/timetables")
public class APITimetableService {
	
	TimetableService service;

	public APITimetableService() {
		super();
		this.service = new TimetableService();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<TimetableRow> getAll() {
		return service.getTimetableRows();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") String id) {
		TimetableRow timetableRow = service.getTimetableRowById(id);
		if (timetableRow != null) {
			return Response.status(200).entity(timetableRow).build();
		} else {
			return Response.status(404).build();
		}
		
	}
	
	@GET
	@Path("/station/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStationRowById(@PathParam("id") String id) {
		ArrayList<TimetableRow> tRows = service.getStationTimetableRows(id);
		if (tRows != null) {
			return Response.status(200).entity(tRows).build();
		} else {
			return Response.status(404).build();
		}
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(TimetableRow timetableRow) {
		if (service.add(timetableRow)) {
			return Response.status(200).entity(timetableRow).build();
		} else {
			return Response.status(500).entity("Greska pri dodavanju").build();
		}
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response edit(TimetableRow timetableRow, @PathParam("id") String id) {
		if (service.update(id, timetableRow)) {
			return Response.status(200).entity(timetableRow).build();
		}
		return Response.status(404).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") String id) {
		if (service.delete(id)) {
			return Response.status(200).build();
		}
		return Response.status(404).build();
	}
}
