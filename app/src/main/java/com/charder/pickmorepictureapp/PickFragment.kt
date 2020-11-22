package com.charder.pickmorepictureapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.charder.pickmorepictureapp.Room.entity.ImageItem
import com.charder.pickmorepictureapp.Room.task.getAll
import com.charder.pickmorepictureapp.Room.task.getImageItemById
import com.charder.pickmorepictureapp.Room.task.insert
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URI
import java.net.URL


class PickFragment : Fragment() {

    lateinit var bt_pick : Button
    lateinit var bt_load : Button
    lateinit var rv_show : RecyclerView
    lateinit var showAdapter : ShowAdapter
    var imageItemList :MutableList<ImageItem> = arrayListOf()

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
        bt_load = view.findViewById(R.id.bt_load)
        rv_show = view.findViewById(R.id.rv_show)

        rv_show.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        bt_load.setOnClickListener {
            loadId(requireActivity())
//            Navigation.findNavController(it).navigate(R.id.action_pickFragment_to_loadFragment)

            getImageItemById(currentId!!,callback = { imageItem ->
//                try {
//                    val imageUrl = URL(testUrl)
//                    val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
//                    requireActivity().runOnUiThread{
//                        val uri = getImageUri(requireActivity(), bitmap)
//                        uri?.let{
//                            var uris : MutableList<Uri> = arrayListOf()
//                            uris.add(it)
//                            imagefullScreen(requireActivity() , uris , 0)
//                        }
//                    }
//                }catch (e : MalformedURLException) {
//                    e.printStackTrace();
//                } catch ( e: IOException) {
//                    e.printStackTrace();
//                } catch (e: IllegalStateException) {
//                    e.printStackTrace();
//                }


                requireActivity().runOnUiThread {
//                var bitmap = BitmapFactory.decodeByteArray(imageItem.imgBitmap , 0 , imageItem.imgBitmap!!.size)
//                bitmap = restoreBitmap(bitmap)
                    val testuri = urlTouri(testUrl)
                    Log.e("testuri","${testuri}")
                    testuri?.let{
                            var uris : MutableList<Uri> = arrayListOf()
                            uris.add(it)
                            imagefullScreen(requireActivity() , uris , 0)
                    }
                }



            })

        }
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
        getAll {
            imageItemList = it
            showAdapter = ShowAdapter(requireActivity() , imageItemList)
            requireActivity().runOnUiThread {
                rv_show.adapter = showAdapter
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
                                val imageItem = ImageItem()
                                var path = uri.path.toString()
                                val pathList = path.split("/")
                                imageItem.name = pathList.last() + ".jpg"
                                var cbitmap = compressionBitmap(bitmap!!)
                                val baOutputStream : ByteArrayOutputStream = ByteArrayOutputStream()
                                cbitmap?.compress(Bitmap.CompressFormat.PNG , 50 , baOutputStream)

                                imageItem.imgBitmap = baOutputStream.toByteArray()
                                imageItem.id = null
                                insert(imageItem , callback = {
//                                    imageItem.id = it
                                    Log.e("imageTask","id: ${it} save ok")
                                })

                            }
                            getAll {
                                imageItemList = it
                                showAdapter = ShowAdapter(requireActivity() , imageItemList)
                                requireActivity().runOnUiThread {
                                    rv_show.adapter = showAdapter
                                }

                            }

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
    inner class ShowAdapter(val _activity: Activity, val imageItemList: MutableList<ImageItem>) : RecyclerView.Adapter<ShowAdapter.ShowViewHolder>() {

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

        override fun getItemCount(): Int { return imageItemList.size }


        override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
            val imageItem = imageItemList.get(position)
            holder.tv_name.setText(imageItem.name)
            var bitmap = BitmapFactory.decodeByteArray(imageItem.imgBitmap , 0 , imageItem.imgBitmap!!.size)
            bitmap = restoreBitmap(bitmap)
            holder.iv_show.setImageBitmap(bitmap)
            holder.itemView.setOnClickListener {
//                var b = Bundle()
//                b.putSerializable("imageItem",imageItem)
//                Navigation.findNavController(it).navigate(R.id.action_pickFragment_to_imageViewFragment , b)

                var uris : MutableList<Uri> = arrayListOf()
                for (item in imageItemList){
                    var bitmap = BitmapFactory.decodeByteArray(item.imgBitmap , 0 , item.imgBitmap!!.size)
                    bitmap = restoreBitmap(bitmap)
                    uris.add(getImageUri(_activity , bitmap)!!)
                }
                imagefullScreen(requireActivity() , uris , position)
            }
        }

    }
}