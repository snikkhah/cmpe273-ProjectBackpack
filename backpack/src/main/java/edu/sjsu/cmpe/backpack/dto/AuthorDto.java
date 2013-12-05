package edu.sjsu.cmpe.backpack.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.backpack.domain.UserFile;

@JsonPropertyOrder(alphabetic = true)
public class AuthorDto extends LinksDto {

    private UserFile UserFile;

    /**
     * @param book
     */
    public AuthorDto(UserFile UserFile) {
	super();
	this.setAuthor(UserFile);
    }

	public UserFile getAuthor() {
		return UserFile;
	}

	public void setAuthor(UserFile UserFile) {
		this.UserFile = UserFile;
	}



}
