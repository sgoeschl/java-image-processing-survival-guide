package org.github.jipsg.jai;

import org.github.jipsg.common.AbstractImageTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Base class for testing Java Advanced Imaging.
 */
public class AbstractJaiTest extends AbstractImageTest {

    @Override
    public void setup() {
        super.setup();
        super.setModuleName("jai");
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
