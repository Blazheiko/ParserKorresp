package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;



public class ListNews {
    private  ObservableList<NewsData> listNews = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private volatile int сount = 1;

    // для потоков которые добавляют данные
    public synchronized void addListNews (NewsData newsData){
        newsData.setNumber(сount);
        listNews.add(newsData);
        сount++;
    }
    // для таблицы TableView которорая отображает данные
    public ObservableList getListNews (){
        return listNews;
    }

    // сортируем новости по дате
   public void sortListNews (){
       Collections.sort(listNews);
   }

}
