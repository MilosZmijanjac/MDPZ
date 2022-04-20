package model;

import java.util.HashMap;

public class DataSource {
	
	public HashMap<String, Station> stations = new HashMap<>();
	private static DataSource instance = null;

	public static DataSource getInstance() {
		if (instance == null) {
			instance = new DataSource();
		}
		return instance;
	}
	
	private DataSource() {
		stations.put("BL", new Station("BL","Banja Luka"));
		stations.put("KD", new Station("KD","Kozarska Dubica"));
		stations.put("PD", new Station("PD","Prijedor"));
		stations.put("NG", new Station("NG","Novi Grad"));
		stations.put("SA", new Station("SA","Sarajevo"));
	}
}
