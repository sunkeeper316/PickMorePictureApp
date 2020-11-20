package com.charder.pickmorepictureapp.Room.task

import android.os.AsyncTask
import com.charder.pickmorepictureapp.Room.entity.ImageItem
import com.charder.pickmorepictureapp.db


fun insert(imageItem : ImageItem , callback : (Long) -> Unit) {

    AsyncTask.execute {
        val id = db.imageItemDao().insert(imageItem)
        callback(id)
    }

}

fun getAll( callback : ( MutableList<ImageItem>) -> Unit) {
    AsyncTask.execute {
        val imageItems = db.imageItemDao().getAllImageItem()
        callback(imageItems)
    }


}

fun getImageItemById(id : Long , callback : (ImageItem) -> Unit)  {
    AsyncTask.execute {
        val imageItem = db.imageItemDao().getImageItem(id)
        callback(imageItem)
    }
}