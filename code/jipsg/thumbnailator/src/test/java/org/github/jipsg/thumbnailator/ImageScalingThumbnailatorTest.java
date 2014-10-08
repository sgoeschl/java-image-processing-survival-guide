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

import junit.framework.TestCase;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import org.github.jipsg.common.image.BufferedImageFactory;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Image manipulation samples.
 */
public class ImageScalingThumbnailatorTest extends TestCase {

    public File createOutputFileName(String directory, String fileName, String format) {

        File outputDir = new File(new File(new File(new File("./target"), "out"), directory), "thumbnailator");

        if(!outputDir.exists()) {
            outputDir.mkdirs();
        }

        return new File(outputDir, fileName + "." + format);
    }


    public File createOutputFileName(String directory, File file, String format) {
        return createOutputFileName(directory, file.getName(), format);
    }

    // ======================================================================
    // Image Scaling
    // ======================================================================

    @Test
    public void testScaleImages() throws Exception {

        final int[] IMAGE_SCALE_DIMENSIONS = new int[] { 575, 199, 80, 60 };
        final float QUALITY = 0.8f;

        File sourceImageDir = new File("../../images/willhaben");
        File[] sourceImageFiles = sourceImageDir.listFiles();

        long currentTime = System.currentTimeMillis();

        for(File sourceImageFile : sourceImageFiles) {

            BufferedImage bufferedImage = ImageIO.read(sourceImageFile);

            for(int size : IMAGE_SCALE_DIMENSIONS) {

                BufferedImage scaledBufferedImage = Thumbnails.of(bufferedImage).
                        width(size).
                        height(size).
                        keepAspectRatio(true).
                        outputQuality(QUALITY).
                        antialiasing(Antialiasing.ON).
                        asBufferedImage();

                bufferedImage = scaledBufferedImage;

                BufferedImageFactory.writeBufferedImage(scaledBufferedImage, "jpeg", createOutputFileName("testScaleImages/" + size, sourceImageFile, "jpeg"));
            }
        }

        long duration = System.currentTimeMillis() - currentTime;

        System.out.println("Scaling one source image to " + IMAGE_SCALE_DIMENSIONS.length + " previews took " + duration/sourceImageFiles.length + " ms");
    }


}
