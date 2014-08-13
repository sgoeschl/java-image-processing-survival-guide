/*
 * Copyright 2003-2014, IT20one GmbH, Vienna, Austria
 * All rights reserved.
 */
package org.github.jipsg.sanselan;

import org.github.jipsg.common.image.BufferedImageOperations;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Load various images.
 */
public class ImageConversionSanselanTest extends AbstractSanselanTest {

    @Before
    public void setup() {
        super.setup();
    }

    // ======================================================================
    // Image format conversion
    // ======================================================================

    @Test
    public void testWriteImageFormatsAsJpeg() throws Exception {

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
            File targetImageFile = createOutputFileName("testWriteImageFormatsAsJpeg", sourceImageFile, formatName);
            ImageIO.write(bufferedImage, formatName, targetImageFile);
        }
    }

    @Test
    public void testWriteImageFormatsAsPng() throws Exception {

        String formatName = "png";
        List<File> sourceImageFileList = new ArrayList<File>();

        // fails with org.apache.commons.imaging.ImageReadException: Invalid marker found in entropy data
        // sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
        sourceImageFileList.add(getImageFile("png", "marble.png"));
        sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));
        sourceImageFileList.add(getImageFile("gif", "marble.gif"));

        for(File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            File targetImageFile = createOutputFileName("testWriteImageFormatsAsPng", sourceImageFile, formatName);
            ImageIO.write(bufferedImage, formatName, targetImageFile);
        }
    }

    // ======================================================================
    // Transparent Images
    // ======================================================================

    /**
     * Convert images having a transparency layer (alpha-channel) to JPG. Without
     * further handling the alpha-channel will be rendered black.
     * The test fails with "org.apache.commons.imaging.ImageWriteException: This image format (Jpeg-Custom) cannot be written."
     */
    @Test(expected = org.apache.commons.imaging.ImageWriteException.class)
    public void testWriteTransparentImagesAsJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("gif", "test-image-transparent.gif"));
        sourceImageFileList.add(getImageFile("png", "test-image-transparent.png"));

        for(File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            File targetImageFile = createOutputFileName("testWriteTransparentImagesAsJpeg", sourceImageFile, formatName);
            writeBufferedImage(bufferedImage, formatName, targetImageFile);
        }
    }

    /**
     * Convert images having a transparency layer (alpha-channel) to JPG. Fill
     * the alpha-channel with Color.WHITE to have a useful image.
     * The test fails with "org.apache.commons.imaging.ImageWriteException: This image format (Jpeg-Custom) cannot be written."
     */
    @Test(expected = org.apache.commons.imaging.ImageWriteException.class)
    public void testWriteTransparentImagesWithAlphaChannelHandlingAsJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("gif", "test-image-transparent.gif"));
        sourceImageFileList.add(getImageFile("png", "test-image-transparent.png"));

        for(File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = BufferedImageOperations.fillTransparentPixel(createBufferedImage(sourceImageFile));
            assertValidBufferedImage(bufferedImage);
            File targetImageFile = createOutputFileName("testWriteTransparentImagesWithAlphaChannelHandlingAsJpeg", sourceImageFile, formatName);
            writeBufferedImage(bufferedImage, formatName, targetImageFile);
        }
    }
}
