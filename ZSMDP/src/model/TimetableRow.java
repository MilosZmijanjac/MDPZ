package model;

import java.io.Serializable;
import java.util.ArrayList;

public class TimetableRow implements Serializable {

	private static final long serialVersionUID = 1L;

	private int timetableRow_id=0;
	
	private static int count=0;
	
	private ArrayList<TimetableSlot> timeSlots;
	
	
	@SuppressWarnings("static-access")
	public TimetableRow() {
		super();
		this.timeSlots = new ArrayList<>();
		this.timetableRow_id=count++;
	}
	
	@SuppressWarnings("static-access")
	public TimetableRow(ArrayList<TimetableSlot> timeSlots) {
		super();
		this.timeSlots = timeSlots;
		this.timetableRow_id=count++;
	}
	
	public  int getTimetableRow_id() {
		return timetableRow_id;
	}

	public  void setTimetableRow_id(int timetableRow_id) {
		this.timetableRow_id = timetableRow_id;
	}

	public ArrayList<TimetableSlot> getTimeSlots() {
		return timeSlots;
	}

	public void setTimeSlots(ArrayList<TimetableSlot> timeSlots) {
		this.timeSlots = timeSlots;
	}
		
}
