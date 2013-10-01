package edu.sjsu.cmpe.dropbox.dto;

import java.util.ArrayList;

import edu.sjsu.cmpe.dropbox.domain.File;

public class AuthorsDto extends LinksDto{
	ArrayList<File> files = new ArrayList<File>();
	public AuthorsDto(ArrayList<File> files) {
		// TODO Auto-generated constructor stub
		super();
		this.setAuthors(files);
	}
	
	public ArrayList<File> getAuthors() {
		return files;
	}
	public void setAuthors(ArrayList<File> files) {
		this.files = files;
	}

}
