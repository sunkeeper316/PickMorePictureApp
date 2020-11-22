package com.charder.pickmorepictureapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import android.widget.ImageView
import com.charder.pickmorepictureapp.Room.db.AppDatabase
import com.github.tntkhang.fullscreenimageview.library.FullScreenImageViewActivity
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.*

val testUrl = "http://file.holdinghands.com.tw:8082/HOLDINGHAND2.0/COMPANY/209/jr5oJiazYNok5GH5QqDYnQfXfApFy3Lnso5P1wlq.jpeg"


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

fun getBitmap(iv: ImageView) : Bitmap {
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
//    Log.e("bitmap" , "width height = ${bitmap?.width}  ${bitmap?.height}")
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
//    Log.e("bitmap" , "width height = ${bitmap?.width}  ${bitmap?.height}")
    return bitmap
}

fun imagefullScreen(activity: Activity, uris: MutableList<Uri>, position: Int){
    val fullImageIntent = Intent(activity, FullScreenImageViewActivity::class.java)
    val uriString = ArrayList<String>()
    for (uri in uris) {
        uriString.add(uri.toString())
    }
    fullImageIntent.putExtra(FullScreenImageViewActivity.URI_LIST_DATA, uriString)
    fullImageIntent.putExtra(FullScreenImageViewActivity.IMAGE_FULL_SCREEN_CURRENT_POS, position)
    activity.startActivity(fullImageIntent)
}

fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}

fun urlTouri(myUrlStr : String) : Uri? {
//    val myUrlStr = "xyz"
    val url: URL
    var uri: Uri? = null
    try {
        url = URL(myUrlStr)
        uri = Uri.parse(url.toURI().toString())

    } catch (e1: MalformedURLException) {
        e1.printStackTrace()
    } catch (e: URISyntaxException) {
        e.printStackTrace()
    }
    return uri
}