package com.charder.pickmorepictureapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.charder.pickmorepictureapp.Room.entity.ImageItem
import com.easystudio.rotateimageview.RotateZoomImageView


class RotateZoomImageViewFragment : Fragment() {

    lateinit var iv_show : RotateZoomImageView
    lateinit var layout : RelativeLayout
    lateinit var imageItem : ImageItem


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rotate_zoom_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_show = view.findViewById(R.id.iv_show)
        layout = view.findViewById(R.id.layout)
        if (arguments != null){
            imageItem = arguments?.getSerializable("imageItem") as ImageItem
            var bitmap = BitmapFactory.decodeByteArray(imageItem.imgBitmap , 0 , imageItem.imgBitmap!!.size)

            iv_show.setImageBitmap(bitmap)
        }
        iv_show.setOnTouchListener { v, event ->
            return@setOnTouchListener iv_show.onTouch(v, event)
        }
        val lp = RelativeLayout.LayoutParams(250, 250)
        lp.addRule(RelativeLayout.BELOW)
        iv_show.setLayoutParams(lp)

    }
}