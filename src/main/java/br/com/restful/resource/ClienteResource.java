package br.com.restful.resource;

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

import br.com.restful.controller.PlanetController;
import br.com.restful.model.Planet;


/**
 * Classe responsavel por conter os metodos REST de acesso ao webservice
 */
@Path("/")
public class ClienteResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Planet> listAll() {
		System.out.println("ArrayList de planetas");
		PlanetController teste = new PlanetController();
		System.out.println(teste.listAll());
		return teste.listAll();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{idOrName}")
	public Response getById(@PathParam("idOrName") String idOrName) {
		System.out.println("Calling getByid");
		Planet planet = new PlanetController().searchById(idOrName);
		if (planet == null) {
			 planet = new PlanetController().searchByName(idOrName);
		}
		if (planet != null) {
			return Response.ok().type(MediaType.APPLICATION_JSON).entity(planet).build();
		} else {
			return Response.status(404).entity("Planet not found").build();
		}
	}
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvarClienteJson(Planet planet) {
		boolean isClienteGravado = new PlanetController().savePlanet(planet);
		if (isClienteGravado == true) {
			return Response.ok().entity(planet).build();
		} else {
			return Response.status(500).entity("Error to save planet").build();
		}

	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePlanet(Planet cliente) {
		boolean isClienteAtualizado = new PlanetController().updatePlanet(cliente);
		if (isClienteAtualizado == true) {
			return Response.ok().entity(cliente).build();
		} else {
			return Response.status(500).entity("Error to update planet").build();
		}

	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePlanet(@PathParam("id") String id) {
		System.out.println("Calling delete method");
		boolean isClienteDeletado = new PlanetController().deletePlanet(id);
		if (isClienteDeletado == true) {
			System.out.println("Planet with id " + id + " deleted");
			return Response.ok().entity(id).build();
		} else {
			return Response.status(500).entity("Error to delete planet: " + id).build();
		}

	}

}
