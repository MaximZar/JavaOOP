import java.net.*;

public class URLDepthPair {

  private String url;
  private int depth = 0;

  public URLDepthPair(String site, int num) {
    url = site;
    depth = num;
  }

  public String getURL() {
    return url;
  }
  public void setURL(String newUrl) {
    url = newUrl;
  }
  public int getDepth() {
    return depth;
  }
  public String toString() {
    return Integer.toString(depth) + '\t' + url;
  }
  public String getHost() {
    try {
      return (new URL(url)).getHost();
    } catch (MalformedURLException e) {
        System.err.println("MalformedURLException: " + e.getMessage());
        return null;
    }
  }
  public String getPath() {
    try {
      return (new URL(url)).getPath();
    }
    catch (MalformedURLException e) {
        System.err.println("MalformedURLException: " + e.getMessage());
        return null;
    }
  }
}