package de.uniba.dsg;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Logger;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

public class CustomSpotifyApi {
    private static final Logger LOGGER = Logger.getLogger(CustomSpotifyApi.class.getName());

    private static SpotifyApi api;
    private static Properties properties = new Properties();
    private static volatile LocalDateTime expirationTime;
    
    
    //only static fields and methods
    private static final Object lock = new Object();

    //creates the properties object from info in config.properties
    //instantiates api object using properties object
    //all this is done when class is loaded, 
    //so api and properties object will be available even if class is not instantiated
    static {
        try (InputStream stream = CustomSpotifyApi.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(stream);
            api = new SpotifyApi.Builder()
                    .setClientId(properties.getProperty("spotifyClientId"))
                    .setClientSecret(properties.getProperty("spotifyClientSecret"))
                    .build();
        } catch (IOException e) {
            LOGGER.severe("Cannot load configuration file.");
        }
    }

    private CustomSpotifyApi() {}

    //creates a lock until is done executing, making it thread-safe
    //calls signin and returns api already with access token
    public static SpotifyApi getInstance() {
        synchronized (lock) {
            try {
                signIn();
            } catch (SpotifyWebApiException | IOException e) {
                LOGGER.severe("Failed to authenticate with Spotify API.");
            }
            return api;
        }
    }

    //requests credentials from already built api, sets access token on api and sets expirationTime
    private static void signIn() throws SpotifyWebApiException, IOException {
        if (expirationTime == null || expirationTime.isBefore(LocalDateTime.now())) {
            ClientCredentialsRequest credentialsRequest = api.clientCredentials().build();
            ClientCredentials credentials = credentialsRequest.execute();
            // expiration is in seconds
            expirationTime = LocalDateTime.now().plusSeconds(credentials.getExpiresIn() - 5);
            api.setAccessToken(credentials.getAccessToken());
        }
    }
}
