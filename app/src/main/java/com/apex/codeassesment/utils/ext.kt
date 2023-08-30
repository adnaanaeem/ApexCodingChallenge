package com.apex.codeassesment.utils

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.apex.codeassesment.data.model.User
import com.bumptech.glide.Glide

fun ImageView.loadImageWithGlide(url: String?) {
    Glide.with(this)
        .load(url)
        .into(this)
}

inline fun <reified T : Any> Context.navigateToActivity(vararg extras: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)

    for (extra in extras) {
        val (key, value) = extra
        when (value) {
            is Int -> intent.putExtra(key, value)
            is String -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            is User -> intent.putExtra(key, value)
            // Add more types as needed
            else -> throw IllegalArgumentException("Unsupported extra type: ${value?.javaClass}")
        }
    }

    startActivity(intent)
}