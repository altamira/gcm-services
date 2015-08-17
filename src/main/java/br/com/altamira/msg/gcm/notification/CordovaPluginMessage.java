package br.com.altamira.msg.gcm.notification;

public class CordovaPluginMessage {

	private String title;
	
	private int msgcnt;
	
	// works as collapse_key for cordovaPushPlugin
	private int notId; 
	
	private String message;
	
	private Object payload;
	
	//Sound to play upon notification receipt - put in the www folder in app
	private String soundname; // 'beep.wav'

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMsgcnt() {
		return msgcnt;
	}

	public void setMsgcnt(int msgcnt) {
		this.msgcnt = msgcnt;
	}

	public int getNotId() {
		return notId;
	}

	public void setNotId(int notId) {
		this.notId = notId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getSoundname() {
		return soundname;
	}

	public void setSoundname(String soundname) {
		this.soundname = soundname;
	}

}
