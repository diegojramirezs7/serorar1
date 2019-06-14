package de.uniba.dsg.jaxrs.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.uniba.dsg.interfaces.PlaylistApi;
import de.uniba.dsg.models.PlaylistRequest;
import de.uniba.dsg.models.Song;

@Path("/playlists")
public class PlaylistResource implements PlaylistApi{

	@Override
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPlaylist(PlaylistRequest request) {
		List<String> seeds = request.getArtistSeeds();
		ArtistResource aResource = new ArtistResource();
		List<Song> songs = new ArrayList<Song>();
		Song song;
		
		//if there are more seeds than required number of songs, just loop until required number of seeds
		if (seeds.size() >= request.getNumberOfSongs()) {
			for (int i =0; i < request.getNumberOfSongs(); i++) {
				song = aResource.getTopTracks(seeds.get(i)).get(0);
				songs.add(song);
			}
		}else {
			//if there are fewer seeds than required number of songs, loop through available seeds
			//then for the remaining spots, get a random seed, from that seed get a random similar artist
			//from that random similar artist get the first of their top tracks
			for (int i =0; i < seeds.size(); i++) {
				song = aResource.getTopTracks(seeds.get(i)).get(0);
				songs.add(song);
			}
			Random gen = new Random();
			int pos;
			String aId;
			for (int j = seeds.size(); j < request.getNumberOfSongs(); j++) {
				pos = gen.nextInt(seeds.size());
				aId = aResource.getSimilarArtist(seeds.get(pos)).getId();
				song = aResource.getTopTracks(aId).get(0);
				songs.add(song);
			}
		}
		return Response.status(201).entity(songs).build();
	}

}
