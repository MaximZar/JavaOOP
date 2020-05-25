import java.awt.geom.Rectangle2D;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class FractalExplorer {
    private int dimensionDisplay;
    private JImageDisplay image;
    private FractalGenerator generator;
    private Rectangle2D.Double complexRange;

    public static void main() {
        FractalExplorer start = new FractalExplorer(800);
        start.createAndShowGUI();
        start.drawFractal();
    }

    public FractalExplorer(int dimension) {
        dimensionDisplay = dimension;
        complexRange = new Rectangle2D.Double();
        generator = new Mandelbrot();
        generator.getInitialRange(complexRange);
        image = new JImageDisplay(dimensionDisplay, dimensionDisplay);
        return;
    }

    public void createAndShowGUI() {
        image.setLayout(new BorderLayout());

        // основная картинка в центер 
        JFrame frame = new JFrame("FractalExp");
        frame.add(image, BorderLayout.CENTER);
        
        JButton resetImageButton = new JButton("Reset Display");
        
        // кнопка вниз
        Reset handler = new Reset();
        resetImageButton.addActionListener(handler);
        frame.add(resetImageButton, BorderLayout.SOUTH);

        Mouse click = new Mouse();
        image.addMouseListener(click);
        
        // кнопка выхода
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        return;
    } 

    private void drawFractal() {
        for (int x = 0; x < dimensionDisplay; x+= 1){
            for (int y = 0; y < dimensionDisplay; y+= 1){
                double xCoord = generator.getCoord(complexRange.x, complexRange.x + complexRange.width, dimensionDisplay, x);
                double yCoord = generator.getCoord(complexRange.y, complexRange.y + complexRange.height, dimensionDisplay, y);
                int numIters = generator.numIterations(xCoord, yCoord);

                if (numIters == -1) {
                    image.drawPixel(x, y, 0);
                } else {
                    float hue = 0.7f + (float) numIters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    image.drawPixel(x, y, rgbColor);
                }

            }
        }
        image.repaint();
    }

    class Reset implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            generator.getInitialRange(complexRange);
            drawFractal();
        }
    }

    class Mouse extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            double xCoord = generator.getCoord(complexRange.x, complexRange.x + complexRange.width, dimensionDisplay, x);
            
            int y = e.getY();
            double yCoord = generator.getCoord(complexRange.y, complexRange.y + complexRange.height, dimensionDisplay, y);
            
            generator.recenterAndZoomRange(complexRange, xCoord, yCoord, 0.5);
            
            drawFractal();
        }
    }

}