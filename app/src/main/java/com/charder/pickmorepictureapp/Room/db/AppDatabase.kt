package com.charder.pickmorepictureapp.Room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.charder.pickmorepictureapp.Room.dao.ImageItemDao
import com.charder.pickmorepictureapp.Room.entity.ImageItem


// entities指定資料庫內包含的表格，每個entity都會轉成一個真實表格
@Database(entities = [ImageItem::class ], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageItemDao() : ImageItemDao
}