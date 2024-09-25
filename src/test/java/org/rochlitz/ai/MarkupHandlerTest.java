package org.rochlitz.ai;

import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class MarkupHandlerTest
{

    MarkupHandler markupHandler = new MarkupHandler();

    // Beispiel-Markup-Text mit Java-Code
    String markupText = "Der folgende Code ist ein Beispiel für eine JUnit Test Klasse in Java:\n\n"
        + "```java\n"
        + "package com.example.tests;\n\n"
        + "import org.junit.jupiter.api.Test;\n"
        + "public class TestExample {\n"
        + "    @Test\n"
        + "    public void testAddition() {\n"
        + "        System.out.println(\"test  !!!!!!!!!!!!\");\n"
        + "    }\n"
        + "}\n"
        + "```\n";

    // Test für die Methode zur Extraktion des Java-Codes
    @Test
    public void testExtractJavaCode() {
        String expectedCode = "package com.example.tests;\n\n"
            + "import org.junit.jupiter.api.Test;\n"
            + "public class TestExample {\n"
            + "    @Test\n"
            + "    public void testAddition() {\n"
            + "        System.out.println(\"test  !!!!!!!!!!!!\");\n"
            + "    }\n"
            + "}\n";


        String extractedCode = markupHandler.extractJavaCode(markupText);
        assertEquals(expectedCode, extractedCode, "Der Java-Code wurde nicht korrekt extrahiert.");
    }

    // Test für die Methode zur Extraktion des Paketnamens
    @Test
    public void testExtractPackageName() {
        String extractedCode = markupHandler.extractJavaCode(markupText);
        String packageName = markupHandler.extractPackageName(extractedCode);
        assertEquals("com.example.tests", packageName, "Der Paketname wurde nicht korrekt extrahiert.");
    }

    // Test für die Methode zur Extraktion des Klassennamens
    @Test
    public void testExtractClassName() {
        String extractedCode = markupHandler.extractJavaCode(markupText);
        String className = markupHandler.extractClassName(extractedCode);
        assertEquals("TestExample", className, "Der Klassenname wurde nicht korrekt extrahiert.");
    }

    // Test für die Dateipfadgenerierung
    @Test
    public void testGenerateFilePath() {
        String packageName = "com.example.tests";
        String className = "TestExample";
        String expectedPath = "src/test/java/com/example/tests/TestExample.java";
        String filePath = markupHandler.generateFilePath(packageName, className);
        assertEquals(expectedPath, filePath, "Der Dateipfad wurde nicht korrekt generiert.");
    }

    // Test für die Methode saveCodeToFile
    @Test
    public void testSaveCodeToFile() {
        String code = "package com.example.tests;\n\n"
            + "import org.junit.jupiter.api.Test;\n"
            + "public class TestExample {\n"
            + "    @Test\n"
            + "    public void testAddition() {\n"
            + "        System.out.println(\"test 2 !!!!!!!!!!!!\");\n"
            + "    }\n"
            + "}\n";
        String packageName = "com.example.tests";
        String className = "TestExample";
        String filePath = markupHandler.generateFilePath(packageName, className);

        // Speichere den Code in die Datei
        markupHandler.saveCodeToFile(code, filePath);

        // Überprüfe, ob die Datei existiert
        File file = new File(filePath);
        assertTrue(file.exists(), "Die Datei wurde nicht korrekt erstellt.");

        // Lösche die Datei nach dem Test
//        file.delete();
    }
}
