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
package org.github.jipsg.sanselan;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.github.jipsg.common.AbstractImageTest;
import org.github.jipsg.common.image.BufferedImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for testing Apache Sanselan.
 */
public class AbstractSanselanTest extends AbstractImageTest {

    @Override
    public void setup() {
        super.setModuleName("sanselan");
        super.setup();
    }

    @Override
    public BufferedImage createBufferedImage(File file) throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        // set optional parameters if you like
        params.put(ImagingConstants.BUFFERED_IMAGE_FACTORY,
                new ManagedImageBufferedImageFactory());
        // read image
        return Imaging.getBufferedImage(file, params);
    }

    @Override
    public void writeBufferedImage(final BufferedImage bufferedImage, final String formatName, final File file) throws Exception {
        final ImageFormat format = getImageFormat(formatName);
        final Map<String, Object> params = new HashMap<String, Object>();
        Imaging.writeImage(bufferedImage, file, format, params);
    }

    @Override
    public void writeBufferedImage(BufferedImage bufferedImage, float quality, int dpi, String formatName, File file) throws Exception {
        writeBufferedImage(bufferedImage, formatName, file);
    }

    /**
     * Some quick and dirty image scaling - please note that for best performance
     * and quality you should use image rescaling libraries.
     */
    @Override
    public BufferedImage resample(BufferedImage bufferedImage, int width, int height) {

        Dimension imageDimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
        Dimension boundaryDimension = new Dimension(width, height);
        Dimension scaledDimension = BufferedImageUtils.getScaledDimension(imageDimension, boundaryDimension);

        double scaleX = scaledDimension.getWidth()/bufferedImage.getWidth();
        double scaleY = scaledDimension.getHeight()/bufferedImage.getHeight();

        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp biLinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return biLinearScaleOp.filter(
                bufferedImage,
                new BufferedImage(scaledDimension.width, scaledDimension.height, bufferedImage.getType()));
    }

    private ImageFormat getImageFormat(String formatName) {

        if("jpg".equalsIgnoreCase(formatName)) {
            return ImageFormats.JPEG;
        }
        else if("jpeg".equalsIgnoreCase(formatName)) {
            return ImageFormats.JPEG;
        }
        else if("png".equalsIgnoreCase(formatName)) {
            return ImageFormats.PNG;
        }
        else {
            throw new IllegalArgumentException("Don't know how to handle : " + formatName);
        }
    }
}
