package org.example.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.*;
import java.util.List;
import java.util.Map;

public class MainTest extends Main {

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
