package org.rochlitz.ai;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkupHandler
{

    // Basisverzeichnis, wo die Dateien gespeichert werden sollen
//    public static final String BASE_DIRECTORY = "src/main/java"; // Beispielpfad, anpassen nach Bedarf
    public String baseDirectory ; // Beispielpfad, anpassen nach Bedarf
    public String contextPath = "src/test/java";//file separator

    public MarkupHandler() {


        baseDirectory = System.getProperty("user.dir")+File.separator + contextPath;
        System.out.println("Das aktuelle Projekt befindet sich in: " + baseDirectory);

    }

    public Optional<String> extractSourceCodeToFile(String markupText) {

        // Den Quellcode extrahieren
        String extractedCode = extractJavaCode(markupText);

        return saveSourceCode(extractedCode);
    }

    private   Optional<String> saveSourceCode(String extractedCode)
    {
        if (extractedCode != null) {
            // Den Package-Namen extrahieren
            String packageName = extractPackageName(extractedCode);
            // Den Klassennamen extrahieren
            String className = extractClassName(extractedCode);

            if (className != null) {
                // Erzeuge den vollständigen Dateipfad
                String filePath = generateFilePath(packageName, className);

                // Speichere den Quellcode in die Datei
                saveCodeToFile(extractedCode, filePath);

                return  filePath.describeConstable();
            } else {
                System.out.println("Kein Klassenname gefunden.");
            }
        } else {
            System.out.println("Kein Java-Code gefunden.");
        }
        return Optional.empty();
    }

    // Methode, um Java-Code aus dem Markup-Text zu extrahieren
    public   String extractJavaCode(String markupText) {
        // Regex, um den Code innerhalb von ```java``` zu finden
        Pattern pattern = Pattern.compile("```java\\n([\\s\\S]*?)```");
        Matcher matcher = pattern.matcher(markupText);
        if (matcher.find()) {
            return matcher.group(1); // Der Java-Code, der innerhalb der ```java```-Tags gefunden wurde
        }
        return null;
    }

    // Methode, um den Package-Namen aus dem Quellcode zu extrahieren
    public   String extractPackageName(String code) {
        // Regex, um den Package-Namen zu finden (package <packageName>;)
        Pattern pattern = Pattern.compile("package\\s+([\\w\\.]+);");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1); // Der gefundene Package-Name
        }
        return null;
    }

    // Methode, um den Klassennamen aus dem Quellcode zu extrahieren
    public   String extractClassName(String code) {
        // Regex, um den Klassennamen zu finden (public class <ClassName>)
        Pattern pattern = Pattern.compile("public class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1); // Der gefundene Klassenname
        }
        return null;
    }

    // Methode, um den vollständigen Dateipfad basierend auf dem Package-Namen und dem Klassennamen zu generieren
    public   String generateFilePath(String packageName, String className) {
        // Wenn ein Package vorhanden ist, ersetze Punkte durch Verzeichnistrenner
        if (packageName != null) {
            String packagePath = packageName.replace('.', File.separatorChar);
            return baseDirectory + File.separator + packagePath + File.separator + className + ".java";
        }
        // Falls kein Package angegeben ist, speichere die Datei im Basisverzeichnis
        return baseDirectory + File.separator + className + ".java";
    }

    // Methode, um den extrahierten Code in eine Datei zu speichern
    public   void saveCodeToFile(String code, String filePath) {
        try {
            // Erzeuge das Verzeichnis, falls es noch nicht existiert
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            // Schreibe den Code in die Datei
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(code);
                System.out.println("Code wurde in die Datei " + filePath + " gespeichert.");
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }
}
