package rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import app.Main;
import model.TimetableRow;

public class TimetableService {
	public static void add(TimetableRow timetableRow) {
		try {

			URL url = new URL(Main.TIMETABLES_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = new ObjectMapper().registerModule(new JavaTimeModule())
					.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).writeValueAsString(timetableRow);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}
			conn.disconnect();

		  } catch (Exception e) {
			  Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
			//e.printStackTrace();

		  } 
	}
	
	public static void remove(String id) {
		try {
			URL url=new URL(Main.TIMETABLES_URL+id);
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
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, ex.toString());
		}
		
	}
	
	public static void update(String id, TimetableRow tRow) {
		
		try {

			URL url = new URL(Main.TIMETABLES_URL+id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = new ObjectMapper().registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).writeValueAsString(tRow);

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}
			conn.disconnect();

		  } catch (Exception e) {
			  Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
			//e.printStackTrace();

		  } 
	}
	
	public static ArrayList<TimetableRow> getTimetableRows()
	{
		ArrayList<TimetableRow> lines =null;
		
		try {
			URL url = new URL(Main.TIMETABLES_URL);
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
            lines=new ObjectMapper().registerModule(new JavaTimeModule()).readValue(output, new TypeReference<ArrayList<TimetableRow>>(){});
			conn.disconnect();
		}catch(Exception e) {
			//e.printStackTrace();
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
		}
		return lines;
		
	}
	
	public static ArrayList<TimetableRow> getStationTimetableRows(String station_id)
	{
		ArrayList<TimetableRow> lines = null;
        try {
			URL url = new URL(Main.TIMETABLES_STATION_URL+station_id);
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
            lines=new ObjectMapper().registerModule(new JavaTimeModule()).readValue(output, new TypeReference<TimetableRow>(){});
			conn.disconnect();
		}catch(Exception e) {
			//e.printStackTrace();
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
		}
		return lines;
	}
	
	public static TimetableRow getTimetableRowById(String id)
	{

		TimetableRow line = null;
        try {
			URL url = new URL(Main.TIMETABLES_URL+id);
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
            line=new ObjectMapper().registerModule(new JavaTimeModule()).readValue(output, new TypeReference<TimetableRow>(){});
			conn.disconnect();
		}catch(Exception e) {
			//e.printStackTrace();
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
		}
        return line;
	}
}
