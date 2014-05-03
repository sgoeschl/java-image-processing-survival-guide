# The Java Image Processing Survival Guide

Authors: Siegfried Goeschl, Harald Kuhr

Keywords: Java, Image Scaling, Image Conversion, ImageMagick, JMagick, PDFBox, Java Advanced Imaging API, JAI, ImageIO API, TwelveMonkeys ImageIO Plugins

## Abstract

This paper provides real-life experience in replacing an ImageMagick/JMagick image conversion & scaling with a pure Java implementation covering Apache PDFBox, Java Advanced Imaging API (JAI), TwelveMonkeys ImageIO plugins and different Java-based image-scaling libraries. 

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
* Use ImageIO and JAI for converting image formats to JPEG
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

[I think the we should promote using the ImageIO API over JAI :-)]

-The heavy lifting of image format conversion is provided by the Java Advanced Image (JAI) API - "The Java Advanced Imaging API provides a set of object-oriented interfaces that support a simple, high-level programming model which lets you manipulate images easily"- (http://www.oracle.com/technetwork/java/javase/tech/jai-142803.html).

The heavy lifting of image format conversion is provided by the Java ImageIO API (the javax.imageio package). 

"This package contains the basic classes and interfaces for describing the contents of image files, including metadata and thumbnails (IIOImage); for controlling the image reading process (ImageReader, ImageReadParam, and ImageTypeSpecifier) and image writing process (ImageWriter and ImageWriteParam); for performing transcoding between formats (ImageTranscoder), and for reporting errors (IIOException)." (http://docs.oracle.com/javase/7/docs/api/javax/imageio/package-summary.html#package_description)

In theory, it could be as simple as:

    File/InputStream inFile = ...;
    File/OutputStream outFile = ...;

    BufferedImage image = ImageIO.read(inFile);
    
    if (!ImageIO.write(image, format, outFile)) {
        // Handle image not written case
    }

* TIFF is not supported out of the box using Java ImageIO

This is where JAI comes in. Supports many TIFF flavors, but requires native libraries installed to do so. The fall-back Java version is limitted.

* Many JPEGs can't be out of the box read using ImageIO

JAI has some extra support, but many JPEG images can't be read using JAI either. Requires native libraries.
[Ok, might come back to this later]

* Also, you typically want more control over meta data and compression options

    // TODO: More elaborate example, adapt from twelvemonkeys README.md :-)

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

[Not sure if you are aware of this, but the TwelveMonkeys library also comes with its own resampling module, that should create very high quality resampled results. The algorithms are very similar to those of ImageMagick and others (based on the same original C source), and is a Java port of the algorithms found in the "ancient" book Graphic Gems III (Academic Press, 1994.). 
I've never measured performance against the options you considered, but I believe the quality should be at least as good. Probably slower. Also, it comes with Servlet filters for a completely different on-the-fly scaling approach, that has been used with success on many projects, but I'm not sure if we want to go too much in-depth on that.]


## 6. Test With Real-Life Data

When the overall implementation was roughly finished a few hundred production images were used for a more thorough testing and the results were not promising - as expected. A couple of images were either not converted at all or the resulting images were severely broken.

A closer look revealed the following problems

* Alpha-channel handling is broken in Java (http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6371389)
* CMYK color space is not supported
* Missing/wrong image metadata causes NPE and decoding Exceptions
* Memory usage of conversion - Decompression bombs - http://www.aerasec.de/security/advisories/decompression-bomb-vulnerability.html
* Unsupported JPEG compression formats

## 7. In the Need of Twelve Monkeys

The Twelvemonkeys ImageIO project was created to solve many of the problems mentioned above. It was started when I was working for a web CMS (Content managment system) vendor [Escenic, but we might skip mentioning names], that created CMS solutions targeted for the media insdustry (newspapers, broadcast). It's a web-centric CMS, and 
the initial version was created, because we/the web content management system needed support for more image formats. 

### History

#### 1.x version
* Java (prior to J2SE  1.4) had only limitted support for reading JPEG, PNG and GIF
* And more importantly, no official API for writing images existed.
* The APIs that existed was based around the java.awt.Image class, and not the more versatile BufferedImage class.

* JMagick had support for many formats, but had no stream support, wich is very bad when working with web architectures. Temporary writing to disk increases the response time, and slows down overall performance. Plus the lack of binary compatibiltiy from version to version and being a nightmare to install.
* JAI was around, but at the time reading/writing images using JAI requrired a different API. 
* None of the libraries had proper support for PSD format (JMagick didn't support reading individual layers)

The initial version had a simple read/write API, that dealt with BufferedImages. 

#### 2.x version
Nowadays, the world is a little different, thus the goals have changed: 

* ImageIO (javax.imageio package) has become the standard API to read/write images in Java. 
* Thus the goal has been to help the Java platform read as many formats, as complete as possible, using the ImageIO API.
* JAI ImageIO has rich support for formats:
	* However, it has bugs, and when you hit them: No-one listens. Seems to have no support from Oracle. 
	* No official updates for the last years (last release was ...)
    * Dying community for the same reasons
    * Requires native libraries for full format support/best performance. Which means more difficult installation. And worse, no native libraries exist for many modern popular architectures (ie, 64 bit architectures)
    * License issues (http://stackoverflow.com/questions/23166231/java-advanced-imaging-license). 
    * Some parts seems to be open source, some parts not (like the native code)
    * Multiple, semi-overlapping forks (GitHub, Google code, private) with the same + its own license issues, making matters worse...
* That said, if you can deal with the license, and don't run into the bugs mentioned, JAI is still the most complete and mature packages for dealing with images in Java.
    
* JMagick hasn't changed much. TwelveMonkeys has wrapper readers/writers to allow users to use ImageMagicks rich format support, while using the ImageIO API. However, due to the nature of the library, it will never have the same performance or rich features.

* Apache Commons Imaging has emerged from Sanselan. A quite mature library, but unfortunately has its own API. Combined with the the fact that it doesn't support all the same formats as ImageIO, this means you either have to program against multiple APIs, create your own wrappers or even your own abstraction API on top of these multiple APIs.

- Conclusion: We need something better! We deserver better. :-)

#### 3.0 version (current)
 - To be released "very soon" (development/pre-release versions has been in use at customer sites for 2-3 years)
 - Very much improved JPEG (read) support (CMYK handling, improved color profile handling, JFIF and EXIF support) over the standard JPEGImageReader that comes with the (Oracle) JRE.
    - Solves most of the issues that usually crops up at StackOverflow, "Exception reading JPEG with colorspace X", "Inconsistent metadata when reading JPEG" etc.
 - Full TIFF baseline support + most de facto standards + many popular extensions (read-only for now)
 - Support for CMYK color space and proper ICC profile based CMYK to RGB conversion support throughout the project

#### 3.1 [this is all unofficial and this is becoming too much like a commercial, so I'm fine with skipping this part ]
 - CMYK JPEG write support, better JPEG metadata support
 - TIFF write support, TIFF metadata support
 - PNM read/write, metadata support
 - CR2 support (limited read, thumbnail), TIFF/EXIF metadata
 - (maybe JPEG lossless reading)


### TwevelMonkeys JPEG plug-in in aprticular
 Goal: Read everything that can be read by other software*. 
 
 Currently, not doing too bad. 
 - However, because the base JPEG decoding is done by the same JNI code that the standard Java JPEGImageReader uses, we "only" support Huffman encoded, lossy JPEG, either baseline or progressive (ie, no Arithmetic encoding, no lossless compression). These are the compressions used in roughly 90% of all JPEGs in the known universe, and most web browsers supports onle these types of JPEG compression as well, so in a web environment it is not much of a problem.
 
 *) Other software here, typically means libJPEG, but also Adobe Photoshop and others. 

