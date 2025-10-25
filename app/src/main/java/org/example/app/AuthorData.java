import java.util.ArrayList;
import java.util.List;

package org.example.app;

public class AuthorData {
    private String authorID;
    private String name;
    private int articles;
    private int emptyArticles;
    private int hIndex;
    private List<Publication> emptyArticlesList = new ArrayList<>();

    public String getAuthorID() { return authorID; }
    public void setAuthorID(String authorID) { this.authorID = authorID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getArticles() { return articles; }
    public void setArticles(int articles) { this.articles = articles; }

    public int getEmptyArticles() { return emptyArticles; }
    public void setEmptyArticles(int emptyArticles) { this.emptyArticles = emptyArticles; }

    public int getHIndex() { return hIndex; }
    public void setHIndex(int hIndex) { this.hIndex = hIndex; }

    public List<Publication> getEmptyArticlesList() { return emptyArticlesList; }
    public void setEmptyArticlesList(List<Publication> emptyArticlesList) { this.emptyArticlesList = emptyArticlesList; }
}
