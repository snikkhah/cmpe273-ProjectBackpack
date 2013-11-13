package edu.sjsu.cmpe.dropbox.api.resources;



import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.sun.jersey.core.impl.provider.entity.StringProvider;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.dropbox.domain.File;
import edu.sjsu.cmpe.dropbox.domain.User;
import edu.sjsu.cmpe.dropbox.dto.UserDto;
import edu.sjsu.cmpe.dropbox.dto.LinkDto;
import edu.sjsu.cmpe.dropbox.dto.LinksDto;
import edu.sjsu.cmpe.dropbox.dto.FileDto;
import edu.sjsu.cmpe.dropbox.dto.FilesDto;
import freemarker.ext.beans.SimpleMapModel;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

	@Path("/v1/users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public class DropboxResource {
		private MongoClient mongoClient;
		private DB db;
		private DBCollection colluser,colldocument;
		private Configuration cfg;
		private Template template;
		
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
	    	cfg = createFreemarkerConfiguration();
	    }

 public boolean checkOwnerOfFile(int userId,int fileId){
	    	
	    	DBObject ownerID = null;
	    	BasicDBObject query = new BasicDBObject("fileID",fileId);
	    	query.append("owner",userId);
	    	BasicDBObject fields = new BasicDBObject();    	
	    	DBCursor cursor = colldocument.find(query, fields);
			while (cursor.hasNext()) {
				ownerID = cursor.next();
				System.out.println("ownerId" + ownerID.get("owner"));
			}
		if (ownerID == null){
			System.out.println("OwnerId and userId doesn't match");
			return false;
		}
		else {
			System.out.println("OwnerId and userId match");
			return true;
			}
	    }
	    
	    
	    public boolean checkFileSharedWith(int userId,int fileId){
	    	DBObject object = null;
	    	BasicDBObject query = new BasicDBObject("fileID",fileId);
	    	query.append("sharedWith", userId);
	    	BasicDBObject fields = new BasicDBObject();	    	
	    	DBCursor cursor = colldocument.find(query,fields);
	    	while(cursor.hasNext()){
	    		object = cursor.next();
	    		System.out.println("sharedWith" + object.get("sharedWith"));
	    	}
	    	if(object == null){
	    		System.out.println("File cannot be shared with user");
	    		return false;
	    	}
	    	else{
	    		System.out.println("File can be shared with the user as user exists in sharedWith");
	    		return true;
	    	}    	
	    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Timed(name = "view-all-users")
	public Response getUsers() {
    	List<DBObject> users;
    	Writer output = new StringWriter();
    	BasicDBObject query = new BasicDBObject().append("userID", new BasicDBObject("$exists",true));
    	DBCursor cursor = colluser.find(query);
    	users = cursor.toArray();
    	try {
			template = cfg.getTemplate("entry_template.ftl");
			SimpleHash root = new SimpleHash();
			root.put("users", users);
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return Response.status(200).entity(output.toString()).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/signup")
    @Timed(name = "signup-users")
	public Response sigupUsers() {
    	
    	Writer output = new StringWriter();
    	try {
			template = cfg.getTemplate("signup.ftl");
			SimpleHash root = new SimpleHash();
			root.put("firstName", "");
			root.put("lastName", "");
			root.put("password", "");
			root.put("confirmPassword", "");
			root.put("email", "");
			root.put("designation", "");	
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return Response.status(200).entity(output.toString()).build();
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(name = "create-user")
    public Response setUserByEmail(User user) {
	// FIXME - Dummy code
    	int status = 200;
    	BasicDBObject query = new BasicDBObject();
    	BasicDBObject field = new BasicDBObject();
    	field.put("userCount", 1);
    	DBCursor cursor = colluser.find(query,field);
    	int userID=99;
    	BasicDBObject obj = (BasicDBObject) cursor.next();
    	userID=obj.getInt("userCount"); 	
    	DBCursor duplicate = colluser.find(new BasicDBObject("email",user.getEmail()));
    	if (duplicate.size()!=0){
    		status = 409;
    	}
    	else if(!user.getPassword().equals(user.getConfirmPassword())) {
    		status = 400;
    	}
    	else {
    	BasicDBObject ob = new BasicDBObject();
    	ob.append("userID", userID);
    	ob.append("firstName", user.getFirstName());
    	ob.append("lastName", user.getLastName());
    	ob.append("password", user.getPassword());
    	ob.append("confirmPassword", user.getConfirmPassword());
    	ob.append("email", user.getEmail());
    	ob.append("status", user.getStatus());
    	ob.append("designation", user.getDesignation());
    	ob.append("myFiles",new ArrayList<String>());
//    	ob.append("filesShared",new ArrayList<String>());   	
    	colluser.insert(ob);
    	BasicDBObject countQuery = new BasicDBObject().append("userCount", userID);
    	BasicDBObject newDoc = new BasicDBObject();
    	newDoc.append("$set", new BasicDBObject("userCount",++userID));
    	colluser.update(countQuery,newDoc );
    	}
    	Writer output = new StringWriter();
    	try {
    		
			template = cfg.getTemplate("signup.ftl");
			SimpleHash root = new SimpleHash();
			root.put("firstName", user.getFirstName());
			root.put("lastName", user.getLastName());
			root.put("password", user.getPassword());
			root.put("confirmPassword", user.getConfirmPassword());
			root.put("email", user.getEmail());
			root.put("designation", user.getDesignation());					
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
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

    	return Response.status(status).entity(links).build();
  	
    }
    

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/login")
    @Timed(name = "login-users")
	public Response getLogin() {
    	
    	Writer output = new StringWriter();
    	try {
			template = cfg.getTemplate("login.ftl");
			SimpleHash root = new SimpleHash();
			root.put("password", "");
			root.put("email", "");
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return Response.status(200).entity(output.toString()).build();
    }

    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.TEXT_PLAIN)
    @Timed(name = "login-user")
    public Response login(User user) {
    	Writer output = new StringWriter();
    	BasicDBObject query = new BasicDBObject("email",user.getEmail());
    	DBObject cursor = colluser.findOne(query);
    	String pass = ((BasicDBObject)cursor).getString("password");
    	int userID = ((BasicDBObject)cursor).getInt("userID");
    	int status = 200;
    	try {
			template = cfg.getTemplate("login.ftl");
			SimpleHash root = new SimpleHash();
			root.put("password", user.getPassword());
			root.put("email", user.getEmail());
			
			if (!pass.equals(user.getPassword())){
    			status = 401;
			}
//			root.put("password_error", error);
			template.process(root, output);
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return Response.status(status).entity(userID).build();
    }
    @ GET
    @Path("/{userID}")
    @Produces(MediaType.TEXT_HTML)
    @Timed(name = "view-user")
    public Response getUserByUID(@PathParam("userID") int uID) {
    	DBObject user = colluser.findOne(new BasicDBObject().append("userID", uID ));
    	Writer output = new StringWriter();

    	try {
			template = cfg.getTemplate("user_template.ftl");
			SimpleHash root = new SimpleHash();
			root.put("user", user);
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return Response.status(200).entity(output.toString()).build();
    }
    
    @DELETE
    @Path("/{userID}")
    @Timed(name = "delete-user")
    public Response deleteUserByEmail(@PathParam("userID") int userID) {

    	BasicDBObject user = new BasicDBObject();
    	user.put("userID", userID);
    	colldocument.remove(new BasicDBObject("owner",userID));
    	
    	colluser.remove(user);
    	return Response.status(201).entity(new LinkDto("create-user", "/users","POST")).build();
    }

    @PUT
    @Path("/{userID}")
    @Timed(name = "update-userdata")
    public void updateUserdataByUserID(@PathParam("userID") int userID,@QueryParam("firstName") String firstName,@QueryParam("lastName") String lastName,@QueryParam("password") String password,@QueryParam("email") String email,@QueryParam("status") String status,@QueryParam("designation") String designation) {
	// FIXME - Dummy code
	   BasicDBObject ob = new BasicDBObject();
	   if(firstName != null)
   	ob.append("firstName", firstName);
	   if(lastName != null)
   	ob.append("lastName", lastName);
	   if(password != null)
   	ob.append("password", password);
	   if(email != null)
   	ob.append("email", email);
	   if(status != null)
   	ob.append("status", status);
	   if(designation != null)
   	ob.append("designation", designation);

        	BasicDBObject query = new BasicDBObject().append("userID", userID);
        	BasicDBObject newDoc = new BasicDBObject().append("$set", ob);
        	colluser.update(query,newDoc );
        	
}
    
    @GET
    @Path("/{userID}/files/createFile")
    @Produces(MediaType.TEXT_HTML)
    @Timed(name = "Create-file")
    public Response createFilePage(@PathParam("userID") int userID) {
    	
    	DBObject user = colluser.findOne(new BasicDBObject("userID",userID));
    	Writer output = new StringWriter();

    	try {
			template = cfg.getTemplate("createFile.ftl");
			SimpleHash root = new SimpleHash();
			root.put("name", "");
			root.put("accessType", "");
			root.put("userID", userID);
			root.put("user", user);
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	return Response.status(200).entity(output.toString()).build();
    }

    
    @POST
    @Path("/{userID}/files")
    @Timed(name = "create-file")
    public Response createUserFileByUserID(@PathParam("userID") int userID, File file) {
	// FIXME - Dummy code

    	BasicDBObject query1 = new BasicDBObject();
    	BasicDBObject field = new BasicDBObject();
    	field.put("fileCount", 1);
    	DBCursor cursor = colldocument.find(query1,field);
    	int fileCount=99;
    	BasicDBObject obj = (BasicDBObject) cursor.next();
    	fileCount=obj.getInt("fileCount"); 	
 
    	file.setFileID(fileCount);
    	BasicDBObject ob = new BasicDBObject();
    	ob.append("name", file.getName());
    	ob.append("fileID", fileCount);
    	ob.append("owner", userID);
    	ob.append("accessType", file.getAccessType());
    	ob.append("sharedWith", new ArrayList<String>());
    	colldocument.insert(ob);
//    	BasicDBObject query2 = new BasicDBObject().append("userID", userID);
//    	BasicDBObject newDoc1 = new BasicDBObject().append("$push", new BasicDBObject().append("myFiles", fileCount));
//    	colluser.update(query2,newDoc1 );
    	BasicDBObject countQuery = new BasicDBObject().append("fileCount", fileCount);
    	BasicDBObject newDoc2 = new BasicDBObject();
    	newDoc2.append("$set", new BasicDBObject("fileCount",++fileCount));
    	colldocument.update(countQuery,newDoc2 );

    	DBObject user = colluser.findOne(new BasicDBObject("userID",userID));
    	
    	Writer output = new StringWriter();

    	try {
			template = cfg.getTemplate("createFile.ftl");
			SimpleHash root = new SimpleHash();
			root.put("name", file.getName());
			root.put("accessType", file.getAccessType());
			root.put("user", user);
			root.put("userID", userID);
			
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	LinkDto link = new LinkDto("view-file", "/users/" + userID + "/files/ " + file.getFileID(),"GET");
    	return Response.status(201).entity(link).build();
    }
      
    @GET
    @Path("/{userID}/files")
    @Produces(MediaType.TEXT_HTML)
    @Timed(name = "Get-myfiles")
    public Response getMyFilesByUserID(@PathParam("userID") int userID) {
    	BasicDBObject query = new BasicDBObject().append("owner",userID);
    	BasicDBObject fields = new BasicDBObject();

    	DBCursor cursor = colldocument.find(query, fields);
    	List<DBObject> files = cursor.toArray();
    	int owner =0;
    	for (int i=0; i<files.size();i++){
    		ArrayList<Integer> sharedFiles = (ArrayList<Integer>) files.get(i).get("sharedWith");
    		List<String> names = new ArrayList<String>();
    		for (int y=0; y<sharedFiles.size();y++){
    			owner = sharedFiles.get(y);
    			BasicDBObject temp = (BasicDBObject)colluser.findOne(new BasicDBObject("userID",owner));
        		String foo = temp.getString("lastName");
        		names.add(foo);
    		}
    		((BasicDBObject)(files.get(i))).append("sharedWithNames", names);
    	}
    	Writer output = new StringWriter();

    	try {
			template = cfg.getTemplate("file_template.ftl");
			SimpleHash root = new SimpleHash();
			root.put("userID", userID);
			root.put("files", files);
			root.put("userFile", true);
			template.process(root, output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	return Response.status(200).entity(output.toString()).build();
    }
   
    @PUT
	@Path("/{userID}/files/{id}")
	@Timed(name = "update-files")
	public Response updateFileByEmail(@PathParam("userID") int userID, @PathParam("id") int id,
			@QueryParam("sharedWith") String sharedWithID) {
		
		boolean result = checkOwnerOfFile(userID,id);
		int responseCode=200;
		if(result){
		BasicDBObject query = new BasicDBObject("email",sharedWithID);
		BasicDBObject person = (BasicDBObject)colluser.findOne(query);
		int personID = person.getInt("userID");
		
		
		BasicDBObject query2 = new BasicDBObject("fileID", id);
		BasicDBObject newDoc2 = new BasicDBObject().append("$push",new BasicDBObject("sharedWith", personID));
		colldocument.update(query2, newDoc2);
		}
	else{
		System.out.println("User does not have permission to share the file");
		responseCode = 401;
		
	}
		return Response.status(responseCode).entity(true).build();
}
	
    @GET
       @Path("/{userID}/files/{id}")
       @Timed(name = "view-file")
       public Response getMyFileByUserIdById(@PathParam("userID") int userID, @PathParam("id") int id) {
    	String output ="";
    	if(checkOwnerOfFile(userID,id)){
       		BasicDBObject query = new BasicDBObject("fileID",id);
       		DBCursor cursor = colldocument.find(query);
       		while (cursor.hasNext()) {
       			output +=cursor.next();
       		}
       	}
       	return Response.status(200).entity(output).build();
}

    
    @DELETE
    @Path("/{userID}/files/{id}")
    @Timed(name = "delete-file")
    public Response deleteMyFileByUserIdAndId(@PathParam("userID") int userID, @PathParam("id") Integer id) {

    	if(checkOwnerOfFile(userID,id)){    	
    		System.out.println("userId has permission to delete the file");
    		colldocument.remove(new BasicDBObject().append("fileID", id));    	
    	}
    	else{ 
    		System.out.println("userId does not have permission to delete the file");
    	}
//    	return new LinkDto("create-file", "/users/" + userID,"POST");
    	return Response.status(200).entity(new LinkDto("create-file", "/users/" + userID,"POST")).build();
    } 

    @GET
    @Path("/{userID}/filesShared/{id}")
    @Timed(name = "view-filesShared")
    public Response getFilesSharedByEmailById(@PathParam("userID") int userID, @PathParam("id") int id) {

    	BasicDBObject andQuery = new BasicDBObject();
    	List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
    	obj.add(new BasicDBObject("fileID", id));
    	obj.add(new BasicDBObject("sharedWith", userID));
    	andQuery.put("$and", obj);
    	DBCursor cursor = colldocument.find(andQuery);
    	String output = "";
    	while(cursor.hasNext()) {
    	    output +=cursor.next();
    	}
    	    	
    	return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/{userID}/filesShared")
    @Produces(MediaType.TEXT_HTML)
    @Timed(name = "Get-filesshared")
    public Response getSharedFilesByUserID(@PathParam("userID") int userID) {
    	BasicDBObject query = new BasicDBObject().append("sharedWith",userID);
    	BasicDBObject fields = new BasicDBObject();
    	DBCursor cursor = colldocument.find(query, fields);
    	List<DBObject> files = cursor.toArray();
    	
    	Writer output = new StringWriter();

    	try {
			template = cfg.getTemplate("file_template.ftl");
			SimpleHash root = new SimpleHash();
			root.put("userID", userID);
			root.put("files", files);
			root.put("userFile", false);
			root.put("owners", null);
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	return Response.status(200).entity(output.toString()).build();
    }
    
    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(DropboxResource.class, "/freemarker");
        return retVal;
    }
  
}
