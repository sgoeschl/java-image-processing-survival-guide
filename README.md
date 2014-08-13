# The Java Image Processing Survival Guide

For a few customers I needed to do a lot of things with images

* Process million of images - many of them were user-supplied
* Convert various image formats (JPG, GIF, PNG, TIFF, BMP) into JPEGs
* Converting PDFs into JPEGs
* Resample images
* Handle CMYK image
* Handle images with alpha-cannel
* Apply image operations such as geyscaling, dithering and sharpening
* Setting image compression and DPI for storing JPGs

In order to get the things done I used various image processing libraries with varying success

* Java ImageIO (comes with the JDK)
* Java Advanced Imaging (JAI)
* Apache Commons Imaging (also known as Sanselan)
* TwelveMonkey library

The problem with all of those libraries is that I had various issues and information how to solve my issues were hard to find on the internet. 

Therefore the idea was born to write a paper & sample code covering the image processing libraries I used - maybe you can save a lot of time :-)