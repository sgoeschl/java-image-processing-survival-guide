package org.github.jipsg.twelvemonkey;

import com.twelvemonkeys.image.ResampleOp;
import org.github.jipsg.common.AbstractImageTest;
import org.github.jipsg.common.image.BufferedImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Base class for testing TwelveMonkey library.
 */
public class AbstractTwelveMonkeyTest extends AbstractImageTest {

    @Override
    public void setup() {
        super.setModuleName("twelvemonkey");
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

    @Override
    public BufferedImage resample(BufferedImage bufferedImage, int width, int height) {
        Dimension imageDimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
        Dimension boundaryDimension = new Dimension(width, height);
        Dimension scaledDimension = BufferedImageUtils.getScaledDimension(imageDimension, boundaryDimension);
        ResampleOp resampleOp = new ResampleOp(scaledDimension.width, scaledDimension.height);
        return resampleOp.filter(bufferedImage, null);
    }
}
