package com.charder.pickmorepictureapp

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.charder.pickmorepictureapp.Room.entity.ImageItem
import com.charder.pickmorepictureapp.Room.task.getImageItemById


class LoadFragment : Fragment() {

    lateinit var imageitem : ImageItem

    lateinit var iv_show : ImageView

    lateinit var tv_show : TextView
    lateinit var tv_show2 : TextView

    var AXIS_X_MIN = 0.0f
    var AXIS_X_MAX = 0.0f
    var AXIS_Y_MIN = 0.0f
    var AXIS_Y_MAX = 0.0f

    lateinit var mCurrentViewport : RectF
    var mContentRect: Rect? = null

    var scaleFactor : Float = 1.0f
    var scaleWidth : Float = 1.0f
    var scaleHeight : Float = 1.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_load, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_show = view.findViewById(R.id.iv_show)
        tv_show = view.findViewById(R.id.tv_show)
        tv_show2 = view.findViewById(R.id.tv_show2)

        var Location : IntArray = IntArray(2)

        iv_show.getLocationInSurface(Location)
        tv_show.setText("x: ${Location[0]} y: ${Location[1]}")

         iv_show.drawable?.let{
             val dw : Drawable = it
             var dwidth = dw.getIntrinsicWidth();
             var dheight = dw.getIntrinsicHeight();
             Log.e("RectF","dwidth: ${dwidth} dheight: ${dheight}")
         }

        Log.e("RectF","dwidth: ${tv_show.height} dheight: ${tv_show.width}")

        //剛開始Layout還沒繪製所以要用監聽方式取得
        iv_show.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    Log.e("RectF","${left}  ${top} ${bottom} ${right}")
            mCurrentViewport = RectF(left.toFloat() , top.toFloat() , bottom.toFloat() , right.toFloat())

            Log.e("RectF","dwidth: ${tv_show.height} dheight: ${tv_show.width}")
        }
//        iv_show.getLocalVisibleRect(rect)
//        iv_show.getGlobalVisibleRect(rect)
        var rect : Rect = Rect()
        Log.e("RectF","${scaleWidth}  ${scaleHeight}")
//        Log.e("RectF","${rect.top}  ${rect.bottom}")
//        Log.e("RectF","${rect.left}  ${rect.right}")

        currentId?.let {
            getImageItemById(it,callback = { imageItem ->
                imageitem = imageItem


                var bitmap = BitmapFactory.decodeByteArray(imageitem.imgBitmap , 0 , imageitem.imgBitmap!!.size)
                bitmap = restoreBitmap(bitmap)
                requireActivity().runOnUiThread {
                    iv_show.setImageBitmap(bitmap)
                    val dw : Drawable= iv_show.drawable

                    var dwidth = dw.getIntrinsicWidth();
                    var dheight = dw.getIntrinsicHeight();
                    tv_show2.setText("dwidth: ${dwidth} dheight: ${dheight}")

                }

            })
        }

        scaleGestureDetector = ScaleGestureDetector(requireActivity(),object : ScaleGestureDetector.OnScaleGestureListener{


            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                detector?.scaleFactor?.let{
                    scaleFactor *= it
                    Log.e("onScale","${scaleFactor}")
                    scaleFactor = Math.max(0.5f,Math.min(scaleFactor,5.0f))
                    iv_show.scaleX = scaleFactor
                    iv_show.scaleY = scaleFactor
                    scaleWidth = iv_show.width * scaleFactor
                    scaleHeight = iv_show.height * scaleFactor
                }
                val imageMatrix = iv_show.imageMatrix

                val bitmapDrawable = iv_show.drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
//                Log.e("onScale","${iv_show.width}")
//                Log.e("onScale","${iv_show.height}")
                return true
            }
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }
            override fun onScaleEnd(detector: ScaleGestureDetector?) {

            }


        })
        view.setOnTouchListener{ v, event ->
            val isScaleGestureDetector = scaleGestureDetector.onTouchEvent(event)
//            val iGestureDetector = gestureDetector.onTouchEvent(event)
            if (isScaleGestureDetector){
                return@setOnTouchListener isScaleGestureDetector
            }else
                return@setOnTouchListener true
//                return@setOnTouchListener iGestureDetector
            }

    }
}

//int vwidth = iv.getWidth() - iv.getPaddingLeft() - iv.getPaddingRight();
//int vheight = iv.getHeight() - iv.getPaddingTop() - iv.getPaddingBottom();
//
//int realImageWidth = (int)((vwidth - dwidth) * 0.25f);
//// (I do not know why I had to put 0.25f instead of 0.5f,
//// but I think this issue is a consequence of the screen density)
//
//int realImageHeight = (int)((vheight - dheight) * 0.25f);