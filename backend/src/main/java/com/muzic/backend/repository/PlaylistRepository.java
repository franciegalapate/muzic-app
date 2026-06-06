package com.muzic.backend.repository;

import com.muzic.backend.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Playlist findByJournalEntryId(Long journalEntryId);
}