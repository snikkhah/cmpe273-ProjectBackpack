package edu.sjsu.cmpe.dropbox.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.dropbox.domain.File;

@JsonPropertyOrder(alphabetic = true)
public class AuthorDto extends LinksDto {

    private File file;

    /**
     * @param book
     */
    public AuthorDto(File file) {
	super();
	this.setAuthor(file);
    }

	public File getAuthor() {
		return file;
	}

	public void setAuthor(File file) {
		this.file = file;
	}



}
