package org.example.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.*;
import java.util.List;
import java.util.Map;

public class MainTest extends Main {

    @Test
    void getValidFilePath_ValidPath_ReturnValidData() {
        String path = "E:\\just\\path\\to\\file.txt";
        InputStream originalStream = System.in;

        try {
            ByteArrayInputStream testIn = new ByteArrayInputStream(path.getBytes());
            System.setIn(testIn);

            String result;
            result = getValidFilePath("");

            Assertions.assertEquals(path, result);

        } finally {
            System.setIn(originalStream);
        }
    }

    @Test
    void getValidFilePath_NullArg_OutputsEmptyString() {
        String path = "E:\\just\\path\\to\\file.txt";

        InputStream originalInputStream = System.in;
        PrintStream originalOutputStream = System.out;

        try {
            ByteArrayInputStream emptyTestIn = new ByteArrayInputStream(path.getBytes());
            System.setIn(emptyTestIn);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            getValidFilePath(null);

            String result = outputStream.toString();

            Assertions.assertEquals("", result);

        } finally {
            System.setIn(originalInputStream);
            System.setOut(originalOutputStream);
        }
    }

    @Test
    void getValidFilePath_EmptyInput_OutputsErrorMessage() {
        String input = "\nvalid/path/after/empty.txt";
        InputStream originalInputStream = System.in;
        PrintStream originalOutputStream = System.out;

        try {
            ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
            System.setIn(testIn);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            String result = getValidFilePath("Введите путь: ");

            String output = outputStream.toString();

            Assertions.assertTrue(output.contains("Путь не может быть пустым"));
            Assertions.assertEquals("valid/path/after/empty.txt", result);

        } finally {
            System.setIn(originalInputStream);
            System.setOut(originalOutputStream);
        }
    }


    @Test
    void processAuthors_ValidData_ReturnCorrectMap() throws JsonProcessingException {
        List<String> authors = List.of("1287962");

        Map<String, AuthorData> resultMap = processAuthors(authors);

        Assertions.assertEquals(1, resultMap.size());

        AuthorData authorData = resultMap.get("1287962");
        Assertions.assertNotNull(authorData);
        Assertions.assertEquals("1287962", authorData.getAuthorID());
        Assertions.assertEquals("Журавлева Анастасия Сергеевна", authorData.getName());
        Assertions.assertEquals(1, authorData.getNumberOfArticles());
        Assertions.assertEquals(1, authorData.getNumberOfEmptyArticles());
        Assertions.assertEquals(0, authorData.getHIndex());

        Assertions.assertEquals(1, authorData.getEmptyArticlesList().size());
        Publication article = authorData.getEmptyArticlesList().get(0);
        Assertions.assertEquals("РАЗРАБОТКА МЕТОДА ФОРМИРОВАНИЯ ОБЩЕГО СЕТЕВОГО КЛЮЧА В ПРОТОКОЛЕ OWE В WI-FI СЕТЯХ", article.getTitle());
        Assertions.assertEquals(0, article.getCitations());
        Assertions.assertEquals("В сборнике: Подготовка профессиональных кадров в магистратуре в эпоху цифровой трансформации (ПКМ-2024). Сборник лучших докладов V Всероссийской научно-технической и научно-методической конференции магистрантов и их руководителей. В 2-х томах. Санкт-Петербург, 2025. С. 380-384.", article.getPublicationDetails());
    }
}
