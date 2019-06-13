package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistsRelatedArtistsRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistsTopTracksRequest;

import de.uniba.dsg.CustomSpotifyApi;
import de.uniba.dsg.interfaces.ArtistApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;

@Path("/artists/{artist-id}/")
public class ArtistResource implements ArtistApi{

	@Override
	@GET
	public Interpret getArtist(@PathParam("artist-id") String artistId) {
		if (artistId == null || artistId == "") {
			throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist id"));
		}
		GetArtistRequest artistRequest = CustomSpotifyApi.getInstance().getArtist(artistId).build();
		try {
			Artist artist = artistRequest.execute();
			if (artist == null) {
				throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching artist found for query: %s", artistId)));
			}
			Interpret result = new Interpret();
			result.setId(artist.getId());
			result.setName(artist.getName());
			result.setPopularity(artist.getPopularity());
			result.setGenres(Arrays.asList(artist.getGenres()));
			
			return result;
		}catch(SpotifyWebApiException | IOException e) {
			throw new RemoteApiException(new ErrorMessage(e.getMessage()));
		}
	}

	@Override
	@GET
	@Path("/top-tracks")
	public List<Song> getTopTracks(@PathParam("artist-id") String artistId) {
		if (artistId == null) {
			throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist id"));
		}
		GetArtistsTopTracksRequest topTracksRequest = CustomSpotifyApi.getInstance().getArtistsTopTracks(artistId, CountryCode.DE).build();
		try {
			//getArtistsTopTracks will return an array of tracks
			//to build a list of songs we need the desired fields of each track to build the respective song
			Track[] queryResult = topTracksRequest.execute();
			Song[] songs = new Song[5];
			
			String name, artist_str;
			double duration;
			
			for(int i =0; i<songs.length; i++) {
				artist_str = "";
				name = queryResult[i].getName();
				ArtistSimplified[] artists = queryResult[i].getArtists();
				//getArtists returns an array of ArtistSimplified, 
				//this loop gets the name of each artist in the array
				for(int j = 0; j < artists.length; j++) {
					if (j == artists.length - 1) {
						artist_str += artists[j].getName();
					}else {
						artist_str += artists[j].getName() + ", ";
					}
				}
				duration = queryResult[i].getDurationMs() / 1000;
				songs[i] = new Song(name, artist_str, duration);
			}
			return Arrays.asList(songs);
		}catch(SpotifyWebApiException | IOException e) {
			throw new RemoteApiException(new ErrorMessage(e.getMessage()));
		}
	}

	@Override
	@GET
	@Path("/similar-artist")
	public Interpret getSimilarArtist(@PathParam("artist-id") String artistId) {
		if (artistId == null) {
			throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist id"));
		}
		GetArtistsRelatedArtistsRequest similarRequest = CustomSpotifyApi.getInstance().getArtistsRelatedArtists(artistId).build();
		try {
			//getArtistsTopTracks will return an array of tracks
			//to build a list of songs we need the desired fields of each track to build the respective song
			Artist[] artistArray = similarRequest.execute();
			//get random integer between 0 and length of array. So each time suggested artist is different
			Random gen = new Random();
			int choice = gen.nextInt(artistArray.length - 1);
			Interpret result = new Interpret();
			Artist art = artistArray[choice];
			result.setId(art.getId());
			result.setName(art.getName());
			result.setPopularity(art.getPopularity());
			result.setGenres(Arrays.asList(art.getGenres()));
			return result;
		}catch(SpotifyWebApiException | IOException e) {
			throw new RemoteApiException(new ErrorMessage(e.getMessage()));
		}
	}
}
