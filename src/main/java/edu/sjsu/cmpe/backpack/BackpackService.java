package edu.sjsu.cmpe.backpack;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.backpack.api.resources.BackpackResource;
import edu.sjsu.cmpe.backpack.api.resources.RootResource;
import edu.sjsu.cmpe.backpack.config.LibraryServiceConfiguration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;

public class BackpackService extends Service<LibraryServiceConfiguration> {
    public static void main(String[] args) throws Exception {
	new BackpackService().run(args);
    }

    @Override
    public void initialize(Bootstrap<LibraryServiceConfiguration> bootstrap) {
	bootstrap.setName("library-service");
    }

    @Override
    public void run(LibraryServiceConfiguration configuration,
	    Environment environment) throws Exception {
	/** Root API */
	environment.addResource(RootResource.class);
	/** Books APIs */
	environment.addResource(BackpackResource.class);

//	All the codes that are to handle Sessions
	HashSessionManager manager = new HashSessionManager();
	SessionHandler sessions = new SessionHandler(manager);
	environment.setSessionHandler(sessions);
	
    }
}
