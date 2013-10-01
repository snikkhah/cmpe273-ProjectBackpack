package edu.sjsu.cmpe.dropbox;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.dropbox.api.resources.DropboxResource;
import edu.sjsu.cmpe.dropbox.api.resources.RootResource;
import edu.sjsu.cmpe.dropbox.config.LibraryServiceConfiguration;


public class DropboxService extends Service<LibraryServiceConfiguration> {
    public static void main(String[] args) throws Exception {
	new DropboxService().run(args);
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
	environment.addResource(DropboxResource.class);

    }
}
