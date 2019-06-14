package de.uniba.dsg.models;

import java.util.List;

/**
 * TODO:
 * Playlist attributes should be
 * - title:String
 * - size:int
 * - tracks:List<Song>
 */
public class Playlist {
	String title;
	
	int size;
	List<Song> tracks;
	
	public Playlist(String title, int size, List<Song> tracks) {
		this.title = title;
		this.size = size;
		this.tracks = tracks;
	}
	
	public Playlist() {}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<Song> getTracks() {
		return tracks;
	}

	public void setTracks(List<Song> tracks) {
		this.tracks = tracks;
	}

	
}