import org.jetbrains.annotations.Nullable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

package org.example.app;

public class FileWorker {
    public static @Nullable List<String> readFile(String path) throws IOException {
        List<String> authors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {

                String[] dataLine = line.split(" ");
                if (dataLine.length > 1) {
                    authors.addAll(Arrays.asList(dataLine));
                }
                else {
                    authors.add(line);
                }
            }

            return authors;
        }
        catch (Exception e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            e.printStackTrace();
            return authors;
        }
    }

    public static void saveToFile(String json, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения файла: " + e.getMessage());
        }
    }
}
