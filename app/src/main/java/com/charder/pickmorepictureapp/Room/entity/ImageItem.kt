package com.charder.pickmorepictureapp.Room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "ImageItem") //@Entity(tableName = "ShowItemT")

 class ImageItem
//(@PrimaryKey val id: Long,
//                      @ColumnInfo(name = "name") var name: String?,
//                      @ColumnInfo(name = "imgBitmap") var imgBitmap: ByteArray?)
    : Serializable {

//     constructor(id: Long, name: String? , imgBitmap:ByteArray?) {
//
//     }
//    constructor() : this(id , name ,imgBitmap ){
//
//    }

    // Kotlin id 一定要用Long???
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null
    @ColumnInfo
    var name : String? = null
    @ColumnInfo
    var imgBitmap : ByteArray? = null
}

//@PrimaryKey val id: Long,
//@ColumnInfo(name = "name") var name: String?,
//@ColumnInfo(name = "imgBitmap") var imgBitmap: ByteArray?