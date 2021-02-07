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

import org.json.simple.JSONObject;

import com.mongodb.MongoException;

import br.com.restful.controller.PlanetController;
import br.com.restful.filebeat.Logger;
import br.com.restful.model.Planet;


/**
 * Classe responsavel por conter os metodos REST de acesso ao webservice
 */
@Path("/")
public class ClienteResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Planet> listAll() {
		Logger log = new Logger();
		ArrayList<Planet> listPlanets = new ArrayList<Planet>();
		System.out.println("ArrayList de planetas");
		PlanetController controller = new PlanetController();
		System.out.println(controller.listAll());
		try {
			listPlanets = controller.listAll();
			log.writeLogEvent("E61", "INFORMATION", "All planets successful listed");
			}catch(Exception e) {
				JSONObject json = new JSONObject();
				json.put("message ", "Unable to list planets");
				json.put("Exception ", e);
				log.writeLogEvent("E31", "ERROR", json.toString().replaceAll("\"", ""));
				e.printStackTrace();
		}
		return listPlanets;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{idOrName}")
	public Response getById(@PathParam("idOrName") String idOrName) {
		Logger log = new Logger();
		System.out.println("Calling getByid");
		Planet planet = null;
		//Try get planet by id
		try {
			planet = new PlanetController().searchById(idOrName);
			log.writeLogEvent("E62", "INFORMATION", "The planet of ID "+ idOrName + " has been found");
		} catch(NullPointerException e) {
			JSONObject json = new JSONObject();
			json.put("message ", "An Exception has been throwed when trying to get a planet with ID: "+ idOrName);
			json.put("Exception ", e);
			log.writeLogEvent("E32", "ERROR", json.toString().replaceAll("\"", ""));
			e.printStackTrace();
		}
		//If planet still null, try get planet by name
		if (planet == null) {
			try {
				planet = new PlanetController().searchByName(idOrName);
				log.writeLogEvent("E63", "INFORMATION", "The planet of name "+ idOrName + " has been found");
			}catch(NullPointerException e) {
				JSONObject json = new JSONObject();
				json.put("message ", "An Exception has been throwed when trying to get a planet with ID: "+ idOrName);
				json.put("Exception ", e);
				log.writeLogEvent("E33", "ERROR", json.toString().replaceAll("\"", ""));
				e.printStackTrace();
			}
			 
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
		boolean isPlanetSaved = false;
		Logger log = new Logger();
		try{
			isPlanetSaved = new PlanetController().savePlanet(planet);
			log.writeLogEvent("E64", "INFORMATION", "The planet " + planet.toString() +  " has been successful inserted");
		} catch(MongoException e){
			JSONObject json = new JSONObject();
			json.put("message ", "Unable to insert the planet " + planet.toString() +  " on MongoDB ");
			json.put("Exception ", e);
			log.writeLogEvent("E34", "ERROR", json.toString().replaceAll("\"", ""));
			e.printStackTrace();
		}
		if (isPlanetSaved == true) {
			return Response.ok().entity(planet).build();
		} else {
			return Response.status(500).entity("Error to save planet").build();
		}

	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePlanet(Planet planet) {
		Logger log = new Logger();
		boolean isPlanetUpdated = false;
		try{
			isPlanetUpdated = new PlanetController().updatePlanet(planet);
			log.writeLogEvent("E65", "INFORMATION", "The planet " + planet.toString() +  " has been successful inserted");
		}catch(MongoException e) {
			JSONObject json = new JSONObject();
			json.put("message ", "Unable to update the planet " + planet.toString() +  " on MongoDB ");
			json.put("Exception ", e);
			log.writeLogEvent("E35", "ERROR", json.toString().replaceAll("\"", ""));
			e.printStackTrace();
		}
		if (isPlanetUpdated == true) {
			return Response.ok().entity(planet).build();
		} else {
			return Response.status(500).entity("Error to update planet").build();
		}

	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePlanet(@PathParam("id") String id) {
		Logger log = new Logger();
		System.out.println("Calling delete method");
		boolean isPlanetDeleted = false;
		try{
			isPlanetDeleted = new PlanetController().deletePlanet(id);
			log.writeLogEvent("E66", "INFORMATION", "The planet with ID: " + id +  " has been successful deleted");
		} catch(MongoException e) {
			JSONObject json = new JSONObject();
			json.put("message ", "Unable to update the plane with ID: " + id +  "on MongoDB ");
			json.put("Exception ", e);
			log.writeLogEvent("E66", "ERROR", json.toString().replaceAll("\"", ""));
			e.printStackTrace();
		}
		if (isPlanetDeleted == true) {
			System.out.println("Planet with id " + id + " deleted");
			return Response.ok().entity(id).build();
		} else {
			return Response.status(500).entity("Error to delete planet: " + id).build();
		}

	}

}
