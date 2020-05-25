import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.JComponent;

class JImageDisplay extends JComponent {
  
  private static final long serialVersionUID = 1L;
  private BufferedImage buffer;

  public JImageDisplay(int width, int height) {
    buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    super.setPreferredSize(new Dimension(width, height));
    return;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(buffer, 0, 0, buffer.getWidth(), buffer.getHeight(), null);
    return;
  }

  public void clearImage() {
    int width = getWidth();
    int height = getHeight();
    int[] rgbArray = new int[width * height];
    buffer.setRGB(0, 0, width, height, rgbArray, 0, 1);
    return;
  }

  public void drawPixel(int x, int y, int rgbColor) {
      buffer.setRGB(x, y, rgbColor);
      return;
  }
}