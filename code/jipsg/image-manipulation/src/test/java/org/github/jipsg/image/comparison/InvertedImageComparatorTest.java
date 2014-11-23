package org.github.jipsg.image.comparison;

import org.github.jipsg.common.image.BufferedImageFactory;
import org.github.jipsg.imageio.BaseImageIoTest;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by sgoeschl on 18/11/14.
 */
public class InvertedImageComparatorTest extends BaseImageIoTest {


    @Override
    public void setup() {
        super.setModuleName("image-processing");
        super.setup();
    }

    @Test
    public void testIdenticalImage() throws Exception {

        final BufferedImage bufferedImage1 = BufferedImageFactory.create("../../images/samples/open-office-01.pdf.300.0.png");
        final BufferedImage bufferedImage2 = BufferedImageFactory.create("../../images/samples/open-office-01.pdf.300.0.png");
        final InvertingImageComparator imageComparator = new InvertingImageComparator();
        final BufferedImage diffImages = imageComparator.compare(bufferedImage1, bufferedImage2);
        BufferedImageFactory.writeBufferedImage(diffImages, "png", new File("./target/inverted-identical.png"));
    }

    @Test
    public void testModifiedImage() throws Exception {

        final BufferedImage bufferedImage1 = BufferedImageFactory.create("../../images/samples/open-office-01.pdf.300.0.png");
        final BufferedImage bufferedImage2 = BufferedImageFactory.create("../../images/samples/open-office-02.pdf.300.0.png");
        final InvertingImageComparator imageComparator = new InvertingImageComparator();
        final BufferedImage diffImages = imageComparator.compare(bufferedImage1, bufferedImage2);
        BufferedImageFactory.writeBufferedImage(diffImages, "png", new File("./target/inverted-modified.png"));
    }

}
