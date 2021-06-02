package org.unibl.etf.mdp.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AZSInterface extends Remote {
	
    public void upload(byte[] fileStream, String filename, String user, String station) throws RemoteException;
	
	public byte[] download(String name) throws RemoteException;

}
