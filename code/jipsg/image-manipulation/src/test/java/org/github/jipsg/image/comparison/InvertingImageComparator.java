package org.github.jipsg.image.comparison;

import org.github.jipsg.common.image.ops.InvertImageOp;

import java.awt.*;
import java.awt.image.*;

/**
 * Created by sgoeschl on 18/11/14.
 */
public class InvertingImageComparator {

    public BufferedImage compare(BufferedImage bim1, BufferedImage bim2) {

        // create an inverted image
        InvertImageOp invertImageOp = new InvertImageOp();
        BufferedImage invertedImage = invertImageOp.filter(bim1, null);

        // make a transparent image using white
        BufferedImage transparentImage = createTransparentImage(bim2, Color.white);

        final BufferedImage finalImage = new BufferedImage(bim1.getWidth(),bim1.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = finalImage.createGraphics();
        g.drawImage(invertedImage, 0, 0, null);
        g.drawImage(transparentImage, 0, 0, null);
        g.dispose();

        return finalImage;
    }

    /**
     * Make provided image transparent wherever color matches the provided color.
     *
     * @param im BufferedImage whose color will be made transparent.
     * @param color Color in provided image which will be made transparent.
     * @return Image with transparency applied.
     */
    public static BufferedImage createTransparentImage(final BufferedImage im, final Color color) {

        final ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for (white)... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFFFFFFFF;

            public final int filterRGB(final int x, final int y, final int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        final Image image = Toolkit.getDefaultToolkit().createImage(ip);

        final BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }
}
