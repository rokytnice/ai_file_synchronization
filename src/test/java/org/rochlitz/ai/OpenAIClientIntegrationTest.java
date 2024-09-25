package org.rochlitz.ai;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class OpenAIClientIntegrationTest {

    @Test
    public void testRequestAiImplementation() {
        String requestBody = "Schreibe eine neu Klasse die mir eine Linie zeichnet das ganze mit Junit 5"
            ;


        // Durchführung des echten API-Aufrufs
        try {
            OpenAIClient.requestAiImplementation(requestBody);
            // Wenn kein Fehler auftritt, gilt der Test als bestanden
            assertTrue(true, "Die API-Anfrage wurde erfolgreich durchgeführt.");
        } catch (IOException e) {
            // Wenn ein Fehler auftritt, wird der Test fehlschlagen
            fail("API-Anfrage ist fehlgeschlagen: " + e.getMessage());
        } catch (RuntimeException e) {
            // Wenn ein Fehler während der Response-Verarbeitung auftritt, wird der Test ebenfalls fehlschlagen
            fail("Fehler während der Verarbeitung der API-Antwort: " + e.getMessage());
        }
    }
}
