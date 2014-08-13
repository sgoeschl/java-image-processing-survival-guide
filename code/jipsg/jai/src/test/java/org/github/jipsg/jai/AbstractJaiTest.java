package org.github.jipsg.jai;

import org.github.jipsg.common.AbstractImageTest;
import org.github.jipsg.common.image.BufferedImageUtils;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationBilinear;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Base class for testing Java Advanced Imaging.
 */
public class AbstractJaiTest extends AbstractImageTest {

    @Override
    public void setup() {
        super.setModuleName("jai");
        super.setup();
    }

    @Override
    public BufferedImage createBufferedImage(File file) throws Exception {
        return ImageIO.read(file);
    }

    @Override
    public void writeBufferedImage(BufferedImage bufferedImage, String formatName, File file) throws Exception {
        ImageIO.write(bufferedImage, formatName, file);
    }

    /**
     * Some quick and dirty image scaling - please note that for best performance
     * and quality you should use image rescaling libraries.
     */
    @Override
    public BufferedImage resample(BufferedImage bufferedImage, int width, int height) {

        Dimension imageDimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
        Dimension boundaryDimension = new Dimension(width, height);
        Dimension scaledDimension = BufferedImageUtils.getScaledDimension(imageDimension, boundaryDimension);

        double scaleX = scaledDimension.getWidth()/bufferedImage.getWidth();
        double scaleY = scaledDimension.getHeight()/bufferedImage.getHeight();

        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp biLinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return biLinearScaleOp.filter(
                bufferedImage,
                new BufferedImage(scaledDimension.width, scaledDimension.height, bufferedImage.getType()));
    }
}
