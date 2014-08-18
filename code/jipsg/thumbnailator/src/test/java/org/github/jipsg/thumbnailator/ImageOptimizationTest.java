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

import com.jhlabs.image.LevelsFilter;
import com.jhlabs.image.SharpenFilter;
import org.github.jipsg.common.image.BufferedImageOperations;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Image optimization samples.
 */
public class ImageOptimizationTest extends AbstractImageIoTest {

    @Before
    public void setup() {
        super.setup();
        setModuleName("thumbnailator");
    }

    // ======================================================================
    // Image format conversion
    // ======================================================================

    @Test
    public void testImageOptimization() throws Exception {

        BufferedImage result;
        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("samples", "under-exposed-black-white-image.jpg"));

        for(File sourceImageFile : sourceImageFileList) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);

            BufferedImage leveledBufferedImage = new AutoCorrectionFilter().filter(bufferedImage, null);
            BufferedImage sharpenedBufferedImage = new SharpenFilter().filter(leveledBufferedImage, null);

            File targetImageFile = createOutputFileName("testImageOptimization", sourceImageFile, formatName);
            writeBufferedImage(sharpenedBufferedImage, formatName, targetImageFile);
        }
    }

}
