package org.example.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилитарный класс для работы с файлами.
 * <p>
 * Предоставляет методы для чтения и сохранения данных в файлы.
 * </p>
 *
 * @since 2025-10-26
 */
public class FileWorker {
    private static final Logger logger = LogManager.getLogger(FileWorker.class);

    /**
     * Читает файл и возвращает список UID авторов.
     * <p>
     * Каждая строка файла становится отдельным элементом списка.
     * Элементы одной строки, записанные через пробел становятся отдельными элементами списка.
     * </p>
     *
     * @since 2025-10-26
     * @param filePath путь к файлу для чтения
     * @return список authorID из файла.
     */
    public static List<String> readFile(String filePath) {
        List<String> authors = new ArrayList<>();

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            logger.warn("Файл не существует: {}", filePath);
            return authors;
        }
        if (!Files.isReadable(path)) {
            logger.warn("Файл не читаемый: {}", filePath);
            return authors;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();

                if (!trimmedLine.isEmpty()) {

                    String[] dataLine = trimmedLine.split(" ");
                    for (String author : dataLine) {
                        if (!author.isEmpty()) {
                            authors.add(author);
                        }
                    }
                }
            }

            logger.info("Файл: {} обработан.", filePath);

            return authors;
        }
        catch (Exception e) {
            logger.error("Ошибка при чтении файла: ", e);
            return authors;
        }
    }

    /**
     * Записывает информацию об авторах в файл, по указанному пути.
     * <p>
     * Сохраняет переданную строку по указанному пути.
     * </p>
     *
     * @since 2025-10-26
     * @param json строка с данными.
     * @param path путь к файлу с результатами.
     */
    public static void saveToFile(String json, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
            logger.info("Данные сохранены в файл {}", path);
            System.out.println("Данные успешно сохранены в файл: " + path);

        } catch (Exception e) {
            logger.error("Ошибка сохранения файла: ", e);
            System.out.println("Ошибка сохранения данных в файл: " + path);
        }
    }
}
