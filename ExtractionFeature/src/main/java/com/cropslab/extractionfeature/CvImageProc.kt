package com.cropslab.extractionfeature

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import org.apache.commons.math3.stat.descriptive.moment.Mean
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class CVImageProc(_image: Bitmap) {
    var image: Bitmap = _image
        private set
    lateinit var segmentedImage: Bitmap
        private set
    var width = 0
    var height = 0
    var totalPixel = 0
    private lateinit var sourceBitmapMat: Mat

    var meanColorRed = 0.0
        private set
    var meanColorGreen = 0.0
        private set
    var meanColorBlue = 0.0
        private set
    var standarDeviationColorRed: Double = 0.0
        private set
    var standarDeviationColorGreen: Double = 0.0
        private set
    var standarDeviationColorBlue: Double = 0.0
        private set

    var area: Double = 0.0
        private set
    var perimeter: Double = 0.0
        private set
    var circularity: Double = 0.0
        private set
    lateinit var cannyImage: Bitmap
        private set

    init {
        width = image.width
        height = image.height
        totalPixel = width * height
        this.watershedSegmentation()
        this.calculateColorFeature()
        this.calculateShapeFeature()
    }

    private fun watershedSegmentation() {
        val o: BitmapFactory.Options = BitmapFactory.Options()
        o.inSampleSize = 4
        this.sourceBitmapMat = Mat()
        val rgba = Mat()
        val threeChannel = Mat()

        Utils.bitmapToMat(this.image, this.sourceBitmapMat)
        val grayMat = this.sourceBitmapMat
        Imgproc.cvtColor(grayMat, rgba, Imgproc.COLOR_RGBA2RGB)

        Imgproc.cvtColor(rgba, threeChannel, Imgproc.COLOR_RGB2GRAY)
        Imgproc.threshold(threeChannel, threeChannel, 100.0, 255.0,
            Imgproc.THRESH_BINARY)

        val fg = Mat(rgba.size(), CvType.CV_8U)
        Imgproc.erode(threeChannel, fg, Mat(), Point(-1.0, -1.0), 2)

        val bg = Mat(rgba.size(), CvType.CV_8U)
        Imgproc.dilate(threeChannel, bg, Mat(), Point(-1.0, -1.0), 3)
        Imgproc.threshold(bg, bg, 1.0, 128.0, Imgproc.THRESH_BINARY)

        val markers = Mat(rgba.size(), CvType.CV_8U, Scalar(0.0))
        Core.add(fg, bg, markers)

        val markerTempo = Mat()
        markers.convertTo(markerTempo, CvType.CV_32S)

        Imgproc.watershed(rgba, markerTempo)
        markerTempo.convertTo(markers, CvType.CV_8U)

        this.segmentedImage = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)
        Imgproc.applyColorMap(markers, markers, 4)
        Utils.matToBitmap(fg, this.segmentedImage)
    }

    private fun calculateColorFeature() {
        val segmentedImageWidth = this.segmentedImage.width
        val segmentedImageHeight = this.segmentedImage.height

        var sumRedPixel = 0.0
        var sumGreenPixel = 0.0
        var sumBluePixel = 0.0
        val redPixelArray = DoubleArray(totalPixel)
        val bluePixelArray = DoubleArray(totalPixel)
        val greenPixelArray = DoubleArray(totalPixel)
        var start = 0
        for (i in 0 until segmentedImageWidth) {
            for (j in 0 until segmentedImageHeight) {
                val pixel = this.segmentedImage.getPixel(i, j)
                val sourcePixel = this.image.getPixel(i, j)
                var newPixel = 0

                if (pixel.equals(-1)) {
                    newPixel = pixel
                } else {
                    newPixel = sourcePixel
                }
                val red = Color.red(newPixel).toDouble()
                val blue = Color.blue(newPixel).toDouble()
                val green = Color.green(newPixel).toDouble()

                sumRedPixel += red
                sumGreenPixel += blue
                sumBluePixel += green

                redPixelArray[start] = red
                bluePixelArray[start] = blue
                greenPixelArray[start] = green

                ++start
            }
        }

        // calculate mean & standar deviation
        val mean = Mean()
        this.meanColorRed = mean.evaluate(redPixelArray)
        this.meanColorGreen = mean.evaluate(greenPixelArray)
        this.meanColorBlue = mean.evaluate(bluePixelArray)
        val std = StandardDeviation()
        this.standarDeviationColorRed = std.evaluate(redPixelArray)
        this.standarDeviationColorGreen = std.evaluate(greenPixelArray)
        this.standarDeviationColorBlue = std.evaluate(bluePixelArray)
    }

    private fun calculateShapeFeature() {
        val grayMat = Mat()
        val cannyEdges = Mat()
        val hierarchy = Mat()
        val contourList = ArrayList<MatOfPoint>()

        Imgproc.cvtColor(this.sourceBitmapMat, grayMat, Imgproc.COLOR_RGB2GRAY)
        Imgproc.Canny(this.sourceBitmapMat, cannyEdges, 10.0, 100.0)
        Imgproc.findContours(cannyEdges, contourList, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

        val contours = Mat()
        contours.create(cannyEdges.rows(), cannyEdges.cols(), CvType.CV_8UC3)
        for (i in 0 until contourList.size) {
            Imgproc.drawContours(contours, contourList, i, Scalar(255.0, 255.0, 255.0), -1)
        }

        this.cannyImage = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)
        Utils.matToBitmap(contours, this.cannyImage)
        this.setShapeFeature(contourList)
    }

    private fun setShapeFeature(contourList: ArrayList<MatOfPoint>) {
        var currentMax = 0.0
        var arcLength = 0.0
        var circularity = 0.0
        val perimeter = MatOfPoint2f()
        for (c in contourList) {
            val area = Imgproc.contourArea(c)
            if (area > currentMax) {
                currentMax = area
                c.convertTo(perimeter, CvType.CV_32FC2)
                arcLength = Imgproc.arcLength(perimeter, true)
                if (arcLength > 0) {
                    circularity = 4 * Math.PI * area / Math.pow(arcLength, 2.0)
                }
            }
        }
        this.area = currentMax
        this.perimeter = arcLength
        this.circularity = circularity
    }
}