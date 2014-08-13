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
 * Created with IntelliJ IDEA.
 * User: sgoeschl
 * Date: 13/08/14
 * Time: 21:43
 * To change this template use File | Settings | File Templates.
 */

import org.github.jipsg.common.image.ops.AlphaChannelImageOp;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.util.Collection;

/**
 * Contains ready-to use image operations without additional dependencies.
 */
public class BufferedImageOperations {

    public static BufferedImage fillTransparentPixel(BufferedImage src) {

        // Since JDK 5 the JPG conversion messes up images with alpha-channels
        // therefore fill the alpha-channel with white pixels

        if(src.getColorModel().hasAlpha()) {
            return apply(src, new AlphaChannelImageOp());
        }
        else {
            return src;
        }
    }

    public static BufferedImage apply(BufferedImage source, Collection<BufferedImageOp> bufferedImageOps) {
        BufferedImage result = source;

        for (BufferedImageOp bufferedImageOp : bufferedImageOps) {
            result = bufferedImageOp.filter(result, null);
        }

        return result;
    }

    public static BufferedImage apply(BufferedImage source, BufferedImageOp... bufferedImageOps) {
        BufferedImage result = source;

        for (BufferedImageOp bufferedImageOp : bufferedImageOps) {
            result = bufferedImageOp.filter(result, null);
        }

        return result;
    }
}