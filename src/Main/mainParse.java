package Main;

import Controller.ControllerList;
import Controller.ControllerMenu;
import Model.ListNews;
import Model.NewsData;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import threadPars.LoadHtml;
import java.io.*;
import java.util.*;

public class mainParse extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;


    List<Thread> threads = new ArrayList<>();
    ListNews listNews = new ListNews();


    public mainParse() {
        // пустой конструктор
    }

     //Возвращает данные в виде наблюдаемого списка новостей.

    public ObservableList<NewsData> getListNews() {
        return listNews.getListNews();
    }

    //Возвращает главную сцену.

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Сайт Корреспондент новости с 1 по 15 июня");
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1200);


        // Устанавливаем иконку приложения.
        this.primaryStage.getIcons().add(new Image("file:src/image/archive.png"));

        initControllerMenu();
        showlistNews();
        // запускаем потоки которые скачивают и парсят новости каждый по своей дате
        try {
            createThreads();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void initControllerMenu() {
        try {
            // Загружаем корневой макет из view файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(mainParse.class.getResource("/view/menus.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout,1200,800);
            primaryStage.setScene(scene);

            // Даём контроллеру доступ к главному прилодению.
            ControllerMenu controller = loader.getController();
            controller.setMainParse(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Показывает в корневом макете сведения о персонале
     */
    public void showlistNews() {
        try {
            // Загружажаем таблицу с новостями.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(mainParse.class.getResource("/view/viewListNews.fxml"));
            AnchorPane ListNew = (AnchorPane)loader.load();

            // Помещаем таблицу с новостями в центр корневого макета.

            rootLayout.setCenter(ListNew);

            // Даём контроллеру доступ к главному приложению.
         ControllerList controller = loader.getController();
         controller.setMainParse(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createThreads () throws InterruptedException {

        for (int i = 1 ; i <= 5 ; i++) {
            System.out.println("В цыкле createThreads");
            String korrespondentURL = "https://korrespondent.net/all/2018/june/";
            korrespondentURL = korrespondentURL + i + "/";
            threads.add(new Thread (new LoadHtml(korrespondentURL,listNews,i)));

        }
        for (Thread thread: threads) {
            thread.start();// стартуем все потоки
        }
       /* for (Thread thread: threads) {
            thread.join(); // ждем пока все потоки выполнятся
        }*/

    }


     // Сохраняет текущую информацию заголовки и даты новостей в указанном файле.

    public void saveListNewsToFile(File file) {

        // конвертируем данные listNews в строку
        String string = "";

        for ( var newsData : listNews.getListNews()){

            string = string + newsData.toString()+ "\n";

        }

        // записываем строку в текстовый файл
        try (PrintWriter filenew = new PrintWriter(file.getPath(), "Cp1251")){
            filenew.println(string);
            filenew.close();

        } catch (FileNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!!!");
            alert.setHeaderText("Файл не сохранен!!!");
            alert.setContentText("Данные:\n" + file.getPath()+"не сохранились");

            alert.showAndWait();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        launch(args);
         }
}
