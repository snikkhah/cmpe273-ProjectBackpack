package edu.sjsu.cmpe.backpack.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.backpack.domain.Review;
import edu.sjsu.cmpe.backpack.domain.UserFile;

@JsonPropertyOrder(alphabetic = true)
public class FileDto extends LinksDto {
    private UserFile UserFile;

    /**
     * @param book
     */
    public FileDto(UserFile UserFile) {
	super();
	this.setFile(UserFile);
    }

	public UserFile getFile() {
		return UserFile;
	}

	public void setFile(UserFile UserFile) {
		this.UserFile = UserFile;
	}

}
