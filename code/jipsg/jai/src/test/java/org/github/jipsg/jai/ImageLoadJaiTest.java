/*
 * Copyright 2003-2014, IT20one GmbH, Vienna, Austria
 * All rights reserved.
 */
package org.github.jipsg.jai;

import org.github.jipsg.common.AbstractImageTest;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Load various images.
 */
public class ImageLoadJaiTest extends AbstractJaiTest {
    @Before
    public void setup() {
        super.setup();
        this.setModuleName("jai");
    }

    // ======================================================================
    // General
    // ======================================================================

    /**
     * List available image formats.
     * see http://examples.javacodegeeks.com/desktop-java/imageio/list-read-write-supported-image-formats/
     */
    @Test
    public void testListSupportedImageFormats() throws Exception {

        Set<String> set = new HashSet<String>();

        // Get list of all informal format names understood by the current set of registered readers
        String[] formatNames = ImageIO.getReaderFormatNames();

        for (int i = 0; i < formatNames.length; i++) {
            set.add(formatNames[i].toLowerCase());
        }
        System.out.println("Supported read formats: " + set);

        set.clear();

        // Get list of all informal format names understood by the current set of registered writers
        formatNames = ImageIO.getWriterFormatNames();

        for (int i = 0; i < formatNames.length; i++) {
            set.add(formatNames[i].toLowerCase());
        }
        System.out.println("Supported write formats: " + set);

        set.clear();

        // Get list of all MIME types understood by the current set of registered readers
        formatNames = ImageIO.getReaderMIMETypes();

        for (int i = 0; i < formatNames.length; i++) {
            set.add(formatNames[i].toLowerCase());
        }
        System.out.println("Supported read MIME types: " + set);

        set.clear();

        // Get list of all MIME types understood by the current set of registered writers
        formatNames = ImageIO.getWriterMIMETypes();

        for (int i = 0; i < formatNames.length; i++) {
            set.add(formatNames[i].toLowerCase());
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

        for(File sourceImageFile : sourceImageFileList) {
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
     * CMYK color model is not supported.
     */
    @Test(expected = javax.imageio.IIOException.class)
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
}
