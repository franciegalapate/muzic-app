package com.muzic.backend.service;

import com.muzic.backend.dto.TrackDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyService {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    private SpotifyApi buildAuthenticatedClient() throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        ClientCredentialsRequest request = spotifyApi.clientCredentials().build();
        ClientCredentials credentials = request.execute();
        spotifyApi.setAccessToken(credentials.getAccessToken());

        return spotifyApi;
    }

    public List<TrackDTO> searchTracks(List<String> keywords, List<String> genres, int limit) {
        List<TrackDTO> tracks = new ArrayList<>();

        try {
            SpotifyApi spotifyApi = buildAuthenticatedClient();

            for (String keyword : keywords) {
                if (tracks.size() >= limit) break;

                String query = keyword + " genre:" + String.join(" ", genres);
                SearchTracksRequest searchRequest = spotifyApi.searchTracks(query)
                        .limit(Math.min(5, limit - tracks.size()))
                        .build();

                Track[] results = searchRequest.execute().getItems();

                for (Track track : results) {
                    if (tracks.size() >= limit) break;
                    TrackDTO dto = new TrackDTO();
                    dto.setId(track.getId());
                    dto.setName(track.getName());
                    dto.setArtist(track.getArtists()[0].getName());
                    dto.setAlbumName(track.getAlbum().getName());
                    dto.setSpotifyUrl(track.getExternalUrls().get("spotify"));
                    if (track.getPreviewUrl() != null) {
                        dto.setPreviewUrl(track.getPreviewUrl());
                    }
                    if (track.getAlbum().getImages().length > 0) {
                        dto.setAlbumImageUrl(track.getAlbum().getImages()[0].getUrl());
                    }
                    tracks.add(dto);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch tracks from Spotify", e);
        }

        return tracks;
    }
}