package edu.sjsu.cmpe.backpack.domain;

import java.util.ArrayList;

public class User {
    private int userID;
	private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private String email;
    private String status;
    private String designation;
    private ArrayList<UserFile> myFiles=new ArrayList<UserFile>();
    private ArrayList<UserFile> filesShared=new ArrayList<UserFile>();
    // add more fields here

    /**
     * @return the isbn
     */
    public User()
    {
    	setPassword("user123");
    	setStatus("Activated");
    }

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<UserFile> getmyFiles() {
		return myFiles;
	}

	public void setmyFiles(ArrayList<UserFile> myFiles) {
		this.myFiles = myFiles;
	}

	public ArrayList<UserFile> getFilesShared() {
		return filesShared;
	}

	public void setFilesShared(ArrayList<UserFile> filesShared) {
		this.filesShared = filesShared;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
