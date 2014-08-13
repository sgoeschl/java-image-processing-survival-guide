/*
 * Copyright 2003-2014, IT20one GmbH, Vienna, Austria
 * All rights reserved.
 */
package org.github.jipsg.sanselan;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.IBufferedImageFactory;
import org.github.jipsg.AbstractImageTest;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Load various images using Apache Commons Imaging.
 */
public class LoadImageTest extends AbstractImageTest {

    @Before
    public void setup() {
        super.setup();
        super.setModuleName("sanselan");
    }

    private BufferedImage read(final File file)
            throws ImageReadException, IOException {
        final Map<String, Object> params = new HashMap<String, Object>();

        // set optional parameters if you like
        params.put(ImagingConstants.BUFFERED_IMAGE_FACTORY,
                new ManagedImageBufferedImageFactory());

        // params.put(SanselanConstants.PARAM_KEY_VERBOSE, Boolean.TRUE);

        // read image
        final BufferedImage image = Imaging.getBufferedImage(file, params);

        return image;
    }

    /**
     * List available image formats.
     * <p/>
     * see http://examples.javacodegeeks.com/desktop-java/imageio/list-read-write-supported-image-formats/
     */
    @Test
    public void testListSupportedImageFormats() throws Exception {

    }

    /**
     * Plain-vanilla JPEG
     */
    @Test(expected = org.apache.commons.imaging.ImageReadException.class)
    public void testLoadJPEGImage() throws Exception {
        assertValidBufferedImage(read(getImageFile("jpg", "test-image-rgb-01.jpg")));
    }

    // ======================================================================
    // General
    // ======================================================================

    /**
     * CMYK color model is supported.
     */
    @Test(expected = org.apache.commons.imaging.ImageReadException.class)
    public void testLoadCMYKImage() throws Exception {
        assertValidBufferedImage(read(getImageFile("jpg", "test-image-cmyk-uncompressed.jpg")));
    }

    // ======================================================================
    // JPEG
    // ======================================================================

    /**
     * Load a TIFF image with compression 2.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 2" but got
     * an "ArrayIndexOutOfBoundsException"
     */
    @Test
    public void testLoadTiffGrayWithCompression2() throws Exception {
        assertValidBufferedImage(read(getImageFile("tiff", "test-single-gray-compression-type-2.tiff")));
    }

    /**
     * Load a TIFF image with compression 3.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 3"
     */
    @Test
    public void testLoadTiffWithCompression3() throws Exception {
        assertValidBufferedImage(read(getImageFile("tiff", "test-single-gray-compression-type-3.tiff")));
    }

    // ======================================================================
    // TIFF
    // ======================================================================

    /**
     * Load a TIFF image with compression 4.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 4"
     */
    @Test
    public void testLoadTiffWithCompression4() throws Exception {
        assertValidBufferedImage(read(getImageFile("tiff", "test-single-gray-compression-type-4.tiff")));
    }

    /**
     * Load a TIFF image with compression 4.
     * Expecting a "javax.imageio.IIOException: Unsupported TIFF Compression value: 4"
     */
    @Test
    public void testLoadTiffMultiPageGray() throws Exception {
        assertValidBufferedImage(read(getImageFile("tiff", "test-multi-gray-compression-type-4.tiff")));
    }

    /**
     * Load a TIFF image with compression LZW.
     */
    @Test
    public void testLoadTiffSingleCmykCompressionLzw() throws Exception {
        assertValidBufferedImage(read(getImageFile("tiff", "test-single-cmyk-compression-lzw.tiff")));
    }

    private static class ManagedImageBufferedImageFactory implements
            IBufferedImageFactory {

        public BufferedImage getColorBufferedImage(final int width, final int height,
                                                   final boolean hasAlpha) {
            final GraphicsEnvironment ge = GraphicsEnvironment
                    .getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();
            final GraphicsConfiguration gc = gd.getDefaultConfiguration();
            return gc.createCompatibleImage(width, height,
                    Transparency.TRANSLUCENT);
        }

        public BufferedImage getGrayscaleBufferedImage(final int width, final int height,
                                                       final boolean hasAlpha) {
            return getColorBufferedImage(width, height, hasAlpha);
        }
    }
}
