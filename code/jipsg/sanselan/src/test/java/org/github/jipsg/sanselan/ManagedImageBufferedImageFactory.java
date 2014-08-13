package org.github.jipsg.sanselan;

import org.apache.commons.imaging.common.IBufferedImageFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: sgoeschl
 * Date: 13/08/14
 * Time: 20:40
 * To change this template use File | Settings | File Templates.
 */
public class ManagedImageBufferedImageFactory implements
        IBufferedImageFactory {

    public BufferedImage getColorBufferedImage(final int width, final int height,
                                               final boolean hasAlpha) {
        final GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        final GraphicsDevice gd = ge.getDefaultScreenDevice();
        final GraphicsConfiguration gc = gd.getDefaultConfiguration();
        return gc.createCompatibleImage(width, height,
                Transparency.TRANSLUCENT);
    }

    public BufferedImage getGrayscaleBufferedImage(final int width, final int height,
                                                   final boolean hasAlpha) {
        return getColorBufferedImage(width, height, hasAlpha);
    }
}
