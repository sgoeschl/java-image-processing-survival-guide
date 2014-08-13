package org.github.jipsg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: sgoeschl
 * Date: 13/08/14
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class AbstractImageTest {

    private String moduleName;
    private File imageDirectory;

    public void setup() {
        this.imageDirectory = new File("../../images");
    }

    public File getImageDirectory() {
        return imageDirectory;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public File getImageFile(String folderName, String fileName) throws IOException {
        File folderFile = new File(getImageDirectory(), folderName);
        File result = new File(folderFile, fileName);
        if (!result.exists() || !result.canRead()) {
            throw new IOException("Can't open/read the following file : " + result.getAbsolutePath());
        }
        return result;
    }

    public void assertValidBufferedImage(BufferedImage bufferedImage) {
        assertNotNull("bufferedImage is null", bufferedImage);
        assertTrue(bufferedImage.getHeight() > 0);
        assertTrue(bufferedImage.getWidth() > 0);
    }
}
