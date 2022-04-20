package sockets;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.Main;
import model.Message;

public class MulticastSender {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    
    public  void multicast(
      Message msg)  {try {
        socket = new DatagramSocket();
        group = InetAddress.getByName(Main.MULTICAST_IP);
        String multicastMessage=msg.getSenderName()+": "+msg.getText();
        buf = multicastMessage.getBytes();
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();}
      catch(Exception e) {
    	  //e.printStackTrace();
    	  Logger.getLogger(MulticastSender.class.getName()).log(Level.WARNING, e.toString());
      }
    }
}