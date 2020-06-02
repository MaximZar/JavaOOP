import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

class Crawler {

  private static final String linkHTMLStart = "a href=\"";
  private static final String linkHTMLEnd = "\"";

  public static void main(String args[]) throws UnknownHostException, IOException {
    String url;
    int depth = 0;
    int numberOfInputThreads;

    // проверка на ошибки
    if (args.length != 3)
      return;
    try {
      url = args[0];
      depth = Integer.parseInt(args[1]);
      numberOfInputThreads = Integer.parseInt(args[2]);
    } catch (NumberFormatException nfe) {
      System.out.println("usage: java Crawler <URL> <depth> <number of crawler threads>");
      return;
    }

    // обозначаем пул
    URLDepthPair startSite = new URLDepthPair(url, 0);
    URLPool sites = new URLPool(depth);
    sites.addURL(startSite);

    // даем потокам задание и ждем выполнения
    int countActiveThreadOnStart = Thread.activeCount();
    int waitingThreads = sites.getWaitingThreads();
    while (waitingThreads <= numberOfInputThreads) {
      int countThreadsNow = Thread.activeCount() - countActiveThreadOnStart;
      // создаем новый поток если не дошли до нужного количества
      // иначе проверяем а не закончили ли выполняться все потоки
      // если закончили - показываем сайты и завершаем программу
      if (countThreadsNow < numberOfInputThreads) {
        new Thread(new CrawlerTask(sites)).start();
      } else if (waitingThreads == numberOfInputThreads) {
          for (URLDepthPair pair:sites.getFinishedList()) {
            System.out.println(pair);
          }
          System.exit(1);
      }
      waitingThreads = sites.getWaitingThreads();
    }
  }

  public static LinkedList<String> getSites(URLDepthPair siteForAnalysis) {
    LinkedList<String> urls = new LinkedList<String>();
    // подключаемся к сайту
    String host = siteForAnalysis.getHost();
    String path = siteForAnalysis.getPath();
    Socket connection;
    try {
      connection = new Socket(host, 80);
    } catch (UnknownHostException e) {
      System.err.println("UnknownHostException: " + e.getMessage());
      return urls;
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
      return urls;
    }
    try {
      connection.setSoTimeout(3000);
    } catch (SocketException e) {
        System.err.println("SocketException: " + e.getMessage());
        return urls;
    }
    
    // делаем запрос
    OutputStream outputStream;
    try {
      outputStream = connection.getOutputStream();
    } catch (IOException exce) {
      System.err.println("IOException: " + exce.getMessage());
      return urls;
    }
    PrintWriter printWr = new PrintWriter(outputStream, true);
    printWr.println("GET " + path + " HTTP/1.1");
    printWr.println("Host: " + host);
    printWr.println("Connection: close");
    printWr.println();

    // получаем страницу
    InputStream inputStream;
    try {
      inputStream = connection.getInputStream();
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
      return urls;
    }
    InputStreamReader in = new InputStreamReader (inputStream);
    BufferedReader inLines = new BufferedReader(in);

    return searchLinks(inLines, urls, siteForAnalysis);
  }
  public static LinkedList<String> searchLinks(BufferedReader inLines, 
    LinkedList<String> urls, URLDepthPair siteForAnalysis) {
    // ищем все link`и
    while(true) {
      String line;
      int beginIndex = 0;
      int endIndex = 0;
      
      try {
        line = inLines.readLine();
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
        break;
      }

      if(line == null) break;
      while (true){
        beginIndex = line.indexOf(linkHTMLStart,beginIndex);
        if(beginIndex == -1) break;
        beginIndex += linkHTMLStart.length();
        endIndex = line.indexOf(linkHTMLEnd, beginIndex);
        urls.add(line.substring(beginIndex,endIndex));
        beginIndex = endIndex;
      }
    }
    // проверяем правильность введения ссылки (чтобы потом можно было смотреть на следующей глубине)
    // ссылки формата /courses приводим к формату ...hexlet.io/courses
    for (int i = 0; i < urls.size(); i += 1) {
      if (urls.get(i).startsWith("/")) {
        String rebuildSite = siteForAnalysis.getURL() + urls.get(i).substring(1);
        urls.set(i, rebuildSite);
      }  
    }
    return urls;
  }

}