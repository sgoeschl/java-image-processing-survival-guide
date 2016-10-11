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
package org.github.jipsg.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.github.jipsg.imageio.BaseImageIoTest;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * PDF manipulation code based on Apache PDFBox.
 */
public class PdfBoxPreviewTest extends BaseImageIoTest {

    private static final int DPI_72 = 72;
    private static final int START_PAGE = 1;
    private static final int LAST_PAGE = 16;

    @Before
    @Override
    public void setup() {
        super.setup();
        setModuleName("pdfbox");
    }

    @Test
    public void shouldCreatePdfPreviewImages() throws Exception {

        final int imageType = TYPE_INT_RGB;

        // final PDDocument pdDocument = PDDocument.load("./../../pdf/test-large-scan.pdf");
        final PDDocument pdDocument = PDDocument.load("./../../pdf/erste-document-01.pdf");

        final List<BufferedImage> images = toImages(pdDocument, START_PAGE, LAST_PAGE, DPI_72, imageType);

        assertNotNull(images);
        assertFalse(images.isEmpty());
        assertEquals(images.get(0).getType(), imageType);

        for (int i = 0; i < images.size(); i++) {
            File targetImageFile = createOutputFileName("shouldCreatePdfPreviewImages", "page-" + i, "jpeg");
            writeBufferedImage(images.get(i), "jpeg", targetImageFile);
        }
    }

    @SuppressWarnings("unchecked")
    public List<BufferedImage> toImages(PDDocument pdDocument, int startPage, int endPage, int resolution, int imageType) throws Exception {
        final List<BufferedImage> result = new ArrayList<BufferedImage>();
        final List<PDPage> pages = pdDocument.getDocumentCatalog().getAllPages();
        final int pagesSize = pages.size();

        for (int i = startPage - 1; i < endPage && i < pagesSize; i++) {
            PDPage page = pages.get(i);
            PDRectangle cropBox = page.findCropBox();
            float width = cropBox.getWidth();
            float height = cropBox.getHeight();
            int currResolution = calculateResolution(resolution, width, height);
            BufferedImage image = page.convertToImage(imageType, currResolution);

            if (image != null) {
                result.add(image);
            }
        }

        return result;
    }

    /**
     * Calculate the resolution being used assuming that the DPI is used
     * for an A4 page.
     */
    protected int calculateResolution(int dpi, float cropBoxWidth, float cropBoxHeight) {
        int result;

        float maxPoints = Math.max(cropBoxWidth, cropBoxHeight);
        float pointForRequestedResolution = 29.7f * dpi / 2.54f;
        result = Math.round((pointForRequestedResolution * DPI_72 / maxPoints));
        return result;
    }
}
