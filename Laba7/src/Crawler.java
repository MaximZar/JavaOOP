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
    // с этого начинаем скан
    int depth = 0;
    String url;
    
    try {
      url = args[0];
      depth = Integer.parseInt(args[1]);
    } catch (NumberFormatException nfe) {
      System.out.println("usage: java Crawler <URL> <depth>");
      return;
    }

    URLDepthPair startSite = new URLDepthPair(url, 0);    
    ArrayList<URLDepthPair> sites = new ArrayList<URLDepthPair>();
    sites.add(startSite);

    // первая глубина
    ArrayList<String> process = getSites(startSite);
    for (String site:process) {
      URLDepthPair pair = new URLDepthPair(site, 1);
      if (!sites.contains(pair)) sites.add(pair);
    }

    int depthCounter = 2;
    while (depthCounter <= depth) {
      ArrayList<String> allSitesOnDepth = new ArrayList<String>();
      for (String siteOnDepth:process) {
        URLDepthPair siteOnDepthPair = new URLDepthPair(siteOnDepth, depthCounter);
        ArrayList<String> sitesOfSiteOnDepth = getSites(siteOnDepthPair);
        for (String tmp:sitesOfSiteOnDepth) {
          if (!allSitesOnDepth.contains(tmp)) allSitesOnDepth.add(tmp);
        }
      }
      for (String site:allSitesOnDepth) {
        if (!sites.contains(site)) sites.add(new URLDepthPair(site, depthCounter));
      }

      depthCounter += 1;
    }

    for (URLDepthPair site:sites) {
      System.out.println(site.getDepth() + "\t" + site.getURL());
    }
  }

  public static ArrayList<String> getSites(URLDepthPair siteForAnalysis) {
    ArrayList<String> urls = new ArrayList<String>();

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
      connection.setSoTimeout(2000);
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
    printWr.println("host: " + host);
    printWr.println("connection: close");
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
    // удаляем ссылки, которые не являются сайтами
    // например tel
    ArrayList<String> beautiUrls = new ArrayList<String>();
    for (String site:urls) {
      if (site.startsWith("http")) beautiUrls.add(site);
    }

    return beautiUrls;
  }
}