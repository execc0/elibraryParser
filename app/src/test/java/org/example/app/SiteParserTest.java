package org.example.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SiteParserTest extends SiteParser{

    @Test
    void tryGetArticle_ValidAuthorID_ReturnTrue() {
        String testUID = "482409";

        SiteParser siteParser = new SiteParser();
        AuthorData authorData = new AuthorData();

        boolean result = siteParser.tryGetArticle(testUID, authorData);

        Assertions.assertTrue(result);
    }

    @Test
    void tryGetArticle_InvalidAuthorID_ReturnFalse() {
        String testUID = "a2345b";

        SiteParser siteParser = new SiteParser();
        AuthorData authorData = new AuthorData();

        boolean result = siteParser.tryGetArticle(testUID, authorData);

        Assertions.assertFalse(result);
    }

    @Test
    void tryGetArticle_NullAuthorID_ReturnFalse() {
        SiteParser siteParser = new SiteParser();
        AuthorData authorData = new AuthorData();

        boolean result = siteParser.tryGetArticle(null, authorData);

        Assertions.assertFalse(result);
    }

    @Test
    void tryGetArticle_EmptyAuthorID_ReturnFalse() {
        SiteParser siteParser = new SiteParser();
        AuthorData authorData = new AuthorData();

        boolean result_1 = siteParser.tryGetArticle("", authorData);
        boolean result_2 = siteParser.tryGetArticle("   ", authorData);

        Assertions.assertFalse(result_1);
        Assertions.assertFalse(result_2);
    }

    @Test
    void tryGetArticle_NullAuthorData_ReturnFalse() {
        SiteParser siteParser = new SiteParser();

        boolean result = siteParser.tryGetArticle("482409", null);

        Assertions.assertFalse(result);
    }

    @Test
    void tryGetArticle_ValidAuthorID_AuthorDataContainsCorrectName() {
        SiteParser siteParser = new SiteParser();
        AuthorData authorData = new AuthorData();

        siteParser.tryGetArticle("482409", authorData);

        Assertions.assertNotEquals("Неизвестно", authorData.getName());
    }

    @Test
    void connectElibrary_ValidAuthorID_NotNullReturn() {
        String authorID = "482409";

        Document doc = connectElibrary(authorID);

        Assertions.assertNotNull(doc);
    }

    @Test
    void connectElibrary_InvalidAuthorID_NullReturn() {
        String authorID = "a2345b";

        Document doc = connectElibrary(authorID);

        Assertions.assertNull(doc);
    }

    @Test
    void extractAuthorName_ValidData_ReturnCorrectName() {
        String html = """
                <head>
                <meta name="robots" content="noindex,nofollow">
                <title>Шиян Павел Анатольевич - Список публикаций</title>
                </head>
        """;
        Document doc = Jsoup.parse(html);
        AuthorData authorData = new AuthorData();

        extractAuthorName(doc, authorData);

        Assertions.assertEquals("Шиян Павел Анатольевич", authorData.getName());
    }

    @Test
    void extractAuthorName_ValidData_ReturnNN() {
        String html = """
                <head>
                <meta name="robots" content="noindex,nofollow">
                </head>
        """;
        Document doc = Jsoup.parse(html);
        AuthorData authorData = new AuthorData();

        extractAuthorName(doc, authorData);

        Assertions.assertEquals("Неизвестно", authorData.getName());
    }

    @Test
    void parsePublicationTable_ValidData_ReturnCorrectData() {
        String html = """
                <table width="580" cellspacing="0" cellpadding="3" id="restab" style="border-spacing: 0px 4px; table-layout: fixed;">
                   <tbody>
                   <tr id="arw82490425">
                   <td align="center">
                   <font color="#00008f"><b>1.</b></font><br>
                   </td>
                   <td align="left">
                   <a href="/item.asp?id=82490425"><b>ИНФОРМАЦИОННЫЕ ТЕХНОЛОГИИ</b></a><br>
                   <font color="#00008f"><i>Шиян А.А., Шиян П.А.</i></font><br>
                   <font color="#00008f">Санкт-Петербург, 2025.s</font>
                   </td>
                   <td align="center">0</td>
                   </tr>
                
                   <tr id="arw68550782">
                   <td align="center">
                   <font color="#00008f"><b>2.</b></font><br>
                   </td>
                   <td align="left">
                   <a href="/item.asp?id=68550782"><b>СОВРЕМЕННЫЕ АСПЕКТЫ КИБЕРБЕЗОПАСНОСТИ И ЗАЩИТЫ ДАННЫХ</b></a><br>
                   <font color="#00008f"><i>Маннанов А.А., Шиян П.А.</i></font><br>
                   <font color="#00008f">В сборнике: Актуальные проблемы инфотелекоммуникаций в науке и образовании (АПИНО 2024). Материалы XIII Международной научно-технической и научно-методической конференции. Санкт-Петербург, 2024. С. 488-492.</font>
                   </td>
                   <td align="center">1</td>
                   </tr>
                
                   <tr id="arw65698663">
                   <td align="center">
                   <font color="#00008f"><b>3.</b></font><br>
                   </td>
                   <td align="left">
                   <a href="/item.asp?id=65698663"><b>КОММУНИКАЦИЯ В КОНТЕКСТЕ ЦИФРОВИЗАЦИИ И МЕДИАТИЗАЦИИ</b></a><br>
                   <font color="#00008f"><i>Кропанина А.А., Шиян П.А.</i></font><br>
                   <font color="#00008f">В сборнике: Подготовка профессиональных кадров в магистратуре для цифровой экономики (ПКМ-2023). Всероссийская научно-техническая и научно-методическая конференция магистрантов и их руководителей. Сборник лучших докладов: в 2-х томах. Санкт-Петербург, 2024. С. 495-497.</font>
                   </td>
                   <td align="center">0</td>
                   </tr>
                   </tbody>
                   </table>
        """;

        Document document = Jsoup.parse(html);
        Element publicationsTable = document.selectFirst("table#restab");
        AuthorData authorData = new AuthorData();
        List<Integer> articlesRef = new ArrayList<>();

        parsePublicationTable(publicationsTable, authorData, articlesRef);

        Assertions.assertEquals(3, articlesRef.size());
        Assertions.assertTrue(articlesRef.contains(1));
        Assertions.assertTrue(articlesRef.contains(0));
        Assertions.assertEquals(2, authorData.getNumberOfEmptyArticles());
        Assertions.assertEquals(3, authorData.getNumberOfArticles());
    }

    @Test
    void calcHIndex_ValidData_ReturnOne() {
        List<Integer> articlesCit_1 = Arrays.asList(0, 1, 0);
        List<Integer> articlesCit_2 = Arrays.asList(7, 5, 9, 1, 0, 4, 0, 5);
        List<Integer> articlesCit_3 = Arrays.asList(3, 3, 3);
        AuthorData authorData = new AuthorData();

        calcHIndex(articlesCit_1, authorData);
        Assertions.assertEquals(1, authorData.getHIndex());

        calcHIndex(articlesCit_2, authorData);
        Assertions.assertEquals(4, authorData.getHIndex());

        calcHIndex(articlesCit_3, authorData);
        Assertions.assertEquals(4, authorData.getHIndex());
    }

    @Test
    void calcHIndex_NullData_ReturnZero() {
        List<Integer> articlesCit_1 = null;
        AuthorData authorData = new AuthorData();

        calcHIndex(articlesCit_1, authorData);
        Assertions.assertEquals(0, authorData.getHIndex());
    }

    @Test
    void calcHIndex_EmptyData_ReturnZero() {
        List<Integer> articlesCit_1 = new ArrayList<>();
        AuthorData authorData = new AuthorData();

        calcHIndex(articlesCit_1, authorData);
        Assertions.assertEquals(0, authorData.getHIndex());
    }
}
