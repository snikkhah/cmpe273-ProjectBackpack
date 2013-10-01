package edu.sjsu.cmpe.dropbox.api.resources;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.dropbox.domain.File;
import edu.sjsu.cmpe.dropbox.domain.User;
import edu.sjsu.cmpe.dropbox.domain.Review;
import edu.sjsu.cmpe.dropbox.dto.AuthorDto;
import edu.sjsu.cmpe.dropbox.dto.AuthorsDto;
import edu.sjsu.cmpe.dropbox.dto.UserDto;
import edu.sjsu.cmpe.dropbox.dto.LinkDto;
import edu.sjsu.cmpe.dropbox.dto.LinksDto;
import edu.sjsu.cmpe.dropbox.dto.FileDto;
import edu.sjsu.cmpe.dropbox.dto.FilesDto;

	@Path("/v1/users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public class DropboxResource {
		private static HashMap<String,User> users=new HashMap<String,User>();
//		private static Long isbn = new Long(1);
	    public DropboxResource() {
		// do nothing
	    }

    @GET
    @Path("/{email}")
    @Timed(name = "view-user")
    public User getUserByEmail(@PathParam("email") String email) {
	// FIXME - Dummy code	
	User user = new User();
	user=users.get(email);
	UserDto userResponse = new UserDto(user);
	userResponse.addLink(new LinkDto("view-user", "/users/" + email,
    		"GET"));
    	userResponse.addLink(new LinkDto("update-user",
    		"/users/" + email, "PUT"));
    	userResponse.addLink(new LinkDto("delete-user",
        		"/users/" + email, "DELETE"));
    	userResponse.addLink(new LinkDto("create-file",
        		"/users/" + email +"/files", "POST"));
    	if (user.getmyFiles().size()>0){
    	userResponse.addLink(new LinkDto("view-all-files",
        		"/users/" + email + "/files", "GET"));
   	}
 	// add more links
	return user;
    }
    
    
    @POST
    @Timed(name = "create-user")
    public Response setUserByEmail(User user) {
	// FIXME - Dummy code

    	users.put(user.getEmail(), user);
    	
    	LinksDto links = new LinksDto();
    	links.addLink(new LinkDto("view-user", "/users/" + user.getEmail(),
    		"GET"));
    	links.addLink(new LinkDto("update-user",
    		"/users/" + user.getEmail(), "PUT"));
    	links.addLink(new LinkDto("update-user",
        		"/users/" + user.getEmail(), "POST"));
    	links.addLink(new LinkDto("delete-user",
        		"/users/" + user.getEmail(), "DELETE"));
    	links.addLink(new LinkDto("create-file",
        		"/users/" + user.getEmail() +"/files", "POST"));
    	return Response.status(201).entity(links).build();
    }
   
    @DELETE
    @Path("/{email}")
    @Timed(name = "delete-user")
    public LinkDto deleteUserByEmail(@PathParam("email") String email) {
	// FIXME - Dummy code
    	users.remove(email);
    	return new LinkDto("create-user", "/users","POST");
    }

    @PUT
    @Path("/{email}")
    @Timed(name = "update-user")
    public LinksDto updateUserByEmail(@PathParam("email") String email,@QueryParam("status") String status) {
	// FIXME - Dummy code
    	users.get(email).setStatus(status);
    	LinksDto userResponse = new LinksDto();
    	userResponse.addLink(new LinkDto("view-user", "/users/" + email,
        		"GET"));
        	userResponse.addLink(new LinkDto("update-user",
        		"/users/" + email, "PUT"));
        	userResponse.addLink(new LinkDto("update-user",
            		"/users/" + email, "POST"));
        	userResponse.addLink(new LinkDto("delete-user",
            		"/users/" + email, "DELETE"));
        	userResponse.addLink(new LinkDto("create-file",
            		"/users/" + email +"/files", "POST"));
        	if (users.get(email).getmyFiles().size()>0){
            	userResponse.addLink(new LinkDto("view-all-files",
                		"/users/" + email + "/files", "GET"));
            	}
    	return userResponse;
  
}

    @POST
    @Path("/{email}/files")
    @Timed(name = "create-file")
    public Response createUserFileByEmail(@PathParam("email") String email, File file) {
	// FIXME - Dummy code
    	users.get(email).getmyFiles().add(file);
    	LinkDto link = new LinkDto("view-file", "/users/" + email + "/files/ " + file.getFileID(),"GET");

    	return Response.status(201).entity(link).build();
    }
      
    @GET
    @Path("/{email}/files/{id}")
    @Timed(name = "view-file")
    public FileDto getFileByEmailById(@PathParam("email") String email, @PathParam("id") int id) {
	// FIXME - Dummy code	
    	File file = users.get(email).getmyFiles().get(id-1);
    	FileDto fileResponse = new FileDto(file);
    	fileResponse.addLink(new LinkDto("view-file", "/users/" + email + "/files/" + id,
    		"GET"));
	// add more links
	return fileResponse;
    }
    
    @GET
    @Path("/{email}/files")
    @Timed(name = "view-all-files")
    public FilesDto getAllFilesByEmail(@PathParam("email") String email) {
	// FIXME - Dummy code	
    	ArrayList<File> files = users.get(email).getmyFiles();
    	FilesDto filesResponse = new FilesDto(files);
    	filesResponse.addLink(null);
	// add more links
	return filesResponse;
    }
           
    @POST
    @Path("/{email}/filesShared")
    @Timed(name = "create-file")
    public Response createUserFileSharedByEmail(@PathParam("email") String email, File file) {
	// FIXME - Dummy code
    	users.get(email).getFilesShared().add(file);
    	LinkDto link = new LinkDto("view-file", "/users/" + email + "/filesShared/ " + file.getFileID(),"GET");

    	return Response.status(201).entity(link).build();
    }
    @GET
    @Path("/{email}/filesShared/{id}")
    @Timed(name = "view-filesShared")
    public FileDto getFilesSharedByEmailById(@PathParam("email") String email, @PathParam("id") int id) {
	// FIXME - Dummy code	
    	File file = users.get(email).getFilesShared().get(id-1);
    	FileDto fileResponse = new FileDto(file);
    	fileResponse.addLink(new LinkDto("view-filesShared", "/users/" + email + "/filesShared/" + id,
    		"GET"));
	// add more links
	return fileResponse;
    }
    @GET
    @Path("/{email}/filesShared")
    @Timed(name = "view-all-filesShared")
    public FilesDto getAllFilesSharedsByEmail(@PathParam("email") String email) {
	// FIXME - Dummy code	
    	ArrayList<File> filesShared = users.get(email).getFilesShared();
    	FilesDto filesSharedResponse = new FilesDto(filesShared);
    	filesSharedResponse.addLink(null);
	// add more links
    	return filesSharedResponse;
    }
  
}
