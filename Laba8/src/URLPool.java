import java.util.LinkedList;

public class URLPool {

  public LinkedList<URLDepthPair> finished;
  private LinkedList<URLDepthPair> unfinished;
  public int waitingThreads;
  public int maxDepth;
  
  public URLPool(int mD) {
    waitingThreads = 0;
    maxDepth = mD;
    unfinished = new LinkedList<URLDepthPair>();
    finished = new LinkedList<URLDepthPair>();
  }

  public synchronized void addURL(URLDepthPair pair) {
    unfinished.addLast(pair);
    if (waitingThreads > 0) waitingThreads--;
    this.notify();
  }
  public synchronized URLDepthPair getPair() {
    if (unfinished.size() == 0) {
      waitingThreads += 1;
      try {
        this.wait();
      } catch (InterruptedException e) {
        System.err.println("MalformedURLException: " + e.getMessage());
        return null;
      }
    } 
    URLDepthPair depthPair = unfinished.removeFirst();
    finished.add(depthPair);
    return depthPair;
  }
  public synchronized int getMaxDepth() {
    return maxDepth;
  }
  public synchronized int getWaitingThreads() {
    return waitingThreads;
  }
  public synchronized LinkedList<URLDepthPair> getFinishedList() {
    return finished;
  }
  
}