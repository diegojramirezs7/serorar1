package de.uniba.dsg.models;

/**
 * TODO:
 * Song attributes should be
 * - title:String
 * - artist:String (possibly multiple artists concatenated with ", ")
 * - duration:double (in seconds)
 */
public class Song {
	private String title;
	private String artist;
	private double duration;
	
	public Song(String title, String artist, double duration) {
		this.title = title;
		this.artist = artist;
		this.duration = duration;
	}
	
	public Song() {}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
}