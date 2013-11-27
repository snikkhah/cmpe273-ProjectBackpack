package edu.sjsu.cmpe.backpack.dto;

import java.util.ArrayList;

import edu.sjsu.cmpe.backpack.domain.UserFile;

public class FilesDto extends LinksDto {

    private ArrayList<UserFile> UserFiles = new ArrayList<UserFile>();

    /**
     * @param book
     */
    public FilesDto(ArrayList<UserFile> UserFiles) {
	super();
	this.setFiles(UserFiles);
    }

	public ArrayList<UserFile> getFiles() {
		return UserFiles;
	}

	public void setFiles(ArrayList<UserFile> UserFiles) {
		this.UserFiles = UserFiles;
	}

}
