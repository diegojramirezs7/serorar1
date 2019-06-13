package de.uniba.dsg.models;

import java.util.List;

/**
 * TODO:
 * PlaylistRequest attributes should be
 * - title:String
 * - artistSeeds:List<String>, must be serialized as 'seeds'
 * - numberOfSongs:int, must be serialized as 'size'
 */
public class PlaylistRequest {
	String title;
	List<String> artistSeeds;
	int numberOfSongs;
	
	public PlaylistRequest(String title, List<String> artistSeeds, int size) {
		this.title = title;
		this.artistSeeds = artistSeeds;
		this.numberOfSongs = size;
	}
	
	public PlaylistRequest(String title, List<String> artistSeeds) {
		this.title = title;
		this.artistSeeds = artistSeeds;
		this.numberOfSongs = 10;
	}
	
	public PlaylistRequest() {
		this.title = "";
		this.artistSeeds = null;
		this.numberOfSongs = 10;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getArtistSeeds() {
		return artistSeeds;
	}
	public void setArtistSeeds(List<String> artistSeeds) {
		this.artistSeeds = artistSeeds;
	}
	public int getNumberOfSongs() {
		return numberOfSongs;
	}
	public void setNumberOfSongs(int numberOfSongs) {
		this.numberOfSongs = numberOfSongs;
	}
	
	
	
}
