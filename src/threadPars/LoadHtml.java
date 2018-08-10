package threadPars;

import Model.ListNews;
import Model.NewsData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadHtml implements Runnable {
    private String tempURL ;
    private ListNews listNews ;
    private int i ;
    private SimpleDateFormat format = new SimpleDateFormat();
    private Date newsDate = null;
    private String strHtml ="" ; // для тестов
    private String strLinkNews ="" ;
    private String strTitle = "";
    private String strDate ="";
   /// int numderNews = 1;

    public LoadHtml(String tempURL, ListNews listNews, int i){
        this.listNews = listNews;
        this.tempURL = tempURL;
        this.i = i;
    }
    @Override
    public void run() {

        // System.out.println("В методе LoadHtml");
        // считываем html по заданной URL со всеми вкладками.

        String strHtml="";
        String strLink = "";
        try
        {
            do {
                URL url = new URL(tempURL);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(),"UTF-8"));
                String str ;
                boolean writeHtml = false ;
                while((str = br.readLine())!=null){
                    if ( writeHtml ){
                        strHtml += str + "\n";
                        // вырезаем ссылку на следующую страницу
                        if (str.matches ( "(.*)link pagination__forward\"(.*)" )){
                            char[] link = str.toCharArray();
                            boolean writeLink = false ;
                            strLink = "" ;
                            for (int j=0 ; j < link.length ; j++ ){
                                if (j+5 < link.length && link[j]== 'h'&& link[j+1]== 't'&& link[j+2]== 't'&&
                                        link[j+3]== 'p'&& link[j+4]== 's'&& link[j+5]== ':'){
                                    writeLink = true ;
                                }
                                if (writeLink)
                                    if (link [j]== '"') writeLink = false ;
                                    else strLink +=link [j];

                            }
                        }
                    }
                    if (str.matches ( "(.*)articles-list(.*)" ))writeHtml = true ;
                    else if (str.matches ( "(.*)/ul(.*)" ))writeHtml = false ;
                }
                br.close();
                //System.out.println( strLink);
                tempURL = strLink ;// присваиваем ссылку на следующую страницу

            }while ( tempURL!="" );


        }catch(IOException e)
        {
            e.printStackTrace();
        }
        // распознаем полученный текст и складываем данные в коллекцию listNews
        // arrayHtml [i-1] = strHtml ;


        // System.out.println("В методе parse");
        format.applyPattern("dd MMMM yyyy,mm:hh");
        char[] c = strHtml.toCharArray();   // всю страницу загоняем в массив символов и с ним работаем
        boolean writeTitle = false;
        boolean writeDate = false;
        boolean writeStrTitle = false;
        boolean writeStrDate = false;
        boolean writeLinkNews = false;

        int x = 0;
        int x1 = 0;

        // вырезаем нужные на данные и добавляем их в коллекцию
        for (int j = 1; j < c.length; j++) {
            // Вырезаем заголовки
            if (c[j] == 'h' && c[j + 1] == '3' && c[j - 1] != '/') {
                writeTitle = true;
            }
                    if (writeTitle && c[j] == '/' && c[j + 1] == 'a') {
                        writeTitle = false;
                        writeStrTitle = false;
                        strTitle = strTitle.trim();
                    }
                    if (writeTitle && c[j] == '<') {
                        writeStrTitle = false;
                    }
                    if (writeTitle && writeStrTitle) strTitle += c[j];// пишем заголовок
                    if (writeTitle && c[j] == '>') {
                        writeStrTitle = true;
                    }
                    // вырезаем ссылку на новость
                    if (writeTitle) {
                        if (j + 5 < c.length && c[j] == 'h' && c[j + 1] == 't' && c[j + 2] == 't' &&
                                c[j + 3] == 'p' && c[j + 4] == 's' && c[j + 5] == ':') {
                            writeLinkNews = true;
                        }
                        if (writeLinkNews)
                            if (c[j] == '"') writeLinkNews = false;
                            else strLinkNews += c[j];

                    }
                    // вырезаем даты
                    if (j + 4 < c.length && c[j] == '_' && c[j + 1] == 'd' && c[j + 2] == 'a' && c[j + 3] == 't' && c[j + 4] == 'e') {
                        writeDate = true;
                    }
                    if (writeDate && c[j] == '<') {
                        writeStrDate = false;
                    }
                    if (writeDate && writeStrDate) strDate += c[j];// пишем дату
                    if (writeDate && c[j] == '>') {
                        writeStrDate = true;
                    }
                    // форматируем дату
                    if (writeDate && j + 3 < c.length && c[j] == '/' && c[j + 1] == 'd' && c[j + 2] == 'i' && c[j + 3] == 'v') {
                        writeDate = false;
                        writeStrDate = false;
                        strDate = strDate.replaceAll("&nbsp;", " ");
                        int poz1 = strDate.lastIndexOf('-');
                        strDate = strDate.substring(poz1 + 1);
                        strDate = strDate.trim();
                        //System.out.println(strDate);
                        try {
                            newsDate = format.parse(strDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // создаем новый элемент коллекции и наполняем его данными
                        NewsData newsData = new NewsData();
                        //newsData.setNumber(numderNews);
                        newsData.setStrTitle(strTitle);
                        newsData.setStrDate(strDate);
                        newsData.setNewsDate(newsDate);
                        newsData.setStrLinkNews(strLinkNews);

                       listNews.addListNews (newsData );


                        //listNews.add(newsData); // добавляем новость в список
                        //System.out.println(newsData);

                       // numderNews++;
                        strTitle = "";
                        strDate = "";
                        strLinkNews = "";
                    }
        }

    }


}

