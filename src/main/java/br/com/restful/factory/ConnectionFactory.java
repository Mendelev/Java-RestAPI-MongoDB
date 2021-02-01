package br.com.restful.factory;


import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
/**
 * Classe responsavel por conter os metodos para criar e fechar o banco de dados.
 */

public class ConnectionFactory {
	
	private String SENHA_MONGODB;
	private String HOST_MONGODB;
	private String PORTA_MONGODB;
	private String USUARIO_MONGODB;
	private String DRIVER;
	private String DATABASE;
	
	/**
	 * Metodo responsavel por criar uma conexao com o banco
	 */

	public MongoClient createConnection() {

		// conexão local
		HOST_MONGODB = "10.2.21.9";
		PORTA_MONGODB = "8082";
		USUARIO_MONGODB = "testUser";
		SENHA_MONGODB = "pass";
		DATABASE = "test";
		
		
		String connect = "mongodb://" + USUARIO_MONGODB + ":" + SENHA_MONGODB + "@" + HOST_MONGODB + ":" + PORTA_MONGODB + "/" + DATABASE;		
		
		ConnectionString connString = new ConnectionString(connect);
		
		
		MongoClient mongoClient = null;
		try {

			MongoClientSettings settings = MongoClientSettings.builder()
				    .applyConnectionString(connString)
				    .retryWrites(true)
				    .build();
			mongoClient = MongoClients.create(settings);
			System.out.println("Conexao criada");

		} catch (Exception e) {
			System.out.println("Erro ao criar conexao com o banco: " + connString);
			e.printStackTrace();
		}
		
		return mongoClient; 
		
	}

}
