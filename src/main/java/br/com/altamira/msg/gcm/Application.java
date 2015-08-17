package br.com.altamira.msg.gcm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.com.altamira.msg.AWSNotificationMessageConverter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SpringBootApplication
@EnableTransactionManagement
public class Application {

	/**
	 * Entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * IAM credential user: sales-microservice-user ATTENTION: only necessary
	 * resources on SNS, SQS and DynamoDB to run this services was allowed
	 * 
	 * @return AWSCredentials
	 */
	@Bean
	public AWSCredentials awsCredentials() {
		return new BasicAWSCredentials("AKIAIRRCBYEI5CMTSNJA",
				"/m/rkj+/zmcWWitzi7lcNRXZ4iAmN3OVqIbVTS2R");
	}

	/**
	 * 
	 * @param awsCredentials
	 * @return AmazonSQSAsyncClient
	 */
	@Bean
	public AmazonSQSAsyncClient amazonSQS(AWSCredentials awsCredentials) {
		return new AmazonSQSAsyncClient(awsCredentials);
	}

	/**
	 *
	 * @param awsCredentials
	 * @return
	 */
	@Bean
	public AmazonSNS amazonSNS(AWSCredentials awsCredentials) {
		return new AmazonSNSClient(awsCredentials);
	}

	/**
	 * 
	 * @param awsCredentials
	 * @return AmazonS3Client
	 */
	@Bean
	public AmazonS3Client amazonS3Client(AWSCredentials awsCredentials) {
		return new AmazonS3Client(awsCredentials);
	}

	/**
	 * 
	 * @param awsCredentials
	 * @param amazonS3Client
	 * @return AmazonDynamoDBClient
	 */
	@Bean
	public AmazonDynamoDBClient amazonDynamoDBClient(
			AWSCredentials awsCredentials, AmazonS3Client amazonS3Client) {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCredentials);
		client.setRegion(Region.getRegion(Regions.SA_EAST_1));
		return client;
	}

	/**
	 * 
	 * @param amazonSqs
	 * @param resourceIdResolver
	 * @return QueueMessagingTemplate
	 */
	@Bean
	public QueueMessagingTemplate queueMessagingTemplate(AmazonSQS amazonSqs,
			ResourceIdResolver resourceIdResolver) {
		amazonSqs.setRegion(Region.getRegion(Regions.SA_EAST_1));
		// AWSNotificationMessageConverter converter = new
		// AWSNotificationMessageConverter();
		// converter.setSerializedPayloadClass(String.class);
		// return new QueueMessagingTemplate(amazonSqs, resourceIdResolver,
		// converter);
		return new QueueMessagingTemplate(amazonSqs, resourceIdResolver);
	}

	/**
	 * 
	 * @param amazonSns
	 * @param resourceIdResolver
	 * @return NotificationMessagingTemplate
	 */
	@Bean
	public NotificationMessagingTemplate notificationMessagingTemplate(
			AmazonSNS amazonSns, ResourceIdResolver resourceIdResolver) {
		amazonSns.setRegion(Region.getRegion(Regions.SA_EAST_1));
		/**
		 * Waiting for this
		 * https://github.com/spring-cloud/spring-cloud-aws/pull/75 to be merged
		 * to get this works
		 * 
		 * AWSNotificationMessageConverter converter = new
		 * AWSNotificationMessageConverter();
		 * converter.setSerializedPayloadClass(String.class); return new
		 * NotificationMessagingTemplate(amazonSns, resourceIdResolver,
		 * converter);
		 */
		return new NotificationMessagingTemplate(amazonSns, resourceIdResolver);
	}

	/**
	 * 
	 * @return Jackson2ObjectMapperBuilder
	 */
	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		// builder.indentOutput(true).dateFormat(new
		// SimpleDateFormat("yyyy-MM-dd"));
		builder.indentOutput(true).serializationInclusion(Include.NON_NULL);
		return builder;
	}

	/**
	 * Custom Queue message converter used to convert messages from SNS -> SQS
	 * 
	 * @return QueueMessageHandlerFactory
	 */
	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory() {
		List<MessageConverter> converters = new ArrayList<>();

		converters.add(new AWSNotificationMessageConverter());

		CompositeMessageConverter converter = new CompositeMessageConverter(
				converters);

		QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
		factory.setArgumentResolvers(Arrays.asList(new PayloadArgumentResolver(
				converter)));

		return factory;
	}

}

