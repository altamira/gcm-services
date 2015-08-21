package br.com.altamira.message.gcm.registration.service;

//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.altamira.message.gcm.registration.model.RegistrationInfo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class RegistrationService {

	private static final Logger LOG = LoggerFactory
			.getLogger(RegistrationService.class);

	/**
     *
     */
	public static final String MSG_REGISTER = "GCM_REGISTER";

	private final AmazonDynamoDBClient amazonDynamoDBClient;

	/**
	 *
	 * @param amazonDynamoDBClient
	 * @param runtimeService
	 * @param repositoryService
	 * @param queueMessagingTemplate
	 * @param notificationMessagingTemplate
	 */
	@Autowired
	public RegistrationService(AmazonDynamoDBClient amazonDynamoDBClient) {
		this.amazonDynamoDBClient = amazonDynamoDBClient;
	}

	public RegistrationInfo register(RegistrationInfo registrationInfo)
			throws JsonProcessingException {

		ObjectMapper map = new ObjectMapper();

		if (registrationInfo.getId() != null) {
			LOG.debug("Update registration info for a device: {}",
					map.writeValueAsString(registrationInfo));
		} else {
			LOG.debug("Registering a new device: {}",
					map.writeValueAsString(registrationInfo));
		}

		// try {

		/*
		 * if (registrationInfo.getId() == null) {
		 * registrationInfo.setId(java.util.UUID.randomUUID().toString()); }
		 */

		DynamoDBMapper mapper = new DynamoDBMapper(this.amazonDynamoDBClient);

		mapper.save(registrationInfo);

		return registrationInfo;

		/*
		 * } catch (Exception e) { message.getHeaders().put("Exception", e);
		 * notificationMessagingTemplate.sendNotification(MSG_STORE_ROLLBACK,
		 * message.toString(), "Notify Lead Store Error"); }
		 */

	}

	public List<String> getTokensByEmail(String email) {
		DynamoDB dynamoDB = new DynamoDB(this.amazonDynamoDBClient);

		Table table = dynamoDB.getTable("GCM_REGISTRATION");

		KeyAttribute keyAttribute = new KeyAttribute("email", email);
		
		long twoWeeksAgoMilli = (new Date()).getTime() - (15L*24L*60L*60L*1000L);
		Date twoWeeksAgo = new Date();
		twoWeeksAgo.setTime(twoWeeksAgoMilli);
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		//String twoWeeksAgoStr = df.format(twoWeeksAgo);

		QuerySpec spec = new QuerySpec()
			//.withExclusiveStartKey("email", email)
		    //.withKeyConditionExpression("id = :v_id and ReplyDateTime > :v_reply_dt_tm")
		    //.withFilterExpression("PostedBy = :v_posted_by")
		    /*.withValueMap(new ValueMap()
		        .withString(":v_id", "Amazon DynamoDB#DynamoDB Thread 1")
		        .withString(":v_reply_dt_tm", twoWeeksAgoStr)
		        .withString(":v_posted_by", "User B"))*/
			.withHashKey(keyAttribute)
			.withProjectionExpression("#t")
			.withNameMap(new NameMap().with("#t", "token"))
			//.withProjectionExpression("token")
		    .withConsistentRead(true);

		List<String> tokens = new ArrayList<String>();
		
		ItemCollection<QueryOutcome> items = table.query(spec);

		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
		    tokens.add(iterator.next().get("token").toString());
		}
		
		return tokens;
	}

}
