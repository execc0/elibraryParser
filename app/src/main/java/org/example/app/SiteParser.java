package org.example.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Класс для парсинга информации с сайта elibrary.
 * <p>
 * Предоставляет методы для получения информации об авторе.
 * </p>
 *
 * @since 2025-10-26
 */
public class SiteParser {
    private static final Logger logger = LogManager.getLogger(SiteParser.class);
    private static final String ELIBRARY_URL_TEMPLATE = "https://www.elibrary.ru/author_items.asp?authorid=";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    private static final int TIMEOUT_MS = 30000;
    private static final String PUBLICATIONS_TABLE_SELECTOR = "table#restab";
    private static final String PUBLICATION_ROW_SELECTOR = "tr[id^=arw]";
    private static final String TITLE_DELIMITER = " - ";

    /**
     * Получает и анализирует информацию о публикациях автора.
     * <p>
     * Выполняет запрос к сайту elibrary, извлекает данные о публикациях,
     * рассчитывает количество цитирований и индекс Хирша.
     * </p>
     *
     * @since 2025-10-26
     * @param authorID UID автора в системе elibrary.
     * @param authorData объект класса {@link AuthorData} для сохранения полученной информации.
     * @return true или false в зависимости от успешности получения информации.
     */
    public boolean tryGetArticle(String authorID, AuthorData authorData) {
        if (authorID == null || authorID.trim().isEmpty()) {
            logger.warn("Передан пустой authorID");
            return false;
        }

        if (authorData == null) {
            logger.warn("Передан null authorData");
            return false;
        }

        try {
            Document document = connectElibrary(authorID);

            if (document == null) {
                return false;
            }

            extractAuthorName(document, authorData);

            Element publicationsTable = document.selectFirst(PUBLICATIONS_TABLE_SELECTOR);
            if (publicationsTable == null) {
                logger.warn("Таблица публикаций не найдена для автора: {}", authorID);
                return false;
            }

            List<Integer> articlesRef = new ArrayList<>();
            parsePublicationTable(publicationsTable, authorData, articlesRef);

            calcHIndex(articlesRef, authorData);

            return true;
        }
        catch (Exception e) {
            logger.error("Ошибка парсинга страницы: {}", String.valueOf(e));
            return false;
        }
    }

    /**
     * Получает html код страницы с нужным автором.
     * <p>
     * Получает html код страницы с UID автора, который был передан при вызове метода.
     * В случае возникновения ошибки возвращает null.
     * </p>
     *
     * @since 2025-10-26
     * @param authorID UID автора, необходимое для получения информации о нем.
     * @return Объект класса {@link Document} для дальнейшего парсинга страницы.
     */
    private Document connectElibrary(String authorID) {
        try {
            String url = ELIBRARY_URL_TEMPLATE + authorID;
            logger.info("Выполнение запроса к: {}", url);

            Connection.Response response = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MS)
                    .followRedirects(true)
                    .execute();

            logger.info("Соединение с сайтом установлено, статус: {}", response.statusCode());
            return response.parse();

        } catch (Exception e) {
            logger.error("Ошибка при выполнении запроса для автора {}: {}", authorID, e.getMessage());
            return null;
        }
    }

    /**
     * Извлекает имя автора из кода страницы.
     *
     * @since 2025-10-26
     * @param document объект класса {@link Document}, содержащий html код страницы.
     * @param authorData объект класса {@link AuthorData} для хранения информации об авторе.
     */
    private void extractAuthorName(Document document, AuthorData authorData) {
        try {

            String title = document.title();
            if (title.contains(TITLE_DELIMITER)) {
                String authorName = title.split(TITLE_DELIMITER)[0].trim();
                authorData.setName(authorName);
                logger.debug("Извлечено имя автора: {}", authorName);

            } else {
                logger.warn("Не удалось извлечь имя автора из заголовка: {}", title);
                authorData.setName("Неизвестно");

            }
        } catch (Exception e) {
            logger.warn("Ошибка при извлечении имени автора: {}", e.getMessage());
            authorData.setName("Неизвестнл");
        }
    }

    /**
     * Обрабатывает таблицу публикаций и извлекает данные о статьях.
     * <p>
     * Получает данные о статьях автора, выбирая элементы таблицы соответствующие CSS-селектору:
     * [id^=arw] - элементы, у которых атрибут id начинается с "arw".
     * </p>
     *
     * @since 2025-10-26
     * @param publicationsTable HTML-элемент таблицы публикаций.
     * @param authorData объект класса {@link AuthorData} для хранения информации об авторе.
     * @param articlesRef список для хранения информации о цитированиях статей автора для дальнейшего
     *                    вычисления индекса Хирша.
     */
    private void parsePublicationTable(Element publicationsTable, AuthorData authorData, List<Integer> articlesRef) {
        Elements publicationRows = publicationsTable.select(PUBLICATION_ROW_SELECTOR);
        Publication publication;

        authorData.setNumberOfArticles(publicationRows.size());

        for (Element row : publicationRows) {
            publication = new Publication();
            if (publication.parsePublicationRow(row)) {

                int citations;
                if ((citations = publication.getCitations()) == 0) {
                    authorData.setNumberOfEmptyArticles(authorData.getNumberOfEmptyArticles() + 1);
                    authorData.getEmptyArticlesList().add(publication);
                }

                articlesRef.add(citations);
            }
        }
    }

    /**
     * Вычисляет индекс Хирша.
     *
     * @since 2025-10-26
     * @param articlesRef список, содержащая информацию о цитированиях статей автора для
     *                    вычисления индекса Хирша.
     * @param authorData объект класса {@link AuthorData} для хранения информации об авторе.
     */
    private void calcHIndex(List<Integer> articlesRef, AuthorData authorData) {
        Integer[] array = articlesRef.toArray(new Integer[0]);
        Arrays.sort(array,  Collections.reverseOrder());

        if (array.length == 1) {
            authorData.setHIndex(0);
            return;
        }

        for (int i = 0; i < array.length; i++) {
            if (i > array[i]) {
                authorData.setHIndex(i - 1);
                break;
            }
        }
    }
}
