package com.charder.pickmorepictureapp

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.charder.pickmorepictureapp.Room.entity.ImageItem
import com.charder.pickmorepictureapp.Room.task.getImageItemById


class LoadFragment : Fragment() {

    lateinit var imageitem : ImageItem

    lateinit var iv_show : ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_load, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_show = view.findViewById(R.id.iv_show)


        currentId?.let {
            getImageItemById(it,callback = { imageItem ->
                imageitem = imageItem

                var bitmap = BitmapFactory.decodeByteArray(imageitem.imgBitmap , 0 , imageitem.imgBitmap!!.size)
                bitmap = restoreBitmap(bitmap)
                iv_show.setImageBitmap(bitmap)
            })
        }
    }
}