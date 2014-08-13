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
package org.github.jipsg.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Abstract test class for image testing.
 */
public abstract class AbstractImageTest {

    private String moduleName;
    private File imageDirectory;

    public abstract BufferedImage createBufferedImage(final File file) throws Exception;
    public abstract void writeBufferedImage(final BufferedImage bufferedImage, final String formatName, final File file) throws Exception;
    public abstract BufferedImage resample(final BufferedImage bufferedImage, int width, int height);

    public void setup() {
        File currDirectory = new File("");
        if(currDirectory.getAbsolutePath().endsWith("jipsg")) {
            this.imageDirectory = new File("../images");
        }
        else {
            this.imageDirectory = new File("../../images");
        }
    }

    private File getImageDirectory() {
        return imageDirectory;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Get the given file.
     * @param folderName Name of the folder under "images"
     * @param fileName File name
     * @return the file
     */
    public File getImageFile(String folderName, String fileName) throws IOException {
        File folderFile = new File(getImageDirectory(), folderName);
        File result = new File(folderFile, fileName);
        if (!result.exists() || !result.canRead()) {
            throw new IOException("Can't open/read the following file : " + result.getAbsolutePath());
        }
        return result;
    }

    /**
     * Some dumb sanity check that we have a valid buffered image.
     */
    public void assertValidBufferedImage(BufferedImage bufferedImage) {
        assertNotNull("bufferedImage is null", bufferedImage);
        assertTrue(bufferedImage.getHeight() > 0);
        assertTrue(bufferedImage.getWidth() > 0);
    }

    public File createOutputFileName(String directory, File file, String format) {
        return createOutputFileName(directory, file.getName(), format);
    }

    public File createOutputFileName(String directory, String fileName, String format) {

        File outputDir = new File(new File(new File(new File("./target"), "out"), directory), this.moduleName);

        if(!outputDir.exists()) {
            outputDir.mkdirs();
        }

        return new File(outputDir, fileName + "." + format);
    }
}
