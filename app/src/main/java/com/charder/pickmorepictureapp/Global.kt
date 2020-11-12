package com.charder.pickmorepictureapp

import android.graphics.Bitmap
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import java.io.Serializable

const val REQ_PICKMORE_PICTURE = 105

lateinit var scaleGestureDetector : ScaleGestureDetector
lateinit var gestureDetector : GestureDetector

class ShowItem : Serializable{
    var name : String = ""
    var img : Bitmap? = null

}