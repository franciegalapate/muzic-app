package com.muzic.backend.dto;

import lombok.Data;

@Data
public class TrackDTO {
    private String id;
    private String name;
    private String artist;
    private String albumName;
    private String albumImageUrl;
    private String previewUrl;
    private String spotifyUrl;
}