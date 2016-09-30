package org.github.jipsg.common.image.ops;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

public class AlphaChannelImageOp implements BufferedImageOp {

    /**
     * Fill the alpha-channel with white pixels.
     */
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        BufferedImage result = new BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(src, 0, 0, result.getWidth(), result.getHeight(), Color.WHITE, null);
        return result;
    }

    public Rectangle2D getBounds2D(BufferedImage src) {
        return null;
    }

    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
        return null;
    }

    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        return null;
    }

    public RenderingHints getRenderingHints() {
        return null;
    }
}
