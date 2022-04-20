package rest;

import java.util.HashMap;

import model.DataSource;
import model.Station;

public class StationService {
	
	DataSource data = DataSource.getInstance();

	public HashMap<String,Station> getStations() {
		return data.stations;
	}

	public Station getById(String id) {
		
         return data.stations.get(id); 
		
	}

	public boolean add(Station station) {
		if(data.stations.containsKey(station.getStation_id()))
			return false;
		data.stations.put(station.getStation_id(), station);
		return data.stations.containsKey(station.getStation_id());
	}

	public boolean update(Station station, String id) {
		
		if (data.stations.containsKey(id)) {
			data.stations.put(id, station);
			return true;
		} else {
			return false;
		}
	}

	public boolean remove(String id) {
		
		if (data.stations.containsKey(id)) {
			data.stations.remove(id);
			return true;
		} else {
			return false;
		}
		
	}
	
	
}
