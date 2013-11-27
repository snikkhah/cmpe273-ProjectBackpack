package edu.sjsu.cmpe.backpack.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.backpack.domain.User;

@JsonPropertyOrder(alphabetic = true)
public class UserDto extends LinksDto {
    private User user;

    /**
     * @param user
     */
    public UserDto(User user) {
	super();
	this.user = user;
    }

    /**
     * @return the user
     */
    public User getBook() {
	return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setBook(User user) {
	this.user = user;
    }
}
