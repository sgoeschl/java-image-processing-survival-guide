# The Java Image Processing Survival Guide

Authors: Siegfried Goeschl, Harald Kuhr

Keywords: Java, Image Scaling, Image Conversion, ImageMagick, JMagick, PDFBox, Java Advanced Imaging API, JAI, TwelveMonkeys ImageIO Plugins

## Abstract

This paper provides real-life experience in replacing an ImageMagick/JMagick image conversion & scaling with a pure Java implementation covering Apache PDFBox, Java Advanced Imaging API (JAI), TwelveMonkey ImageIO plugins and different Java-based image-scaling libraries. 

## 1. The Customer Call

It was one of those perfect days starting with a promising customer call - "How difficult is it to replace our ImageMagick/JMagick-based image processing with a Java library?!". Java has graphics support built in for ages and additionally there is the Java Advanced Imaging API around - "Well, depends on your requirements but not too difficult". 

What are the customer's problems and requirements? ImageMagick is a native image-processing library which is wrapped by JMagick using JNI (Java Native Interface) to expose a Java interface but this approach has a few short-comings

* Installing the ImageMagick binaries for different target platforms is not straight-forward
* JMagick does not support the most current ImageMagick libraries which contains must-have bug fixes and improvements
* Getting ImageMagick/JMagick to run on Mac OS failed miserably which left the Macbook-based developers unhappy
* Any ImageMagick exceptions escaping through the JNI layer causes the JVM to terminate

On the other hand ImageMagick is a powerful and field-proven piece of software being used to implement the following requirements

* Processing 5 million user-uploaded images per month
* Converting arbitrary image formats such as PNG, TIFF and BMP into JPEG image
* Converting PDF documents to a preview image using GhostScript under the hood
* Creating five scaled-down images with varying resolution based on the user-uploaded image
* Efficiently handling a peak load of 45 image per minute and server
* Apply image sharpening to improve quality

In short - it looks somehow difficult now.

## 2. Divide and Conquer

Tackling a difficult problem requires some "divide and conquer" to keep your brain operational

* Use Apache PDFBox for PDF to image conversion
* Use JAI for converting image formats to JPEG
* Use a Java-based business-friendly library for scaling the images
* Hide the mechanics behind a Java implementation to be exposed to the caller

In short - it looks like a plan.

## 3. Creating PDF Preview Image

Quoting from the source - "The Apache PDFBox™ library is an open source Java tool for working with PDF documents. This project allows creation of new PDF documents, manipulation of existing documents and the ability to extract content from documents. Apache PDFBox also includes several command line utilities" (see http://pdfbox.apache.org).

The task at hand is a PDF to JPEG conversion which is perfectly doable but not straight-forward since the functionality is implemented as part of the PDFBox command line tools (see http://pdfbox.apache.org/commandline/#pdfToImage). With little effort the functionality was extracted in the following Java code snippet

```
List<BufferedImage> pdfToImage(Object source, int startPage, int endPage, int resolution, float quality) {

    List<BufferedImage> result = new ArrayList<BufferedImage>();
    PDDocument pdDocument = loadPDDocument(source);
    List<PDPage> pages = pdDocument.getDocumentCatalog().getAllPages();

    for (int i = startPage - 1; i < endPage && i < pages.size(); i++) {
        PDPage page = pages.get(i);
        BufferedImage image = page.convertToImage(BufferedImage.TYPE_INT_RGB, resolution);
        result.add(image);
    }
    return result;
}
```

Creating the PDF preview can be done using the following call requesting a 72 DPIs (dots per inch) with good image quality 

```
BufferImage pdfPreviewImage = pdfToImage(pdfFile, 1, 2, 72, 0.8f).get(0);
```

In short - that rocks.

## 4. Convert Image Formats to JPEG

The heavy lifting of image format conversion is provided by the Java Advanced Image (JAI) API - "The Java Advanced Imaging API provides a set of object-oriented interfaces that support a simple, high-level programming model which lets you manipulate images easily" (http://www.oracle.com/technetwork/java/javase/tech/jai-142803.html).

* TIFF is not supported transparently using Java ImageIO

## 5. Scaling Images Efficiently

Why bother with efficiency up-front when "premature optimization is the root of all evil"? But was Donald Knuth actually says - "We should forget about small efficiencies, say about 97% of the time: premature optimization is the root of all evil. Yet we should not pass up our opportunities in that critical 3%." Assuming an peak load of 45 user-uploaded images per minute and knowing the costs of adding additional server hardware it is assumed that we are in the critical 3% ballpark.

Image scaling efficiency can be achieved on two levels

* Use a Java-based image scaling library implementing all of the best practices
* Reuse scaled images to create the next smaller image

When searching the Internet you find a plethora of image scaling libraries which makes the choice difficult. In order to make a qualified decision a test driver was implemented to convert a fixed set of test images using the following libraries

* imgscalr (see http://www.thebuzzmedia.com/software/imgscalr-java-image-scaling-library/)
* thumbnailator (see https://code.google.com/p/thumbnailator/) 
* java-image-scaling (see https://code.google.com/p/java-image-scaling/)

The good news are that all of them work and show comparable performance so the decision comes down to image quality and additional features. At the end "thumbnailator" was chosen due to its watermark support - this feature is not immediately needed but it is good to know that watermarking can be added using two lines of code (see https://code.google.com/p/thumbnailator/wiki/Examples#Creating_a_thumbnail_with_a_watermark).

A naive implementation could use the original image to create the five scaled down instances but the requires CPU time depends mostly on the size of the source and target image.

The CPU time needed for scaling depends mostly on the size of the source and target image and using a naive implementation could use the original image to create the five scaled down instances. Consequently the production code uses an already scaled image to create the next smaller image which has a dramatic impact on performance as shown below

[TBD]

## 6. Test With Real-Life Data

When the overall implementation was roughly finished a few hundred production images were used for a more thorough testing and the results were not promising - as expected. A couple of images were either not converted at all or the resulting images were severely broken.

A closer look revealed the following problems

* Alpha-channel handling is broken in Java (http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6371389)
* CMYK color space is not supported
* missing/wrong image metadata causes NPE
* Memory usage of conversion - Decompression bombs - http://www.aerasec.de/security/advisories/decompression-bomb-vulnerability.html
* Unsupported JPEG compression formats

## 7. In the Need of Twelve Monkeys





