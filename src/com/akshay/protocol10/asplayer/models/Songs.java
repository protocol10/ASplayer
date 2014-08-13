package com.akshay.protocol10.asplayer.models;

import com.j256.ormlite.field.DatabaseField;

public class Songs {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String path;

	@DatabaseField
	private String title;

	@DatabaseField
	private String artist_name;

	@DatabaseField
	private String album_name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String path() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist_name;
	}

	public void setArtist(String artist_name) {
		this.artist_name = artist_name;
	}

	public String getAlbum() {
		return album_name;
	}

	public void setAlbum(String album_name) {
		this.album_name = album_name;
	}
}
