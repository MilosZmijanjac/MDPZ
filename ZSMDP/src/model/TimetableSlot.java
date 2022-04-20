package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class TimetableSlot implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int slot_id;
	
	private static int count=0;

	private Station station;
	
	private LocalDateTime arrivalTime;
	
	private LocalDateTime expectedTime;
	
	private boolean trainPassed;
	

	public TimetableSlot() {
		super();
		this.slot_id=count++;
	}

	public TimetableSlot(Station station, LocalDateTime arrivalTime, LocalDateTime expectedTime, boolean trainPassed) {
		super();
		this.station = station;
		this.arrivalTime = arrivalTime;
		this.expectedTime = expectedTime;
		this.trainPassed = trainPassed;
		this.slot_id=count++;
	}
	
	public int getSlot_id() {
		return slot_id;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public LocalDateTime getExpectedTime() {
		return expectedTime;
	}

	public void setExpectedTime(LocalDateTime expectedTime) {
		this.expectedTime = expectedTime;
	}

	public boolean isTrainPassed() {
		return trainPassed;
	}

	public void setTrainPassed(boolean trainPassed) {
		this.trainPassed = trainPassed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(slot_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimetableSlot other = (TimetableSlot) obj;
		return slot_id == other.slot_id;
	}

	@Override
	public String toString() {
		return "TimetableSlot [slot_id=" + slot_id + ", station=" + station + ", arrivalTime=" + arrivalTime
				+ ", expectedTime=" + expectedTime + ", trainPassed=" + trainPassed + "]";
	}
	
	
	
	
	

}
