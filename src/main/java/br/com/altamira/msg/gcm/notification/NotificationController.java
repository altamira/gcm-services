package br.com.altamira.msg.gcm.notification;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.altamira.msg.MessageToProcess;
import br.com.altamira.msg.gcm.registration.RegistrationService;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/{version}/notify")
public class NotificationController {
	private static final Logger LOG = LoggerFactory
			.getLogger(NotificationController.class);

	private RegistrationService registrationService;
	private NotificationService notificationService;

	/**
	 *
	 * @param notificationMessagingTemplate
	 */
	@Autowired
	public NotificationController(
			RegistrationService registrationService,
			NotificationService notificationService) {
		this.registrationService = registrationService;
		this.notificationService = notificationService;
	}

	/**
	 * Register an new Device to Push Notification
	 * 
	 * @param deviceInfo
	 * @throws JsonProcessingException
	 */
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void notify(
			@RequestParam(value = "token", required = false, defaultValue = "") String tokenToNotify,
			@RequestParam(value = "email", required = false, defaultValue = "") String emailToNotify,
			@RequestBody MessageToProcess message)
			throws JsonProcessingException {
		LOG.info("Request to send notification to: " + (tokenToNotify.length() == 0 ? emailToNotify
				: tokenToNotify));

		// Just one device at a time, registered or not
		if (tokenToNotify.length() > 0) {
			
			notificationService.notifyCordovaPlugin(
					tokenToNotify, 
					(String) message.getHeaders().get("title"), 
					(String) message.getHeaders().get("message"), 
					message.getPayload());
			
		// Devices from a registered user
		} else if (emailToNotify.length() > 0) {
			
			List<String> tokens = registrationService
					.getTokensByEmail(emailToNotify);

			for (String token : tokens) {
				notificationService.notifyCordovaPlugin(
						token, 
						(String) message.getHeaders().get("title"),
						(String) message.getHeaders().get("message"), 
						message.getPayload());
			}
			
		}
	}

}
