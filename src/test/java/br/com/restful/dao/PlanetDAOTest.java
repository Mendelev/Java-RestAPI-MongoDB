package br.com.restful.dao;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.FongoDB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import br.com.restful.model.Planet;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import de.bwaldvogel.mongo.MongoServer;
import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.experimental.runners.Enclosed;


import static org.junit.Assert.assertEquals;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {SpringMongoConfiguration.class})

//@RunWith(Enclosed.class)
public class PlanetDAOTest {
	
	@Mock
	private MongoClient mockClient;
	@Mock
	private MongoDatabase _mockDB;
	
	protected static MongoCollection<Document> planets;
	
	
	private static final String CONNECTION_STRING = "mongodb://%s:%d";
	  protected MongodExecutable _mongodExe;
	  protected static MongodProcess _mongod;
	  protected static int port=27018;  
	  protected static String ip = "localhost";
    
	  
	  //@Before
	  public static void setUp() throws Exception {
		  MongodExecutable _mongodExe;

		  
	        
		  MongodStarter starter = MongodStarter.getDefaultInstance();
			MongoCollection<Document> col = null;


			int port = Network.getFreeServerPort();
			ImmutableMongodConfig mongodConfig = MongodConfig.builder()
			    .version(Version.Main.PRODUCTION)
			    .net(new Net(port, Network.localhostIsIPv6()))
			    .build();

			try {
			 _mongodExe = starter.prepare(mongodConfig);
			 _mongod = _mongodExe.start();
			 MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, ip, port)), "test");
			 col = mongoTemplate.getCollection("planets");
			 planets =  col;
			 System.out.println("Lista de documentos\n");
			 System.out.println(dataListDocuments().toString());
			 planets.insertMany(dataListDocuments());	

			}catch(Exception e) {
				e.printStackTrace();
			}	
			
		  	
		  System.out.println("TEST no setup\n");
			printDB(planets);
	  }
	  

	  //@After
	  public static void tearDown() throws Exception {
	    _mongod.stop();
	  }


	public static List<Document> dataListDocuments(){
		List<Document> seedData = new ArrayList<Document>();

        seedData.add(new Document("id", "01")
            .append("name", "Tatooine")
            .append("climate", "arid")
            .append("terrain", "desert")
        );

        seedData.add(new Document("id", "02")
                .append("name", "Alderaan")
                .append("climate", "temperate")
                .append("terrain", "grasslands, mountains")
        );

        seedData.add(new Document("id", "03")
                .append("name", "Yavin IV")
                .append("climate", "temperate, tropical")
                .append("terrain", "jungle, rainforests")
       );
        
        return seedData;
		
	}
	
	public static ArrayList<Planet> dataArrayListPlanets(){
		ArrayList<Planet> listPlanets = new ArrayList<Planet>();
		Planet planet1 = new Planet("01","Tatooine","arid","desert");
		Planet planet2 = new Planet("02","Alderaan","temperate","grasslands, mountains");
		Planet planet3 = new Planet("03","Yavin IV","temperate, tropical","jungle, rainforests");
		
		listPlanets.add(planet1);
		listPlanets.add(planet2);
		listPlanets.add(planet3);
		
		
		return listPlanets;
		
	}
	
	public static void printDB(MongoCollection<Document> collection) {
    	System.out.println(collection.count());
    	MongoCursor<Document> cursor = collection.find().iterator();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                System.out.println(
                    "In the " + doc.get("id") + ", " + doc.get("name") + 
                    " by " + doc.get("climate") + " topped the charts for " + 
                    doc.get("terrain") + " straight weeks."
                );
            }
        } finally {
            cursor.close();
        } 
        
    }
	
	//@RunWith(Parameterized.class)
    public static class ListAll {		    
        @Test
    	public void testListAll() throws Exception {		
    		//Given
        	setUp();
    		PlanetDAO dao = new PlanetDAO("test",planets);
    		ArrayList<Planet> listPlanets = new ArrayList<Planet>();
    		ArrayList<Planet> listPlanetsTestData = new ArrayList<Planet>();
    		listPlanetsTestData=dataArrayListPlanets();
    				
    		//Then
    		listPlanets = dao.listAll();	
    		
    		//When
    		assertEquals(listPlanets,listPlanetsTestData);
    		tearDown();
    	}
  }
    @RunWith(Parameterized.class)
    public static class getById {		    

	    @Parameterized.Parameters
        public static Object[][] data() {
            return new Object[][]{{"01",new Planet("01","Tatooine","arid","desert") },
            		{"InexistentID", null}, {"03", new Planet("03","Yavin IV","temperate, tropical","jungle, rainforests")}            	
            };
        }

        @Parameterized.Parameter(0)
        public String idToTest;
        @Parameterized.Parameter(1)
        public Planet expectedPlanet;
	    

        @Test
    	public void getByIdTest() throws Exception {
    		//Given
        	setUp();
    		PlanetDAO dao = new PlanetDAO("test",planets);
    		Planet planet = null;
    		//Then
    		planet = dao.getById(idToTest);
    		//When
    		assertEquals(planet,expectedPlanet);
    		tearDown();
    	}
  }
    
    @RunWith(Parameterized.class)
    public static class GetByName {		    

	    @Parameterized.Parameters
        public static Object[][] data() {
            return new Object[][]{{"Tatooine", new Planet("01","Tatooine","arid","desert")},
        		{"InexistentName", null }, {"Yavin IV", new Planet("03","Yavin IV","temperate, tropical","jungle, rainforests")}           	
            };
        }

        @Parameterized.Parameter(0)
        public String nameToTest;
        @Parameterized.Parameter(1)
        public Planet expectedPlanet;
	    

        @Test
    	public void getByNameTest() throws Exception {
    		//Given
        	setUp();
    		PlanetDAO dao = new PlanetDAO("test",planets);
    		Planet planet = null;
    		//Then
    		planet = dao.getByName(nameToTest);
    		//When
    		assertEquals(expectedPlanet,planet);
    		tearDown();
    	}
  }
    
    @RunWith(Parameterized.class)
    public static class Insert {		    

    	@Parameterized.Parameters
        public static Object[][] data() {
            return new Object[][]{{new Planet("04","Bespin","temperate","gas giant"),true},
            	{new Planet(null,null,"temperate","gas giant"),false},
            	{new Planet(null,"Bespin","temperate","gas giant"),false},
            	{new Planet("04",null,"temperate","gas giant"),false},
            	{new Planet(null,null,null,null),false}
            };
        }

        @Parameterized.Parameter(0)
        public Planet planet;
        @Parameterized.Parameter(1)
        public boolean expectedResult;
	    

        @Test
    	public void insertTest() throws Exception {
    		//Given
        	setUp();
        	PlanetDAO dao = new PlanetDAO("test",planets);
    		//Then
        	boolean result = dao.insert(planet);
    		//When
    		assertEquals(expectedResult,result);	
    		tearDown();
    	}
  }
    
    @RunWith(Parameterized.class)
    public static class Update {		    

	    @Parameterized.Parameters
        public static Object[][] data() {
            return new Object[][]{{new Planet("03","Yavin IV","updated climate","updated terrain"),true},
            {new Planet(null,"Yavin IV","updated climate","updated terrain"),false},
            {new Planet("03",null,"updated climate","updated terrain"),false},
	    	{new Planet("03",null,null,"updated terrain"),false},
    		{new Planet(null,null,null,null),false}        	
            };
        }

	    @Parameterized.Parameter(0)
        public Planet planet;
        @Parameterized.Parameter(1)
        public boolean expectedResult;
	    

        @Test
    	public void updateTest() throws Exception {
    		//Given
        	setUp();
        	PlanetDAO dao = new PlanetDAO("test",planets);
    		//Then
    		boolean result = dao.insert(planet);
    		//When
    		assertEquals(expectedResult,result);		
    		tearDown();
    	}
  }
    
    @RunWith(Parameterized.class)
    public static class Delete {		    

	    @Parameterized.Parameters
        public static Object[][] data() {
            return new Object[][]{{"01",true}        	
            };
        }

        @Parameterized.Parameter(0)
        public String idToDelete;
        @Parameterized.Parameter(1)
        public boolean result;
	    

        @Test
    	public void deleteTest() throws Exception {
    		//Given
        	setUp();
        	PlanetDAO dao = new PlanetDAO("test",planets);
    		//Then
    		result = dao.delete(idToDelete);
    		//When
    		assertEquals(true,result);	
    		tearDown();
    	}
  }

    
	

}

