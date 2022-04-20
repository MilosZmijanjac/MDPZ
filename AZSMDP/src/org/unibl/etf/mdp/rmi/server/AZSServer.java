package org.unibl.etf.mdp.rmi.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.unibl.etf.mdp.rmi.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AZSServer implements AZSInterface{

	public static  String PATH ;
	public static  String FILES ;
	public static  int PORT ;
	public static  String POLICY_FILE ;
	
	static{
		try (InputStream inputStream = new FileInputStream("./server.properties")) {
			
		   Properties serverprop = new Properties();
		   serverprop.load(inputStream);
		   PATH = serverprop.getProperty("path_to_resources");
		   FILES = serverprop.getProperty("path_to_reports");
		   PORT = Integer.parseInt(serverprop.getProperty("port"));
		   POLICY_FILE=serverprop.getProperty("server_policy_file");
		
		   }catch(Exception ex){
			   //ex.printStackTrace();
			   Logger.getLogger(AZSServer.class.getName()).log(Level.WARNING, ex.toString());
		   }
	}
	
	public AZSServer() throws RemoteException{
		super();
	}
	
	public void upload(byte[] fileStream, String filename, String user,
			String station) throws RemoteException {
		try{
		String finalFileName = station +"_"+ user +"_"+filename+"_"+ System.currentTimeMillis()+".pdf";
		File file = new File(FILES+File.separator + finalFileName);
		
		try{
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.beginText(); 
		    contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
		    contentStream.newLineAtOffset(50F, 750F);
		    contentStream.showText(new String(fileStream));
		    contentStream.endText();
		    contentStream.close();
		    document.save(file);
		    document.close();
		}catch(Exception ex){Logger.getLogger(AZSServer.class.getName()).log(Level.WARNING, ex.toString());}
		
		ReportDetails reportDetails = new ReportDetails(user,LocalDateTime.now(), fileStream.length);
		//serializacija objekta u JSON
		new ObjectMapper().registerModule(new JavaTimeModule()).writeValue(new File(FILES+File.separator + finalFileName.replace(".pdf", ".json")), reportDetails);
		
		}catch(Exception ex){
			//ex.printStackTrace();
			Logger.getLogger(AZSServer.class.getName()).log(Level.WARNING, ex.toString());
		}
		
	}

	public byte[] download(String name) throws RemoteException {
		try{
			//vracanje procitanih bajtova iz fajla sa zadane putanje
			PDDocument doc = PDDocument.load(new File(name));
			byte[] bytes=new PDFTextStripper().getText(doc).replace("\n","").getBytes();
			doc.close();
		return bytes;
		
		}catch(Exception ex){
			//ex.printStackTrace();
			Logger.getLogger(AZSServer.class.getName()).log(Level.WARNING, ex.toString());
			return null;
		}
	}
	
	public List<String> list()throws IOException,RemoteException{
	
			return Files.list(Paths.get(FILES)).map(m->m.toString()).collect(Collectors.toList());
	
	}
	
	
public static void main(String[] args) {
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
			//ex.printStackTrace();
			Logger.getLogger(AZSServer.class.getName()).log(Level.WARNING, ex.toString());
		}

	}
}
