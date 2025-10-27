package org.example.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Класс для хранения информации о публикации.
 *
 * @since 2025-10-26
 */
public class Publication {
    /** Название статьи. */
    private String title;
    /** Количество цитирований. */
    private int citations;
    /** Место публикации и дата. */
    private String publicationDetails;

    private static final Logger logger = LogManager.getLogger(Publication.class);

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getCitations() { return citations; }
    public void setCitations(int citations) { this.citations = citations; }

    public String getPublicationDetails() { return publicationDetails; }
    public void setPublicationDetails(String publicationDetails) { this.publicationDetails = publicationDetails; }

    /**
     * Парсит HTML-строку таблицы публикации и извлекает данные.
     *
     * <p>
     * Извлекает следующую информацию:
     * - Название статьи из ссылки с селектором {@code a[href*=id] b}
     * - Количество цитирований из третьей ячейки таблицы
     * - Детали публикации из элементов {@code font}
     * </p>
     *
     * @since 2025-10-26
     * @param row элемент HTML кода, содержащий информацию о статье.
     * @return true если данные успешно извлечены, false в случае ошибки парсинга.
     */
    public boolean parsePublicationRow(Element row) {
        try {
            Element infoCell = row.select("td").get(1);

            Element titleLink = infoCell.selectFirst("a[href*=id] b");
            if (titleLink != null) {
                setTitle(titleLink.text());
            }
            else {
                setTitle("Неизвестно.");
                logger.warn("Ошибка при получении названия статьи.");
            }

            Elements fontElements = infoCell.select("font");
            if (fontElements.size() > 1) {
                StringBuilder details = new StringBuilder();

                for (int i = 1; i < fontElements.size(); i++) { // TODO: тут i была равна 1...

                    String text = fontElements.get(i).text().trim();
                    if (!text.isEmpty()) {

                        if (details.length() > 0)
                            details.append(" ");

                        details.append(text);
                    }
                }

                setPublicationDetails(details.toString());

            } else {
                setPublicationDetails("Данные не найдены.");
                logger.warn("Ошибка при получении дополнительной информации о статье.");
            }

            Element citationsCell = row.select("td").get(2);
            if (citationsCell != null) {

                String citationsText = citationsCell.text().trim();

                try {
                    setCitations(Integer.parseInt(citationsText));
                    logger.info("Установлено количество цитирований для статье {}", titleLink.text());

                } catch (NumberFormatException e) {
                    setCitations(0);
                    logger.error("Ошибка при получении данных и цитировании. Установлено 0.");
                }
            } else {
                setCitations(0);
                logger.error("Ошибка при получении данных и цитировании. Установлено 0.");
            }

            return true;

        } catch (Exception e) {
            logger.error("Ошибка парсинга строки: {}", e.getMessage());
            return false;
        }
    }
}