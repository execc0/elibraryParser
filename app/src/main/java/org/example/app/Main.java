package org.example.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.event.*;
import java.io.File;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Main extends Application {
    private static final double WINDOW_WIDTH_RATIO = 0.35;
    private static final double WINDOW_HEIGHT_RATIO = 0.35;

    private static final Logger logger = LogManager.getLogger(Main.class);

    private Text textPathToInputFile;
    private Text textPathToOutputFile;
    private Text textProcessedFile;

    private int authorsCount;
    private int authorsSuccessProcessed;

    @Override
    public void start(Stage stage) {
        logger.info("Приложение запущено");

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        double windowWidth = bounds.getWidth() * WINDOW_WIDTH_RATIO;
        double windowHeight = bounds.getHeight() * WINDOW_HEIGHT_RATIO;

        stage.setScene(new Scene(createContent(stage), windowWidth, windowHeight));
        stage.setTitle("Elibrary parser");
        stage.setMinHeight(windowHeight + 10);
        stage.setMinWidth(windowWidth);
        stage.show();
    }

    private Pane createContent(Stage stage) {
        Pane rootPane = new Pane();

        createTextContent(rootPane);
        createButtonNodesContent(rootPane, stage);

        return rootPane;
    }

    /**
     * Метод для создания нужных текстовых полей в приложении.
     *
     * <p>
     * Создает объекты класса {@link Text} с использованием специального метода {@link #createTextNode(String, double,  double)}.
     * </p>
     *
     * @since 2025-10-31
     */
    private void createTextContent(Pane pane) {
        try {
            Text textForInputFile = createTextNode("Path to file with author's UID:", 30, 30);
            textPathToInputFile = createTextNode("", 30, 50);

            Text textForOutputFile = createTextNode("The path to the results file:", 30, 90);
            textPathToOutputFile = createTextNode("", 30, 110);

            Text textProcessed = createTextNode("Processed unique identifiers:", 30, 150);
            textProcessedFile = createTextNode("", 30, 170);

            pane.getChildren().addAll(
                    textForInputFile, textForOutputFile, textProcessed,
                    textPathToInputFile, textPathToOutputFile, textProcessedFile);

            logger.info("Текстовые поля были созданы.");
        } catch (Exception e) {
            logger.warn("Текстовые поля не были созданы: {}", e.getMessage());
        }
    }

    /**
     * Метод для создания нужных кнопок в приложении.
     *
     * <p>
     * Создает объекты класса {@link Button} с использованием специального метода {@link #createButtonNode(String, double,  double)}.
     * </p>
     *
     * @since 2025-10-31
     */
    private void createButtonNodesContent(Pane pane, Stage stage) {
        try {
            Button buttonChooseInputFile = createButtonNode("Ch_oose file", 190, 12);
            buttonChooseInputFile.setOnAction(event -> handleInputFileSelection(stage));

            Button buttonChooseOutputFile = createButtonNode("_Choose file", 175, 72);
            buttonChooseOutputFile.setOnAction(event -> handleOutputFileSelection(stage));

            Button buttonStartProcessing = createButtonNode("Start Processing", 220, 260);
            buttonStartProcessing.setOnAction(event -> startFileProcessing());

            pane.getChildren().addAll(buttonChooseInputFile, buttonChooseOutputFile, buttonStartProcessing);

            logger.info("Кнопки были созданы.");
        } catch (Exception e) {
            logger.warn("Кнопки не были созданы: {}", e.getMessage());
        }
    }

    /**
     * Вспомогательный метод для создания объекта класса {@link Text}.
     *
     * <p>
     * Создает объект класса {@link Text} и устанавливает для него текст и положение.
     * </p>
     *
     * @since 2025-10-31
     * @param text текст, который будет показан пользователю.
     * @param x координата на оси OX для установки положения кнопки.
     * @param y координата на оси OY для установки положения кнопки.
     * @return возвращает объект класса {@link Text} с установленными значениями.
     */
    private Text createTextNode(String text, double x, double y) {
        Text textNode = new Text();
        textNode.setText(text);
        setTranslate(textNode,x, y);

        return textNode;
    }

    /**
     * Вспомогательный метод для создания объекта класса {@link Button}.
     *
     * <p>
     * Создает объект класса {@link Button} и устанавливает для него текст и положение.
     * </p>
     *
     * @since 2025-10-31
     * @param text текст на кнопке, который будет показан пользователю.
     * @param x координата на оси OX для установки положения кнопки.
     * @param y координата на оси OY для установки положения кнопки.
     * @return возвращает объект класса {@link Button} с установленными значениями.
     */
    private Button createButtonNode(String text, double x, double y) {
        Button button = new Button(text);
        setTranslate(button, x, y);

        return button;
    }

    /**
     * Метод, который вызывает при нажатии кнопки выбора файла с UID авторов.
     *
     * @since 2025-10-31
     */
    private void handleInputFileSelection(Stage stage) {
        File selectedFile = showFileChooser(stage, "Select input file", "Text files (*.txt)", "*.txt");

        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();

            if (!fileName.endsWith(".txt")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText("Внимание!");
                alert.setContentText("Формат файла должен быть txt!");
                alert.showAndWait();
                return;
            }

            textPathToInputFile.setText(selectedFile.getAbsolutePath());
            logger.info("Для получения информации выбран файл: {}", textPathToInputFile.getText());
        }
    }

    /**
     * Метод, который вызывается при нажатии кнопки выбора файла под результаты.
     *
     * @since 2025-10-31
     */
    private void handleOutputFileSelection(Stage stage) {
        File selectedFile = showFileChooser(stage, "Select output file", "JSON files (*.json)", "*.json");

        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();

            if (!fileName.endsWith(".json")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText("Внимание!");
                alert.setContentText("Формат файла должен быть json!");
                alert.showAndWait();
                return;
            }

            textPathToOutputFile.setText(selectedFile.getAbsolutePath());
            logger.info("Для вывода информации выбран файл: {}", textPathToOutputFile.getText());
        }
    }

    /**
     * Метод для вызова окна выбора файла.
     *
     * <p>
     * Создает объект класса {@link FileChooser} для выбора файла.
     * Устанавливает заголовок окна и поддерживаемые для выбора форматы.
     * </p>
     *
     * @since 2025-10-31
     */
    private File showFileChooser(Stage stage, String title, String filterDescription, String extension) {
        logger.info("Пользователь начал выбор файла.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filterDescription, extension);
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showOpenDialog(stage);
    }

    /**
     * Начинает процесс получения информации об авторах по их UID.
     * <p>
     * Проверяет, что текстовые поля {@link #textPathToInputFile} и {@link #textPathToOutputFile}
     * содержат пути, и после начинает процесс получения данных с сайта.
     * По окончании процесса сохраняет данные через {@link FileWorker#saveToFile(String, String)}.
     * </p>
     *
     * @since 2025-10-31
     */
    private void startFileProcessing() {
        if (textPathToInputFile.getText().trim().isEmpty() || textPathToOutputFile.getText().trim().isEmpty()) {
            logger.warn("Попытка начать поиск информации без указанных путей к файлам.");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Перед началом работы выбери оба файла!");
            alert.showAndWait();

            return;
        }
        try {
            logger.info("Начат процесс получения информации об авторах.");

            List<String> authors = FileWorker.readFile(textPathToInputFile.getText());
            authorsCount = authors.size();

            Map<String, AuthorData> authorsData = processAuthors(authors);

            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(authorsData);
                FileWorker.saveToFile(json, textPathToOutputFile.getText());
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при сохранении файла", e);
            }
        }  catch (Exception e) {
            logger.error("Критическая ошибка при выполнении приложения", e);
        }
    }

    /**
     * Устанавливает позицию {@link Node} элемента.
     *
     * @since 2025-10-31
     * @param x координата по оси OX.
     * @param y координата по оси OY.
     */
    private void setTranslate(Node node, double x, double y) {
        logger.info("Элемент {} перемещен на позицию x: {} y: {}", node, x, y);
        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    /**
     * Обрабатывает каждый authorID для получения подробной информации.
     * <p>
     * Перебирает все значения переданного списка и через класс {@link SiteParser}
     * получает подробную информацию о каждом авторе, сохраняя все это в
     * интерфейс Map, для дальнейшего удобного сохранения в json формат.
     * </p>
     *
     * @since 2025-10-25
     * @param authors список, содержащий authorID.
     * @return возвращает Map, содержащий authorID (ключ) и информацию о данном авторе
     * в виде объекта класса {@link AuthorData}.
     */
    protected Map<String, AuthorData> processAuthors(List<String> authors) {
        if (textProcessedFile != null)
            textProcessedFile.setText("");

        SiteParser siteParser = new SiteParser();

        Map<String, AuthorData> authorsData = authors.stream()
                .map(authorId -> {
                    try {
                        AuthorData authorData = new AuthorData();
                        authorData.setAuthorID(authorId);

                        boolean parseSuccess = siteParser.tryGetArticle(authorId, authorData);

                        if (textProcessedFile != null && parseSuccess) {
                            authorsSuccessProcessed += 1;
                            textProcessedFile.setText(authorsSuccessProcessed + "/" + authorsCount);
                        }

                        return parseSuccess ? new AbstractMap.SimpleEntry<>(authorId, authorData) : null;
                    } catch (Exception e) {
                        logger.error("Ошибка при обработке автора: {}", authorId, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .peek(entry -> logger.info("Обработан автор: {}", entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        if (textProcessedFile != null)
            textProcessedFile.setText(authorsData.size() + "/" + authors.size());

        logger.info("Обработано авторов: {}/{}", authorsData.size(), authors.size());
        return authorsData;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
