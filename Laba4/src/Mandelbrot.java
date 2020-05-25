import java.awt.geom.Rectangle2D;

class Mandelbrot extends FractalGenerator {

  public static final int MAX_ITERATIONS = 2000;

  public void getInitialRange(Rectangle2D.Double range) {
    range.x = -2;
    range.y = -1.5;
    range.width = 3;
    range.height = 3;
    return;
  }

  public int numIterations(double x, double y) {
    int iteration = 0;
    double zR = 0;
    double zI = 0;
    double square = zR * zR + zI * zI;
    
    while (iteration < MAX_ITERATIONS && square < 4) {
      double zRNew = zR * zR - zI * zI + x;
      double zINew = 2 * zR * zI + y;
      zR = zRNew;
      zI = zINew;
      square = zR * zR + zI * zI;
      iteration += 1;
    }
    
    if (iteration == MAX_ITERATIONS) return -1;
    return iteration;
  }
}