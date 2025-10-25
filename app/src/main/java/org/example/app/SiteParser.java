import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

package org.example.app;

public class SiteParser {
    private static final Logger logger = LogManager.getLogger(SiteParser.class);

    public boolean tryGetArticle(String authorID, AuthorData author) throws IOException {
        try {
            Connection.Response response = Jsoup.connect("https://www.elibrary.ru/author_items.asp?authorid=" + authorID)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(30000)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .execute();

            logger.info("Соединение с сайтом установлено");

            Document results = response.parse();
            Element table = results.selectFirst("table#restab");
            String title = results.title();
            author.setName(title.split(" - ")[0]);

            if (table != null) {
                Elements publicationRows = table.select("tr[id^=arw]");

                Publication publication;
                List<Integer> articlesRef = new ArrayList<>();

                author.setArticles(publicationRows.size());

                for (Element row : publicationRows) {
                    publication = new Publication();
                    if (publication.parsePublicationRow(row)) {

                        int citations;
                        if ((citations = publication.getCitations()) == 0) {
                            author.setEmptyArticles(author.getEmptyArticles() + 1);
                            author.getEmptyArticlesList().add(publication);
                        }

                        articlesRef.add(citations);
                    }
                }

                Integer[] array = articlesRef.toArray(new Integer[0]);
                Arrays.sort(array,  Collections.reverseOrder());

                if (array.length == 1) {
                    author.setHIndex(0);
                    return true;
                }

                for (int i = 0; i < array.length; i++) {
                    if (i > array[i]) {
                        author.setHIndex(i - 1);
                        break;
                    }
                }

                return true;

            } else {
                logger.warn("Таблица с публикациями не найдена!");
                return false;
            }
        }
        catch (Exception e) {
            logger.error("Ошибка парсинга страницы: {}", String.valueOf(e));
            return false;
        }
    }
}
