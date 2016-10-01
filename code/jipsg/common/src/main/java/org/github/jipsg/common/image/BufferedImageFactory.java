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
package org.github.jipsg.common.image;

/**
 * ImageIO-based factory to handle BufferedImage.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BufferedImageFactory {

    public static BufferedImage create(Object source) throws Exception {

        BufferedImage result;

        if (source instanceof File) {
            File sourceFile = (File) source;
            result = ImageIO.read(sourceFile);
        } else if (source instanceof String) {
            File sourceFile = new File(source.toString());
            result = ImageIO.read(sourceFile);
        } else {
            throw new IllegalArgumentException("Don't know how to handle : " + source.getClass().getName());
        }

        return result;
    }

    public static boolean writeBufferedImage(BufferedImage bufferedImage, String formatName, File file) throws Exception {
        System.out.println("Saving " + file.getPath());
        return ImageIO.write(bufferedImage, formatName, file);
    }
}
