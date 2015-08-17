package br.com.altamira.msg.gcm.notification;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class GCMMessage implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6309468605485875899L;
	
	private List<String> registration_ids;
	
    private CordovaPluginMessage data;
    
    private String collapse_key;
    
    //Default is false
    private boolean delay_while_idle = false;
    
    // Duration in seconds to hold in GCM and retry before timing out. Default 4 weeks (2,419,200 seconds) if not specified.
    private long time_to_live = 2419200;
    
	public CordovaPluginMessage getData() {
		return data;
	}
	
	public void setData(CordovaPluginMessage data){
    	this.data = data;
    }

	public List<String> getRegistration_ids() {
		return registration_ids;
	}

    public void setToken(String token){
        if(registration_ids == null)
            registration_ids = new LinkedList<String>();
        registration_ids.add(token);
    }

	public void setRegistration_ids(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public String getCollapse_key() {
		return collapse_key;
	}

	public void setCollapse_key(String collapse_key) {
		this.collapse_key = collapse_key;
	}

	public boolean isDelay_while_idle() {
		return delay_while_idle;
	}

	public void setDelay_while_idle(boolean delay_while_idle) {
		this.delay_while_idle = delay_while_idle;
	}

	public long getTime_to_live() {
		return time_to_live;
	}

	public void setTime_to_live(long time_to_live) {
		this.time_to_live = time_to_live;
	}

    
}
