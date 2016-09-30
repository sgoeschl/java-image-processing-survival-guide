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

import com.twelvemonkeys.image.ResampleOp;
import org.github.jipsg.common.AbstractImageTest;
import org.github.jipsg.common.image.BufferedImageUtils;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Base class for testing TwelveMonkey library.
 */
public class BaseTwelveMonkeysTest extends AbstractImageTest {

    @Override
    public void setup() {
        super.setModuleName("twelvemonkeys");
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
    public void writeBufferedImage(BufferedImage bufferedImage, float quality, int dpi, String formatName, File file) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BufferedImage resample(BufferedImage bufferedImage, int width, int height) {
        Dimension imageDimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
        Dimension boundaryDimension = new Dimension(width, height);
        Dimension scaledDimension = BufferedImageUtils.getScaledDimension(imageDimension, boundaryDimension);
        ResampleOp resampleOp = new ResampleOp(scaledDimension.width, scaledDimension.height);
        return resampleOp.filter(bufferedImage, null);
    }
}
