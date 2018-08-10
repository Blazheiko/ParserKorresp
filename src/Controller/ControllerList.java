package Controller;

import Main.mainParse;
import Model.NewsData;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import java.util.ResourceBundle;


public class ControllerList {

    private mainParse mainPars;

   public ControllerList() { }

    /*
     * Вызывается главным приложением, чтобы оставить ссылку на самого себя
     */



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea textNews;

    @FXML
    private TableColumn<NewsData, Integer> columnNumber;


    @FXML
    private TableView<NewsData> tableNews;

    @FXML
    private TableColumn<NewsData, String> columnDate;

    @FXML
    private TableColumn<NewsData, String> columnTitle;

    @FXML
    void initialize() {
        //вносим данные с listNews в таблицу первый способ
        //columnDate.setCellValueFactory(new PropertyValueFactory<NewsData, String>("strDate"));
        //columnTitle.setCellValueFactory(new PropertyValueFactory<NewsData, String>("strTitle"));
        // tableNews.setItems(mainPars.getListNews());

        // вносим данные с listNews в таблицу второй способ

        //columnNumber.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        columnNumber.setCellValueFactory(cellData ->
                cellData.getValue().numberProperty().asObject());
        columnDate.setCellValueFactory(cellData -> cellData.getValue().strDateProperty());
        columnTitle.setCellValueFactory(cellData -> cellData.getValue().strTitleProperty());


        // следим за выбором новости в таблице и загружаем ее
       tableNews.getSelectionModel().selectedItemProperty().addListener(
              (observable, oldValue, newValue) -> textNews.setText(loadNews(newValue.getStrLinkNews())));



    }

    /*
     * Вызывается главным приложением, которое даёт на себя ссылку.
     */
    public void setMainParse (mainParse mainPars) {
        this.mainPars = mainPars;

        // Добавление в таблицу данных из наблюдаемого списка
        tableNews.setItems(mainPars.getListNews());
    }
    // считываем текст определенной новости.
    public  String loadNews(String tempURL){
        String strHtml="";
        String strNews ="";

        try
        {
            URL url = new URL(tempURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(),"UTF-8"));
            String str ;
            boolean writeHtml = false ;
            while((str = br.readLine())!=null){
                if (str.matches ( "(.*)<em>(.*)" ))writeHtml = false ;
                if ( writeHtml )strHtml += str + "\n";
                if (str.matches ( "(.*)\"post-item__text\"(.*)" ))writeHtml = true ;
            }
            br.close();

        }catch(IOException e)
        {
            e.printStackTrace();
        }

        // вырезаем из текста лишние символы и javascript
        char [] news = strHtml.toCharArray();
        boolean writeStrNews = false ;
        boolean script = false ;
        int counter = 0 ;

        for (int i = 0 ; i< news.length ; i++){
            if (news[i] == '<') {
                writeStrNews = false ;
                counter = 0 ;
            }
            // ищем в тексте javascript
            if (i+6 < news.length && news[i] == '<'&& news[i+1] == 's'&& news[i+2] == 'c'&& news[i+3] == 'r'&& news[i+4] == 'i'&& news[i+5] == 'p'
                    && news[i+6] == 't') script = true ;
            if (i+7 < news.length && news[i] == '<'&& news[i+1] == '/'&& news[i+2] == 's'&& news[i+3] == 'c'&& news[i+4] == 'r'&& news[i+5] == 'i'&& news[i+6] == 'p'
                    && news[i+7] == 't') script = false ;
            // пишем текст новости
            if (writeStrNews && !script ) {
                strNews += news[i];
                counter ++ ;
                if (counter >120 && (news[i] == ' '||news[i] == '.' ||news[i] == ','||news[i] == '!'||news[i] == '?')){
                    strNews += "\n"; // если строчка длинная то вставляем символ переноса строки
                    counter = 0 ;
                }
            }
            if (news[i] == '>') writeStrNews = true ;
        }
        return strNews ;
    }


}