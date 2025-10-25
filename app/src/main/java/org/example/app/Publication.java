import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

package org.example.app;

public class Publication {
    private String title;
    private int citations;
    private String publicationDetails;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getCitations() { return citations; }
    public void setCitations(int citations) { this.citations = citations; }

    public String getPublicationDetails() { return publicationDetails; }
    public void setPublicationDetails(String publicationDetails) { this.publicationDetails = publicationDetails; }

    private static final Logger logger = LogManager.getLogger(Publication.class);

    public boolean parsePublicationRow(Element row) {
        try {
            Element infoCell = row.select("td").get(1);

            Element titleLink = infoCell.selectFirst("a[href*=id] b");
            if (titleLink != null) {
                setTitle(titleLink.text());
            }

            Elements fontElements = infoCell.select("font");
            if (fontElements.size() > 1) {
                StringBuilder details = new StringBuilder();
                for (int i = 1; i < fontElements.size(); i++) {
                    String text = fontElements.get(i).text().trim();
                    if (!text.isEmpty()) {
                        if (details.length() > 0) details.append(" ");
                        details.append(text);
                    }
                }
                setPublicationDetails(details.toString());
            }

            Element citationsCell = row.select("td").get(2);
            if (citationsCell != null) {
                String citationsText = citationsCell.text().trim();
                try {
                    setCitations(Integer.parseInt(citationsText));
                } catch (NumberFormatException e) {
                    setCitations(0);
                }
            }

            return true;

        } catch (Exception e) {
            logger.error("Ошибка парсинга строки: {}", e.getMessage());
            return false;
        }
    }
}