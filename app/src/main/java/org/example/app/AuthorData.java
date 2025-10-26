package org.example.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения информации об авторе.
 *
 * @since 2025-10-26
 */
public class AuthorData {
    /** Уникальный идентификатор автора в системе elibrary. */
    private String authorID;
    /** ФИО автора. */
    private String name;
    /** Количество статей автора. */
    private int numberOfArticles;
    /** Количество статей автора с 0 цитированием. */
    private int numberOfEmptyArticles;
    /** Индекс Хирша автора. */
    private int hIndex;
    /** Статьи автора с 0 цитированием. */
    private List<Publication> emptyArticlesList = new ArrayList<>();

    public String getAuthorID() { return authorID; }
    public void setAuthorID(String authorID) { this.authorID = authorID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getNumberOfArticles() { return numberOfArticles; }
    public void setNumberOfArticles(int articles) { this.numberOfArticles = articles; }

    public int getNumberOfEmptyArticles() { return numberOfEmptyArticles; }
    public void setNumberOfEmptyArticles(int emptyArticles) { this.numberOfEmptyArticles = emptyArticles; }

    public int getHIndex() { return hIndex; }
    public void setHIndex(int hIndex) { this.hIndex = hIndex; }

    public List<Publication> getEmptyArticlesList() { return emptyArticlesList; }
    public void setEmptyArticlesList(List<Publication> emptyArticlesList) { this.emptyArticlesList = emptyArticlesList; }
}
