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
package org.github.jipsg.thumbnailator;

import com.jhlabs.image.*;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Image manipulation samples.
 */
public class ImageManipulationTest extends AbstractImageIoTest {

    @Before
    public void setup() {
        super.setup();
        setModuleName("thumbnailator");
    }

    // ======================================================================
    // Image Auto-correction
    // ======================================================================

    /**
     * Run an auto-correction on the image affecting color, contract and levels
     * similar to functionality found in photo processing tools. Please note
     * that this is not a "highlight shadows" or "darken highlight" functionality.
     */
    @Test
    public void testAutoCorrectionFilter() throws Exception {

        BufferedImage result;
        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("samples", "under-exposed-black-white-image.jpg"));
        sourceImageFileList.add(getImageFile("samples", "under-exposed-color-image.jpg"));

        for (File sourceImageFile : sourceImageFileList) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);

            BufferedImage leveledBufferedImage = new AutoCorrectionFilter().filter(bufferedImage, null);
            BufferedImage sharpenedBufferedImage = new UnsharpFilter().filter(leveledBufferedImage, null);

            File targetImageFile = createOutputFileName("testAutoCorrectionFilter", sourceImageFile, formatName);
            writeBufferedImage(sharpenedBufferedImage, formatName, targetImageFile);
        }
    }

    // ======================================================================
    // Greyscale Images
    // ======================================================================

    /**
     * Concert an image to grey-scale using AWT's drawImage().
     * See http://www.codebeach.com/2008/03/convert-color-image-to-gray-scale-image.html
     */
    @Test
    public void testConvertToGrayScaleUsingAwt() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();
        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));

        for (File sourceImageFile : sourceImageFileList) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);

            BufferedImage greyscaleImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Graphics graphics = greyscaleImage.getGraphics();
            graphics.drawImage(bufferedImage, 0, 0, null);
            graphics.dispose();
            assertValidBufferedImage(greyscaleImage);

            File targetImageFile = createOutputFileName("testConvertToGrayScaleUsingAwt", sourceImageFile, formatName);
            writeBufferedImage(greyscaleImage, formatName, targetImageFile);
        }
    }

    /**
     * Concert an image to grey-scale using NTSC brightness calculation based
     * on JHLabs GrayScale filter. Please note that you can overwrite
     * GrayscaleFilter.filterRGB() if you are not satisfied with the
     * RGB to greyscale mapping.
     */
    @Test
    public void testConvertToGrayScaleUsingFilter() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();
        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));

        for (File sourceImageFile : sourceImageFileList) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);

            BufferedImage greyscaleImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            new GrayscaleFilter().filter(bufferedImage, greyscaleImage);
            assertValidBufferedImage(greyscaleImage);

            File targetImageFile = createOutputFileName("testConvertToGrayScaleUsingFilter", sourceImageFile, formatName);
            writeBufferedImage(greyscaleImage, formatName, targetImageFile);
        }
    }

    // ======================================================================
    // Black & White Images
    //
    // Convert a color image to black & white in order to minimize the
    // amount of required storage, i.e. using TYPE_BYTE_BINARY. For some
    // reason the JPGs are stored as RGB (consuming a lot of storage) while
    // PNG allows to use a 1-bit color information. Maybe someone can shed
    // some light ...
    // ======================================================================

    /**
     * Concert an image to monochrome using AWT's drawImage().
     * See http://bethecoder.com/applications/tutorials/java/image-io/how-to-create-black-and-white-image-from-color-image.html
     */
    @Test
    public void testConvertToMonochromeUsingAwt() throws Exception {

        List<File> sourceImageFileList = new ArrayList<File>();
        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));

        for (File sourceImageFile : sourceImageFileList) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);

            BufferedImage monochromeImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
            Graphics2D graphics = monochromeImage.createGraphics();
            graphics.drawImage(bufferedImage, 0, 0, null);
            graphics.dispose();
            assertValidBufferedImage(monochromeImage);

            writeBufferedImage(monochromeImage, "png", createOutputFileName("testConvertToMonochromeUsingAwt", sourceImageFile, "png"));
            writeBufferedImage(monochromeImage, "jpeg", createOutputFileName("testConvertToMonochromeUsingAwt", sourceImageFile, "jpeg"));
        }
    }

    /**
     * Concert an image to a black/white image based
     * on JHLabs DiffusionFilter filter.
     */
    @Test
    public void testConvertToMonochromeUsingDiffusionFilter() throws Exception {

        String formatName = "png";
        List<File> sourceImageFileList = new ArrayList<File>();
        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));

        for (File sourceImageFile : sourceImageFileList) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);

            BufferedImage monochromeImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
            DiffusionFilter diffusionFilter = new DiffusionFilter();
            diffusionFilter.setColorDither(false);
            diffusionFilter.setSerpentine(true);

            diffusionFilter.filter(bufferedImage, monochromeImage);

            assertValidBufferedImage(monochromeImage);

            writeBufferedImage(monochromeImage, "png", createOutputFileName("testConvertToMonochromeUsingDiffusionFilter", sourceImageFile, "png"));
            writeBufferedImage(monochromeImage, "jpeg", createOutputFileName("testConvertToMonochromeUsingDiffusionFilter", sourceImageFile, "jpeg"));
        }
    }

    /**
     * Concert an image to a black/white image based
     * on JHLabs DitherFilter filter.
     */
    @Test
    public void testConvertToMonochromeUsingDitherFilter() throws Exception {

        String formatName = "png";
        List<File> sourceImageFileList = new ArrayList<File>();
        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));

        for (File sourceImageFile : sourceImageFileList) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);

            BufferedImage monochromeImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
            DitherFilter ditherFilter = new DitherFilter();
            ditherFilter.filter(bufferedImage, monochromeImage);

            assertValidBufferedImage(monochromeImage);

            writeBufferedImage(monochromeImage, "png", createOutputFileName("testConvertToMonochromeUsingDitherFilter", sourceImageFile, "png"));
            writeBufferedImage(monochromeImage, "jpeg", createOutputFileName("testConvertToMonochromeUsingDitherFilter", sourceImageFile, "jpeg"));
        }
    }

    // ======================================================================
    // Image Scaling
    // ======================================================================

    @Test
    public void testScaleImages() throws Exception {

        final int[] SIZES = new int[] { 575, 199, 80, 60};
        final float QUALITY = 0.8f;

        File sourceImageDir = new File("../../images/willhaben");
        File[] sourceImageFiles = sourceImageDir.listFiles();

        long currentTime = System.currentTimeMillis();

        for(File sourceImageFile : sourceImageFiles) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);

            for(int size : SIZES) {

                BufferedImage scaledBufferedImage = Thumbnails.of(bufferedImage).
                        width(size).
                        height(size).
                        keepAspectRatio(true).
                        outputQuality(QUALITY).
                        antialiasing(Antialiasing.ON).
                        asBufferedImage();

                bufferedImage = scaledBufferedImage;

                writeBufferedImage(scaledBufferedImage, "jpeg", createOutputFileName("testScaleImages/" + size, sourceImageFile, "jpeg"));
            }
        }

        long duration = System.currentTimeMillis() - currentTime;

        System.out.println("Scaling one source image to " + SIZES.length + " previews took " + duration/sourceImageFiles.length + " ms");
    }


}
