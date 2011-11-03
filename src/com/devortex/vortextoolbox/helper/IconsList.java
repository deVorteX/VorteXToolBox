package com.devortex.vortextoolbox.helper;

public class IconsList {
	private String name;// = new ArrayList<String>();
	private String imageURL;// = new ArrayList<String>();
	private String downloadURL;// = new ArrayList<String>();

	/** In Setter method default it will return arraylist
	* change that to add */

	public String getName() {
	return name;
	}

	public void setName(String name) {
	this.name = name;
	}

	public String getDownloadURL() {
	return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
	this.downloadURL = downloadURL;
	}

	public String getImageURL() {
	return imageURL;
	}

	public void setImageURL(String imageURL) {
	this.imageURL = imageURL;
	}
}