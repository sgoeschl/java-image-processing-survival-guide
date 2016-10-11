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
package org.github.jipsg.twelvemonkeys;

import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Load various images.
 */
public class ImageLoadTwelveMonkeysTest extends BaseTwelveMonkeysTest {

    @Before
    @Override
    public void setup() {
        super.setup();
    }

    // ======================================================================
    // General
    // ======================================================================

    /**
     * List available image formats.
     * <p>
     * see http://examples.javacodegeeks.com/desktop-java/imageio/list-read-write-supported-image-formats/
     */
    @Test
    public void testListSupportedImageFormats() throws Exception {

        Set<String> set = new HashSet<String>();

        // Get list of all informal format names understood by the current set of registered readers
        String[] formatNames = ImageIO.getReaderFormatNames();

        for (final String formatName : formatNames) {
            set.add(formatName.toLowerCase());
        }
        System.out.println("Supported read formats: " + set);

        set.clear();

        // Get list of all informal format names understood by the current set of registered writers
        formatNames = ImageIO.getWriterFormatNames();

        for (final String formatName : formatNames) {
            set.add(formatName.toLowerCase());
        }
        System.out.println("Supported write formats: " + set);

        set.clear();

        // Get list of all MIME types understood by the current set of registered readers
        formatNames = ImageIO.getReaderMIMETypes();

        for (final String formatName : formatNames) {
            set.add(formatName.toLowerCase());
        }
        System.out.println("Supported read MIME types: " + set);

        set.clear();

        // Get list of all MIME types understood by the current set of registered writers
        formatNames = ImageIO.getWriterMIMETypes();

        for (final String formatName : formatNames) {
            set.add(formatName.toLowerCase());
        }
        System.out.println("Supported write MIME types: " + set);
    }

    // ======================================================================
    // Load various image formats
    // ======================================================================

    @Test
    public void testLoadVariousImageFormats() throws Exception {

        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
        sourceImageFileList.add(getImageFile("png", "marble.png"));
        sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));
        sourceImageFileList.add(getImageFile("gif", "marble.gif"));
        // sourceImageFileList.add(getImageFile("jp2", "marble.jp2"));

        for (File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
        }
    }

    // ======================================================================
    // JPEG
    // ======================================================================

    /**
     * Plain-vanilla JPEG
     */
    @Test
    public void testLoadJPEGImage() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("jpg", "test-image-rgb-01.jpg")));
    }

    /**
     * CMYK color model is supported.
     */
    @Test
    public void testLoadCMYKImage() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("jpg", "test-image-cmyk-uncompressed.jpg")));
    }

    // ======================================================================
    // TIFF
    // ======================================================================

    /**
     * Load a TIFF image with compression 2.
     */
    @Test
    public void testLoadTiffGrayWithCompression2() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-gray-compression-type-2.tiff")));
    }

    /**
     * Load a TIFF image with compression 3.
     */
    @Test
    public void testLoadTiffWithCompression3() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-gray-compression-type-3.tiff")));
    }

    /**
     * Load a TIFF image with compression 4.
     */
    @Test
    public void testLoadTiffWithCompression4() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-gray-compression-type-4.tiff")));
    }

    /**
     * Load a multi-page TIFF.
     */
    @Test
    public void testLoadTiffMultiPageGray() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-multi-gray-compression-type-4.tiff")));
    }

    /**
     * Load a TIFF image with compression type 7 (JPEG).
     */
    @Test
    public void testLoadTiffMultiRgbCompression7() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-multi-rgb-compression-type-7.tiff")));
    }

    /**
     * Load a TIFF image with compression LZW.
     */
    @Test
    public void testLoadTiffSingleCmykCompressionLzw() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-cmyk-compression-lzw.tiff")));
    }

    // ======================================================================
    // Multi-page TIFF extraction
    // ======================================================================

    /**
     * Load a multi-page TIFF image and split it into its individual pages.
     */
    @Test
    public void testExtractPagesFromMultiPageTiffCompression4() throws Exception {

        File sourceImageFile = getImageFile("tiff", "test-multi-gray-compression-type-4.tiff");
        ImageInputStream is = ImageIO.createImageInputStream(sourceImageFile);

        // get the first matching reader
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(is);
        ImageReader imageReader = iterator.next();
        imageReader.setInput(is);

        // split the multi-page TIFF
        int pages = imageReader.getNumImages(true);
        for (int i = 0; i < pages; i++) {
            BufferedImage bufferedImage = imageReader.read(i);
            assertValidBufferedImage(bufferedImage);
        }

        assertEquals("Expect to have 2 pages", 2, pages);
    }

    /**
     * Load a multi-page TIFF image and split it into its individual pages.
     */
    @Test
    public void testExtractPagesFromMultiPageTiffCompression7() throws Exception {

        File sourceImageFile = getImageFile("tiff", "test-multi-rgb-compression-type-7.tiff");
        ImageInputStream is = ImageIO.createImageInputStream(sourceImageFile);

        // get the first matching reader
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(is);
        ImageReader imageReader = iterator.next();
        imageReader.setInput(is);

        // split the multi-page TIFF
        int pages = imageReader.getNumImages(true);
        for (int i = 0; i < pages; i++) {
            BufferedImage bufferedImage = imageReader.read(i);
            assertValidBufferedImage(bufferedImage);
        }

        assertEquals("Expect to have 10 pages", 10, pages);
    }

}
