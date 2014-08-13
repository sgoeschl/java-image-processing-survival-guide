/*
 * Copyright 2003-2014, IT20one GmbH, Vienna, Austria
 * All rights reserved.
 */
package org.github.jipsg.twelvemonkey;

import org.github.jipsg.common.image.BufferedImageOperations;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Load various images.
 */
public class ImageResamplingTwelveMonkeyTest extends AbstractTwelveMonkeyTest {

    @Before
    public void setup() {
        super.setup();
    }

    /**
     * Convert images having a transparency layer (alpha-channel) to JPG. Without
     * further handling the alpha-channel will be rendered black
     */
    @Test
    public void testResamplingImagesAsJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
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
