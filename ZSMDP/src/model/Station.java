package model;

import java.io.Serializable;
import java.util.Objects;

public class Station implements Serializable {

	private static final long serialVersionUID = 1L;

	private String station_id;
	
	private String station_name;
	

	public Station() {
		super();
	}

	public Station(String station_id, String station_name) {
		super();
		this.station_id = station_id;
		this.station_name = station_name;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	@Override
	public String toString() {
		return "Station [station_id=" + station_id + ", station_name=" + station_name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(station_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		return Objects.equals(station_id, other.station_id);
	}
	
	

}
