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


import static org.junit.Assert.assertEquals;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {SpringMongoConfiguration.class})

@RunWith(Parameterized.class)
public class PlanetDAOTest {
	
	//@Parameterized.Parameter(0)
    public String idOrNameToTest;
    //@Parameterized.Parameter(1)
    public Planet expectedPlanet;

	 
 // creates the test data
    enum Type {ALLPLANETS,GETID,GETNAME,INSERT,UPDATE,DELETE};
    @Parameters
    public static Collection<Object[]> data() {
    	return Arrays.asList(new Object[][]{{Type.ALLPLANETS,null,null},{Type.GETID,"01", new Planet("01","Tatooine","arid","desert") },
    		{Type.GETID,"InexistentID", null }, {Type.GETID,"03", new Planet("03","Yavin IV","temperate, tropical","jungle, rainforests")},
    		{Type.GETNAME,"Tatooine", new Planet("01","Tatooine","arid","desert") },
    		{Type.GETNAME,"InexistentName", null }, {Type.GETNAME,"Yavin IV", new Planet("03","Yavin IV","temperate, tropical","jungle, rainforests")},
    		{Type.INSERT,null,null},
    		{Type.UPDATE,null,null},
    		{Type.DELETE,null,null}});
    }

	
	@Mock
	private MongoClient mockClient;
	@Mock
	private MongoDatabase _mockDB;
	
	private Type type;

	
	protected MongoCollection<Document> planets;
	
	private static final String CONNECTION_STRING = "mongodb://%s:%d";
	  protected MongodExecutable _mongodExe;
	  protected MongodProcess _mongod;
	  protected int port=27018;  
	  protected String ip = "localhost";
    
	 public PlanetDAOTest(Type type, String idOrNameToTest, Planet expectedPlanet) {
		 this.type=type;
		 this.idOrNameToTest=idOrNameToTest;
		 this.expectedPlanet=expectedPlanet;
	 }

	  
	  
	  @Before
	  public void setUp() throws Exception {
		  MongodExecutable _mongodExe;

		  
	        
		  MongodStarter starter = MongodStarter.getDefaultInstance();
			MongoCollection<Document> col = null;


			//int port = Network.getFreeServerPort();
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
	  

	  @After
	  public void tearDown() throws Exception {
	    _mongod.stop();
	  }

	
	
	public void printDB(MongoCollection<Document> collection) {
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
	
	@Test
	public void testListAll() throws Exception {
		
		//Given
		Assume.assumeTrue(type == Type.ALLPLANETS);
		PlanetDAO dao = new PlanetDAO("test",planets);
		ArrayList<Planet> listPlanets = new ArrayList<Planet>();
		ArrayList<Planet> listPlanetsTestData = new ArrayList<Planet>();
		listPlanetsTestData=dataArrayListPlanets();
				
		//Then
		listPlanets = dao.listAll();	
		
		//When
		assertEquals(listPlanets,listPlanetsTestData);	
		
		
		
	}
	
	
	@Test
	public void getByIdTest() {
		//Given
		Assume.assumeTrue(type == Type.GETID);
		PlanetDAO dao = new PlanetDAO("test",planets);
		Planet planet = null;
		//Then
		planet = dao.getById(idOrNameToTest);
		//When
		assertEquals(planet,expectedPlanet);		
	}
	
	@Test
	public void getByNameTest() {
		//Given
		Assume.assumeTrue(type == Type.GETNAME);
		PlanetDAO dao = new PlanetDAO("test",planets);
		Planet planet = null;
		//Then
		planet = dao.getByName(idOrNameToTest);
		//When

		assertEquals(planet,expectedPlanet);		
	}
	
	@Test
	public void insertTest() {
		//Given
		Assume.assumeTrue(type == Type.INSERT);
		PlanetDAO dao = new PlanetDAO("test",planets);
		Planet planet = new Planet("04","Bespin","temperate","gas giant");
		boolean result;
		//Then
		result = dao.insert(planet);
		//When
		assertEquals(result,true);		
	}
	
	public void updateTest() {
		//Given
		Assume.assumeTrue(type == Type.UPDATE);
		PlanetDAO dao = new PlanetDAO("test",planets);
		Planet planet = new Planet("03","Yavin IV","updated climate","updated terrain");
		boolean result;
		//Then
		result = dao.insert(planet);
		//When
		assertEquals(result,true);			
	}
	
	public void deleteTest() {
		//Given
		Assume.assumeTrue(type == Type.DELETE);
		PlanetDAO dao = new PlanetDAO("test",planets);
		boolean result;
		//Then
		result = dao.delete("01");
		//When
		assertEquals(result,true);		
	}

}

