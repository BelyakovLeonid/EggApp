package com.example.eggyapp.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun Resources.getBitmap(bitmapId: Int): Bitmap = BitmapFactory.decodeResource(this, bitmapId)