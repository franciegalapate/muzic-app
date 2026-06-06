package com.muzic.backend.service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnthropicService {

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode analyzeMood(String journalContent) {
        AnthropicClient client = AnthropicOkHttpClient.builder()
                .apiKey(apiKey)
                .build();

        String prompt = """
                Analyze this journal entry and extract mood information for music curation.
                
                Journal entry: "%s"
                
                Respond ONLY with a valid JSON object, no markdown, no explanation:
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

        MessageCreateParams params = MessageCreateParams.builder()
                .model(Model.CLAUDE_SONNET_4_5)
                .maxTokens(1024)
                .addUserMessage(prompt)
                .build();

        Message message = client.messages().create(params);
        String responseText = message.content().get(0).text().get().text();

        try {
            return objectMapper.readTree(responseText);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse mood analysis response", e);
        }
    }
}