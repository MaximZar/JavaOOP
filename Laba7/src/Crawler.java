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

    URLDepthPair startSite = new URLDepthPair(url, depth);

    LinkedList<URLDepthPair> sites = new LinkedList<URLDepthPair>();

    // LinkedList<URLDepthPair> process = getSites(startSite);
    
    LinkedList <URLDepthPair> inQueueURLs = new LinkedList <URLDepthPair>();
    LinkedList <URLDepthPair> processedURLs = new LinkedList <URLDepthPair>();
    ArrayList<String> seenURLs = new ArrayList<String>();
    int maxDepth = 0;
    inQueueURLs.add(new URLDepthPair(args[0],0));
    seenURLs.add(args[0]);

    while (inQueueURLs.size() != 0){
      URLDepthPair pair = inQueueURLs.pop();
      processedURLs.add(pair);
      int depth1 = pair.getDepth();
      ArrayList<String> links = new ArrayList<String>();
      links = getSites(pair);
      if(maxDepth > depth1){
        for (String link:links){
            if(!seenURLs.contains(link)){
              inQueueURLs.add(new URLDepthPair(link,depth1 + 1));
              seenURLs.add(link);
          }
        }
      }
    }
    // while(process.size() > 0) {
      
    //   URLDepthPair localSite = process.pop();
    //   if (localSite.getURL().startsWith("/")) localSite.setURL(startSite.getURL() + localSite.getURL().substring(1));
    //   sites.add(localSite);
    //   LinkedList<URLDepthPair> localProcess = getSites(localSite);
    //   System.out.println("LLLL: " + localSite.getURL());
    //   process.addAll(localProcess);
    //   sites.addAll(process);
    // }

    // for (URLDepthPair pair:sites) {
    //   int depthForUsers = Math.abs(pair.getDepth() - depth);
    //   System.out.println(depthForUsers + "\t" + pair.getURL());
    // } 
    for (String web:seenURLs) {
      System.out.println(web);
    }

  }

  public static ArrayList<String> getSites(URLDepthPair site) {
    ArrayList<String> urls = new ArrayList<String>();

    // подключаемся к сайту
    String host = site.getHost();
    String path = site.getPath();
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
      String string;
      int beginIndex = 0;
      int endIndex = 0;

      try {
        string = inLines.readLine();
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
        return urls;
      }
      if(string == null) return urls;
      while (true){
        beginIndex = string.indexOf(linkHTMLStart,beginIndex);
        if(beginIndex == -1)break;
        beginIndex += linkHTMLStart.length();
        endIndex = string.indexOf(linkHTMLEnd, beginIndex);
        urls.add(string.substring(beginIndex,endIndex));
        beginIndex = endIndex;
      }
    }

  }
}