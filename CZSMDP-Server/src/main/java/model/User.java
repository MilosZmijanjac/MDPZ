package model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String _username;
	
	private String _password;
	
	private String _salt;
	
	private String _stationID;
	
	private Integer _port;

	public User() {
		super();
	}

	public User(String _username, String _password, String _stationID, String _salt) {
		super();
		this._username = _username;
		this._password = _password;
		this._salt=_salt;
		this._stationID = _stationID;
	}

	public String get_username() {
		return _username;
	}

	public void set_username(String _username) {
		this._username = _username;
	}

	public String get_password() {
		return _password;
	}

	public void set_password(String _password) {
		this._password = _password;
	}

	public String get_stationID() {
		return _stationID;
	}

	public void set_stationID(String _stationID) {
		this._stationID = _stationID;
	}

	public String get_salt() {
		return _salt;
	}

	public void set_salt(String _salt) {
		this._salt = _salt;
	}

	public Integer get_port() {
		return _port;
	}

	public void set_port(Integer _port) {
		this._port = _port;
	}
	
	@Override
	public String toString() {
		return "User [_username=" + _username + ", _password=" + ", _stationID=" + _stationID + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(_stationID, _username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(_stationID, other._stationID) && Objects.equals(_username, other._username);
	}
	
	
	
}
