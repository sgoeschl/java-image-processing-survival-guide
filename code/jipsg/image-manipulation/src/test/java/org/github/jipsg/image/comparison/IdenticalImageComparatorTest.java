package org.github.jipsg.image.comparison;

import org.github.jipsg.common.image.BufferedImageFactory;
import org.github.jipsg.imageio.BaseImageIoTest;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.assertNull;

/**
 * Created by sgoeschl on 18/11/14.
 */
public class IdenticalImageComparatorTest extends BaseImageIoTest {


    @Override
    public void setup() {
        super.setModuleName("image-processing");
        super.setup();
    }

    @Test
    public void testIdenticalImage() throws Exception {

        final BufferedImage bufferedImage1 = BufferedImageFactory.create("../../images/samples/open-office-01.pdf.300.0.png");
        final BufferedImage bufferedImage2 = BufferedImageFactory.create("../../images/samples/open-office-01.pdf.300.0.png");
        final IdenticalImageComparator identicalImageComparator = new IdenticalImageComparator();
        assertNull(identicalImageComparator.diffImages(bufferedImage1, bufferedImage2));
    }

    @Test
    public void testModifiedImage() throws Exception {

        final BufferedImage bufferedImage1 = BufferedImageFactory.create("../../images/samples/open-office-01.pdf.300.0.png");
        final BufferedImage bufferedImage2 = BufferedImageFactory.create("../../images/samples/open-office-02.pdf.300.0.png");
        final IdenticalImageComparator identicalImageComparator = new IdenticalImageComparator();
        final BufferedImage bufferedImage = identicalImageComparator.diffImages(bufferedImage1, bufferedImage2);
        BufferedImageFactory.writeBufferedImage(bufferedImage, "png", new File("./target/indentical-modified.png"));
    }

}
