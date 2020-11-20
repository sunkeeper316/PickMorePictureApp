package com.charder.pickmorepictureapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import android.widget.ImageView
import com.charder.pickmorepictureapp.Room.db.AppDatabase
import java.io.Serializable

const val REQ_PICKMORE_PICTURE = 105
const val PREFERENCES_NAME = "preferences"
const val IMAGE = "image"

lateinit var scaleGestureDetector : ScaleGestureDetector
lateinit var gestureDetector : GestureDetector

lateinit var db : AppDatabase
var currentId : Long? = null

class ShowItem : Serializable{
    var name : String = ""
    var img : Bitmap? = null

}

fun getBitmap(iv : ImageView) : Bitmap {
    val bd = iv.drawable as BitmapDrawable
    val b = bd.bitmap
    return b
}

fun loadId(activity: Activity) {
    val preferences = activity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    currentId = preferences.getLong(IMAGE, -1)


}

fun saveId(activity: Activity, id: Long) {
    val preferences = activity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    preferences.edit().putLong(IMAGE, id).apply()

}

fun compressionBitmap(bitmap: Bitmap) : Bitmap{
    var bitmap = bitmap
    val width: Float = bitmap.width.toFloat()
    val height: Float = bitmap.height.toFloat()

    val scaleWidth: Float = 0.5f
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleWidth)
    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width.toInt(), height.toInt(), matrix,
        true);
    Log.e("bitmap" , "width height = ${bitmap?.width}  ${bitmap?.height}")
    return bitmap
}
fun restoreBitmap(bitmap: Bitmap) : Bitmap{
    var bitmap = bitmap
    val width: Float = bitmap.width.toFloat()
    val height: Float = bitmap.height.toFloat()

    val scaleWidth: Float = 2f
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleWidth)
    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width.toInt(), height.toInt(), matrix,
        true);
    Log.e("bitmap" , "width height = ${bitmap?.width}  ${bitmap?.height}")
    return bitmap
}