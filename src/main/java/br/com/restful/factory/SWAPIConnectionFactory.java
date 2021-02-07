package br.com.restful.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.codehaus.jettison.json.JSONException;
//import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;


public class SWAPIConnectionFactory {
	
	
	public JsonObject startConnection(String url_string) {
		
		JsonObject data_obj = null;
		 try {

	            URL url = new URL(url_string);

	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("GET");
	            conn.connect();

	            //Getting the response code
	            int responsecode = conn.getResponseCode();

	            if (responsecode != 200) {
	                throw new RuntimeException("HttpResponseCode: " + responsecode);
	            } else {

	                String inline = "";
	                Scanner scanner = new Scanner(url.openStream());

	                //Write all the JSON data into a string using a scanner
	                while (scanner.hasNext()) {
	                    inline += scanner.nextLine();
	                }

	                //Close the scanner
	                scanner.close();
	                
	                System.out.println("Dados lidos pelo scanner:\n");
	                System.out.println(inline);
	                System.out.println("Encerrando leitura\n");

	                //Using the JSON simple library parse the string into a json object
	                JSONParser parse = new JSONParser();
	                data_obj = (JsonObject) parse.parse(inline);

	                //Get the required object from the above created object
	                
	                
	            }
	     }  catch (Exception e) {
	            e.printStackTrace();
	     }           
	                
	     return data_obj;           

		 
	}
	
	public JsonObject getBuilder(String path, String pageNumber) throws Exception {		
		HttpGet httpGet;
        if (pageNumber == null) {
            httpGet = new HttpGet("https://swapi.dev/api/" + path + "/");
        } else {
            httpGet = new HttpGet("https://swapi.dev/api/" + path + "/?page=" + pageNumber);
        }
        return this.getRequest(httpGet);
    }
	
	public JsonObject getRequest(HttpGet getRequest) throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        getRequest.addHeader("accept", "application/json");
        HttpResponse response = httpClient.execute(getRequest);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        JsonObject jsonObject = deserialize(stringBuilder.toString());
        bufferedReader.close();

        return jsonObject;
    }

    public JsonObject deserialize(String json) {
        Gson gson = new Gson();
        JsonObject jsonClass = gson.fromJson(json, JsonObject.class);
        return jsonClass;
    }

    /*public JsonObject innerRequest(String uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        return getRequest(httpGet);
    }	
    */
}
