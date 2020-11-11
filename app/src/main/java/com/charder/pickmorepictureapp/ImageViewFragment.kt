package com.charder.pickmorepictureapp

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView


class ImageViewFragment : Fragment() {

    lateinit var iv_show : ImageView

    lateinit var showitem : ShowItem

//    lateinit var scaleGestureDetector : ScaleGestureDetector
    var scaleFactor : Float = 1.0f
    var xCoOrdinate : Float = 1.0f
    var yCoOrdinate : Float = 1.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_show = view.findViewById(R.id.iv_show)
        iv_show.setOnTouchListener { v, event ->
            when(event?.action){
                MotionEvent.ACTION_DOWN -> {
                    Log.e("MotionEvent" , "ACTION_DOWN")
                    xCoOrdinate = v.x - event.getRawX()
                    yCoOrdinate = v.y - event.getRawY()
                }
                MotionEvent.ACTION_UP -> {
                    Log.e("MotionEvent" , "ACTION_UP")
                }
                MotionEvent.ACTION_POINTER_UP ->{
                    Log.e("MotionEvent" , "ACTION_POINTER_UP")
                }
                MotionEvent.ACTION_POINTER_DOWN ->{
                    Log.e("MotionEvent" , "ACTION_POINTER_DOWN")
                }
                MotionEvent.ACTION_MOVE ->{
                    Log.e("MotionEvent" , "ACTION_MOVE")
                    v.animate()
                            .x(event.getRawX() + xCoOrdinate)
                            .y(event.getRawY() + yCoOrdinate)
                            .setDuration(0)
                            .start()
                }

            }
            if (scaleGestureDetector.onTouchEvent(event)){
                return@setOnTouchListener true
            }
            return@setOnTouchListener true
        }
        scaleGestureDetector = ScaleGestureDetector(requireActivity(),object : ScaleGestureDetector.OnScaleGestureListener{

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                detector?.scaleFactor?.let{
                    scaleFactor *= it
                    iv_show.scaleX = scaleFactor
                    iv_show.scaleY = scaleFactor
                }

                return true
            }
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }
            override fun onScaleEnd(detector: ScaleGestureDetector?) {

            }
            

        })
        if (arguments != null){
            showitem = arguments?.getSerializable("showItem") as ShowItem
            iv_show.setImageBitmap(showitem.img)
        }

    }
}