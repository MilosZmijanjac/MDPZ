package org.unibl.etf.mdp.rmi.client;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import org.unibl.etf.mdp.rmi.server.*;
//SAMO ZA TESTIRANJE, OBRISATI KASNIJE
public class AZSClient {

	public static final String PATH = "resources";
	
	public static void main(String[] args)  {
		System.setProperty("java.security.policy", PATH + File.separator + "client_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try{
		String name = "AZS";
		Registry registry = LocateRegistry.getRegistry(1099);
		AZSInterface azs = (AZSInterface) registry.lookup(name);

		Scanner in = new Scanner(System.in);
		String opcija = null;
		while (!"4".equals(opcija)) {
			System.out.println("Izaberite opciju: ");
			System.out.println("1 - Upload");
			System.out.println("2 - Download");
			System.out.println("3 - List");
			System.out.println("4 - Kraj rada");

			opcija = in.nextLine();

			switch (opcija) {
			case "1":
				azs.upload("123456789".getBytes(),"Test","Marko","KD");
				break;
			case "2":
				String fajl=in.nextLine();
				System.out.print(azs.download(fajl));
			;
				break;
			case "3":
				System.out.println(azs.list());
			case "4":
				break;
			default:
				System.out.println("Izabrana opcija ne postoji!");
			}
		}
		in.close();
	} catch (Exception ex) {
		ex.printStackTrace();
	}
		
	}
}
