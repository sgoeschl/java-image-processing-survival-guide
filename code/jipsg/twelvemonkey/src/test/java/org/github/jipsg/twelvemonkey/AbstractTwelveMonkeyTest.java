package org.github.jipsg.twelvemonkey;

import org.github.jipsg.common.AbstractImageTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Base class for testing TwelveMonkey library.
 */
public class AbstractTwelveMonkeyTest extends AbstractImageTest {

    @Override
    public void setup() {
        super.setup();
        super.setModuleName("twelvemonkey");
    }

    @Override
    public BufferedImage createBufferedImage(File file) throws Exception {
        return ImageIO.read(file);
    }

    @Override
    public void writeBufferedImage(BufferedImage bufferedImage, String formatName, File file) throws Exception {
        ImageIO.write(bufferedImage, formatName, file);
    }
}
