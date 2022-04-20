package rest;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.Main;
import app.controller.UsersController;

import java.io.InputStreamReader;
import java.io.OutputStream;

import model.Station;

public class StationService {


	public static HashMap<String,Station> getStations() {
		HashMap<String,Station> map=null;
		try {
			URL url = new URL(Main.STATIONS_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output=br.readLine();
            map=new ObjectMapper().readValue(output, new TypeReference<HashMap<String,Station>>(){});
			conn.disconnect();
		}catch(Exception e) {
			//e.printStackTrace();
			Logger.getLogger(StationService.class.getName()).log(Level.WARNING, e.toString());
		}
		return map;
	}

	public static Station getById(String id) {
		
         Station station=null;
         try {
 			URL url = new URL(Main.STATIONS_URL+id);
 			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 			conn.setRequestMethod("GET");
 			conn.setRequestProperty("Accept", "application/json");

 			if (conn.getResponseCode() != 200) {
 				throw new RuntimeException("Failed : HTTP error code : "
 						+ conn.getResponseCode());
 			}

 			BufferedReader br = new BufferedReader(new InputStreamReader(
 				(conn.getInputStream())));

 			String output=br.readLine();
             station=new ObjectMapper().readValue(output, new TypeReference<Station>(){});
 			conn.disconnect();
 		}catch(Exception e) {
 			//e.printStackTrace();
 			Logger.getLogger(StationService.class.getName()).log(Level.WARNING, e.toString());
 		}
         return station;
		
	}

	public static void add(Station station) {
		try {

			URL url = new URL(Main.STATIONS_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = new ObjectMapper().writeValueAsString(station);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}
			conn.disconnect();

		  } catch (Exception e) {
			//e.printStackTrace();
			  Logger.getLogger(StationService.class.getName()).log(Level.WARNING, e.toString());
		  } 
	}

	public static void update(Station station, String id) {
		
		try {

			URL url = new URL(Main.STATIONS_URL+id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = new ObjectMapper().writeValueAsString(station);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}
			conn.disconnect();

		  } catch (Exception e) {
			  Logger.getLogger(StationService.class.getName()).log(Level.WARNING, e.toString());
			//e.printStackTrace();
		  } 
	}

	public static void remove(String id) {
		try {
			URL url=new URL(Main.STATIONS_URL+id);
    		HttpURLConnection conn=(HttpURLConnection)url.openConnection();
    		conn.setDoOutput(true);
    		conn.setRequestMethod("DELETE");
    		
    		OutputStream out=conn.getOutputStream();
    		out.flush();
    		out.close();
    		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
    			throw new RuntimeException("Failed : HTTP error code : "
    				+ conn.getResponseCode());
    		}
    		conn.disconnect();
		}catch(Exception ex) {
			//ex.printStackTrace();
			Logger.getLogger(StationService.class.getName()).log(Level.WARNING, ex.toString());
		}
	}
}
