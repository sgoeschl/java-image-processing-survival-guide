# The Java Image Processing Survival Guide

## 1. The Problem at Hand

It was one of those glorious days starting with a customer request - "How difficult is it to replace our ImageMagick/JMagick-based image processing with a Java library?!". Java has graphics support built in for ages and there is the Java Advanced Imaging API (JAI - http://www.oracle.com/technetwork/java/javase/tech/jai-142803.html). The well-known answer was - "Well, it depends on your requirements but probably not too difficult". 

What are the customer's problems and requirements? ImageMagick is a native image-processing library which is wrapped by JMagick using JNI (Java Native Interface) to expose a Java interface but this approach has a few short-comings such as

* Installing the ImageMagick binaries for different target platforms is not entirely easy
* JMagick does not support the most current ImageMagick libraries which contains must-have bug fixes and improvements
* Getting ImageMagick/JMagick to run on Mac OS failed miserably which left the Macbook-based developers unhappy
* Any ImageMagick exceptions coming through the JNI layer causes the JVM to die which is far from perfect for a production server due to "Denial Of Service" attacks

On the other hand ImageMagick is a powerful and field-proven software and being used to implement the following requirements

* Converting arbitrary image formats such as PNG, TIFF and BMP into full-sized JPEGs
* Converting PDF documents to a preview image using GhostScript under the hood
* Creating five thumbnails with varying resolution based on the full-sized JPEG image
* Handling 5 million uploaded images per month
* Efficiently handling a peak load of 45 image per minute and server















