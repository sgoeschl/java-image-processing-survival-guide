/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.github.jipsg.twelvemonkey;

import org.github.jipsg.common.image.BufferedImageOperations;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Load various images.
 */
public class ImageConversionTwelveMonkeyTest extends AbstractTwelveMonkeyTest {

    @Before
    public void setup() {
        super.setup();
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
            File targetImageFile = createOutputFileName("testImageWriteAsJpeg", sourceImageFile, formatName);
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
            File targetImageFile = createOutputFileName("testImageWriteAsPng", sourceImageFile, formatName);
            writeBufferedImage(bufferedImage, formatName, targetImageFile);
        }
    }

    // ======================================================================
    // Transparent Images
    // ======================================================================

    /**
     * Convert images having a transparency layer (alpha-channel) to JPG. Without
     * further handling the alpha-channel will be rendered black
     */
    @Test
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
     */
    @Test
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
