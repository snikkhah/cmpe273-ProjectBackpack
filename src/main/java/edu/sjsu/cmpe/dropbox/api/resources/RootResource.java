package edu.sjsu.cmpe.dropbox.api.resources;

import java.io.IOException;
import java.net.URL;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.dropbox.dto.LinkDto;
import edu.sjsu.cmpe.dropbox.dto.LinksDto;

@Path("/v1/")
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.TEXT_HTML)
public class RootResource {

    public RootResource() {
	// do nothing
    }

    @GET
    @Timed(name = "get-root")
    public Response getRoot() {
	LinksDto links = new LinksDto();
	links.addLink(new LinkDto("create-user", "/users", "POST"));

	return Response.ok(links).build();}
/*   @GET
    public Response index()
   {
	   String pageContent = "";
	   try
	   {
		   URL clientPage = Resources.getResource("index.html");
		   pageContent = Resources.toString(clientPage, Charsets.UTF_8);
	   }
	   catch(IOException e){
		   return Response.serverError().build();
	   }
	   return Response.ok(pageContent).build();
   }
*/    
}

