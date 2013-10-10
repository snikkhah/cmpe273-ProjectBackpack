package edu.sjsu.cmpe.dropbox.api.resources;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.dropbox.domain.File;
import edu.sjsu.cmpe.dropbox.domain.User;
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
		private static int fileNum =1;
		private MongoClient mongoClient;
		private DB db;
		private DBCollection colluser,colldocument;
		
	    public DropboxResource() {
	    	try {
				mongoClient = new MongoClient( "localhost" , 27017 );
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	db = mongoClient.getDB( "test" );
	    	colluser = db.getCollection("user");
	    	colldocument = db.getCollection("document");
	    }

    @GET
    @Path("/{email}")
    @Timed(name = "view-user")
    public Response getUserByEmail(@PathParam("email") String email) {
	// FIXME - Dummy code	
/*	User user = new User();
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
   	}*/
    	DBCursor cursor = colluser.find(new BasicDBObject().append("email",email));
    	String output = "";
    	while(cursor.hasNext()) {
    	    output +=cursor.next();
    	}

    	return Response.status(200).entity(output).build();
 	// add more links
//	return user;
    }
    
    
    @POST
    @Timed(name = "create-user")
    public Response setUserByEmail(User user) {
	// FIXME - Dummy code

//    	users.put(user.getEmail(), user);
    	BasicDBObject ob = new BasicDBObject();
    	ob.append("firstName", user.getFirstName());
    	ob.append("lastName", user.getLastName());
    	ob.append("password", user.getPassword());
    	ob.append("email", user.getEmail());
    	ob.append("status", user.getStatus());
    	ob.append("designation", user.getDesignation());
    	ob.append("myFiles",new ArrayList<String>());
    	ob.append("filesShared",new ArrayList<String>());   	
    	colluser.insert(ob);
    	
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
    	BasicDBObject document = new BasicDBObject();
    	document.put("email", email);
    	colluser.remove(document);
//    	users.remove(email);
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
//    	users.get(email).getmyFiles().add(file);
    	file.setFileID(fileNum);
    	BasicDBObject ob = new BasicDBObject();
    	ob.append("name", file.getName());
    	ob.append("fileID", fileNum);
    	ob.append("owner", file.getOwner());
    	ob.append("accessType", file.getAccessType());
    	ob.append("sharedWith", new ArrayList<String>());
    	colldocument.insert(ob);
    	BasicDBObject query = new BasicDBObject().append("email", email);
//    	BasicDBObject newDoc = new BasicDBObject().append("$set", new BasicDBObject().append("myFiles", file.getName()));
//    	colluser.update(query,newDoc );
    	BasicDBObject newDoc = new BasicDBObject().append("$push", new BasicDBObject().append("myFiles", file.getFileID()));
    	colluser.update(query,newDoc );
    	LinkDto link = new LinkDto("view-file", "/users/" + email + "/files/ " + file.getFileID(),"GET");
    	fileNum++;
    	return Response.status(201).entity(link).build();
    }
    
    @PUT
    @Path("/{email}/files/{id}")
    @Timed(name = "update-files")
    public void updateFileByEmail(@PathParam("email") String email,@PathParam("id") int id,@QueryParam("sharedWith") String sharedWith) {
	// FIXME - Dummy code
/*   	users.get(email).setStatus(status);
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
*/        	BasicDBObject query = new BasicDBObject().append("email", sharedWith);
        	BasicDBObject newDoc = new BasicDBObject().append("$push", new BasicDBObject().append("filesShared", id));
        	colluser.update(query,newDoc );
        	BasicDBObject query2 = new BasicDBObject().append("fileID", id);
        	BasicDBObject newDoc2 = new BasicDBObject().append("$push", new BasicDBObject().append("sharedWith", sharedWith));
        	colldocument.update(query2,newDoc2);
}
    
    @GET
    @Path("/{email}/files/{id}")
    @Timed(name = "view-file")
    public Response getFileByEmailById(@PathParam("email") String email, @PathParam("id") int id) {
	// FIXME - Dummy code	
//    	File file = users.get(email).getmyFiles().get(id-1);
//       	FileDto fileResponse = new FileDto(file);
//    	fileResponse.addLink(new LinkDto("view-file", "/users/" + email + "/files/" + id,
//    		"GET"));
	// add more links
    	BasicDBObject andQuery = new BasicDBObject();
    	List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
    	obj.add(new BasicDBObject("fileID", id));
    	obj.add(new BasicDBObject("owner", email));
    	andQuery.put("$and", obj);
    	DBCursor cursor = colldocument.find(andQuery);
    	String output = "";
    	while(cursor.hasNext()) {
    	    output +=cursor.next();
    	}

    	return Response.status(200).entity(output).build();

//	return fileResponse;
    }
    
    @DELETE
    @Path("/{email}/files/{id}")
    @Timed(name = "delete-file")
    public LinkDto deleteFileByEmailAndId(@PathParam("email") String email, @PathParam("id") Integer id) {
	// FIXME - Dummy code
    	users.get(email).getmyFiles().remove(id);
    	return new LinkDto("create-file", "/users/" + email,"POST");
    }   
    
    @GET
    @Path("/{email}/files")
    @Timed(name = "view-all-files")
    public Response getAllFilesByEmail(@PathParam("email") String email) {
	// FIXME - Dummy code	
//    	ArrayList<File> files = users.get(email).getmyFiles();
//    	FilesDto filesResponse = new FilesDto(files);
    	DBCursor cursor = colldocument.find(new BasicDBObject().append("owner",email));
    	String output = "";
    	while(cursor.hasNext()) {
    	    output +=cursor.next();
    	}

//    	filesResponse.addLink(null);
	// add more links
    	return Response.status(200).entity(output).build();
    }
/*           
    @POST
    @Path("/{email}/filesShared")
    @Timed(name = "create-file")
    public Response createUserFileSharedByEmail(@PathParam("email") String email, File file) {
	// FIXME - Dummy code
//    	users.get(email).getFilesShared().add(file);
    	BasicDBObject query = new BasicDBObject().append("email", email);
    	BasicDBObject newDoc = new BasicDBObject().append("$push", new BasicDBObject().append("filesShared", file.getFileID()));
    	colluser.update(query,newDoc );

    	LinkDto link = new LinkDto("view-file", "/users/" + email + "/filesShared/ " + file.getFileID(),"GET");

    	return Response.status(201).entity(link).build();
    }
*/    
    @GET
    @Path("/{email}/filesShared/{id}")
    @Timed(name = "view-filesShared")
    public Response getFilesSharedByEmailById(@PathParam("email") String email, @PathParam("id") int id) {
	// FIXME - Dummy code	
//    	File file = users.get(email).getFilesShared().get(id-1);
//    	FileDto fileResponse = new FileDto(file);
//    	fileResponse.addLink(new LinkDto("view-filesShared", "/users/" + email + "/filesShared/" + id,
//    		"GET"));
	// add more links
    	BasicDBObject andQuery = new BasicDBObject();
    	List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
    	obj.add(new BasicDBObject("fileID", id));
    	obj.add(new BasicDBObject("sharedWith", email));
    	andQuery.put("$and", obj);
    	DBCursor cursor = colldocument.find(andQuery);
    	String output = "";
    	while(cursor.hasNext()) {
    	    output +=cursor.next();
    	}

    	return Response.status(200).entity(output).build();
    }
    
    @DELETE
    @Path("/{email}/filesShared/{id}")
    @Timed(name = "delete-fileShared")
    public LinkDto deleteFileSharedByEmailAndId(@PathParam("email") String email, @PathParam("id") Integer id) {
	// FIXME - Dummy code
    	users.get(email).getFilesShared().remove(id);
    	return new LinkDto("create-fileShared", "/users/" + email + "/filesShared/","POST");
    }   
    
    @GET
    @Path("/{email}/filesShared")
    @Timed(name = "view-all-filesShared")
    public Response getAllFilesSharedsByEmail(@PathParam("email") String email) {
	// FIXME - Dummy code	
//    	ArrayList<File> filesShared = users.get(email).getFilesShared();
//    	FilesDto filesSharedResponse = new FilesDto(filesShared);
//    	filesSharedResponse.addLink(null);
	// add more links
    	BasicDBObject query = new BasicDBObject().append("email",email);
    	BasicDBObject fields = new BasicDBObject();
    	fields.put("filesShared", 1);     
    	DBCursor cursor = colluser.find(query, fields);

    	String output = "";
    	while(cursor.hasNext()) {
    	    output +=cursor.next();
    	}

	// add more links
    	return Response.status(200).entity(output).build();
//    	return filesSharedResponse;
    }
  
}
