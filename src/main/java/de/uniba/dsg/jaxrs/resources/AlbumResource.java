package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.browse.GetListOfNewReleasesRequest;

import de.uniba.dsg.CustomSpotifyApi;
import de.uniba.dsg.interfaces.AlbumApi;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Release;

@Path("/albums/new-releases")
public class AlbumResource implements AlbumApi{

	@Override
	@GET
	public List<Release> getNewReleases(@QueryParam("country") String country, @QueryParam("size") int size) {
		GetListOfNewReleasesRequest releaseRequest;
		if(size > 0) {
			releaseRequest = CustomSpotifyApi.getInstance().getListOfNewReleases()
					.country(CountryCode.US).limit(size).build();
		}else {
			releaseRequest = CustomSpotifyApi.getInstance().getListOfNewReleases()
					.country(CountryCode.US).limit(5).build();
		}
		
		try {
			Paging<AlbumSimplified> albumResult = releaseRequest.execute();
			AlbumSimplified[] albumArray = albumResult.getItems();
			Release[] releases = new Release[albumArray.length];
			String name, artist_str;
		
			for (int i = 0; i< albumArray.length; i++) {
				artist_str = "";
				name = albumArray[i].getName();
				ArtistSimplified[] artists = albumArray[i].getArtists();
				for(int j = 0; j < artists.length; j++) {
					if (j == artists.length - 1) {
						artist_str += artists[j].getName();
					}else {
						artist_str += artists[j].getName() + ", ";
					}
				}
				releases[i] = new Release(name, artist_str);
			}
			return Arrays.asList(releases);
		}catch(SpotifyWebApiException | IOException e) {
			throw new RemoteApiException(new ErrorMessage(e.getMessage()));
		}
	}
}
