/*
 * Copyright 2003-2014, IT20one GmbH, Vienna, Austria
 * All rights reserved.
 */
package org.github.jipsg.imageio;

import org.github.jipsg.common.AbstractImageTest;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.util.HashSet;
import java.util.Set;

/**
 * Load various images.
 */
public class LoadImageTest extends AbstractImageTest {
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
    // JPEG
    // ======================================================================

    /**
     * Plain-vanilla JPEG
     */
    @Test
    public void testLoadJPEGImage() throws Exception {
        assertValidBufferedImage(ImageIO.read(getImageFile("jpg", "test-image-rgb-01.jpg")));
    }

    /**
     * CMYK color model is supported.
     */
    @Test(expected = javax.imageio.IIOException.class)
    public void testLoadCMYKImage() throws Exception {
        assertValidBufferedImage(ImageIO.read(getImageFile("jpg", "test-image-cmyk-uncompressed.jpg")));
    }

    // ======================================================================
    // TIFF
    // ======================================================================

    /**
     * Load a TIFF image with compression 2.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 2" but got
     * an "ArrayIndexOutOfBoundsException"
     */
    @Test(expected = java.lang.AssertionError.class)
    public void testLoadTiffGrayWithCompression2() throws Exception {
        assertValidBufferedImage(ImageIO.read(getImageFile("tiff", "test-single-gray-compression-type-2.tiff")));
    }

    /**
     * Load a TIFF image with compression 3.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 3"
     */
    @Test(expected = java.lang.AssertionError.class)
    public void testLoadTiffWithCompression3() throws Exception {
        assertValidBufferedImage(ImageIO.read(getImageFile("tiff", "test-single-gray-compression-type-3.tiff")));
    }

    /**
     * Load a TIFF image with compression 4.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 4"
     */
    @Test(expected = java.lang.AssertionError.class)
    public void testLoadTiffWithCompression4() throws Exception {
        assertValidBufferedImage(ImageIO.read(getImageFile("tiff", "test-single-gray-compression-type-4.tiff")));
    }

    /**
     * Load a TIFF image with compression 4.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 4"
     */
    @Test(expected = java.lang.AssertionError.class)
    public void testLoadTiffMultiPageGray() throws Exception {
        assertValidBufferedImage(ImageIO.read(getImageFile("tiff", "test-multi-gray-compression-type-4.tiff")));
    }

    /**
     * Load a TIFF image with compression LZW.
     */
    @Test(expected = java.lang.AssertionError.class)
    public void testLoadTiffSingleCmykCompressionLzw() throws Exception {
        assertValidBufferedImage(ImageIO.read(getImageFile("tiff", "test-single-cmyk-compression-lzw.tiff")));
    }
}
