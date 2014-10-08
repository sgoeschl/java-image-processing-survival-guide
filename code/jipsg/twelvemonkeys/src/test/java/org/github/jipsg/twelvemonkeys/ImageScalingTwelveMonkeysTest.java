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

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Load various images.
 */
public class ImageScalingTwelveMonkeysTest extends BaseTwelveMonkeysTest {

    @Before
    public void setup() {
        super.setup();
    }

    // ======================================================================
    // Image Scaling
    // ======================================================================

    @Test
    public void testScaleImages() throws Exception {

        final int[] IMAGE_SCALE_DIMENSIONS = new int[] { 575, 199, 80, 60 };

        File sourceImageDir = new File("../../images/willhaben");
        File[] sourceImageFiles = sourceImageDir.listFiles();

        long currentTime = System.currentTimeMillis();

        for(File sourceImageFile : sourceImageFiles) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);

            for(int size : IMAGE_SCALE_DIMENSIONS) {
                BufferedImage scaledBufferedImage = resample(bufferedImage, size, size);
                bufferedImage = scaledBufferedImage;
                writeBufferedImage(scaledBufferedImage, "jpeg", createOutputFileName("testScaleImages/" + size, sourceImageFile, "jpeg"));
            }
        }

        long duration = System.currentTimeMillis() - currentTime;

        System.out.println("Scaling one source image to " + IMAGE_SCALE_DIMENSIONS.length + " previews took " + duration/sourceImageFiles.length + " ms");
    }

}
