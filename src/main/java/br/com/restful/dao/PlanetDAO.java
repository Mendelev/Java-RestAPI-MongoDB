package br.com.restful.dao;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.BasicDBObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.MongoException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
//import org.json.JSONObject;
import org.json.simple.JSONObject;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

import br.com.restful.factory.ConnectionFactory;
import br.com.restful.factory.SWAPIConnectionFactory;
import br.com.restful.filebeat.Logger;
import br.com.restful.model.Planet;

/**
 * Classe responsovel por conter os metodos do CRUD.
 */
 public class  PlanetDAO extends ConnectionFactory {

	protected MongoCollection<Document> planets;

	//Constructor to get database name and connect to database
	//The planetsCollection is optional and is used to instantiate a
	//PlanetDAO for unit tests with a Mock without depends a MongoDB available
	public PlanetDAO(String database, MongoCollection<Document> planetsCollection) {

		if(planetsCollection==null) {
			System.out.println("Planets == null");
			MongoClient mongoClient = createConnection();
			MongoDatabase db = mongoClient.getDatabase("test");			
			planets = db.getCollection("planets");
		}
		else {
			System.out.println("Planets != null");
			planets=planetsCollection;
		}
	}
	
	
	public void calculateFilmsFromPlanet(Planet planet) throws JSONException {
		SWAPIConnectionFactory connection = new SWAPIConnectionFactory();		
		JsonObject jsonObject = new JsonObject();
		int maxPage=6;
		Integer count=1;
		boolean nextPage=true;
		while(nextPage) {
			try {
				jsonObject = connection.getBuilder("planets/",count.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			JsonElement obj2=null;
			JsonObject obj3=null;
			
			JsonArray arr1 = jsonObject.getAsJsonArray("results");
			for(int i=0; i<arr1.size(); i++) {
				obj2 = arr1.get(i);
				obj3 = obj2.getAsJsonObject();
				
				if(obj3.get("name").getAsString().equals(planet.getName())) {
					nextPage=false;
					break;
				}
			}
			
			if(nextPage && count<maxPage) {
				count++;
			}
			else {
				JsonArray arr = obj3.getAsJsonArray("films");				
				planet.setAmountFilms(arr.size());	
			}		
		}
	}

	//List all planets on mongoDB
	
	public ArrayList<Planet> listAll() {  
		
		ArrayList<Planet> listPlanets = new ArrayList<Planet>();	
		MongoCursor<Document> cursor = null;
		
		
		try {

			cursor = planets.find().iterator();			
			
	            while (cursor.hasNext()) {
	                Document doc = cursor.next();
	                
	                Planet planet = new Planet();
	                
	                planet.setId(doc.getString("id"));
	                planet.setName(doc.getString("name"));
	                planet.setClimate(doc.getString("climate"));
	                planet.setTerrain(doc.getString("terrain"));
	                
	                if(planet.getAmountFilms()==0) {
	                	calculateFilmsFromPlanet(planet);
	                }
	                
	                listPlanets.add(planet);                
	                
	            }
	        } catch (Exception e) {
				System.out.println("Error when listing planets: " + e);
				
				e.printStackTrace();
	        }
			finally {
	            cursor.close();
	        }		
			
		return listPlanets;
	}

	//Search a planet by ID	
	public Planet getById(String id) {

		Planet planet = null;

		try {
			
			planet = new Planet();
			
			Document doc = planets.find(eq("id",id)).first();			
		
			planet.setId(doc.getString("id"));
			planet.setName(doc.getString("name"));
			planet.setClimate(doc.getString("climate"));
			planet.setTerrain(doc.getString("terrain"));
			try {
				calculateFilmsFromPlanet(planet);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (NullPointerException e) {
			planet = null;
			System.out
					.println("Error searching planets ID=" + id + "\n" + e);
			//e.printStackTrace();
		}
		return planet;


	}
	
	public Planet getByName(String name) {
		Planet planet = null;

		try {
			
			planet = new Planet();
			
			Document doc = planets.find(eq("name",name)).first();			
		
			planet.setId(doc.getString("id"));
			planet.setName(doc.getString("name"));
			planet.setClimate(doc.getString("climate"));
			planet.setTerrain(doc.getString("terrain"));
			calculateFilmsFromPlanet(planet);
		    
			
		} catch (Exception e) {
			planet = null;
			System.out
					.println("Error searching planets name=" + name + "\n" + e);
			e.printStackTrace();
		} 
		return planet;

	}

    
	public boolean insert(Planet planet) {
		String id = planet.getId();
		String name = planet.getName();
		String climate = planet.getClimate();
		String terrain = planet.getTerrain();
		
		boolean isSaved=false;
		
		//If any attribute is Null, the planet will note be inserted
		if(planet.isNull()) {
			System.out.println("There is at leats one attribute null, this planet will not be inserted");
			return isSaved;
		}
		
		try {
			Document doc = new Document("_id", new ObjectId());
			doc.append("id",id);
			doc.append("name", name);
			doc.append("climate", climate);
			doc.append("terrain", terrain);
			
			planets.insertOne(doc);

			isSaved = true;
			System.out.println("Element inserted: " + doc.toJson());

		} catch (MongoException e) {
			// TODO Auto-generated catch block
			isSaved = false;
			e.printStackTrace();

		}
		return isSaved;
	}

	public boolean update(Planet planet) {
		

		String id = planet.getId();
		String name = planet.getName();
		String climate = planet.getClimate();
		String terrain = planet.getTerrain();	
		
		
		boolean isUpdated = false;
		//If any attribute is Null, the planet will note be inserted
		if(planet.isNull()) {
			System.out.println("There is at least one attribute null, this planet will not be udpated");
			return isUpdated;
		}
		
		try {
			
		
			Bson filter = eq("id",id);
			Bson updateName = set("name",name);
			Bson updateClimate = set("climate",climate);
			Bson updateTerrain = set("terrain",terrain);
			
			UpdateResult updatedName = planets.updateOne(filter, updateName);
			UpdateResult updatedClimate = planets.updateOne(filter, updateClimate);
			UpdateResult updatedTerrain = planets.updateOne(filter, updateTerrain);			
			
			isUpdated = true;
			System.out.println("Update done: " + updatedName + " " + updatedClimate + " " + updatedTerrain);

		} catch (MongoException e) {
			isUpdated = false;
			e.printStackTrace();

		} 
		return isUpdated;

	}
	
	  public boolean delete(String id) {
	 
		boolean isDeleted = false;
		
		try {		
			
			Bson filter = eq("id",id);
			planets.deleteOne(filter);
			
			isDeleted = true;


		} catch (MongoException e) {
			isDeleted = false;
			e.printStackTrace();

		}
		return isDeleted;
	}

}
