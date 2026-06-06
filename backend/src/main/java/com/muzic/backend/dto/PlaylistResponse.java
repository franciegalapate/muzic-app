package com.muzic.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class PlaylistResponse {
    private Long journalEntryId;
    private String mood;
    private String energyLevel;
    private String playlistName;
    private String playlistDescription;
    private List<TrackDTO> tracks;
}