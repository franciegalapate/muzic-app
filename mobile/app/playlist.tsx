import React from "react";
import {
  View,
  Text,
  StyleSheet,
  Image,
  Linking,
  ScrollView,
  TouchableOpacity,
  StatusBar,
} from "react-native";
import { useLocalSearchParams, useRouter } from "expo-router";
import { PlaylistResponse, Track } from "../services/api";

export default function PlaylistScreen() {
  const { data } = useLocalSearchParams();
  const router = useRouter();
  const playlist: PlaylistResponse = JSON.parse(data as string);

  const moodEmoji: Record<string, string> = {
    happy: "😊",
    sad: "😢",
    anxious: "😰",
    peaceful: "😌",
    energetic: "⚡",
    melancholic: "🌧️",
    angry: "😤",
    excited: "🎉",
    tired: "😴",
    romantic: "💜",
    calm: "🍃",
  };

  const renderTrack = (item: Track, index: number) => (
    <TouchableOpacity
      key={item.id}
      style={styles.trackCard}
      onPress={() => Linking.openURL(item.spotifyUrl)}
    >
      {item.albumImageUrl ? (
        <Image source={{ uri: item.albumImageUrl }} style={styles.albumArt} />
      ) : (
        <View style={[styles.albumArt, styles.albumArtPlaceholder]} />
      )}
      <View style={styles.trackInfo}>
        <Text style={styles.trackName} numberOfLines={1}>
          {item.name}
        </Text>
        <Text style={styles.trackArtist} numberOfLines={1}>
          {item.artist} · {index + 1}
        </Text>
      </View>
      <TouchableOpacity
        style={styles.playButton}
        onPress={() => Linking.openURL(item.spotifyUrl)}
      >
        <Text style={styles.playIcon}>▷</Text>
      </TouchableOpacity>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <StatusBar barStyle="light-content" />
      <ScrollView>
        {/* Header */}
        <View style={styles.header}>
          <TouchableOpacity
            onPress={() => router.back()}
            style={styles.backButton}
          >
            <Text style={styles.backText}>← Back</Text>
          </TouchableOpacity>

          <Text style={styles.moodLabel}>
            MOOD · {playlist.mood.toUpperCase()}
          </Text>
          <Text style={styles.playlistName}>{playlist.playlistName}</Text>
          <Text style={styles.trackCount}>{playlist.tracks.length} tracks</Text>

          {/* Action Buttons */}
          <View style={styles.actionRow}>
            <TouchableOpacity style={styles.playAllButton}>
              <Text style={styles.playAllText}>▶ Play all</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.shuffleButton}>
              <Text style={styles.shuffleText}>⇄ Shuffle</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Track List */}
        <View style={styles.trackList}>
          {playlist.tracks.map((track, index) => renderTrack(track, index))}
        </View>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#0D0D1A",
  },
  header: {
    padding: 24,
    paddingTop: 64,
    background: "#1A1030",
  },
  backButton: {
    marginBottom: 24,
  },
  backText: {
    color: "#A855F7",
    fontSize: 16,
  },
  moodLabel: {
    color: "#9CA3AF",
    fontSize: 12,
    fontWeight: "600",
    letterSpacing: 2,
    marginBottom: 8,
  },
  playlistName: {
    fontSize: 32,
    fontWeight: "800",
    color: "#FFFFFF",
    marginBottom: 8,
  },
  trackCount: {
    color: "#9CA3AF",
    fontSize: 14,
    marginBottom: 24,
  },
  actionRow: {
    flexDirection: "row",
    gap: 12,
  },
  playAllButton: {
    backgroundColor: "#A855F7",
    borderRadius: 50,
    paddingVertical: 12,
    paddingHorizontal: 28,
  },
  playAllText: {
    color: "#FFFFFF",
    fontWeight: "700",
    fontSize: 15,
  },
  shuffleButton: {
    borderRadius: 50,
    paddingVertical: 12,
    paddingHorizontal: 28,
    borderWidth: 1.5,
    borderColor: "#FFFFFF",
  },
  shuffleText: {
    color: "#FFFFFF",
    fontWeight: "600",
    fontSize: 15,
  },
  trackList: {
    padding: 16,
    paddingTop: 8,
  },
  trackCard: {
    flexDirection: "row",
    alignItems: "center",
    paddingVertical: 10,
    paddingHorizontal: 8,
    gap: 12,
    borderBottomWidth: 1,
    borderBottomColor: "#1A1030",
  },
  albumArt: {
    width: 52,
    height: 52,
    borderRadius: 8,
  },
  albumArtPlaceholder: {
    backgroundColor: "#2D1F4E",
  },
  trackInfo: {
    flex: 1,
  },
  trackName: {
    color: "#FFFFFF",
    fontSize: 15,
    fontWeight: "600",
    marginBottom: 3,
  },
  trackArtist: {
    color: "#9CA3AF",
    fontSize: 13,
  },
  playButton: {
    width: 36,
    height: 36,
    borderRadius: 18,
    borderWidth: 1.5,
    borderColor: "#3D2F5E",
    alignItems: "center",
    justifyContent: "center",
  },
  playIcon: {
    color: "#A855F7",
    fontSize: 14,
  },
});
