### Color & Shape Extraction

This library is trying to help you to get the feature of your image easily. With the help of OpenCV and Apache Math, you can do some custom processing of your images to get the feature of it.

### Inital Setup

* For starter, Don't be bothered to install and do some basic configuration of installing OpenCV into your project. *We already take care of that*. In case you want to do some basic installing of OpenCV, check out the link [here](https://android.jlelse.eu/a-beginners-guide-to-setting-up-opencv-android-library-on-android-studio-19794e220f3c) to get the brief of introduction about how to install OpenCV in your Android Application
* We use `Apache Math` from another open source project by Apache to do some calculation inside image processing
* Install the dependency through gradle. Before that, add jitpack repository into the first of your `build.gradle`
```groovy
allprojects {
    repositories {
	    ...
	    maven { url 'https://jitpack.io' }
	}
}
```
Then add the dependency
```groovy
implementation 'com.github.DitoHI:extraction-features:0.1.1'
```
* **Important!** Please import your OpenCV module to `build.gradle` of *ExtractionFeature* and change it to your named OpenCV Library. For example, the case I'm using is implementing my OpenCV module with named `OpenCV` and put it in `build.gradle` of ExtractionFeature inside dependencies
```groovy
dependencies {
    ...
    implementation project(':OpenCV')
    ...
}
```
* Load the OpenCV library in the very first of your application. The name of library is up to your prefered version of OpenCV. I use the latest version (now) of OpenCV 4 because it supports the four dominant CPU Architectures, which is the most compatible with the ABI Management from Google  
```kotlin
System.loadLibrary("opencv_java4")
```

### Basic Usage

* Get your image as Bitmap
```kotlin
val imageTesting = BitmapFactory.decodeResource(resources, R.drawable.image_testing)
```
* Put your bitmap image as the parameter of CvImageProc
```kotlin
val cVImageProc = CVImageProc(imageTesting)
```
* Get the parameters of the feature by calling its properties directly
```kotlin
// to get the color feature
val meanColorRedFeature = cVImageProc.meanColorRed
val meanColorGreenFeature = cVImageProc.meanColorGreen
val meanColorBlueFeature = cVImageProc.meanColorBlue

val stdColorRedFeature = cVImageProc.standarDeviationColorRed
val stdColorGreenFeature = cVImageProc.standarDeviationColorGreen
val stdColorBlueFeature = cVImageProc.standarDeviationColorBlue

// to get the shape feature
val area = cVImageProc.area
val perimeter = cVImageProc.perimeter
val circularity = cVImageProc.circularity
```

License
--------

    Copyright 2019 CropsLab, Inc.

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated                 
    documentation files (the "Software"), to deal in the Software without restriction, including without limitation the           
    rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit       
    persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the         
    Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE         
    WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR         
    COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
