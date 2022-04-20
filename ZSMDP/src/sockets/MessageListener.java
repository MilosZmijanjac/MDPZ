package sockets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Message;

import app.controller.HomeController;
import app.controller.LoginController;
import app.controller.TimetablesController;
import javafx.application.Platform;

public class MessageListener extends Thread {
	private Socket socket;
	private ObjectInputStream in;
	private boolean stop=false;
	private HomeController hc;

	public MessageListener(Socket socket,HomeController hc) throws IOException{
		this.socket=socket;
		this.hc=hc;
		in=new ObjectInputStream(socket.getInputStream());
	}
	@Override
	public void run() {
		try {
		while(!stop&&socket.isConnected()) {
			
				Object obj=in.readObject();
				
				if(obj instanceof Message) {
					Message message=(Message)obj;
					switch(message.getMessageType()) {
					case START:
						break;
					case TEXT:
						showText(message);
						break;
					case FILE:
						showText(message);
						saveFile(message);
						break;
					case END:
						stop=true;
						break;
					}
					stop=true;
				}			
			
		}
		} catch (ClassNotFoundException | IOException e) {
			Logger.getLogger(MessageListener.class.getName()).log(Level.WARNING, e.toString());
			//e.printStackTrace();
		} 
		
		try {
			in.close();
			socket.close();
		} catch (IOException e) {
			Logger.getLogger(MessageListener.class.getName()).log(Level.WARNING, e.toString());
		//	e.printStackTrace();
		}
	}
	
	private void saveFile(Message message) throws IOException {
		File recievedFile=new File(LoginController.user.get_username()+File.separator+message.getAttachmentName());
		FileOutputStream fos=new FileOutputStream(recievedFile);
		fos.write(message.getAttachment());
		fos.flush();
		fos.close();
	}
	
	private void showText(Message message) {
		System.out.println("from " + message.getSenderName()+" "+message.getSenderPort());
		System.out.println("to " + message.getRecieverName()+" "+message.getRecieverPort());
		System.out.println("text: "+ message.getText());
		
		Platform.runLater(()->{if(!HomeController.inboxMap.containsKey(message.getSenderName()))
			HomeController.inboxMap.put(message.getSenderName(),new ArrayList<Message>());
		HomeController.inboxMap.get(message.getSenderName()).add(message);hc.showMessage(message);hc.addNotification(message); hc.refreshChatBox(message.getSenderName());});
		
	}
	

}
