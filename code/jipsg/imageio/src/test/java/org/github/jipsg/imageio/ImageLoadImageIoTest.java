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
package org.github.jipsg.imageio;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Load various images.
 */
public class ImageLoadImageIoTest extends BaseImageIoTest {

    @Before
    public void setup() {
        super.setup();
        super.setModuleName("imageio");
    }

    // ======================================================================
    // General
    // ======================================================================

    /**
     * List available image formats.
     * <p>
     * see http://examples.javacodegeeks.com/desktop-java/jai/list-read-write-supported-image-formats/
     */
    @Test
    public void testListSupportedImageFormats() throws Exception {

        Set<String> set = new HashSet<String>();

        // Get list of all informal format names understood by the current set of registered readers
        String[] formatNames = ImageIO.getReaderFormatNames();

        for (String formatName : formatNames) {
            set.add(formatName.toLowerCase());
        }
        System.out.println("Supported read formats: " + set);

        set.clear();

        // Get list of all informal format names understood by the current set of registered writers
        formatNames = ImageIO.getWriterFormatNames();

        for (String formatName : formatNames) {
            set.add(formatName.toLowerCase());
        }
        System.out.println("Supported write formats: " + set);

        set.clear();

        // Get list of all MIME types understood by the current set of registered readers
        formatNames = ImageIO.getReaderMIMETypes();

        for (String formatName : formatNames) {
            set.add(formatName.toLowerCase());
        }
        System.out.println("Supported read MIME types: " + set);

        set.clear();

        // Get list of all MIME types understood by the current set of registered writers
        formatNames = ImageIO.getWriterMIMETypes();

        for (String formatName : formatNames) {
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
        // sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));
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
     * CMYK color model is not supported and fails with "javax.imageio.IIOException: Unsupported Image Type"
     */
    @Ignore
    @Test
    public void testLoadCMYKImage() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("jpg", "test-image-cmyk-uncompressed.jpg")));
    }

    // ======================================================================
    // TIFF
    // ======================================================================

    /**
     * Load a TIFF image with compression 2. Silently no buffered image
     * is loaded at call causing assertValidBufferedImage() to fail.
     */
    @Test
    @Ignore
    public void testLoadTiffGrayWithCompression2() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-gray-compression-type-2.tiff")));
    }

    /**
     * Load a TIFF image with compression 3. Silently no buffered image
     * is loaded at call causing assertValidBufferedImage() to fail.
     */
    @Test
    @Ignore
    public void testLoadTiffWithCompression3() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-gray-compression-type-3.tiff")));
    }

    /**
     * Load a TIFF image with compression 4. Silently no buffered image
     * is loaded at call causing assertValidBufferedImage() to fail.
     */
    @Test
    @Ignore
    public void testLoadTiffWithCompression4() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-gray-compression-type-4.tiff")));
    }

    /**
     * Load a TIFF image with compression 4. Silently no buffered image
     * is loaded at call causing assertValidBufferedImage() to fail.
     */
    @Test
    @Ignore
    public void testLoadTiffMultiPageGray() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-multi-gray-compression-type-4.tiff")));
    }

    /**
     * Load a TIFF image with compression LZW. Silently no buffered image
     * is loaded at call causing assertValidBufferedImage() to fail.
     */
    @Test
    @Ignore
    public void testLoadTiffSingleCmykCompressionLzw() throws Exception {
        assertValidBufferedImage(createBufferedImage(getImageFile("tiff", "test-single-cmyk-compression-lzw.tiff")));
    }

    // ======================================================================
    // Decompression Bobs
    // ======================================================================

    @Test
    public void testLoadTImage() throws Exception {
        BufferedImage bufferedImage = createBufferedImage(getImageFile("bombs", "picture-1G-19000x19000.png"));
        assertEquals(19000, bufferedImage.getHeight());
        assertEquals(19000, bufferedImage.getWidth());
    }
}
