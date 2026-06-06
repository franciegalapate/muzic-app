package com.muzic.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.muzic.backend.dto.PlaylistResponse;
import com.muzic.backend.dto.TrackDTO;
import com.muzic.backend.model.JournalEntry;
import com.muzic.backend.model.Playlist;
import com.muzic.backend.repository.JournalEntryRepository;
import com.muzic.backend.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final AnthropicService anthropicService;
    private final SpotifyService spotifyService;
    private final JournalEntryRepository journalEntryRepository;
    private final PlaylistRepository playlistRepository;

    public PlaylistResponse generatePlaylist(String journalContent) {

        // Step 1: Analyze mood with Claude
        JsonNode moodAnalysis = anthropicService.analyzeMood(journalContent);

        // Step 2: Save journal entry
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setContent(journalContent);
        journalEntry.setMood(moodAnalysis.get("mood").asText());
        journalEntry.setEnergyLevel(moodAnalysis.get("energy_level").asText());
        journalEntryRepository.save(journalEntry);

        // Step 3: Extract Spotify search params from Claude's response
        List<String> keywords = new ArrayList<>();
        moodAnalysis.get("spotify_seed_keywords").forEach(k -> keywords.add(k.asText()));

        List<String> genres = new ArrayList<>();
        moodAnalysis.get("genres").forEach(g -> genres.add(g.asText()));

        // Step 4: Fetch 20 tracks from Spotify
        List<TrackDTO> tracks = spotifyService.searchTracks(keywords, genres, 20);

        // Step 5: Save playlist
        Playlist playlist = new Playlist();
        playlist.setJournalEntry(journalEntry);
        playlist.setName(moodAnalysis.get("playlist_name").asText());
        playlist.setDescription(moodAnalysis.get("playlist_description").asText());
        playlistRepository.save(playlist);

        // Step 6: Build and return response
        PlaylistResponse response = new PlaylistResponse();
        response.setJournalEntryId(journalEntry.getId());
        response.setMood(moodAnalysis.get("mood").asText());
        response.setEnergyLevel(moodAnalysis.get("energy_level").asText());
        response.setPlaylistName(moodAnalysis.get("playlist_name").asText());
        response.setPlaylistDescription(moodAnalysis.get("playlist_description").asText());
        response.setTracks(tracks);

        return response;
    }
}