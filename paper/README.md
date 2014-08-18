# The Java Image Processing Survival Guide

Authors: Siegfried Goeschl, Harald Kuhr

Keywords: Java, Image Scaling, Image Conversion, ImageMagick, JMagick, PDFBox, Java Advanced Imaging API, JAI, ImageIO API, TwelveMonkeys ImageIO Plugins

## Abstract

This paper provides real-life experience in replacing an ImageMagick/JMagick image conversion & scaling with a pure Java implementation covering Apache PDFBox, Java Advanced Imaging API (JAI), TwelveMonkeys ImageIO plug ins and different Java-based image-scaling libraries. 

## 1. The Customer Inquiry

A day starting with a customer inquiry - "How difficult is it to replace ImageMagick with a Java image processing library?!". A few seconds to ponder over the question - Java has graphics support built in, there is the Java Advanced Imaging API around and some image processing shouldn't be too hard after all - "Well, it depends but not too difficult". 

Let's have a closer look at the customer's system - it is a classified ads platform allowing their users to create adverts and upload images over the browser or native apps as shown below

![Advert Mobile Web](./images/willhaben-advert-mobile.png)
![Image Gallery Web](./images/willhaben-advert-desktop.png)

The heavy lifting is done by ImageMagick - a native image-processing command-line tool available for most platforms. JMagick exposes the native code over JNI (Java Native Interface) but this approach has a few short-comings

* Any ImageMagick exceptions escaping through the JNI layer causes the JVM to terminate
* Installing the ImageMagick binaries for different target platforms requires additional work
* Getting ImageMagick/JMagick to run on Mac OS failed miserably which left the Mac OS X developers unhappy
* JMagick is no longer actively developed and does not support all ImageMagick features

On the other hand ImageMagick is a powerful and field-proven software used by the customer for many years in order to

* Process 5 million user-uploaded images per month
* Convert arbitrary image formats such as PNG, TIFF and BMP into JPEG
* Convert PDF documents to a preview image using GhostScript under the hood
* Create five thumbnail images with varying resolution based on the user-uploaded image
* Handle a peak load of 45 image per minute and server (during batch processing)

Regarding the customer inquiry - somehow replacing ImageMagick looks difficult now.

## 2. Java 2D API Primer

There are a number of common tasks when working with images

* Reading an external GIF, PNG, JPEG image into a *BufferedImage* instance
* Apply one or more buffered image operations such as
    * Affine transformation preserving the "straightness" and "parallelness" of lines, e.g. rotation or scaling
    * Color conversation
    * Convolution operation, e.g. blurring or sharpening of an image
    * Lookup operation to translate source pixel into destination pixels colors using a lookup table, e.g. inverting the colors
* Writing the *BufferedImage* to an external GIF, PNG, or JPEG image

The Java 2D API uses an *Service Provider Interface (SPI)* to utilize image readers & writers provided by extension libraries, e.g. the *Java Advanced Imaging (JAI)* library exports JPEG2000 and TIFF reader/writers.

TODO: explain BufferedImage

## 3. The Road Ahead

Tackling this project requires some "divide and conquer" to keep the mind focused

* Use Apache PDFBox for PDF to image conversion
* Use ImageIO and JAI for converting image formats to JPEG
* Use a Java-based open-source library for creating thumbnail efficiently
* Hide the mechanics behind a Java implementation to be exposed to the caller
* Do some thorough testing with real-life images
* Roll out the new implementation incrementally across the production servers

## 4. Creating PDF Preview Image

Nowadays there is a strong focus on beautiful user interfaces - a simple list of file names and icons is not feasible in the Web 2.0 era. Converting a PDF to a list of images is done using *Apache PDFBox* - an open source Java tool to work with PDF documents. The server-side conversion requires a bit of code since the functionality is implemented as part of the PDFBox command line tools (see http://pdfbox.apache.org/commandline/#pdfToImage). With little effort the functionality was extracted in the Java code snippet shown below

```
List<BufferedImage> pdfToImage(
    Object source, int from,
    int to, int dpi,
    float quality) 
{
    PDDocument pdDocument = loadPDDocument(source);
    List<BufferedImage> result = new ArrayList<BufferedImage>();
    List<PDPage> pages = pdDocument.getDocumentCatalog().getAllPages();

    for (int i = from - 1; i < to && i < pages.size(); i++) 
    {
        PDPage page = pages.get(i);
        BufferedImage image = page.convertToImage(BufferedImage.TYPE_INT_RGB, dpi);
        result.add(image);
    }
    return result;
}
```



## 5. Convert Image Formats to JPEG

[Harald: I think the we should promote using the ImageIO API over JAI :-)]
[Sigi: I would like to use my original approach to hightlight the short-comings]

The heavy lifting of image format conversion is provided by the Java ImageIO API (the javax.imageio package): "This package contains the basic classes and interfaces for describing the contents of image files, including metadata and thumbnails (IIOImage); for controlling the image reading process (ImageReader, ImageReadParam, and ImageTypeSpecifier) and image writing process (ImageWriter and ImageWriteParam); for performing transcoding between formats (ImageTranscoder), and for reporting errors (IIOException)." (http://docs.oracle.com/javase/7/docs/api/javax/imageio/package-summary.html#package_description)

In theory, it could be as simple as:

```
File/InputStream inFile = ...;
File/OutputStream outFile = ...;

BufferedImage image = ImageIO.read(inFile);

if (!ImageIO.write(image, format, outFile)) {
    // Handle image not written case
}
```

The reality looks not so simple any more

* TIFF format is not supported out of the box using Java ImageIO
* Some JPGs images can't be read using plain ImageIO
* JAI has some extra JPG support but requires extra and/or native libraries
* Some control is required regarding JPEG metadata and compression options

# 6. Setting JPEG Metadata

A common requirement for writing JPEGs is setting the compression options and/or DPIs this can be done with the code snippet shown below

```
int dpi = 300:
float quality = 0.8f;

// some code loading the image

IIOMetadata metadata = jpegImageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(source), null);
ImageWriteParam writeParam = imageWriter.getDefaultWriteParam();

// set the DPI in the image metadata
Element tree = (Element) metadata.getAsTree("javax_imageio_jpeg_image_1.0");
NodeList nodeList = tree.getElementsByTagName("app0JFIF");
Element jfif = (Element) nodeList.item(0);
jfif.setAttribute("Xdensity", Integer.toString(dpi));
jfif.setAttribute("Ydensity", Integer.toString(dpi));
jfif.setAttribute("resUnits", "1");
metadata.setFromTree("javax_imageio_jpeg_image_1.0", tree);

// set the compression to use
writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
writeParam.setCompressionQuality(quality);

```
 
Looking at the code snippets raises a few concerns

* A fair amount of knowledge is required to accomplish common tasks
* The code is prone to NPEs when the JPEG metadata is not available


## 7. Creating Thumbnails Efficiently

Why bother with efficiency up-front when "premature optimization is the root of all evil"? But was Donald Knuth actually says - "We should forget about small efficiencies, say about 97% of the time: premature optimization is the root of all evil. Yet we should not pass up our opportunities in that critical 3%." Assuming an peak load of 45 user-uploaded images per minute and knowing the costs of adding additional server hardware it is assumed that we are in the critical 3% ballpark.

Image scaling efficiency can be achieved on two levels

* Use a Java-based image scaling library implementing all of the best practices
* Reuse scaled images to create the next smaller image

When searching the Internet you find a plethora of image scaling libraries which makes the choice difficult. In order to make an informed decision a test driver was implemented to convert a fixed set of test images using the following libraries

* imgscalr (see http://www.thebuzzmedia.com/software/imgscalr-java-image-scaling-library/)
* thumbnailator (see https://code.google.com/p/thumbnailator/) 
* java-image-scaling (see https://code.google.com/p/java-image-scaling/)

The good news are that all of them work and show comparable performance so the decision comes down to image quality and additional features. At the end "thumbnailator" was chosen due to its watermark support - this feature is not immediately needed but it is good to know that watermarking can be added using two lines of code (see https://code.google.com/p/thumbnailator/wiki/Examples#Creating_a_thumbnail_with_a_watermark).

A naive implementation could use the original image to create the five scaled down instances but the requires CPU time depends mostly on the size of the source and target image.

The CPU time needed for scaling depends mostly on the size of the source and target image and using a naive implementation could use the original image to create the five scaled down instances. Consequently the production code uses an already scaled image to create the next smaller image which has a dramatic impact on performance as shown below

[TBD]

[Harald: Not sure if you are aware of this, but the TwelveMonkeys library also comes with its own resampling module, that should create very high quality resampled results. The algorithms are very similar to those of ImageMagick and others (based on the same original C source), and is a Java port of the algorithms found in the "ancient" book Graphic Gems III (Academic Press, 1994.). 
I've never measured performance against the options you considered, but I believe the quality should be at least as good. Probably slower. Also, it comes with Servlet filters for a completely different on-the-fly scaling approach, that has been used with success on many projects, but I'm not sure if we want to go too much in-depth on that.]
[Sigi: no I was not aware of that - we can add a chapter covering "on-the-fly scaling"]


## 8. Test With Real-Life Data

When the overall implementation was roughly finished a few hundred production images were used for a more thorough testing and the results were not promising - as expected. A couple of images were either not converted at all or the resulting images were severely broken.

A few hours later the the following problems were identified:

* GIF & PNG alpha-channels
* CMYK color space
* Missing/wrong image metadata causes NPE and decoding Exceptions
* Memory usage of conversion - Decompression bombs - http://www.aerasec.de/security/advisories/decompression-bomb-vulnerability.html
* Unsupported JPEG compression formats

### 8.x Alpha-Channels

The alpha channel stores transparency information and is used for the GIF and PNG image format on web pages so that images appear to have an arbitrary shape even on a non-uniform background (see http://en.wikipedia.org/wiki/Channel_(digital_image)). 

Unfortunately The alpha-channel handling is broken in Java Advanced Imaging (JAI), (http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6371389) as shown using one of the production images

![Alph-Channel Before](./images/alpha-channel-before.png)
![Alph-Channel After](./images/alpha-channel-after.png)

[More stuff to come ...]

[You should probably explain a little better what is the issue here. In my experience, alhpa channel in general works really well in Java, however, there have been known issues with 4 channel JPEGs and ImageIO (written as ARGB, interpreted as CMYK while read, or the other way around). The bug referred is about JAI and PNG only, it doesn't relate to the standard ImageIO PNGImageWriter.]

### 8.x CMYK Color 

The CMYK color model is a subtractive color model used in color printing whereas most user-generated image uses the RGB additive color model. CMYK color space support is somewhat limited in Java, and there is no built-in CMYK color space, like it has for RGB. The main reason for this is that there is no standard CMYK color space. Unlike RGB that has standardized color profiles, like sRGB and AdobeRGB1998. CMYK color profiles originates from printer manufacturers and the printed press. Manufacturers and organizations have their own standardized profiles. 

Google for CMYK to RGB will come up with various mathematical formulas. Typically something like this (from http://www.rapidtables.com/convert/color/cmyk-to-rgb.htm):

> The R,G,B values are given in the range of 0..255.
> The red (R) color is calculated from the cyan (C) and black (K) colors:

>  *R = 255 × (1-C) × (1-K)*

> The green color (G) is calculated from the magenta (M) and black (K) colors:
 
>    *G = 255 × (1-M) × (1-K)*

> The blue color (B) is calculated from the yellow (Y) and black (K) colors:

>    *B = 255 × (1-Y) × (1-K)*

This formula "works", as in that it will produce an RGB image. But unfortunately, this rather naive implementation does not provide good results, because the color spaces have nonlinear response curves and different gamut. 

So, the best way to satisfy demanding users, is to use a proper ICC color profile, and ICC color transform. This approach usually involves converting the CMYK values into a device independent color space (like Lab or CIEXyz), and then from the independent space to the destination color space (RGB). This will produce much better results. 

Luckily, most image files that use CMYK color space does have an embedded ICC  profile, and when converting we should always use this profile. If there is no embedded ICC profile, we can look for a platform specific "generic CMYK" profile. "Web coated SWOP" or similar might also do in lack of a better alternative. And only fall back to the mathematical formula above as a worst case. [This is the "algorithm" used by TwelveMonkeys, anyway... :-)]

To complicate things slightly in Java land: The default ImageIO JPEGImageReader will not read CMYK images. The most common workaround for now, is to read the image as raster, then convert the YCCK to CMYK, before finally converting to RGB using ICC profile and then creating a BufferedImage from the resulting raster.

### 8.x ICC Color profiles and Color conversion

As mentioned above, converting between color spaces, usually involves ICC color profiles and color transforms. Luckily for us, ICC profiles and conversion has good support in Java, although the functionality is somewhat hidden.

The Java class ColorSpace is used to represent color spaces in Java. It has a subclass, ICC_ColorSpace for color spaces based on ICC profiles, and a corresponding ICC_Profile class to represent the profile itself. 

Color conversion between color spaces and color profiles is handled by the ColorConvertOp. On most platforms, this class delegates to native code to do the actual transformation, making it very fast and efficient. In most cases, magnitudes faster than naive conversion implemented in Java.

Unfortunately, certain profiles contains issues that causes crashes or exceptions i Java:
[Why does loading this jpg using JavaIO give CMMException?](http://stackoverflow.com/questions/4470958/)
[Exception “java.awt.color.CMMException: Invalid image format” thrown when resizing certain images…why?](http://stackoverflow.com/questions/12288813/)
[CMMException when parsing jpeg](https://github.com/haraldk/TwelveMonkeys/issues/34)
These profiles/issues must be recognized and dealt with before they are instantiated and passed to the ColorConvertOp filter. 

In addition, Java 8 creates some new issues, as Oracle (?) has been replacing the rather aging KCMS (developed by Kodak) with Little CMS (LCMS). The upside with the switch to LCMS though, ia a more compatible, better maintained and robust CMM system. However, the short-term downside is that current benchmarks shows it is slower, and it's not 100% compatible with KCMS [as shown by various bugs, like https://github.com/haraldk/TwelveMonkeys/issues/41]. 

Fortunately previous behavior can be restored for now, using a special switch:

    -Dsun.java2d.cmm=sun.java2d.cmm.kcms.KcmsServiceProvider

It's probably a good idea to do so, until libraries and frameworks have been updated to work fully with LCMS or inconsistencies has been worked out. Will have to fix the library at some point. 

Note that loading Custom ColorSpaces will eat memory. When loading many images at once reusing ICC profiles is a good idea. Many images contains embedded standard profiles, that will already be loaded by the JVM. But, be aware! ICC_Profile objects are mutable. It's therefore important to use these mutation operations with care, and make sure you either work on a non-shared instance or create a local copy before making changes. 

### 8.x Memory Usage

During image scaling the source images is loaded and a BufferedImage is created containing the rastered and un-compressed image. In other words the memory foot-print of the BufferedImage can be 10-20 times bigger than the compressed source image which makes the operations team uneasy - a huge source image could cause excessive memory consumption which in turn can be used for a "Denial Of Service Attack". And developers hate it to be considered as the root cause for an successful DOS attack - dutifully the original image conversion source code contains the following sanity check to avoid memory problems

```
if(imageSourceFile.length() > IMAGE_FILE_SIZE_LIMIT) {
    throw new ImageConversionException("The image is too big ...");
}
```

The memory foot-print mostly depends on the image dimension and to a lesser extent to the file size of the uploaded image when compression is used. This obserservation leads directly to notion of "decompression bomb vulnerabilities" as described at [http://www.aerasec.de/security/advisories/decompression-bomb-vulnerability.html](http://www.aerasec.de/security/advisories/decompression-bomb-vulnerability.html). A hand-crafted unicolor PNG image containing 19.000 x 19.000 pixels uses only 44 KB of disk but potentially up to 1 GB of main memory - ooups.

In order to avoid such attacks the image metadata of the uploaded image file are retrieved - this is a fast operation which does not require to load the whole image file. But what are sensible limits regarding image size?

A few examples

* Nikon D610 supports up to 24 megapixel
* Scanned A4 page with 300 DPI results in 35 megapixel (7015 x 4960)
* Nokia Lumia 1020 provides up to 41 megapixel

[Harald: Mention something about subsamping as a workaround, as we really don't need all that resolution in a web/non-huge-image-printing context?]
[Sigi: for image scaling I need to load the source image into BufferedImage therefore memory consumption is an issue for a server environment]

### 8.x Image Metadata

The ImageIO DOM-based metadata is hard to work with.

Often, this is not the metadata you want. Humans typically wants Exif or IPCT metadata, containing copyright, date, photographer etc.

## 8. In the Need of Twelve Monkeys

The Twelvemonkeys ImageIO project was created to solve many of the problems mentioned above. It was started when I was working for a web CMS (Content managment system) vendor [Escenic, but we might skip mentioning names ;-)], that created CMS solutions targeted for the media industry (newspapers, broadcast). It's a web-centric CMS, and 
the initial version was created, because we/the web content management system needed support for more image formats. 

### A little bit of History

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

### On-the-fly conversion of images

Servlet Filter based.
Reads the image from source, writes the scaled version directly to the response stream (alternatively through a cache).

Pros:

- May save disk space
- Saves up-front work that may slow down workflow
 
Cons:

- Needs more resources for the (first) request
- More complicated setup (caching etc)

TwelveMonkeys comes with a set of chainable filters that allows different conversions and effects to be applied to images "on-the-fly".

- Resampling (scaling)
- Cropping (create different aspects)
- Color conversion or effects (like grayscale, vintage/lomo look etc)
- Watermarking
- Content negotiation
- Format conversion (any format to web format like JPEG or PNG)