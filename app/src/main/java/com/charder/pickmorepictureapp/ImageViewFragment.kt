package com.charder.pickmorepictureapp

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.navigation.Navigation
import com.charder.pickmorepictureapp.Room.entity.ImageItem
import com.easystudio.rotateimageview.RotateZoomImageView

class ImageViewFragment : Fragment() {

    lateinit var iv_show : ImageView

    lateinit var imageItem : ImageItem
    lateinit var btRotateZoomImageView : Button

//    lateinit var scaleGestureDetector : ScaleGestureDetector
    var scaleFactor : Float = 1.0f

    var scaleWidth : Float = 1.0f
    var scaleHeight : Float = 1.0f

    var xCoOrdinate : Float = 1.0f
    var yCoOrdinate : Float = 1.0f

    var AXIS_X_MIN = 0.0f
    var AXIS_X_MAX = 0.0f
    var AXIS_Y_MIN = 0.0f
    var AXIS_Y_MAX = 0.0f

    lateinit var mCurrentViewport : RectF
    var mContentRect: Rect? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btRotateZoomImageView = view.findViewById(R.id.btRotateZoomImageView)
        btRotateZoomImageView.setOnClickListener {
            var b = Bundle()
            b.putSerializable("imageItem",imageItem)
            Navigation.findNavController(it).navigate(R.id.action_imageViewFragment_to_rotateZoomImageViewFragment , b)
        }
        view.setOnTouchListener{ v, event ->
            val isScaleGestureDetector = scaleGestureDetector.onTouchEvent(event)
            val iGestureDetector = gestureDetector.onTouchEvent(event)
            if (isScaleGestureDetector){
                return@setOnTouchListener isScaleGestureDetector
            }else{

                return@setOnTouchListener iGestureDetector
            }
        }
        iv_show = view.findViewById(R.id.iv_show)


        xCoOrdinate  = iv_show.x
        yCoOrdinate  = iv_show.y

        scaleWidth  = iv_show.width.toFloat()
        scaleHeight  = iv_show.height.toFloat()
//        iv_show.setOnTouchListener { v, event ->
//
//        }

        gestureDetector = GestureDetector(requireContext(),GestureListener())
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
                Log.e("onScale","${iv_show.width}")
                Log.e("onScale","${iv_show.height}")
                return true
            }
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }
            override fun onScaleEnd(detector: ScaleGestureDetector?) {

            }


        })
        if (arguments != null){
            imageItem = arguments?.getSerializable("imageItem") as ImageItem

            currentId = imageItem.id
            saveId(requireActivity(), currentId!!)

            Log.e ("currentId","currentId ${currentId}")
            var bitmap = BitmapFactory.decodeByteArray(imageItem.imgBitmap , 0 , imageItem.imgBitmap!!.size)
            bitmap = restoreBitmap(bitmap)
            iv_show.setImageBitmap(bitmap)

            btRotateZoomImageView.measure(0,0)
            iv_show.measure(0,0)
            val iv_height = iv_show.measuredHeight.toFloat()
            val bt_height = btRotateZoomImageView.measuredHeight.toFloat()
            val iv_weight = iv_show.measuredWidth.toFloat()

            val bd = iv_show.drawable as BitmapDrawable
            bd.bounds.top
            val b = bd.bitmap

            mCurrentViewport = RectF(iv_show.left.toFloat(), iv_show.top.toFloat() , iv_show.right.toFloat() , iv_show.bottom.toFloat())
            Log.e("RectF","${iv_show.measuredHeight}  ")

            Log.e("RectF","${btRotateZoomImageView.measuredHeight}  ")
            Log.e("RectF","${iv_show.measuredWidth}  ")
            AXIS_Y_MIN = bt_height
            AXIS_X_MAX = iv_weight
            AXIS_Y_MAX = iv_height + bt_height
            mCurrentViewport = RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX)

        }

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        var rect : Rect = Rect()
        var Location : IntArray = IntArray(2)

            iv_show.getLocationOnScreen(Location)

//        iv_show.getLocalVisibleRect(rect)
//        iv_show.getGlobalVisibleRect(rect)

        Log.e("RectF","${scaleWidth}  ${scaleHeight}")
//        Log.e("RectF","${rect.top}  ${rect.bottom}")
//        Log.e("RectF","${rect.left}  ${rect.right}")
    }
    private fun setViewportBottomLeft(x: Float, y: Float) {
        val curWidth: Float = mCurrentViewport.width()
        val curHeight: Float = mCurrentViewport.height()
        val newX: Float = Math.max(AXIS_X_MIN, Math.min(x, AXIS_X_MAX - curWidth))
        val newY: Float = Math.max(AXIS_Y_MIN + curHeight, Math.min(y, AXIS_Y_MAX))

        mCurrentViewport.set(newX, newY - curHeight, newX + curWidth, newY)

        // Invalidates the View to update the display.
        ViewCompat.postInvalidateOnAnimation(requireView())
    }
    inner class GestureListener : GestureDetector.OnGestureListener{

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
        override fun onShowPress(e: MotionEvent?) {

        }
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return true
        }
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            Log.e("onScroll","distanceX${distanceX}")
            Log.e("onScroll","xCoOrdinate${xCoOrdinate}")

            Log.e("onScroll","${distanceY}")
            val maxX = ( scaleWidth - iv_show.width ) / 2
            val maxY = ( scaleHeight - iv_show.height ) / 2
//            if (scaleWidth > iv_show.x){
//                xCoOrdinate = iv_show.x - distanceX
//
//            }
            Log.e("onScroll","maxX${maxX}")
            if (xCoOrdinate > (iv_show.x - maxX)){
                xCoOrdinate = xCoOrdinate - distanceX
            }else if (xCoOrdinate < (iv_show.x + maxX)){
                xCoOrdinate = xCoOrdinate - distanceX
            }
            if (yCoOrdinate > (iv_show.y - maxY)){
                yCoOrdinate = yCoOrdinate - distanceY
            }else if (yCoOrdinate < (iv_show.y + maxY)){
                yCoOrdinate = yCoOrdinate - distanceY
            }

            iv_show.animate()
                    .x(xCoOrdinate)
                    .y(yCoOrdinate)
                    .setDuration(0)
                    .start()
            return true
        }
        override fun onLongPress(e: MotionEvent?) {
            
        }
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            return true
        }
    }
}

//iv_show.setOnTouchListener { v, event ->
//    when(event?.action){
//        MotionEvent.ACTION_DOWN -> {
//            Log.e("MotionEvent" , "ACTION_DOWN")
//            xCoOrdinate = v.x - event.getRawX()
//            yCoOrdinate = v.y - event.getRawY()
//        }
//        MotionEvent.ACTION_UP -> {
//            Log.e("MotionEvent" , "ACTION_UP")
//        }
//        MotionEvent.ACTION_POINTER_UP ->{
//            Log.e("MotionEvent" , "ACTION_POINTER_UP")
//        }
//        MotionEvent.ACTION_POINTER_DOWN ->{
//            Log.e("MotionEvent" , "ACTION_POINTER_DOWN")
//        }
//        MotionEvent.ACTION_MOVE ->{
//            Log.e("MotionEvent" , "ACTION_MOVE")
//            v.animate()
//                    .x(event.getRawX() + xCoOrdinate)
//                    .y(event.getRawY() + yCoOrdinate)
//                    .setDuration(0)
//                    .start()
//        }
//    }
//    if (scaleGestureDetector.onTouchEvent(event)){
//        return@setOnTouchListener true
//    }
//    return@setOnTouchListener true
//}