package org.example.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileWorkerTest {

    @TempDir
    Path tempDir;

    @Test
    void readFile_ValidFile_ReturnsCorrectAuthors() throws Exception {
        Path testFile = tempDir.resolve("test_file.txt");
        Files.write(testFile, List.of("test_1", "test_2", "test_3 test_4"));

        List<String> result = FileWorker.readFile(testFile.toString());

        Assertions.assertEquals(4, result.size());
        Assertions.assertTrue(result.contains("test_1"));
        Assertions.assertTrue(result.contains("test_2"));
        Assertions.assertTrue(result.contains("test_3"));
        Assertions.assertTrue(result.contains("test_4"));
    }

    @Test
    void readFile_ValidFile_ReturnEmptyList() throws Exception {
        Path emptyFile = tempDir.resolve("test_empty_file.txt");
        Files.createFile(emptyFile);

        List<String> result = FileWorker.readFile(emptyFile.toString());

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void readFile_NonExistFile_ReturnEmptyList() {
        List<String> result = FileWorker.readFile("test_nonexist_file.txt");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void saveToFile_ValidFile_CorrectDataInFile() throws Exception {
        Path outputFile = tempDir.resolve("test_output_file.json");
        String json = "text for test";

        FileWorker.saveToFile(json, outputFile.toString());

        Assertions.assertTrue(Files.exists(outputFile));
        String dataFile = Files.readString(outputFile);
        Assertions.assertEquals(json, dataFile);
    }

    @Test
    void saveToFile_NullArgument_NoException() {
        Path outputFile = tempDir.resolve("test_output_null_file.json");

        Assertions.assertDoesNotThrow(() -> {
            FileWorker.saveToFile(null, outputFile.toString());
        });
    }

    @Test
    void saveToFile_WrongTest_NoException() {
        String json = "text for test";

        Assertions.assertDoesNotThrow(() -> {
            FileWorker.saveToFile(json, "test_output_null_file.json");
        });
    }
}
