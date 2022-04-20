package org.unibl.etf.mdp.rmi.server;

import java.io.IOException;
import java.nio.file.Path;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Stream;

public interface AZSInterface extends Remote {
	
    public void upload(byte[] fileStream, String filename, String user, String station) throws RemoteException;

	public byte[] download(String name) throws RemoteException;
	
	public List<String> list()throws IOException,RemoteException;

}
