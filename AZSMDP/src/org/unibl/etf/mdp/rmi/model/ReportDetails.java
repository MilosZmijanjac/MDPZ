package org.unibl.etf.mdp.rmi.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportDetails {

	private String _user;
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime _timestamp;
	
	private long _fileSize;
	
	
	public ReportDetails() {
		super();
	}
	
	
	public ReportDetails(String _user, LocalDateTime _timestamp, long _fileSize) {
		super();
		this._user = _user;
		this._timestamp = _timestamp;
		this._fileSize = _fileSize;
	}


	public String get_user() {
		return _user;
	}


	public void set_user(String _user) {
		this._user = _user;
	}


	public LocalDateTime get_timestamp() {
		return _timestamp;
	}


	public void set_timestamp(LocalDateTime _timestamp) {
		this._timestamp = _timestamp;
	}


	public long get_fileSize() {
		return _fileSize;
	}


	public void set_fileSize(long _fileSize) {
		this._fileSize = _fileSize;
	}

	
	public String toString() {
		return "ReportDetails [_user=" + _user + ", _timestamp=" + _timestamp
				+ ", _fileSize=" + _fileSize + "]";
	}

}
