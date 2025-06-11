package others;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OurUtils {
    static String saveHtmlToFile(String htmlContent) {
        try {
            File tempFile = File.createTempFile("clientPage", ".html");
            String name = tempFile.getName();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(htmlContent);
            }
            System.out.println("HTML content saved to " + tempFile.getAbsolutePath());
            return name;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void openInBrowser(String name) {
        try {
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
            File tempFile = new File(tempDir.toFile(), name);
            if (!tempFile.exists()) {
                System.out.println("Temporary file not found: " + tempFile.getAbsolutePath());
                return;
            }
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + tempFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}