package com.example.eggyapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import android.widget.Toast
import com.example.eggyapp.R

fun Context.showToast(string: String): Toast =
    Toast.makeText(this, string, Toast.LENGTH_LONG).apply {
        view.findViewById<TextView>(android.R.id.message)
            .setTextColor(resources.getColor(R.color.colorGrayLight, theme))
        show()
    }

fun Context.getBitmap(bitmapId: Int): Bitmap = BitmapFactory.decodeResource(resources, bitmapId)
