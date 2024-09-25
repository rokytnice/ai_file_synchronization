package org.rochlitz.ai;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser
{

    /**
     * Verarbeitet den JSON-String und extrahiert das Feld "content" aus "choices[0].message.content".
     *
     * @param jsonString Der JSON-String, der verarbeitet werden soll.
     * @return Der extrahierte Inhalt des Feldes "content".
     */
    public static String extractContentFromJson(String jsonString) {
        // Umwandlung des Strings in ein JSON-Objekt
        JSONObject jsonObject = new JSONObject(jsonString);

        // Zugriff auf das Feld "choices" und dann auf "message" -> "content"
        JSONArray choicesArray = jsonObject.getJSONArray("choices");
        JSONObject firstChoice = choicesArray.getJSONObject(0);
        JSONObject messageObject = firstChoice.getJSONObject("message");
        return messageObject.getString("content");
    }
}
