package edu.sjsu.cmpe.dropbox.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.dropbox.domain.File;
import edu.sjsu.cmpe.dropbox.domain.Review;

@JsonPropertyOrder(alphabetic = true)
public class FileDto extends LinksDto {
    private File file;

    /**
     * @param book
     */
    public FileDto(File file) {
	super();
	this.setFile(file);
    }

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
