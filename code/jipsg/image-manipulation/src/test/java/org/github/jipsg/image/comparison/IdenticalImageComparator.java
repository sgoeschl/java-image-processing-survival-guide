package org.github.jipsg.image.comparison;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Compare two images to check if they are identical - based on Apache PDFBox.
 * See https://github.com/sgoeschl/java-image-processing-survival-guide/raw/master/slides/jipsg.pdf
 */
public class IdenticalImageComparator {

    /**
     * Get the difference between two images, identical colors are set to white,
     * differences are xored, the highest bit of each color is reset to avoid
     * colors that are too light
     *
     * @param bim1
     * @param bim2
     * @return If the images are different, the function returns a diff image If
     * the images are identical, the function returns null If the size is
     * different, a black border on the bottom and the right is created
     * @throws IOException
     */
    public BufferedImage diffImages(BufferedImage bim1, BufferedImage bim2) throws IOException {
        int minWidth = Math.min(bim1.getWidth(), bim2.getWidth());
        int minHeight = Math.min(bim1.getHeight(), bim2.getHeight());
        int maxWidth = Math.max(bim1.getWidth(), bim2.getWidth());
        int maxHeight = Math.max(bim1.getHeight(), bim2.getHeight());
        BufferedImage bim3 = null;
        if (minWidth != maxWidth || minHeight != maxHeight) {
            bim3 = createEmptyDiffImage(minWidth, minHeight, maxWidth, maxHeight);
        }
        for (int x = 0; x < minWidth; ++x) {
            for (int y = 0; y < minHeight; ++y) {
                int rgb1 = bim1.getRGB(x, y);
                int rgb2 = bim2.getRGB(x, y);
                if (rgb1 != rgb2
                        // don't bother about differences of 1 color step
                        && (Math.abs((rgb1 & 0xFF) - (rgb2 & 0xFF)) > 1
                        || Math.abs(((rgb1 >> 8) & 0xFF) - ((rgb2 >> 8) & 0xFF)) > 1
                        || Math.abs(((rgb1 >> 16) & 0xFF) - ((rgb2 >> 16) & 0xFF)) > 1)) {
                    if (bim3 == null) {
                        bim3 = createEmptyDiffImage(minWidth, minHeight, maxWidth, maxHeight);
                    }
                    int r = Math.abs((rgb1 & 0xFF) - (rgb2 & 0xFF));
                    int g = Math.abs((rgb1 & 0xFF00) - (rgb2 & 0xFF00));
                    int b = Math.abs((rgb1 & 0xFF0000) - (rgb2 & 0xFF0000));
                    bim3.setRGB(x, y, 0xFFFFFF - (r | g | b));
                } else {
                    if (bim3 != null) {
                        bim3.setRGB(x, y, Color.WHITE.getRGB());
                    }
                }
            }
        }
        return bim3;
    }

    /**
     * Create an image; the part between the smaller and the larger image is
     * painted black, the rest in white
     *
     * @param minWidth width of the smaller image
     * @param minHeight width of the smaller image
     * @param maxWidth height of the larger image
     * @param maxHeight height of the larger image
     * @return
     */
    private BufferedImage createEmptyDiffImage(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        BufferedImage bim3 = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bim3.getGraphics();
        if (minWidth != maxWidth || minHeight != maxHeight) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, maxWidth, maxHeight);
        }
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, minWidth, minHeight);
        graphics.dispose();
        return bim3;
    }

}
