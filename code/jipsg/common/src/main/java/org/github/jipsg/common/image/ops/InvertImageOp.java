package org.github.jipsg.common.image.ops;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;

public class InvertImageOp implements BufferedImageOp {

    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        RescaleOp op = new RescaleOp(-1.0f, 255f, null);
        return op.filter(src, dest);
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
