package br.com.altamira.message.gcm.registration.service;

//import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.altamira.message.gcm.registration.model.RegistrationInfo;
import br.com.altamira.message.gcm.registration.model.RegistrationTempInfo;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

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

	public RegistrationTempInfo registerTemp(RegistrationTempInfo registrationInfo)
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
	
	public RegistrationInfo update(RegistrationInfo registrationInfo)
			throws JsonProcessingException {

		ObjectMapper map = new ObjectMapper();

		if (registrationInfo.getId() != null) {
			LOG.debug("Update registration info for a device: {}",
					map.writeValueAsString(registrationInfo));
		} else {
			LOG.debug("Registering a new device: {}",
					map.writeValueAsString(registrationInfo));
		}

		DynamoDBMapper mapper = new DynamoDBMapper(this.amazonDynamoDBClient);

		mapper.save(registrationInfo);

		//return registrationInfo;
		
		DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
		Map<String, ExpectedAttributeValue> expectedAttributes = 
		    ImmutableMap.<String, ExpectedAttributeValue>builder()
		        .put("email", new ExpectedAttributeValue(true)
		        .withValue(new AttributeValue()
		        .withS(registrationInfo.getEmail())))
		        .put("token", new ExpectedAttributeValue(true)
		        .withValue(new AttributeValue()
		        .withS(registrationInfo.getToken())))
		        .build();
		saveExpression.setExpected(expectedAttributes);
		saveExpression.setConditionalOperator(ConditionalOperator.AND);
		
		try {
			mapper.save(registrationInfo, saveExpression);
		} catch (ConditionalCheckFailedException e) {
		    //Handle conditional check
		}		
		
		return registrationInfo;

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

    public RegistrationInfo validate(String token) {
   	
		DynamoDBMapper mapper = new DynamoDBMapper(this.amazonDynamoDBClient);

		RegistrationTempInfo registrationTemp = mapper.load(RegistrationTempInfo.class, token);
		
		if (registrationTemp != null) {
	    	RegistrationInfo registrationInfo = new RegistrationInfo();
			
			registrationInfo.setId(registrationTemp.getId());
			registrationInfo.setToken(registrationTemp.getToken());
			registrationInfo.setEmail(registrationTemp.getEmail());
			registrationInfo.setTimestamp(registrationTemp.getTimestamp());
			registrationInfo.setDevice(registrationTemp.getDevice());
			
			mapper.save(registrationInfo);
			
			mapper.delete(registrationTemp);
			
			return registrationInfo;
		}

		return null;
    }
    
	static final String FROM = "sistema@altamira.com.br";   // Replace with your "From" address. This address must be verified.

    static final String SUBJECT = "Altamira - Confirmacao de email";
    
    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
    static final String SMTP_USERNAME = "AKIAJYVC7NT5DSGRFAHQ";  // Replace with your SMTP username.
    static final String SMTP_PASSWORD = "AnpJ3bo7tNIa02fiKLl9lmpkRroHBRr5tCc6v7BqV8/9";  // Replace with your SMTP password.
    
    public void requestEmailValidation(RegistrationTempInfo registrationInfo) throws IOException { 

    	// Construct an object to contain the recipient address.
        Destination destination = new Destination().withToAddresses(new String[]{registrationInfo.getEmail()});
        
        //String validateUrl = String.format("http://gcm.altamira.com.br/api/0.0.4-SNAPSHOT/register/validate/%s", registrationInfo.getToken());
        String validateUrl = String.format("http://gcm.altamira.com.br/#/token/%s", registrationInfo.getToken());
        
        StringBuilder bodyHtml = new StringBuilder();
        
        bodyHtml.append("<html>");
        bodyHtml.append("<head>");
        bodyHtml.append("<title>Altamira - Confirmacao de email</title>");
        bodyHtml.append("<link rel=\"icon\" type=\"image/x-icon\" href=\"http://gcm.altamira.com.br/favicon.ico\" />");
        bodyHtml.append("</head>");
        bodyHtml.append("<body style=\"-webkit-font-feature-settings: 'liga' 1, 'onum' 1, 'kern' 1; background-color: white; color: #3a4145; font-family: Merriweather, serif; font-size: 18px; letter-spacing: 0.100000001490116px; line-height: 31.5px; margin-bottom: 1.75em; text-rendering: geometricPrecision;\">");
        bodyHtml.append("<h2><img src=\"http://gcm.altamira.com.br/images/logo.png\" />&nbsp;&nbsp;Altamira Industria Metalurgica&nbsp;</h2>");
        bodyHtml.append("<p>Este &eacute; um email autom&aacute;tico enviado pelo Sistema de Vendas da empresa Altamira Ind&uacute;stria Metalurgica.</p>");
        bodyHtml.append("<p>Voc&ecirc; esta recebendo esta mensagem para confirmar o endere&ccedil;o de e-mail informado durante a&nbsp;instala&ccedil;&atilde;o do aplicativo de vendas da Altamira.</p>");
        bodyHtml.append("<p>Para liberar as funcionalidades do seu aplicativo &eacute; necess&aacute;rio clicar no link abaixo para&nbsp;validar o seu endere&ccedil;o de e-mail:</p>");
        bodyHtml.append("<p><a title=\"Valida&ccedil;&atilde;o de email\" href=\"" + validateUrl + "\">" + validateUrl + "</a></p>");
        bodyHtml.append("</br>");
        bodyHtml.append("<p>Obrigado por usar o aplicativo e boas vendas !</p>");
        bodyHtml.append("<p>Qualquer d&uacute;vida, sugest&atilde;o ou dificuldade em usar o aplicativo, favor enviar e-mail para <a href=\"mailto:sistema@altamira.com.br\">sistema@altamira.com.br</a>.</p>");
        bodyHtml.append("</br>");
        bodyHtml.append("</body>");
        bodyHtml.append("</html>");
        
        // Create the subject and body of the message.
        Content subject = new Content().withData(SUBJECT);
        Content contentHtml = new Content().withData(bodyHtml.toString()); 
        Body body = new Body().withHtml(contentHtml);
        
        // Create a message with the specified subject and body.
        com.amazonaws.services.simpleemail.model.Message message = new com.amazonaws.services.simpleemail.model.Message().withSubject(subject).withBody(body);
        
        // Assemble the email.
        SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);
        
        /*try
        {   */     
            System.out.println("Sending an email through Amazon SES to validate device.");
        
            // Instantiate an Amazon SES client, which will make the service call. The service call requires your AWS credentials. 
            // Because we're not providing an argument when instantiating the client, the SDK will attempt to find your AWS credentials 
            // using the default credential provider chain. The first place the chain looks for the credentials is in environment variables 
            // AWS_ACCESS_KEY_ID and AWS_SECRET_KEY. 
            // For more information, see http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(
            		/*new BasicAWSCredentials("AKIAIRRCBYEI5CMTSNJA",	"/m/rkj+/zmcWWitzi7lcNRXZ4iAmN3OVqIbVTS2R")*/
            		/*new BasicAWSCredentials(SMTP_USERNAME, SMTP_PASSWORD)*/);
               
            Region REGION = Region.getRegion(Regions.US_WEST_2);
            client.setRegion(REGION);
       
            // Send the email.
            client.sendEmail(request);  
            System.out.println("Email sent!");
        /*}
        catch (Exception ex) 
        {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }*/
   
    }
}
