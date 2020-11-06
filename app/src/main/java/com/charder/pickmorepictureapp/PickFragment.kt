package com.charder.pickmorepictureapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.io.IOException


class PickFragment : Fragment() {

    lateinit var bt_pick : Button
    lateinit var rv_show : RecyclerView
    lateinit var showAdapter : ShowAdapter
    var showItemList :MutableList<ShowItem> = arrayListOf()

    var uriList : MutableList<Uri> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_pick = view.findViewById(R.id.bt_pick)
        rv_show = view.findViewById(R.id.rv_show)
        rv_show.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        bt_pick.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent, REQ_PICKMORE_PICTURE)
            } else {
                Toast.makeText(
                    activity, R.string.textNoImagePickerAppFound, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_PICKMORE_PICTURE -> {
                    intent?.let {
                        if (it.clipData != null) {
                            val mClipData = it.clipData
                            for (i in 0..mClipData!!.itemCount - 1) {
                                val item = mClipData.getItemAt(i)
                                val uri: Uri = item.uri
                                uriList.add(uri)
                                val bitmap: Bitmap? = loadFromUri(uri)
                                val showitem = ShowItem()
                                var path = uri.path.toString()
                                val pathList = path.split("/")
                                showitem.name = pathList.last() + ".jpg"
                                showitem.img = bitmap
                                showItemList.add(showitem)


                            }
                            showAdapter = ShowAdapter(requireActivity() , showItemList)
                            rv_show.adapter = showAdapter
                        }
                    }

                }
            }
        }
    }
    fun loadFromUri(photoUri: Uri): Bitmap? {
        var image: Bitmap? = null
        try {
            // check version of Android on device
            image = if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(requireActivity().getContentResolver(), photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                // support older versions of Android by using getBitmap
                MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        if (data.clipData != null) {
//            val mClipData = data.clipData
//            mArrayUri = ArrayList<Uri>()
//            mBitmapsSelected = ArrayList<Bitmap>()
//            for (i in 0 until mClipData!!.itemCount) {
//                val item = mClipData.getItemAt(i)
//                val uri: Uri = item.uri
//                mArrayUri.add(uri)
//                // Use the loadFromUri method from above
//                val bitmap: Bitmap = loadFromUri(photoUri)
//                mBitmapsSelected.add(bitmap)
//            }
//        }
//    }
    inner class ShowAdapter(val _activity: Activity, val showItemList: MutableList<ShowItem>) : RecyclerView.Adapter<ShowAdapter.ShowViewHolder>() {

        inner class ShowViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
            val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
            val iv_show = itemView.findViewById<ImageView>(R.id.iv_show)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
            return ShowViewHolder(
                LayoutInflater.from(parent?.context).inflate(
                    R.layout.item_pictrue,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int { return showItemList.size }


        override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
            val showItem = showItemList.get(position)
            holder.tv_name.setText(showItem.name)
            holder.iv_show.setImageBitmap(showItem.img)
        }

    }
}