package br.com.altamira.message.gcm.notification.service;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import br.com.altamira.message.gcm.notification.model.CordovaPluginMessage;
import br.com.altamira.message.gcm.notification.model.GCMMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class NotificationService {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(NotificationService.class);

	/**
	 * Google API KEY
	 */
	private static final String GCM_API_KEY = "AIzaSyCZhecyNPnKfsddqXZp3QCPz3hfdyX8SXA";
	
	/**
     * Queue to send notification
     */
	public static final String MSG_SEND = "GCM_SEND_NOTIFICATION";

	private AtomicInteger messageCounter = new AtomicInteger(0);
	
    /**
     * Register an new Device to Push Notification
     * @param deviceInfo
     * @throws JsonProcessingException 
     */
    public void notifyCordovaPlugin(
    		String token,
    		String title,
    		String message,
    		Object payload) throws JsonProcessingException {
    	LOG.info("Send notification to token: {}", token);
    	
    	RestTemplate gcm = new RestTemplate();
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    	headers.add("Authorization", "key=" + GCM_API_KEY);

    	GCMMessage gcmMessage = new GCMMessage();
    	
    	gcmMessage.setToken(token);
    	gcmMessage.setCollapse_key(UUID.randomUUID().toString());
    	gcmMessage.setTime_to_live(0); // now or never
    	
    	CordovaPluginMessage cordovaMessage = new CordovaPluginMessage();
    	
    	if (title == null) { title = ""; }
    	if (message == null) { message = ""; }
    	
    	cordovaMessage.setTitle(title);
    	cordovaMessage.setMessage(message);
    	cordovaMessage.setPayload(payload);
    	cordovaMessage.setMsgcnt(1);
    	cordovaMessage.setNotId(messageCounter.incrementAndGet());
    	
    	gcmMessage.setData(cordovaMessage);
    	
    	// Note the body object as first parameter!
    	HttpEntity<?> httpEntity = new HttpEntity<Object>(gcmMessage, headers);
    	
    	ObjectMapper map = new ObjectMapper();
    	
    	LOG.info("Send gcm message: {}", map.writeValueAsString(gcmMessage));

    	gcm.exchange("https://android.googleapis.com/gcm/send", HttpMethod.POST, httpEntity, GCMMessage.class);
    }
}
