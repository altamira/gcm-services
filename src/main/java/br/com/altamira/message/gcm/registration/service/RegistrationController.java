package br.com.altamira.message.gcm.registration.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.altamira.message.gcm.message.MessageToProcess;
import br.com.altamira.message.gcm.registration.model.RegistrationInfo;
import br.com.altamira.message.gcm.registration.model.RegistrationTempInfo;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/{version}/register")
public class RegistrationController {
	private static final Logger LOG = LoggerFactory
			.getLogger(RegistrationController.class);

    /**
     *
     */
    public static final String MSG_REGISTERED = "GCM_REGISTERED";
    
	private NotificationMessagingTemplate notificationMessagingTemplate;
	private RegistrationService registrationService;

	/**
	 *
	 * @param notificationMessagingTemplate
	 */
	@Autowired
	public RegistrationController(
			RegistrationService registrationService,
			NotificationMessagingTemplate notificationMessagingTemplate) {
		this.registrationService = registrationService;
		this.notificationMessagingTemplate = notificationMessagingTemplate;
	}

	/**
	 * Register an new Device to Push Notification
	 * 
	 * @param deviceInfo
	 * @throws IOException 
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RegistrationTempInfo registrationTempInfo(
			@RequestBody RegistrationTempInfo registrationInfo)
			throws IOException {
		LOG.info("Registering a new device: {}", registrationInfo.toString());

		RegistrationTempInfo register = registrationService.registerTemp(registrationInfo);
		
		registrationService.requestEmailValidation(register);
		
		return register;
	}

	/**
	 * Register an new Device to Push Notification
	 * 
	 * @param deviceInfo
	 * @throws IOException 
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RegistrationInfo updateRegistrationInfo(
			@RequestBody RegistrationInfo registrationInfo)
			throws IOException {
		LOG.info("Update registration info for a device: {}",
				registrationInfo.toString());

		RegistrationInfo register = registrationService.update(registrationInfo);
		
		return register;
	}

	/**
	 * Register an new Device to Push Notification
	 * 
	 * @param deviceInfo
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> query(
			@RequestParam("email") String email) throws JsonProcessingException {
		LOG.info("Request to query tokens by registered user email: {}", email);

		Map<String, Object> tokens = new HashMap<String, Object>();

		tokens.put("email", email);
		tokens.put("tokens", registrationService.getTokensByEmail(email));

		return tokens;
	}

	/**
	 * Register an new Device to Push Notification
	 * 
	 * @param deviceInfo
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/validate/{token}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void validate(
			@PathVariable("token") String token) throws JsonProcessingException {
		LOG.info("Request to validate token: {}", token);

		RegistrationInfo register = registrationService.validate(token);
		
		if (register != null) {
			MessageToProcess message = new MessageToProcess(register);
			
			this.notificationMessagingTemplate.sendNotification(MSG_REGISTERED,
					message.toString(), "Notify a new Device Registered");
		}
	}
}
