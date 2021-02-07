package br.com.restful.filebeat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;


public class Logger {
	
	public void createLogFile() {
		try {
			File file = new File("C:\\logs\\restAPI.log");
			if(!file.exists()) {
				file.createNewFile();
				System.out.println("File created: " + file.getName());
		    } else {
		        System.out.println("File already exists.");
			}
		} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}
	
	public void writeLogEvent(String eventID, String type, String message ) {
		createLogFile();
		JSONObject json = new JSONObject();
		json.put("event_id ", eventID);
		json.put("type ", type);
		json.put("message ", message);		
		
		System.out.println("Chamadas ao método de escrever evento");
		
		try {
			FileWriter writer = new FileWriter("C:\\logs\\restAPI.log",true);
			writer.write(json.toString());
			writer.write("\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
	
	
}
