import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  StatusBar,
} from "react-native";
import { useRouter } from "expo-router";
import { generatePlaylist } from "../services/api";

export default function JournalScreen() {
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const router = useRouter();

  const handleGenerate = async () => {
    if (content.trim().length < 10) {
      setError("Write a little more about your day!");
      return;
    }
    setError("");
    setLoading(true);
    try {
      const playlist = await generatePlaylist(content);
      router.push({
        pathname: "/playlist",
        params: { data: JSON.stringify(playlist) },
      });
    } catch (e) {
      setError("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <StatusBar barStyle="light-content" />
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === "ios" ? "padding" : "height"}
      >
        <ScrollView
          contentContainerStyle={styles.scroll}
          keyboardShouldPersistTaps="handled"
        >
          {/* Heading */}
          <View style={styles.headingContainer}>
            <Text style={styles.heading}>
              How does <Text style={styles.headingAccent}>today</Text> feel?
            </Text>
            <Text style={styles.subheading}>
              Whisper to the AI — it'll find your soundtrack.
            </Text>
          </View>

          {/* Input Card */}
          <View style={styles.inputCard}>
            <View style={styles.micRow}>
              <View style={styles.micButton}>
                <Text style={styles.micIcon}>🎙</Text>
              </View>
              <Text style={styles.inputHint}>
                Tap below to describe your vibe.
              </Text>
              <Text style={styles.waveIcon}>〜♪</Text>
            </View>
            <TextInput
              style={styles.input}
              placeholder={`"Rainy Sunday energy..."\n"I crushed my presentation today!"\n"Feeling lost and a little nostalgic..."`}
              placeholderTextColor="#6B5E8A"
              multiline
              value={content}
              onChangeText={setContent}
              maxLength={2000}
            />
            <Text style={styles.charCount}>{content.length}/2000</Text>
          </View>

          {error ? <Text style={styles.error}>{error}</Text> : null}
        </ScrollView>

        {/* Generate Button */}
        <View style={styles.bottomContainer}>
          <TouchableOpacity
            style={[
              styles.generateButton,
              loading && styles.generateButtonDisabled,
            ]}
            onPress={handleGenerate}
            disabled={loading}
          >
            {loading ? (
              <ActivityIndicator color="#fff" />
            ) : (
              <Text style={styles.generateButtonText}>Generate playlist</Text>
            )}
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#0D0D1A",
  },
  scroll: {
    padding: 24,
    paddingTop: 120,
    paddingBottom: 16,
  },
  headingContainer: {
    marginBottom: 32,
  },
  heading: {
    fontSize: 36,
    fontWeight: "800",
    color: "#FFFFFF",
    lineHeight: 44,
  },
  headingAccent: {
    color: "#A855F7",
  },
  subheading: {
    fontSize: 15,
    color: "#9CA3AF",
    marginTop: 10,
    lineHeight: 22,
  },
  inputCard: {
    backgroundColor: "#1A1030",
    borderRadius: 20,
    padding: 20,
    borderWidth: 1,
    borderColor: "#2D1F4E",
  },
  micRow: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 16,
    gap: 10,
  },
  micButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: "#A855F7",
    alignItems: "center",
    justifyContent: "center",
  },
  micIcon: {
    fontSize: 20,
  },
  inputHint: {
    flex: 1,
    color: "#9CA3AF",
    fontSize: 13,
  },
  waveIcon: {
    color: "#A855F7",
    fontSize: 18,
  },
  input: {
    color: "#FFFFFF",
    fontSize: 16,
    minHeight: 160,
    textAlignVertical: "top",
    lineHeight: 26,
  },
  charCount: {
    color: "#6B5E8A",
    fontSize: 12,
    textAlign: "right",
    marginTop: 8,
  },
  error: {
    color: "#EC4899",
    fontSize: 14,
    marginTop: 12,
    textAlign: "center",
  },
  bottomContainer: {
    padding: 24,
    paddingBottom: 40,
  },
  generateButton: {
    backgroundColor: "#A855F7",
    borderRadius: 50,
    padding: 18,
    alignItems: "center",
  },
  generateButtonDisabled: {
    opacity: 0.6,
  },
  generateButtonText: {
    color: "#FFFFFF",
    fontSize: 16,
    fontWeight: "700",
  },
});
