# The Java Image Processing Survival Guide

## 1. Abstract

This paper provides real-life experience in replacing an ImageMagick/JMagick image conversion & scaling with a pure Java implementation covering Apache PDFBox, Java Advanced Imaging API and image-scaling libraries. 

## 2. The Problem at Hand

It was one of those perfect days starting with a customer request - "How difficult is it to replace our ImageMagick/JMagick-based image processing with a Java library?!". Java has graphics support built in for ages and in addition there is the Java Advanced Imaging API (JAI - http://www.oracle.com/technetwork/java/javase/tech/jai-142803.html) - "Well, depending on your requirements probably not too difficult". 

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
* Apply some sharpening to the converted images

In short - it looked somehow difficult now.

## 3. Divide and Conquer

Tackling a difficult problem requires some "divide and conquer" in order to split a large problem into a couple of smaller ones

* Use Apache PDFBox for PDF to image conversion
* Use JAI for converting image formats to JPEG
* Use a Java-based library for scaling the images
* Test with real-life data

## 4. Converting PDF to a Preview Image

## 5. Convert Image Formats to JPEG

* TIFF is not supported transparently using Java ImageIO

## 6. Scaling Images Efficiently

* Efficiently creating multiple thumbnails
* Tested various image scaling libraries
    * thumbnailator (see https://code.google.com/p/thumbnailator/) 
    * java-image-scaling (see https://code.google.com/p/java-image-scaling/)

## 7. Test With Real-Life Data

When the overall implementation was roughly finished a few hundred production images were used for a more thorough testing and the results were not promising (as expected) - a couple of images were either not converted at all or the resulting images were severely broken. 

A closer look revealed the following root causes

* Alpha-channel handling is broken in Java (http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6371389)
* CMYK color space is not supported
* missing image metadata causes NPE
* Memory usage of conversion - Decompression bombs - http://www.aerasec.de/security/advisories/decompression-bomb-vulnerability.html
* Unsupported JPEG compression formats

## 8. In the Need of Twelve Monkeys



