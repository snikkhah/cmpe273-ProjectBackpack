package edu.sjsu.cmpe.backpack.domain;
 	
import java.util.ArrayList;

public class UserFile {
	private int fileID;
	private String name;
	private String owner;
	private String accessType;
	private ArrayList<String> sharedWith= new ArrayList<String>();
	private boolean update;

	public UserFile()
	{
		setAccessType("private");
		setUpdate(false);
	}
	public int getFileID() {
		return fileID;
	}
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public ArrayList<String> getSharedWith() {
		return sharedWith;
	}
	public void setSharedWith(ArrayList<String> sharedWith) {
		this.sharedWith = sharedWith;
	}
	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}
	
}
