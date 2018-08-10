package Controller;

import Main.mainParse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

public class ControllerMenu {

    private mainParse main;

    public ControllerMenu() {
    }

    /**
     * Вызывается главным приложением, чтобы оставить ссылку на самого себя.
     *
     */

    public void setMainParse (mainParse main) {
        this.main = main;
    }

    @FXML
    private void handleSaveAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Задаём фильтр расширений
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Показываем диалог сохранения файла
        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null) {
            // Убедитесь, что он имеет правильное расширение
            if (!file.getPath().endsWith(".txt")) {
                file = new File(file.getPath() + ".txt");
            }
            main.saveListNewsToFile(file);
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Парсер Корреспондент");
        alert.setHeaderText("Учебная программа");
        alert.setContentText("Автор Блажейко Александр\nПрограмма скачивает с новостного сайта\n" +
                "новости с 1 по 5 июня 2018 года \n" +
                "новости по каждой дате скачивает и распознает свой поток (всего 5 потоков)\n" +
                "парсер вырезает заголовки и даты новостей и складывает в упорядоченный список ");

        alert.showAndWait();
    }
    @FXML
    void initialize() {

    }
}
