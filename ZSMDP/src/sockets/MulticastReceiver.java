package sockets;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.Main;
import app.controller.HomeController;
import app.controller.LoginController;
import javafx.application.Platform;
import model.Message;
import model.MessageType;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    private HomeController hc;
    public static boolean stop=false;
    public MulticastReceiver(HomeController hc) {
    	this.hc=hc;
    }
    public void run() {try {
        socket = new MulticastSocket(4446);
        InetAddress group = InetAddress.getByName(Main.MULTICAST_IP);
        socket.joinGroup(group);
        
        while (!stop) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(
                    packet.getData(), 0, packet.getLength());
            if(received.contains(": ")) {
                 Message msg=new Message(received.split(": ")[0],Main.MULTICAST_PORT,Main.MULTICAST_TAG,Main.MULTICAST_PORT,received.split(": ")[1],MessageType.TEXT);
                 
            if(msg.getSenderName().equals(LoginController.user.get_username()))
            	continue;
        		HomeController.inboxMap.get("multicast").add(msg);
        		Platform.runLater(()->{hc.showMessage(msg);hc.addNotification(msg);hc.refreshNotice();});
            }
          
        }
        socket.leaveGroup(group);
        socket.close();
    }catch(Exception ex) {
    	//ex.printStackTrace();
    	Logger.getLogger(MulticastReceiver.class.getName()).log(Level.WARNING, ex.toString());
    }
    }
}