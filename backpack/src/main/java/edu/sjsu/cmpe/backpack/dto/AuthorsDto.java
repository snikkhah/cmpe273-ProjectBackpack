package edu.sjsu.cmpe.backpack.dto;

import java.util.ArrayList;

import edu.sjsu.cmpe.backpack.domain.UserFile;

public class AuthorsDto extends LinksDto{
	ArrayList<UserFile> UserFiles = new ArrayList<UserFile>();
	public AuthorsDto(ArrayList<UserFile> UserFiles) {
		// TODO Auto-generated constructor stub
		super();
		this.setAuthors(UserFiles);
	}
	
	public ArrayList<UserFile> getAuthors() {
		return UserFiles;
	}
	public void setAuthors(ArrayList<UserFile> UserFiles) {
		this.UserFiles = UserFiles;
	}

}
