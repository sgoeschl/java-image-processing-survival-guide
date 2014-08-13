package org.github.jipsg.imageio;

import org.github.jipsg.common.AbstractImageTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Base class for testing Java ImageIO.
 */
public class AbstractImageIoTest extends AbstractImageTest {

    @Override
    public void setup() {
        super.setup();
        super.setModuleName("imageio");
    }

    @Override
    public BufferedImage createBufferedImage(File file) throws IOException {
        return ImageIO.read(file);
    }

    @Override
    public void writeBufferedImage(BufferedImage bufferedImage, String formatName, File file) throws Exception {
        ImageIO.write(bufferedImage, formatName, file);
    }
}
