/*
 * Copyright 2003-2014, IT20one GmbH, Vienna, Austria
 * All rights reserved.
 */
package org.github.jipsg.jai;

import org.github.jipsg.common.AbstractImageTest;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Load various images.
 */
public class ImageConversionJaiTest extends AbstractJaiTest {

    @Before
    public void setup() {
        super.setup();
        super.setModuleName("jai");
    }

    // ======================================================================
    // Implementation
    // ======================================================================

    private ImageWriter getImageWriter(String formatName) {
        Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName(formatName);
        while(imageWriterIterator.hasNext()) {
            ImageWriter imageWriter = imageWriterIterator.next();
            if("com.sun.imageio.plugins.jpeg.JPEGImageWriter".equals(imageWriter.getClass().getName())) {
                return imageWriter;
            }
        }

        throw new RuntimeException("No image writer found for : " + formatName);
    }

    // ======================================================================
    // Image format conversion
    // ======================================================================

    @Test
    public void testImageWriteAsJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
        sourceImageFileList.add(getImageFile("png", "marble.png"));
        sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));
        sourceImageFileList.add(getImageFile("gif", "marble.gif"));

        for(File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            File targetImageFile = createOutputFileName(sourceImageFile, formatName);
            writeBufferedImage(bufferedImage, formatName, targetImageFile);
        }
    }

    @Test
    public void testImageWriteAsPng() throws Exception {

        String formatName = "png";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
        sourceImageFileList.add(getImageFile("png", "marble.png"));
        sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));
        sourceImageFileList.add(getImageFile("gif", "marble.gif"));

        for(File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            File targetImageFile = createOutputFileName(sourceImageFile, formatName);
            writeBufferedImage(bufferedImage, formatName, targetImageFile);
        }
    }

}
