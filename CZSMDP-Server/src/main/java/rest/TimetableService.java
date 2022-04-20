package rest;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import model.TimetableRow;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TimetableService {

	private static String REDIS_HOST="localhost";
	
	public boolean add(TimetableRow timetableRow) {
		JedisPool pool=new JedisPool(REDIS_HOST);
		try(@SuppressWarnings("resource")
		Jedis jedis=pool.getResource()){
			String timetableKey="timetableRow:"+timetableRow.getTimetableRow_id();
			if(jedis.exists(timetableKey))
			{
			   timetableRow.setTimetableRow_id(jedis.keys("*").size());	
			}
			timetableKey="timetableRow:"+timetableRow.getTimetableRow_id();
			jedis.set(timetableKey,new ObjectMapper().registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).writeValueAsString(timetableRow));
			jedis.save();
			
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
		}
			pool.close();
		
		return true;
	}
	
	public boolean delete(String id) {
		JedisPool pool = new JedisPool(REDIS_HOST);
		
		try(Jedis jedis = pool.getResource()){
			String timetableKey="timetableRow:"+id;
			if(!jedis.exists(timetableKey))
				return false;
			jedis.del(timetableKey);
			jedis.save();
		}catch (Exception e) {
			//e.printStackTrace();
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
		}
		pool.close();
		return true;
	}
	
	public boolean update(String id, TimetableRow tRow) {
		
		JedisPool pool = new JedisPool(REDIS_HOST);
		
		try(Jedis jedis = pool.getResource()){
			String timetableKey="timetableRow:"+id;
			if(!jedis.exists(timetableKey))
				return false;
			
			String timetableRowJson = jedis.get(timetableKey);
			TimetableRow timetableRow=new ObjectMapper().registerModule(new JavaTimeModule()).readValue(timetableRowJson,TimetableRow.class);
			timetableRow.setTimeSlots(tRow.getTimeSlots());
			jedis.set(timetableKey,new ObjectMapper().registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).writeValueAsString(timetableRow));
			jedis.save();
			
			pool.close();
			return true;			
			
		}catch(Exception e ) {
			//e.printStackTrace();
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
		}
		
		return false;
	}
	
	public ArrayList<TimetableRow> getTimetableRows()
	{
		ArrayList<TimetableRow> lines = new ArrayList<>();
		JedisPool pool = new JedisPool(REDIS_HOST);
		
		try(Jedis jedis = pool.getResource()){	
			
			Set<String> keys = jedis.keys("*");
			for(String key : keys) {
				lines.add(new ObjectMapper().registerModule(new JavaTimeModule()).readValue(jedis.get(key), TimetableRow.class));
			}				
			
		}catch(Exception e) {
			//e.printStackTrace();
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
		}
		
		pool.close();
		
		return lines;
	}
	
	public ArrayList<TimetableRow> getStationTimetableRows(String station_id)
	{
		ArrayList<TimetableRow> lines = new ArrayList<>();
		JedisPool pool = new JedisPool(REDIS_HOST);
		
		try(Jedis jedis = pool.getResource()){	
			
			Set<String> keys = jedis.keys("*");
			for(String key : keys) {
				String timetableRowJson=jedis.get(key);
				if(timetableRowJson.contains("\"station_id\":\""+station_id+"\""))
				lines.add(new ObjectMapper().registerModule(new JavaTimeModule()).readValue(timetableRowJson, TimetableRow.class));
			}				
			
		}catch(Exception e) {
			//e.printStackTrace();
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
		}
		
		pool.close();
		
		return lines;
	}
	
	public TimetableRow getTimetableRowById(String id)
	{
		JedisPool pool = new JedisPool(REDIS_HOST);
		TimetableRow timetableRow=null;
		try(Jedis jedis = pool.getResource()){	

			String timetableKey="timetableRow:"+id;
			if(!jedis.exists(timetableKey))
				return null;
			
			String timetableRowJson = jedis.get(timetableKey);
			timetableRow=new ObjectMapper().registerModule(new JavaTimeModule()).readValue(timetableRowJson,TimetableRow.class);			
			
		}catch(Exception e) {
			//e.printStackTrace();
			Logger.getLogger(TimetableService.class.getName()).log(Level.WARNING, e.toString());
		}
		
		pool.close();
		
		return timetableRow;

	}
	
}
