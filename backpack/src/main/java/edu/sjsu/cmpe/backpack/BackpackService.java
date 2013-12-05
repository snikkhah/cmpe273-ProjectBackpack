package edu.sjsu.cmpe.backpack;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.backpack.api.resources.BackpackResource;
import edu.sjsu.cmpe.backpack.api.resources.RootResource;
import edu.sjsu.cmpe.backpack.config.backpackServiceConfiguration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;

import javax.jms.Connection;

public class BackpackService extends Service<backpackServiceConfiguration> {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
    public static void main(String[] args) throws Exception {
	new BackpackService().run(args);
    }

    @Override
    public void initialize(Bootstrap<backpackServiceConfiguration> bootstrap) {
	bootstrap.setName("Backpack-service");
    }

    @Override
    public void run(backpackServiceConfiguration configuration,
	    Environment environment) throws Exception {

//	Setting up Apollo
	String queueName = configuration.getStompQueueName();
	String subsQueueName = configuration.getStompSubscriptionQueueName();
	log.debug("Queue name is {}. Topic name is {}", queueName,
			null);
		// TODO: Apollo STOMP Broker URL and login

		String user = env("APOLLO_USER", configuration.getApolloUser());
		String password = env("APOLLO_PASSWORD", configuration.getApolloPassword());
		String host = env("APOLLO_HOST", configuration.getApolloHost());
		int port = Integer.parseInt(env("APOLLO_PORT", configuration.getApolloPort()));
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);

		Connection connection = factory.createConnection(user, password);
		connection.start();

	
//	All the codes that are to handle Sessions
	HashSessionManager manager = new HashSessionManager();
	SessionHandler sessions = new SessionHandler(manager);
	environment.setSessionHandler(sessions);

	/** Root API */
	environment.addResource(RootResource.class);
	/** Backpack APIs */
	environment.addResource(new BackpackResource(connection, queueName,subsQueueName));

	
    }
    
    private static String env(String key, String defaultValue) {
    	String rc = System.getenv(key);
    	if( rc== null ) {
    	    return defaultValue;
    	}
    	return rc;
    }
}
