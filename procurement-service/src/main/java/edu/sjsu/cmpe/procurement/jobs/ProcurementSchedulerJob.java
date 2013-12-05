package edu.sjsu.cmpe.procurement.jobs;

import java.util.ArrayList;
import java.util.Properties;
import java.io.Serializable;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sound.midi.Receiver;
import javax.ws.rs.core.MediaType;


import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.fusesource.stomp.jms.message.StompJmsMessage;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.Every;
import edu.sjsu.cmpe.procurement.ProcurementService;

/**
 * This job will run at every 5 second.
 */
@Every("40s")
public class ProcurementSchedulerJob extends Job {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public void doJob() {
//	String strResponse = ProcurementService.jerseyClient.resource(
//		"http://ip.jsontest.com/").get(String.class);
//	log.debug("Response from jsontest.com: {}", strResponse);
    	System.out.println("Hi I'm Tom!");
    	try {
    	// TODO: Apollo STOMP Broker URL and login
    	String user = env("APOLLO_USER", "admin");
    	String password = env("APOLLO_PASSWORD", "password");
    	String host = env("APOLLO_HOST", "127.0.0.1");
    	int port = Integer.parseInt(env("APOLLO_PORT", "61613"));

    	StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
    	factory.setBrokerURI("tcp://" + host + ":" + port);

    	Connection connection = factory.createConnection(user, password);
//    	Connection connection = ProcurementService.connection;
    	String queueName = ProcurementService.queueName;
   		connection.start();
   		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
    	Destination dest = new StompJmsDestination(queueName);
    	MessageConsumer consumer = session.createConsumer(dest);
    	ArrayList<String> shareInfo = new ArrayList<String>();
    	
 //   	String topicQueue = "/topic/59640.book";
    	long waitUntil = 5000; // 5000 wait for 5 sec
    	System.out.println("Waiting for messages from " + queueName + "...");
   
    	while(true) {
    	    Message msg = consumer.receive(waitUntil);
    		    if( msg instanceof  TextMessage) {
    			String body = ((TextMessage) msg).getText();
   			
    			System.out.println("Received TextMessage = " + body);
//    			String parse[] = body.split("[:]");
    			shareInfo.add(body);
    		    } 
    		    else if (msg == null) {
    		          System.out.println("No new messages. Existing due to timeout - " + waitUntil / 1000 + " sec");
    		          break;
    		    } else {
    		         System.out.println("Unexpected message type: " + msg.getClass());
    		    }
    	}
    	
    	if (shareInfo.size()!= 0){   		
	//		System.out.println(response);
			for (int i = 0 ; i<shareInfo.size() ; i++){
				System.out.println(shareInfo.get(i));
				String parse[] = shareInfo.get(i).split("[;]");
				String msg = "Dear "+parse[3]+",\n\n"+parse[2]+" has shared a file with you:"+parse[1]+"\nPlease click on the link below to download the file:\n"+parse[4]
						+"\n\nThanks for using Backpack! See you soon.\n\nBackpack Inc.";
				sendEmail(parse[0], msg);

			}

    	}
    	
   		Session session2 = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		String subsQueue = ProcurementService.subscriptionQueueName;
    	Destination dest2 = new StompJmsDestination(subsQueue);
    	MessageConsumer consumer2 = session2.createConsumer(dest2);
    	ArrayList<String> shareInfo2 = new ArrayList<String>();
    	
 //   	String topicQueue = "/topic/59640.book";
    	System.out.println("Waiting for messages from " + subsQueue + "...");
   
    	while(true) {
    	    Message msg = consumer2.receive(waitUntil);
    		    if( msg instanceof  TextMessage) {
    			String body = ((TextMessage) msg).getText();
   			
    			System.out.println("Received TextMessage = " + body);
//    			String parse[] = body.split("[:]");
    			shareInfo2.add(body);
    		    } 
    		    else if (msg == null) {
    		          System.out.println("No new messages. Existing due to timeout - " + waitUntil / 1000 + " sec");
    		          break;
    		    } else {
    		         System.out.println("Unexpected message type: " + msg.getClass());
    		    }
    	}
    	if (shareInfo2.size()!= 0){   		
    		//		System.out.println(response);
    				for (int i = 0 ; i<shareInfo2.size() ; i++){
    					System.out.println(shareInfo2.get(i));
    					String parse[] = shareInfo2.get(i).split("[;]");
    					String msg = "Dear "+parse[2]+",\n\nBackpack Welcomes you to our world!\nYou can log in with your email ID:"+parse[0]
    							+"\nThanks for choosing our service\nSee you soon!\n\nBackpack Inc.";
//    					sina_sad80@yahoo.com;null;Sina Nikkhah;null;null
    					sendEmail(parse[0], msg);
    				}

    	    	}
    	
    	connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void sendEmail(String recieverEmail, String msg){
    	final String username = "cmpe273s@gmail.com";
		final String password = "hlhlsissms";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		javax.mail.Session session = javax.mail.Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			javax.mail.Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("cmpe273s@gmail.com"));
			message.setRecipients(javax.mail.Message.RecipientType.TO,
				InternetAddress.parse(recieverEmail));
			message.setSubject("New Backpack content!");
			message.setText(msg);
 
			Transport.send(message);
 
			System.out.println("Email Sent");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

    }

    private static String env(String key, String defaultValue) {
	String rc = System.getenv(key);
	if( rc== null ) {
	    return defaultValue;
	}
	return rc;
    }

}
