package com.charder.pickmorepictureapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class ImageViewFragment : Fragment() {

    lateinit var iv_show : ImageView

    lateinit var showitem : ShowItem

//    lateinit var scaleGestureDetector : ScaleGestureDetector
    var scaleFactor : Float = 1.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_show = view.findViewById(R.id.iv_show)
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