package de.uniba.dsg.jaxrs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.uniba.dsg.jaxrs.resources.AlbumResource;
import de.uniba.dsg.jaxrs.resources.ArtistResource;
import de.uniba.dsg.jaxrs.resources.PlaylistResource;
import de.uniba.dsg.jaxrs.resources.SearchResource;

@ApplicationPath("/")
/**
 * TODO:
 * The API should always consume JSON
 * The API should always respond with JSON
 */

//javaFX apps extend application class -- javaFX is library used for internet apps
public class MusicApi extends Application {
	//builds and returns a set of resources from resources folder
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(SearchResource.class);
        resources.add(ArtistResource.class);
        resources.add(AlbumResource.class);
        resources.add(PlaylistResource.class);
        return resources;
    }
}
