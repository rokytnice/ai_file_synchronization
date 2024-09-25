package org.rochlitz.ai;

import okhttp3.*;

import java.io.IOException;
@Deprecated
public class OpenAIModels {

    // API-URL für die Modellliste
    private static final String API_URL = "https://api.openai.com/v1/models";
    // Ersetzen Sie diese Werte durch Ihre tatsächlichen API-Schlüssel und Projektinformationen
    private static final String API_KEY = System.getenv("API_KEY");

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Anfrage erstellen
        Request request = new Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer " + API_KEY)           // API-Authentifizierung
//            .addHeader("OpenAI-Organization", ORGANIZATION_ID)         // Organisation ID
//            .addHeader("OpenAI-Project", PROJECT_ID)                   // Projekt ID
            .build();

        // Anfrage senden
        Response response = client.newCall(request).execute();

        // Antwort verarbeiten
        if (response.isSuccessful()) {
            System.out.println("Response Body: " + response.body().string());
        } else {
            System.out.println("Fehler: " + response.code() + " - " + response.message());
        }

        response.close();
    }
}
