package com.muzic.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public JsonNode analyzeMood(String journalContent) {
        String prompt = """
                Analyze this journal entry and extract mood information for music curation.
                
                Journal entry: "%s"
                
                Respond ONLY with a valid JSON object, no markdown, no explanation, no code blocks:
                {
                  "mood": "one word mood (e.g. happy, melancholic, anxious, peaceful, energetic)",
                  "energy_level": "low, medium, or high",
                  "themes": ["theme1", "theme2"],
                  "playlist_name": "a creative playlist name based on the mood",
                  "playlist_description": "a short 1-sentence description",
                  "genres": ["genre1", "genre2", "genre3"],
                  "spotify_seed_keywords": ["keyword1", "keyword2", "keyword3"]
                }
                """.formatted(journalContent);

        String requestBody = """
                {
                  "model": "openai/gpt-oss-120b:free",
                  "messages": [
                    {"role": "user", "content": "%s"}
                  ]
                }
                """.formatted(prompt.replace("\"", "\\\"").replace("\n", "\\n"));

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            System.out.println("OpenRouter response: " + response.body());
            String content = root.get("choices").get(0).get("message").get("content").asText();

            // Strip markdown code blocks if model adds them
            content = content.replaceAll("```json", "").replaceAll("```", "").trim();

            return objectMapper.readTree(content);

        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze mood: " + e.getMessage(), e);
        }
    }
}