package edu.sjsu.cmpe.backpack.domain;

public class Review {
	private int id;
	private int rating;
	private String comment;
	public Review(){

	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		if (rating>0 && rating<6)
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

}
