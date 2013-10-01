package edu.sjsu.cmpe.dropbox.dto;

import java.util.ArrayList;

import edu.sjsu.cmpe.dropbox.domain.File;

public class FilesDto extends LinksDto {

    private ArrayList<File> Files = new ArrayList<File>();

    /**
     * @param book
     */
    public FilesDto(ArrayList<File> Files) {
	super();
	this.setFiles(Files);
    }

	public ArrayList<File> getFiles() {
		return Files;
	}

	public void setFiles(ArrayList<File> Files) {
		this.Files = Files;
	}

}
