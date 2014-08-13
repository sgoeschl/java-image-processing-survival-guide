/*
 * Copyright 2003-2014, IT20one GmbH, Vienna, Austria
 * All rights reserved.
 */
package org.github.jipsg.sanselan;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Load various images.
 */
public class ImageResamplingSanselanTest extends AbstractSanselanTest {

    @Before
    public void setup() {
        super.setup();
    }

    /**
     * Fails for unknown reasons with standard Sanselan code
     */
    @Test(expected = org.apache.commons.imaging.ImageWriteException.class)
    public void testResamplingImagesAsJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        // fails with org.apache.commons.imaging.ImageReadException: Invalid marker found in entropy data
        // sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
        sourceImageFileList.add(getImageFile("png", "marble.png"));
        sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));
        sourceImageFileList.add(getImageFile("gif", "marble.gif"));

        for(File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            BufferedImage resampledBufferdImage = resample(bufferedImage, 640, 480);
            assertValidBufferedImage(resampledBufferdImage);
            File targetImageFile = createOutputFileName("testResamplingImagesAsJpeg", sourceImageFile, formatName);
            writeBufferedImage(resampledBufferdImage, formatName, targetImageFile);
        }
    }
}
