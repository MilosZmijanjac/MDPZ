package model;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String senderName;
	private Integer senderPort;
	private String recieverName;
	private Integer recieverPort;
	private String text;
	private String attachmentName;
	private byte[] attachment;
	private MessageType messageType;
	private String timeStamp;
	
	public Message() {
		super();
	}
	
	public Message(String senderName,Integer senderPort, String recieverName,Integer recieverPort, String text,MessageType messageType) {
		super();
		this.senderName = senderName;
		this.recieverName = recieverName;
		this.senderPort=senderPort;
		this.recieverPort=recieverPort;
		this.text = text;
		this.messageType=messageType;
		this.timeStamp=new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	
	public Message(String senderName,Integer senderPort, String recieverName,Integer recieverPort, String text,MessageType messageType, String attachmentName, byte[] attachment) {
		super();
		this.senderName = senderName;
		this.recieverName = recieverName;
		this.senderPort=senderPort;
		this.recieverPort=recieverPort;
		this.text = text;
		this.messageType=messageType;
		this.attachmentName=attachmentName;
		this.attachment = attachment;
		this.timeStamp=new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Integer getSenderPort() {
		return senderPort;
	}

	public void setSenderPort(Integer senderPort) {
		this.senderPort = senderPort;
	}

	public String getRecieverName() {
		return recieverName;
	}

	public void setRecieverName(String recieverName) {
		this.recieverName = recieverName;
	}

	public Integer getRecieverPort() {
		return recieverPort;
	}

	public void setRecieverPort(Integer recieverPort) {
		this.recieverPort = recieverPort;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	public String getTimestamp() {
		return timeStamp;
	}

}
