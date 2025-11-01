package org.example.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PublicationTest {

    @Test
    void parsePublicationRow_ValidRowWithAllData_ReturnsTrueAndSetsAllFields() {
        String html = """
        <html>
        <body>
        <table>
        <tr>
            <td>1</td>
            <td>
                <a href="/item.asp?id=82490425"><b>ИНФОРМАЦИОННЫЕ ТЕХНОЛОГИИ</b></a>
                <font>Шиян А.А., Шиян П.А.</font>
                <font>Санкт-Петербург, 2025.</font>
            </td>
            <td>0</td>
        </tr>
        </table>
        </body>
        </html>
        """;
        Document doc = Jsoup.parse(html);
        Element row = doc.selectFirst("tr");
        // Act
        Publication parser = new Publication();
        boolean result = parser.parsePublicationRow(row);

        // Assert
        Assertions.assertTrue(result);
        Assertions.assertEquals("ИНФОРМАЦИОННЫЕ ТЕХНОЛОГИИ", parser.getTitle());
        Assertions.assertEquals("Санкт-Петербург, 2025.", parser.getPublicationDetails());
        Assertions.assertEquals(0, parser.getCitations());
    }

}
