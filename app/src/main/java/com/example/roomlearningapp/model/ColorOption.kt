package com.example.roomlearningapp.model

import android.content.Context
import android.os.Build
import com.example.roomlearningapp.R

enum class ColorOption(val color: Int) {
    PURPLE(R.color.purple_200),
    HIGHLIGHTED(R.color.highlight_color),
    NONE(R.color.default_gray);

    fun toArgb(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            context.resources.getColor(color, null)
        else
            context.resources.getColor(color)
}