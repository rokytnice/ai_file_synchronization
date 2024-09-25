package org.rochlitz.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest
{

    private JsonParser openAIResponseService;

    @BeforeEach
    public void setUp() {
        openAIResponseService = new JsonParser();
    }

    @Test
    public void testExtractContentFromJson() {
        // Beispiel-JSON-String
        String jsonString = "{\n" +
            "  \"id\": \"chatcmpl-A8jtvUidTYLmWmKdm3gPXYoNHoVzn\",\n" +
            "  \"object\": \"chat.completion\",\n" +
            "  \"created\": 1726646431,\n" +
            "  \"model\": \"gpt-4-0613\",\n" +
            "  \"choices\": [\n" +
            "    {\n" +
            "      \"index\": 0,\n" +
            "      \"message\": {\n" +
            "        \"role\": \"assistant\",\n" +
            "        \"content\": \"Hier ist eine einfache Java JUnit Testklasse...\",\n" +
            "        \"refusal\": null\n" +
            "      },\n" +
            "      \"logprobs\": null,\n" +
            "      \"finish_reason\": \"stop\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"usage\": {\n" +
            "    \"prompt_tokens\": 26,\n" +
            "    \"completion_tokens\": 250,\n" +
            "    \"total_tokens\": 276,\n" +
            "    \"completion_tokens_details\": {\n" +
            "      \"reasoning_tokens\": 0\n" +
            "    }\n" +
            "  },\n" +
            "  \"system_fingerprint\": null\n" +
            "}";

        // Erwarteter Inhalt des "content"-Felds
        String expectedContent = "Hier ist eine einfache Java JUnit Testklasse...";

        // Tatsächlicher Inhalt, der durch die Service-Methode extrahiert wurde
        String actualContent = openAIResponseService.extractContentFromJson(jsonString);

        // Überprüfe, ob der extrahierte Inhalt dem erwarteten entspricht
        assertEquals(expectedContent, actualContent, "Der extrahierte Inhalt stimmt nicht mit dem erwarteten überein.");
    }
}
