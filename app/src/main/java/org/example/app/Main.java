package org.example.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Приложение запущено");

        try {
            while (true) {
                String authorsFilePath = getValidFilePath("Введите абсолютный путь до файла с authorsID или q для выхода: ");
                if (authorsFilePath.equals("q"))
                    break;

                List<String> authors = FileWorker.readFile(authorsFilePath);

                while (authors.isEmpty()) {
                    System.out.println("Не удалось прочитать авторов из файла: " + authorsFilePath);
                    logger.warn("Не удалось прочитать авторов из файла: {}", authorsFilePath);
                    authorsFilePath = getValidFilePath("Введите абсолютный путь до файла с authorsID: ");
                    authors = FileWorker.readFile(authorsFilePath);
                }

                Map<String, AuthorData> authorsData = processAuthors(authors);

                String resultFilePath = getValidFilePath("Введите абсолютный путь до файла c результатами или q для выхода: ");
                if (resultFilePath.equals("q"))
                    break;

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(authorsData);

                FileWorker.saveToFile(json, resultFilePath);
            }
        } catch (Exception e) {
            logger.error("Критическая ошибка при выполнении приложения", e);
        } finally {
            logger.info("Выполнение приложения завершено");
        }
    }

    /**
     * Возвращает строку, содержащую путь, который ввел пользователь.
     * <p>
     * Выводит пользователю строку, переданную в параметре text, и считывает то,
     * что будет введено им после этого. Проверяет строку на количество символов,
     * чтобы не допустить пустую, как путь к файлу.
     * </p>
     *
     * @param text текст, который будет выведен для пользователя перед вводом.
     * @return строка, содержащая путь.
     * @since 2025-10-25
     */
    protected static String getValidFilePath(String text) {
        if (text == null)
            text = "";

        Scanner scanner = new Scanner(System.in);
        String filePath;

        do {
            logger.info("Пользователю выведено: {}", text);
            System.out.print(text);
            filePath = scanner.nextLine().trim();

            if (filePath.isEmpty()) {
                logger.warn("Путь не может быть пустым");
                System.out.println("Путь не может быть пустым");
            }
        } while (filePath.isEmpty());

        return filePath;
    }

    /**
     * Обрабатывает каждый authorID для получения подробной информации.
     * <p>
     * Перебирает все значения переданного списка и через класс {@link SiteParser}
     * получает подробную информацию о каждом авторе, сохраняя все это в
     * интерфейс Map, для дальнейшего удобного сохранения в json формат.
     * </p>
     *
     * @param authors список, содержащий authorID.
     * @return возвращает Map, содержащий authorID (ключ) и информацию о данном авторе
     * в виде объекта класса {@link AuthorData}.
     * @since 2025-10-25
     */
    protected static Map<String, AuthorData> processAuthors(List<String> authors) {
        SiteParser siteParser = new SiteParser();
        Map<String, AuthorData> authorsData = new HashMap<>();

        AuthorData authorData;
        for (String authorId : authors) {
            try {
                authorData = new AuthorData();
                authorData.setAuthorID(authorId);

                boolean parseSuccess = siteParser.tryGetArticle(authorId, authorData);

                if (parseSuccess) {
                    authorsData.put(authorId, authorData);
                    logger.info("Обработан автор: {}", authorId);
                } else {
                    logger.warn("Не удалось обработать автора: {}", authorId);
                }

            } catch (Exception e) {
                logger.error("Ошибка при обработке автора: {}", authorId, e);
            }
        }

        logger.info("Обработано авторов: {}/{}", authorsData.size(), authors.size());
        return authorsData;
    }
}