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
package org.github.jipsg.jai;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Load various images.
 */
public class ImageResamplingJaiTest extends AbstractJaiTest {

    @Before
    public void setup() {
        super.setup();
    }

    /**
     * Load various image types and re-sample them to 640 x 480.
     */
    @Test
    public void testResamplingImagesAsJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
        sourceImageFileList.add(getImageFile("png", "marble.png"));
        // JAI fails with java.lang.IllegalArgumentException: Unknown image type 0
        // sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));
        sourceImageFileList.add(getImageFile("gif", "marble.gif"));

        for(File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            BufferedImage resampledBufferedImage = resample(bufferedImage, 640, 480);
            assertValidBufferedImage(resampledBufferedImage);
            File targetImageFile = createOutputFileName("testResamplingImagesAsJpeg", sourceImageFile, formatName);
            writeBufferedImage(resampledBufferedImage, formatName, targetImageFile);
        }
    }
    @Test
    public void testWriteImageWithQualityAndDpi() throws Exception {

        File targetImageFile;
        String formatName = "jpeg";

        File sourceImageFile = getImageFile("jpg", "marble.jpg");
        BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
        assertValidBufferedImage(bufferedImage);
        BufferedImage resampledBufferdImage = resample(bufferedImage, 640, 640);
        assertValidBufferedImage(resampledBufferdImage);

        // write as JPEG
        targetImageFile = createOutputFileName("testWriteImageWithQualityAndDpi", sourceImageFile, "jpg");
        writeBufferedImage(resampledBufferdImage, 0.10f, 123, formatName, targetImageFile);
    }

}
