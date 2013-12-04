package edu.sjsu.cmpe.backpack.api.resources;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.backpack.domain.User;
import edu.sjsu.cmpe.backpack.domain.SessionDAO;
import edu.sjsu.cmpe.backpack.dto.LinkDto;
import edu.sjsu.cmpe.backpack.dto.LinksDto;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateModelException;


	@Path("/v1/users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public class BackpackResource {
		private MongoClient mongoClient;
		private DB db;
		private DBCollection colluser,colldocument;
		private Configuration cfg;
		private Template template;
		private String dirRootPath;
		private SessionDAO sessionDAO;
	    public BackpackResource() {
	    	try {
				mongoClient = new MongoClient( "localhost" , 27017 );
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	db = mongoClient.getDB( "backPack" );
	    	colluser = db.getCollection("users");
	    	colldocument = db.getCollection("files");
	    	sessionDAO = new SessionDAO(db);
	    	cfg = createFreemarkerConfiguration();
	    	String os = System.getProperty("os.name");
	    	if (os.toLowerCase().contains("windows")){
	    		dirRootPath = "c:/backpack"; 
	    	}
	    	else if (os.toLowerCase().contains("linux")){
	    		dirRootPath = "./backpack";
	    	}
	    }

 public boolean checkOwnerOfFile(int userId,int fileId){
	    	
	    	DBObject ownerID = null;
	    	BasicDBObject query = new BasicDBObject("fileID",fileId);
	    	BasicDBObject result = (BasicDBObject) colldocument.findOne(query);
	    	if (result.getString("accessType").toLowerCase().equals("public")){
	    		return true;
	    	}
	    	query.append("owner",userId);
	    	BasicDBObject fields = new BasicDBObject();    	
	    	DBCursor cursor = colldocument.find(query, fields);
			while (cursor.hasNext()) {
				ownerID = cursor.next();
//				System.out.println("ownerId" + ownerID.get("owner"));
			}
		if (ownerID == null){
//			System.out.println("OwnerId and userId doesn't match");
			return false;
		}
		else {
//			System.out.println("OwnerId and userId match");
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
	    		System.out.println("UserFile cannot be shared with user");
	    		return false;
	    	}
	    	else{
	    		System.out.println("UserFile can be shared with the user as user exists in sharedWith");
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
        		"/users/" + user.getEmail() +"/UserFiles", "POST"));

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
    public Response login(User user,@Context HttpServletRequest req) {
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
			else {
				HttpSession session= req.getSession(true);
		    	Object foo = session.getAttribute("session");
//		    	if (foo!=null) {
//		    		System.out.println(foo.toString());
//		    	} else {
		    		String sessionID = sessionDAO.startSession(user.getEmail());
		            System.out.println("Session ID is\t" + sessionID);
		    		session.setAttribute("session", sessionID);
//		    	}
			}
//			root.put("password_error", error);
			template.process(root, output);
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	Session Check
    	
    	return Response.status(status).entity(userID).build();
    }
    @ GET
    @Path("/{userID}")
    @Produces(MediaType.TEXT_HTML)
    @Timed(name = "view-user")
    public Response getUserByUID(@PathParam("userID") int userID,@Context HttpServletRequest req) {
		checkSession(userID, req);
		
    	BasicDBObject user = (BasicDBObject) colluser.findOne(new BasicDBObject().append("userID", userID ));        
    	Writer output = new StringWriter();

    	try {
			template = cfg.getTemplate("user_template.ftl");
			SimpleHash root = new SimpleHash();
			root.put("user", user);
			root.put("name", "");
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
    public Response deleteUserByEmail(@PathParam("userID") int userID, @Context HttpServletRequest req) {
    	checkSession(userID, req);
    	BasicDBObject user = new BasicDBObject();
    	user.put("userID", userID);
    	colldocument.remove(new BasicDBObject("owner",userID));
    	
    	colluser.remove(user);
    	return Response.status(201).entity(new LinkDto("create-user", "/users","POST")).build();
    }

    @PUT
    @Path("/{userID}")
    @Timed(name = "update-userdata")
    public void updateUserdataByUserID(@Context HttpServletRequest req,@PathParam("userID") int userID,@QueryParam("firstName") String firstName,@QueryParam("lastName") String lastName,@QueryParam("password") String password,@QueryParam("email") String email,@QueryParam("status") String status,@QueryParam("designation") String designation) {
	// FIXME - Dummy code
    	checkSession(userID, req);
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
    public Response createFilePage(@Context HttpServletRequest req,@PathParam("userID") int userID) {
    	checkSession(userID, req);
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
    @Consumes({MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Timed(name = "create-file")
    public Response createUserFileByUserID(@Context HttpServletRequest req,@PathParam("userID") int userID, /*@FormParam("accessType") String ss,*/@FormDataParam("file") InputStream inputStream,
            @FormDataParam("file") FormDataContentDisposition contentDisposition) throws IOException {
	// FIXME - Dummy code
    	checkSession(userID, req);
    	int response = 201;
    	if (contentDisposition.getFileName().equals("")){
    		response = 406;
    		return Response.status(response).build();
    	}
    		
    	String dirPath = null;
    	String filePath = null;
    	
    	dirPath = dirRootPath+"/"+userID;
    	filePath = dirPath+"/"+contentDisposition.getFileName();
    	System.out.println(filePath);

    	File rootCheck = new File(dirRootPath);
    	if (!rootCheck.exists()) {
		    System.out.println("creating directory: " + dirRootPath);
		    boolean result = rootCheck.mkdir();  

		     if(result) {    
		       System.out.println("DIR "+dirRootPath+" created");  
		     }
		  }

    	File dirCheck = new File(dirPath);
    	if (!dirCheck.exists()) {
		    System.out.println("creating directory: " + dirPath);
		    boolean result = dirCheck.mkdir();  

		     if(result) {    
		       System.out.println("DIR "+ dirPath+" created");  
		     }
		  }
    	
    	File newFile = new File(filePath);
		  if(!newFile.exists()) {
			  	newFile.createNewFile();
			  	OutputStream out = new FileOutputStream(newFile);
		        int read = 0;
		        byte[] bytes = new byte[1024]; 
		        while ((read = inputStream.read(bytes)) != -1)
		        {
		            out.write(bytes, 0, read);
		        } 
		        out.flush();
		        out.close();
		  }
		  else {
			  response=409;
		  }
    	BasicDBObject query1 = new BasicDBObject();
    	BasicDBObject field = new BasicDBObject();
    	field.put("fileCount", 1);
    	DBCursor cursor = colldocument.find(query1,field);
    	int fileCount=99;
    	BasicDBObject obj = (BasicDBObject) cursor.next();
    	fileCount=obj.getInt("fileCount"); 	
 
//    	newFile.setFileID(fileCount);
    	BasicDBObject ob = new BasicDBObject();
    	ob.append("name", contentDisposition.getFileName());
    	ob.append("fileID", fileCount);
    	ob.append("owner", userID);
    	ob.append("accessType", "Private");
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
//			root.put("name", newFile.getName());
//			root.put("accessType", newFile.getAccessType());
			root.put("user", user);
			root.put("userID", userID);
			
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	LinkDto link = new LinkDto("view-file", "/users/" + userID + "/UserFiles/ " + fileCount,"GET");
    	return Response.status(response).entity(link).build();
    }
      
    @GET
    @Path("/{userID}/files")
    @Produces(MediaType.TEXT_HTML)
    @Timed(name = "Get-myfiles")
    public Response getMyFilesByUserID(@Context HttpServletRequest req,@PathParam("userID") int userID) {
    	checkSession(userID, req);
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
			root.put("publicFile", false);
			template.process(root, output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	return Response.status(200).entity(output.toString()).build();
    }
   
    @PUT
	@Path("/{userID}/files/{id}")
	@Timed(name = "update-UserFiles")
	public Response updateFileByEmail(@Context HttpServletRequest req,@PathParam("userID") int userID, @PathParam("id") int fileID,
			@QueryParam("sharedWith") String sharedWithID,@QueryParam("accessType") String accessType) {
    	checkSession(userID, req);
    	int responseCode=200;
		boolean result = checkOwnerOfFile(userID,fileID);
		if(result){
			if (sharedWithID != null){
		BasicDBObject query = new BasicDBObject("email",sharedWithID);
		BasicDBObject person = (BasicDBObject)colluser.findOne(query);
		if (person != null){
			int personID = person.getInt("userID");
			
			
			BasicDBObject query2 = new BasicDBObject("fileID", fileID);
			BasicDBObject newDoc2 = new BasicDBObject().append("$push",new BasicDBObject("sharedWith", personID));
			colldocument.update(query2, newDoc2);
			
			BasicDBObject file = (BasicDBObject) colldocument.findOne(query2);
			BasicDBObject owner = (BasicDBObject) colluser.findOne(new BasicDBObject("userID",userID));
			String fileName = file.getString("name");
			String recieverFullName = person.getString("firstName")+" "+person.getString("lastName");
			String ownerFullName = owner.getString("firstName")+" "+owner.getString("lastName");
			String link = "http://localhost:8000/backpack/v1/users/"+personID+"/filesShared/"+fileID;
			sendEmail(sharedWithID, fileName,ownerFullName, recieverFullName, link);
//			String recieverEmail, String fileName, String owner, String reciever, String link
		}
		else {responseCode = 406;}
		}
		else if (accessType != null){
			BasicDBObject query = new BasicDBObject("fileID",fileID);
			colldocument.update(query, new BasicDBObject("$set",new BasicDBObject("accessType",accessType)));
		}
		}
	else{
		System.out.println("User does not have permission to share the file");
		responseCode = 401;		
	}
		
		return Response.status(responseCode).entity(true).build();
}
	
    @GET
       @Path("/{userID}/files/{id}")
       @Produces("text/plain")
       @Timed(name = "view-file")
       public Response getMyFileByUserIdById(@Context HttpServletRequest req,@PathParam("userID") int userID, @PathParam("id") int id) {
    	checkSession(userID, req);
    	BasicDBObject result=null;
    	if(checkOwnerOfFile(userID,id)){
       		BasicDBObject query = new BasicDBObject("fileID",id);
       		result = (BasicDBObject) colldocument.findOne(query);
           	String path = dirRootPath+"/"+userID+"/"+ result.getString("name");
           	File file = new File(path);
    		ResponseBuilder response = Response.ok((Object) file);
    		response.header("Content-Disposition",
    			"attachment; filename=\""+result.getString("name")+"\"");
    		return response.build();
       	}
    	else 
    		
    		return Response.status(400).entity(false).build();

}

    
    @DELETE
    @Path("/{userID}/files/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(name = "delete-file")
    public Response deleteMyFileByUserIdAndId(@Context HttpServletRequest req,@PathParam("userID") int userID, @PathParam("id") Integer id) {
    	checkSession(userID, req);
    	int response = 200;
    	if(checkOwnerOfFile(userID,id)){    	
//    		System.out.println("userId has permission to delete the file");
    		BasicDBObject query = new BasicDBObject().append("fileID", id); 
    		BasicDBObject result = (BasicDBObject) colldocument.findOne(query);
    		String filePath = dirRootPath+"/"+userID+"/"+result.getString("name");
    		File mywFile = new File(filePath);
  		  if(mywFile.exists()) {
  			mywFile.delete();
  			colldocument.remove(query);
  		  }
  		  else response = 410;
    	}
    	else{ 
//    		System.out.println("userId does not have permission to delete the file");
    		response = 401;
    	}
//    	return new LinkDto("create-file", "/users/" + userID,"POST");
    	return Response.status(response).entity(new LinkDto("create-file", "/users/" + userID,"POST")).build();
    } 

    @GET
    @Path("/{userID}/publicfiles/{id}")
    @Produces("text/plain")
    @Timed(name = "view-filesShared")
    public Response getPublicFilesByEmailById(@Context HttpServletRequest req,@PathParam("userID") int userID, @PathParam("id") int fileID) {
    	checkSession(userID, req);
    	BasicDBObject andQuery = new BasicDBObject();
    	List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
    	obj.add(new BasicDBObject("fileID", fileID));
    	obj.add(new BasicDBObject("accessType", "Public"));
    	andQuery.put("$and", obj);
    	BasicDBObject result= (BasicDBObject) colldocument.findOne(andQuery);
   	   	String path = dirRootPath+"/"+result.getString("owner")+"/"+ result.getString("name");
       	File file = new File(path);
        
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition",
    			"attachment; filename=\""+result.getString("name")+"\"");
		return response.build();
    
    }
    
    @GET
    @Path("/{userID}/filesShared/{id}")
    @Produces("text/plain")
    @Timed(name = "view-filesShared")
    public Response getFilesSharedByEmailById(@Context HttpServletRequest req,@PathParam("userID") int userID, @PathParam("id") int fileID) {
    	checkSession(userID, req);
    	BasicDBObject andQuery = new BasicDBObject();
    	List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
    	obj.add(new BasicDBObject("fileID", fileID));
    	obj.add(new BasicDBObject("sharedWith", userID));
    	andQuery.put("$and", obj);
    	BasicDBObject result= (BasicDBObject) colldocument.findOne(andQuery);
   	   	String path = dirRootPath+"/"+result.getString("owner")+"/"+ result.getString("name");
       	File file = new File(path);
        
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition",
    			"attachment; filename=\""+result.getString("name")+"\"");
		return response.build();

//    	return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/{userID}/filesShared")
    @Produces(MediaType.TEXT_HTML)
    @Timed(name = "Get-filesshared")
    public Response getSharedFilesByUserID(@Context HttpServletRequest req, @PathParam("userID") int userID) {
    	checkSession(userID, req);
    	BasicDBObject query = new BasicDBObject().append("sharedWith",userID);
    	BasicDBObject fields = new BasicDBObject();
    	DBCursor cursor = colldocument.find(query, fields);
    	List<DBObject> files = cursor.toArray();
    	
    	for (int i=0; i<files.size(); i++){
    		int ownerID = (Integer) files.get(i).get("owner");
    		DBObject owner = colluser.findOne(new BasicDBObject("userID",ownerID));
    		String ownerName =(String) owner.get("firstName");
    		ownerName += " "+(String) owner.get("lastName");
    		((BasicDBObject)files.get(i)).append("ownerName", ownerName);
    	}
    	
    	Writer output = new StringWriter();

    	try {
			template = cfg.getTemplate("file_template.ftl");
			SimpleHash root = new SimpleHash();
			root.put("userID", userID);
			root.put("files", files);
			root.put("userFile", false);
			root.put("owners", null);
			root.put("publicFile", false);
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	return Response.status(200).entity(output.toString()).build();
    }
/*    
    @GET
    @Path("/{userID}/publicFiles")
    @Produces(MediaType.TEXT_HTML)
    @Timed(name = "Get-publicFiles")
    public Response getPublicFiles(@PathParam("userID") int userID) {
    	BasicDBObject query = new BasicDBObject("userID",userID);
    	DBObject user = colluser.findOne(query);
    	Writer output = new StringWriter();

    	try {
			template = cfg.getTemplate("publicFiles.ftl");
			SimpleHash root = new SimpleHash();
			root.put("user", user);
			root.put("name", "");
			root.put("userID", userID);
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return Response.status(200).entity(output.toString()).build();
    }
*/    
    @POST
    @Path("/{userID}/publicFiles")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(name = "Search-publicFiles")
    public Response searchPublicFiles(@Context HttpServletRequest req,@PathParam("userID") int userID, SimpleHash input) throws TemplateModelException {
    	checkSession(userID, req);
    	BasicDBObject query = new BasicDBObject("name",new BasicDBObject("$regex",input.get("name")).append("$options", 'i')).append("accessType", "Public");
    	DBCursor files = colldocument.find(query);
    	List<DBObject> filesArray = files.toArray();
    	Writer output = new StringWriter();
    	for (int i=0; i<filesArray.size(); i++){
    		int ownerID = (Integer) filesArray.get(i).get("owner");
    		DBObject owner = colluser.findOne(new BasicDBObject("userID",ownerID));
    		String ownerName =(String) owner.get("firstName");
    		ownerName += " "+(String) owner.get("lastName");
    		((BasicDBObject)filesArray.get(i)).append("ownerName", ownerName);
    	}
    	try {
			template = cfg.getTemplate("file_template.ftl");
			SimpleHash root = new SimpleHash();
			root.put("name",input.get("name"));
			root.put("userID", userID);
			root.put("files", filesArray);
			root.put("userFile", false);
			root.put("publicFile", true);
			root.put("owners", null);
			template.process(root, output);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return Response.status(200).entity(output.toString()).build();
    }
    
    @GET
    @Path("/{userID}/logout")
    @Produces("text/plain")
    @Timed(name = "view-filesShared")
    public Response logoutByEmail(@Context HttpServletRequest req,@PathParam("userID") int userID) {
    	checkSession(userID, req);
    	sessionDAO.endSession(getSession(req));
    	try {
            URI location = new URI("../backpack/v1/users/login");
            throw new WebApplicationException(Response.temporaryRedirect(location).build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    	return Response.status(200).entity(true).build();
    }
    
    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(BackpackResource.class, "/freemarker");
        return retVal;
    }
    private void sendEmail(String recieverEmail, String fileName, String owner, String reciever, String link){
    	final String username = "cmpe273s@gmail.com";
		final String password = "hlhlsissms";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("cmpe273s@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(recieverEmail));
			String msg = "Dear "+reciever+",\n\n"+owner+" has shared a file with you. Please click on the link below to download the file:\n"+link
					+"\n\nThanks for using Backpack! See you soon.\nBackpack Inc.";
			message.setSubject("New Backpack content!");
			message.setText(msg);
 
			Transport.send(message);
 
			System.out.println("Email Sent");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

    }
    
    private String getSession(HttpServletRequest request){
    
    	HttpSession session= request.getSession(true);
    	Object foo = session.getAttribute("session");
    	if (foo!=null) {
    		return foo.toString();
    	}
    	else 
    		return null;
    	
    }
    
    private void checkSession(int userID, HttpServletRequest req){
    	BasicDBObject user = (BasicDBObject) colluser.findOne(new BasicDBObject().append("userID", userID ));
    	String session = getSession(req);
    	String username = "";
    	if(session!=null)
    	username = sessionDAO.findUserNameBySessionId(session);
        if (!username.equals(user.get("email"))) {
            // looks like a bad request. user is not logged in
        	try {
        		req.removeAttribute("session");
                URI location = new URI("../backpack/v1/users/login");
                throw new WebApplicationException(Response.temporaryRedirect(location).build());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
