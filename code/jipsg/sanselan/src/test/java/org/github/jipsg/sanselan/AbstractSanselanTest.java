package org.github.jipsg.sanselan;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.github.jipsg.common.AbstractImageTest;

import javax.imageio.ImageIO;
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

    public void writeBufferedImage(final BufferedImage bufferedImage, final String formatName, final File file) throws Exception {
        final ImageFormat format = getImageFormat(formatName);
        final Map<String, Object> params = new HashMap<String, Object>();
        Imaging.writeImage(bufferedImage, file, format, params);
    }

    private ImageFormat getImageFormat(String formatName) {

        if("jpg".equalsIgnoreCase(formatName)) {
            return ImageFormats.JPEG;
        }
        else if("jepg".equalsIgnoreCase(formatName)) {
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
