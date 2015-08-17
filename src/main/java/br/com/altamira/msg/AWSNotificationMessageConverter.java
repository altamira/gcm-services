package br.com.altamira.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.util.MimeType;

/**
 *  Waiting for this https://github.com/spring-cloud/spring-cloud-aws/pull/75
 *  to be merged to get this works
 *  
 *  AWSNotificationMessageConverter converter = new AWSNotificationMessageConverter();
 *  converter.setSerializedPayloadClass(String.class);
 *  return new NotificationMessagingTemplate(amazonSns, resourceIdResolver, converter);
 *  
 * @author Alessandro Holanda
 */
public class AWSNotificationMessageConverter extends
		MappingJackson2MessageConverter {

	private static final Logger LOG = LoggerFactory
			.getLogger(AWSNotificationMessageConverter.class);

	/* 
	 * Instantiate a MappingJackson2MessageConverter with empty supported mimetypes
	 * this means empty, not null array, it will converted asList
	 * when supported mimetypes are empty, all types are valid for conversion
	 */

    /**
     *
     */
    
	public AWSNotificationMessageConverter() {		
		super(new MimeType[0] 
				/*new MimeType("text", "plain", Charset.forName("UTF-8")),
				new MimeType("application", "json", Charset.forName("UTF-8"))*/);
		
		LOG.debug("Start custom message converter");
		
	}

}
