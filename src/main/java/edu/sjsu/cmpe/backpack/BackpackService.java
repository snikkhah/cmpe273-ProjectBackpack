package edu.sjsu.cmpe.backpack;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.backpack.api.resources.BackpackResource;
import edu.sjsu.cmpe.backpack.api.resources.RootResource;
import edu.sjsu.cmpe.backpack.config.LibraryServiceConfiguration;


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

    }
}
