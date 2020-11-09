package com.charder.pickmorepictureapp

import android.graphics.Bitmap
import android.view.ScaleGestureDetector
import java.io.Serializable

const val REQ_PICKMORE_PICTURE = 105

lateinit var scaleGestureDetector : ScaleGestureDetector

class ShowItem : Serializable{
    var name : String = ""
    var img : Bitmap? = null

}