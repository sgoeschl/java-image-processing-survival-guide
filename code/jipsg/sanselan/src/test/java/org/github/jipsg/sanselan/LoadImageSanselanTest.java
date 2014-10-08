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
package org.github.jipsg.sanselan;

import org.apache.commons.imaging.Imaging;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Load various images using Apache Commons Imaging.
 */
public class LoadImageSanselanTest extends BaseSanselanTest {

    @Before
    public void setup() {
        super.setup();
    }

    /**
     * List available image formats.
     * <p/>
     * see http://examples.javacodegeeks.com/desktop-java/imageio/list-read-write-supported-image-formats/
     */
    @Test
    public void testListSupportedImageFormats() throws Exception {

    }

    // ======================================================================
    // Load various image formats
    // ======================================================================

    @Test
    public void testLoadVariousImageFormats() throws Exception {

        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("gif", "marble.gif"));
        // fails with org.apache.commons.imaging.ImageReadException: Invalid marker found in entropy data
        // sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
        sourceImageFileList.add(getImageFile("png", "marble.png"));
        sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));

        for(File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
        }
    }

    // ======================================================================
    // JPG
    // ======================================================================

    /**
     * Plain-vanilla JPEG
     */
    @Test(expected = org.apache.commons.imaging.ImageReadException.class)
    public void testLoadJPEGImage() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("jpg", "test-image-rgb-01.jpg")));
    }

    /**
     * CMYK color model is supported.
     */
    @Test(expected = org.apache.commons.imaging.ImageReadException.class)
    public void testLoadCMYKImage() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("jpg", "test-image-cmyk-uncompressed.jpg")));
    }

    // ======================================================================
    // TIFF
    // ======================================================================

    /**
     * Load a TIFF image with compression 2.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 2" but got
     * an "ArrayIndexOutOfBoundsException"
     */
    @Test
    public void testLoadTiffGrayWithCompression2() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-gray-compression-type-2.tiff")));
    }

    /**
     * Load a TIFF image with compression 3.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 3"
     */
    @Test
    public void testLoadTiffWithCompression3() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-gray-compression-type-3.tiff")));
    }

    /**
     * Load a TIFF image with compression 4.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 4"
     */
    @Test
    public void testLoadTiffWithCompression4() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-gray-compression-type-4.tiff")));
    }

    /**
     * Load a TIFF image with compression 4.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 4"
     */
    @Test
    public void testLoadTiffMultiPageGray() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-multi-gray-compression-type-4.tiff")));
    }

    /**
     * Load a TIFF image with compression LZW.
     */
    @Test
    public void testLoadTiffSingleCmykCompressionLzw() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-cmyk-compression-lzw.tiff")));
    }

    /**
     * Load a TIFF image with compression type 7 (JPEG).
     */
    @Ignore
    public void testLoadTiffMultiRgbCompression7() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-multi-rgb-compression-type-7.tiff")));
    }

    // ======================================================================
    // Multi-page TIFF extraction
    // ======================================================================

    /**
     * Load a multi-page TIFF image and split it into its individual pages.
     */
    @Test
    public void testExtractPagesFromMultiPageTiff() throws Exception {

        File sourceImageFile = getImageFile("tiff", "test-multi-gray-compression-type-4.tiff");

        List<BufferedImage> bufferedImageList = Imaging.getAllBufferedImages(sourceImageFile);

        for(BufferedImage bufferedImage : bufferedImageList) {
            assertValidBufferedImage(bufferedImage);
        }
        assertEquals("Expect to have 2 pages", 2, bufferedImageList.size());
    }
}
