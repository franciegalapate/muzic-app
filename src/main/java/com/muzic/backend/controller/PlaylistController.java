package com.muzic.backend.controller;

import com.muzic.backend.dto.JournalEntryRequest;
import com.muzic.backend.dto.PlaylistResponse;
import com.muzic.backend.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playlist")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping("/generate")
    public ResponseEntity<PlaylistResponse> generatePlaylist(@RequestBody JournalEntryRequest request) {
        PlaylistResponse response = playlistService.generatePlaylist(request.getContent());
        return ResponseEntity.ok(response);
    }
}