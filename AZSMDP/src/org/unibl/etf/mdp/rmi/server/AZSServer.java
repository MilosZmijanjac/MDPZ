package org.unibl.etf.mdp.rmi.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Properties;

import org.unibl.etf.mdp.rmi.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AZSServer implements AZSInterface{

	public static  String PATH ;
	public static  String FILES ;
	public static  int PORT ;
	public static  String POLICY_FILE ;
	
	public AZSServer() throws RemoteException{
		super();
	}
	
	public void upload(byte[] fileStream, String filename, String user,
			String station) throws RemoteException {
		try{
		String finalFileName = station + filename + System.currentTimeMillis()+".pdf";
		File file = new File(FILES+File.separator + finalFileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(fileStream);
		fos.close();
		
		ReportDetails reportDetails = new ReportDetails(user,LocalDateTime.now(), fileStream.length);
		//serializacija objekta u JSON
		new ObjectMapper().registerModule(new JavaTimeModule()).writeValue(new File(FILES+File.separator + finalFileName.replace(".pdf", ".json")), reportDetails);
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	public byte[] download(String name) throws RemoteException {
		try{
			//vracanje procitanih bajtova iz fajla sa zadane putanje
		return Files.readAllBytes(Paths.get(FILES+File.separator+name));
		
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	private static void loadServerConfig() {
		try{
		Properties serverprop = new Properties();
		serverprop.load(new FileInputStream(new File("./server.properties")));
	   PATH = serverprop.getProperty("path_to_resources");
	   FILES = serverprop.getProperty("path_to_reports");
	   PORT = Integer.parseInt(serverprop.getProperty("port"));
	   POLICY_FILE=serverprop.getProperty("server_policy_file");
	   }catch(Exception ex){
		   ex.printStackTrace();
	   }
	}
	
public static void main(String[] args) {
		
		//ucitavanje konfiguracionog fajla
		loadServerConfig();
		//postavljanje property file-a
		System.setProperty("java.security.policy", POLICY_FILE);
		
		//postavljanje novog SecurityManager-a ako on ne postoji
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try{
		//kreiranje serverskog objekta
		AZSServer server= new AZSServer();
		//generisanje stub-a
		AZSInterface stub = (AZSInterface)UnicastRemoteObject.exportObject(server,0);
		//kreiranje Registry-a
		Registry registry=LocateRegistry.createRegistry(PORT);
		//registrovanje Registry-a
		registry.rebind("AZS", stub);
		
		System.out.println("Server started...");
		
		}catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
