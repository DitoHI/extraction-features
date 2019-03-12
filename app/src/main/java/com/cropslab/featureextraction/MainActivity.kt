package com.cropslab.featureextraction

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cropslab.extractionfeature.CVImageProc

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        System.loadLibrary("opencv_java4")
        val imageTesting = BitmapFactory.decodeResource(resources, R.drawable.image_testing)
        val cVImageProc = CVImageProc(imageTesting)

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

        val TAG_COLOR = "Color Feature"
        Log.i(TAG_COLOR, "ColorFeature_Mean[Color Red: ${meanColorRedFeature}, Color Green: ${meanColorGreenFeature}, Color Blue: ${meanColorBlueFeature}]")
        Log.i(TAG_COLOR,"ColorFeature_StandardDeviation[Color Red: ${stdColorRedFeature}, Color Green: ${stdColorGreenFeature}, Color Blue: ${stdColorBlueFeature}]")
        // Output
        // Color Feature: ColorFeature_Mean[Color Red: 244.87600135803223, Color Green: 245.65409564971924, Color Blue: 242.3908977508545]
        // Color Feature_StandardDeviation[Color Red: 39.43013362345764, Color Green: 36.05372619925521, Color Blue: 48.556128067115]

        val TAG_SHAPE = "Shape Feature"
        Log.i(TAG_SHAPE, "ShapeFeature[Area: ${area}, Perimeter: ${perimeter}, Circularity: ${circularity}]")
        // Output
        // ShapeFeature[Area: 123.5, Perimeter: 54.870057225227356, Circularity: 0.51547308674358]
    }
}
