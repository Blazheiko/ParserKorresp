package Model;

import javafx.beans.property.*;
import java.util.Date;

public class NewsData implements Comparable<NewsData> {
    private IntegerProperty number ;
    private  StringProperty strTitle ;
    private  StringProperty strDate;
    private  ObjectProperty<Date> newsDate;
    private  StringProperty strLinkNews;

    public NewsData(Integer number , String strTitle, String strDate, Date newsDate, String strLinkNews) {
        this.number = new SimpleIntegerProperty(number);
        this.strTitle = new SimpleStringProperty(strTitle);
        this.strDate = new SimpleStringProperty (strDate);
        this.newsDate = new SimpleObjectProperty<>(newsDate);
        this.strLinkNews = new SimpleStringProperty (strLinkNews);
    }

    public NewsData() {
      this ( 0,null,null,null,null) ;
    }

    public String getStrTitle() {
        return strTitle.get();
    }

    public StringProperty strTitleProperty() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle.set(strTitle);
    }

    public String getStrDate() {
        return strDate.get();
    }

    public StringProperty strDateProperty() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate.set(strDate);
    }

    public Date getNewsDate() {
        return newsDate.get();
    }

    public ObjectProperty<Date> newsDateProperty() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate.set(newsDate);
    }

    public String getStrLinkNews() {
        return strLinkNews.get();
    }

    public StringProperty strLinkNewsProperty() {
        return strLinkNews;
    }

    public void setStrLinkNews(String strLinkNews) {
        this.strLinkNews.set(strLinkNews);
    }

    public Integer getNumber() {
        return number.get();
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number.set(number);
    }




    @Override
    // переопределяем тустринг для удобства отладки
    public String toString() {
        return "№" + getNumber()+ " . "+
                "Заглавие - " + getStrTitle() + "\n"+
                "Дата - " + getStrDate() +"\n"+
                "Дата формат Date - " + getNewsDate() +"\n"+
                "ссылка на новость - " + getStrLinkNews() +"\n";
    }

    @Override
    public int compareTo(NewsData o) {
        if (getNewsDate() == null || o.getNewsDate() == null)
            return 0;
        return getNewsDate().compareTo(o.getNewsDate());
    }


  /*  @Override
    public int compare(NewsData o1, NewsData o2) {
        return o1.getNewsDate().compareTo(o2.getNewsDate());
    } */
}
