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

    /** Метод, который возвращает уникальный идентификатор автора в системе elibrary.*/
    public String getAuthorID() { return authorID; }
    /** Место, который устанавливает уникальный идентификатор автора в системе elibrary.*/
    public void setAuthorID(String authorID) { this.authorID = authorID; }

    /** Метод, который возвращает ФИО автора.*/
    public String getName() { return name; }
    /** Место, который устанавливает ФИО автора.*/
    public void setName(String name) { this.name = name; }

    /** Метод, который возвращает количество статей автора.*/
    public int getNumberOfArticles() { return numberOfArticles; }
    /** Место, который устанавливает количество статей автора.*/
    public void setNumberOfArticles(int articles) { this.numberOfArticles = articles; }

    /** Метод, который возвращает количество статей автора с 0 цитированием.*/
    public int getNumberOfEmptyArticles() { return numberOfEmptyArticles; }
    /** Место, который устанавливает количество статей автора с 0 цитированием.*/
    public void setNumberOfEmptyArticles(int emptyArticles) { this.numberOfEmptyArticles = emptyArticles; }

    /** Метод, который возвращает индекс Хирша автора.*/
    public int getHIndex() { return hIndex; }
    /** Место, который устанавливает индекс Хирша автора.*/
    public void setHIndex(int hIndex) { this.hIndex = hIndex; }

    /** Метод, который возвращает статьи автора с 0 цитированием.*/
    public List<Publication> getEmptyArticlesList() { return emptyArticlesList; }
    /** Место, который устанавливает статьи автора с 0 цитированием.*/
    public void setEmptyArticlesList(List<Publication> emptyArticlesList) { this.emptyArticlesList = emptyArticlesList; }
}
