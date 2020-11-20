package com.charder.pickmorepictureapp.Room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.charder.pickmorepictureapp.Room.entity.ImageItem


@Dao
interface ImageItemDao {
    @Query("SELECT * FROM ImageItem")
    fun getAllImageItem() : MutableList<ImageItem>

    @Query("SELECT * FROM ImageItem WHERE id = :id")
    fun getImageItem(id: Long) : ImageItem

    @Insert
    fun insert(imageItem:ImageItem) : Long

    @Delete
    fun delete(imageItem:ImageItem) : Int
}