package com.example.mymoviedatabase.presentation.utils

import android.content.Context
import android.util.TypedValue

class Transform {
    companion object {
        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }

        fun getYear(date: String) : String {
            return date.substringBefore("-")
        }
    }
}