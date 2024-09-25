package org.rochlitz.ai;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OpenAIClient {

    private static final String API_KEY = System.getenv("API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String HISTORY_FILE_PATH = "conversation_history.json";

    public static void requestAiImplementation(String requestBody) throws IOException {
        try {
            // Lese die Historie aus der Datei und füge die neue Nachricht hinzu
            JSONArray conversationHistory = readHistoryFromFile();
            conversationHistory.put(new JSONObject().put("role", "user").put("content", requestBody));

            // Sende die Anfrage
            Response response = getResponse(conversationHistory);
            System.out.println(conversationHistory);
            System.out.println(response.toString());
            String json = response.body().string();
            String content = JsonParser.extractContentFromJson(json);

            // Verarbeite die Antwort und speichere sie in der Historie
            MarkupHandler markupHandler = new MarkupHandler();
            Optional<String> file = markupHandler.extractSourceCodeToFile(content);

            // Antwort des Assistenten speichern
            conversationHistory.put(new JSONObject().put("role", "assistant").put("content", content));
            writeHistoryToFile(conversationHistory);

            // Ausgabe der Antwort
            if (response.isSuccessful()) {
                System.out.println("\n\n\n\nFile generiert: " + file.get() + "\n\n\n\n");
            } else {
                System.out.println("Fehler: " + response.code() + " - " + response.message());
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static Response getResponse(JSONArray conversationHistory) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

        // Erstelle den JSON-Body mit dem Konversationsverlauf
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "gpt-4");
        jsonBody.put("messages", conversationHistory);

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer " + API_KEY)
            .post(body)
            .build();

        return client.newCall(request).execute();
    }

    // Lese die Historie aus der Datei
    private static JSONArray readHistoryFromFile() {
        try {
            File historyFile = new File(HISTORY_FILE_PATH);
            if (historyFile.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(HISTORY_FILE_PATH)));
                return new JSONArray(content);
            }
        } catch (IOException e) {
            log.error("Fehler beim Lesen der Historie aus der Datei", e);
        }
        return new JSONArray(); // Leere Historie, falls die Datei nicht existiert
    }

    // Schreibe die Historie in die Datei
    private static void writeHistoryToFile(JSONArray conversationHistory) {
        try (FileWriter fileWriter = new FileWriter(HISTORY_FILE_PATH)) {
            fileWriter.write(conversationHistory.toString(4)); // JSON mit Einrückung für Lesbarkeit
        } catch (IOException e) {
            log.error("Fehler beim Speichern der Historie in die Datei", e);
        }
    }
}
