package br.com.altamira.message.gcm.registration.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.altamira.message.gcm.registration.model.RegistrationInfo;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/{version}/register")
public class RegistrationController {
	private static final Logger LOG = LoggerFactory
			.getLogger(RegistrationController.class);

	private RegistrationService registrationService;

	/**
	 *
	 * @param notificationMessagingTemplate
	 */
	@Autowired
	public RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	/**
	 * Register an new Device to Push Notification
	 * 
	 * @param deviceInfo
	 * @throws JsonProcessingException
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RegistrationInfo registrationInfo(
			@RequestBody RegistrationInfo registrationInfo)
			throws JsonProcessingException {
		LOG.info("Registering a new device: {}", registrationInfo.toString());

		return registrationService.store(registrationInfo);
	}

	/**
	 * Register an new Device to Push Notification
	 * 
	 * @param deviceInfo
	 * @throws JsonProcessingException
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RegistrationInfo updateRegistrationInfo(
			@RequestBody RegistrationInfo registrationInfo)
			throws JsonProcessingException {
		LOG.info("Update registration info for a device: {}",
				registrationInfo.toString());

		return registrationService.store(registrationInfo);
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

}
