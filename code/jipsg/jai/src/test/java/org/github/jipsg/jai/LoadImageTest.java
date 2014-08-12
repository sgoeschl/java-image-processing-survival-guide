/*
 * Copyright 2003-2014, IT20one GmbH, Vienna, Austria
 * All rights reserved.
 */
package org.github.jipsg.jai;

import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Load various images.
 */
public class LoadImageTest
{
    private File imageDirectory;

    @Before
    public void setup() {
        this.imageDirectory = new File("../images");
    }

    public File getImageDirectory() {
        return imageDirectory;
    }

    /**
     * List available image formats.
     * @see http://examples.javacodegeeks.com/desktop-java/imageio/list-read-write-supported-image-formats/
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

    /**
     * Plain-vanilla JPEG
     */
    @Test
    public void testLoadJPEGImage() throws Exception
    {
        ImageIO.read(new File(getImageDirectory(), "jpg/test-image-01.jpg"));
    }

    /**
     * CMYK color model is not supported.
     */
    @Test(expected = javax.imageio.IIOException.class)
    public void testLoadCMYKImage() throws Exception
    {
        ImageIO.read(new File(getImageDirectory(), "jpg/test-image-cymk-01.jpg"));
    }

    /**
     * Load a TIFF image with compression 3
     */
    @Test
    public void testLoadTiffWithCompression3() throws Exception
    {
        ImageIO.read(new File(getImageDirectory(), "tiff/test-image-compression-3.tiff"));
    }

    /**
     * Load a TIFF image with compression 4
     */
    @Test
    public void testLoadTiffWithCompression4() throws Exception
    {
        ImageIO.read(new File(getImageDirectory(), "tiff/test-image-compression-4.tiff"));
    }

    /**
     * Load a TIFF image with compression 4
     */
    @Test
    public void testLoadTiffMultiPageGray() throws Exception
    {
        ImageIO.read(new File(getImageDirectory(), "tiff/test-image-multipage-gray.tiff"));
    }
}
