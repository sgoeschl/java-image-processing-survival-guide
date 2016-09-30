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

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import org.github.jipsg.common.AbstractImageTest;
import org.github.jipsg.common.image.BufferedImageUtils;
import org.w3c.dom.Element;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Base class for testing Java Advanced Imaging.
 */
public class AbstractJaiTest extends AbstractImageTest {

    @Override
    public void setup() {
        super.setModuleName("jai");
        super.setup();
    }

    @Override
    public BufferedImage createBufferedImage(File file) throws Exception {
        return ImageIO.read(file);
    }

    @Override
    public void writeBufferedImage(BufferedImage bufferedImage, String formatName, File file) throws Exception {
        ImageIO.write(bufferedImage, formatName, file);
    }

    @Override
    public void writeBufferedImage(BufferedImage bufferedImage, float quality, int dpi, String formatName, File targetFile) throws Exception {

        if (formatName.equalsIgnoreCase("jpg") || formatName.equalsIgnoreCase("jpeg")) {
            JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix(formatName).next();
            ImageWriteParam writeParam = imageWriter.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = imageWriter.getDefaultImageMetadata(typeSpecifier, writeParam);

            if (formatName.equalsIgnoreCase("jpg") || formatName.equalsIgnoreCase("jpeg")) {

                Element tree = (Element) metadata.getAsTree("javax_imageio_jpeg_image_1.0");
                Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
                jfif.setAttribute("Xdensity", Integer.toString(dpi));
                jfif.setAttribute("Ydensity", Integer.toString(dpi));
                jfif.setAttribute("resUnits", "1");
                metadata.setFromTree("javax_imageio_jpeg_image_1.0", tree);
            }

            if (quality >= 0 && quality <= 1f) {

                JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
                jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
                jpegParams.setCompressionQuality(quality);

            }

            FileOutputStream os = new FileOutputStream(targetFile);
            final ImageOutputStream stream = ImageIO.createImageOutputStream(os);

            try {
                imageWriter.setOutput(stream);
                imageWriter.write(metadata, new IIOImage(bufferedImage, null, metadata), writeParam);
            } finally {
                stream.close();
            }
        } else {
            writeBufferedImage(bufferedImage, formatName, targetFile);
        }
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

        double scaleX = scaledDimension.getWidth() / bufferedImage.getWidth();
        double scaleY = scaledDimension.getHeight() / bufferedImage.getHeight();

        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp biLinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return biLinearScaleOp.filter(
                bufferedImage,
                new BufferedImage(scaledDimension.width, scaledDimension.height, bufferedImage.getType()));
    }
}
