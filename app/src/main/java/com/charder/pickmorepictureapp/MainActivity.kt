package com.charder.pickmorepictureapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.room.Room
import com.charder.pickmorepictureapp.Room.db.AppDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Room.databaseBuilder(this,AppDatabase::class.java , "ImageDB").build()

    }

    
    override fun onTouchEvent(event: MotionEvent?): Boolean {

//        when(event?.action){
//            MotionEvent.ACTION_DOWN -> {
//                Log.e("MotionEvent" , "ACTION_DOWN")
//            }
//            MotionEvent.ACTION_UP -> {
//                Log.e("MotionEvent" , "ACTION_UP")
//            }
//            MotionEvent.ACTION_POINTER_UP ->{
//                Log.e("MotionEvent" , "ACTION_POINTER_UP")
//            }
//            MotionEvent.ACTION_POINTER_DOWN ->{
//                Log.e("MotionEvent" , "ACTION_POINTER_DOWN")
//            }
//            MotionEvent.ACTION_MOVE ->{
//                Log.e("MotionEvent" , "ACTION_MOVE")
//            }
//        }
//        val isScaleGestureDetector = scaleGestureDetector.onTouchEvent(event)
//        val iGestureDetector = gestureDetector.onTouchEvent(event)
//        if (isScaleGestureDetector){
//            return true
//        }else{
//            return iGestureDetector
//        }
        return super.onTouchEvent(event)
    }

}