package org.github.jipsg.sanselan;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.github.jipsg.common.AbstractImageTest;
import org.github.jipsg.common.image.BufferedImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for testing Apache Sanselan.
 */
public class AbstractSanselanTest extends AbstractImageTest {

    @Override
    public void setup() {
        super.setup();
        super.setModuleName("sanselan");
    }

    @Override
    public BufferedImage createBufferedImage(File file) throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        // set optional parameters if you like
        params.put(ImagingConstants.BUFFERED_IMAGE_FACTORY,
                new ManagedImageBufferedImageFactory());
        // read image
        return Imaging.getBufferedImage(file, params);
    }

    @Override
    public void writeBufferedImage(final BufferedImage bufferedImage, final String formatName, final File file) throws Exception {
        final ImageFormat format = getImageFormat(formatName);
        final Map<String, Object> params = new HashMap<String, Object>();
        Imaging.writeImage(bufferedImage, file, format, params);
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

    private ImageFormat getImageFormat(String formatName) {

        if("jpg".equalsIgnoreCase(formatName)) {
            return ImageFormats.JPEG;
        }
        else if("jpeg".equalsIgnoreCase(formatName)) {
            return ImageFormats.JPEG;
        }
        else if("png".equalsIgnoreCase(formatName)) {
            return ImageFormats.PNG;
        }
        else {
            throw new IllegalArgumentException("Don't know how to handle : " + formatName);
        }
    }
}
