# The Java Image Processing Survival Guide

## 1. The Problem at Hand

It was one of those perfect days starting with a customer request - "How difficult is it to replace our ImageMagick/JMagick-based image processing with a Java library?!". Java has graphics support built in for ages and there is always the Java Advanced Imaging API (JAI - http://www.oracle.com/technetwork/java/javase/tech/jai-142803.html). The well-known answer was - "Well, it depends on your requirements but probably not too difficult". 

What are the customer's problems and requirements? ImageMagick is a native image-processing library which is wrapped by JMagick using JNI (Java Native Interface) to expose a Java interface but this approach has a few short-comings such as

* Installing the ImageMagick binaries for different target platforms is not entirely easy
* JMagick does not support the most current ImageMagick libraries which contains must-have bug fixes and improvements
* Getting ImageMagick/JMagick to run on Mac OS failed miserably which left the Macbook-based developers unhappy
* Any ImageMagick exceptions coming through the JNI layer causes the JVM to die which is far from perfect for a production server due to "Denial Of Service" attacks

On the other hand ImageMagick is a powerful and field-proven software and being used to implement the following requirements

* Processing 5 million user-uploaded images per month
* Converting arbitrary image formats such as PNG, TIFF and BMP into JPEG image
* Converting PDF documents to a preview image using GhostScript under the hood
* Creating five scaled-down images with varying resolution based on a JPEG image
* Efficiently handling a peak load of 45 image per minute and server

In short - it looked difficult now.

## 2. Dissecting the Problem

* JAI for image format conversion
* Open-source library for image scaling 
* Apache PDFBox for PDF to image conversion

## 3. Problems Along the Way

* Alpha-channel handling is broken in Java (http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6371389)
* CMYK color space is not supported
* missing image metadata breaks DPI settings
* out of memory attack - http://www.aerasec.de/security/advisories/decompression-bomb-vulnerability.html
* TIFF is not supported transparently using Java ImageIO
* Efficiently creating multiple thumbnails
* Memory usage of conversion

