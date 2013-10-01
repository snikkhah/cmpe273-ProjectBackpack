package edu.sjsu.cmpe.dropbox.domain;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String status;
    private String designation;
    private ArrayList<File> myFiles=new ArrayList<File>();
    private ArrayList<File> filesShared=new ArrayList<File>();
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

	public ArrayList<File> getmyFiles() {
		return myFiles;
	}

	public void setmyFiles(ArrayList<File> myFiles) {
		this.myFiles = myFiles;
	}

	public ArrayList<File> getFilesShared() {
		return filesShared;
	}

	public void setFilesShared(ArrayList<File> filesShared) {
		this.filesShared = filesShared;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
}
