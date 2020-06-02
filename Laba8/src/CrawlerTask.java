import java.util.*;

public class CrawlerTask implements Runnable {

  public URLPool workPool;

  public CrawlerTask(URLPool linkOnPool) {
    workPool = linkOnPool;
  }

  public void run() {
    URLDepthPair workPair = workPool.getPair(); // получаем пару
    LinkedList<String> linksList = Crawler.getSites(workPair); // получаем ссылки со страницы
    int depth = workPair.getDepth() + 1;
    if (depth > workPool.getMaxDepth()) return; // проверяем не дошли ли до необходимого уровня
    for (String url:linksList) { // добавление всех сайтов для последующей обработки
      workPool.addURL(new URLDepthPair(url, depth));
    }
  }

}