package com.applid.musicbox.ui.helpers

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import com.applid.musicbox.R

object Assets {

    val placeholderId = getRandomPlaceHolder()

    fun getPlaceholderUri(context: Context): Uri {
        return buildUriOfResource(context.resources, placeholderId)
    }




    private fun buildUriOfResource(resources: Resources, resourceId: Int): Uri {
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(resourceId))
            .appendPath(resources.getResourceTypeName(resourceId))
            .appendPath(resources.getResourceEntryName(resourceId))
            .build()
    }
}

fun getRandomPlaceHolder(): Int {
    val randomNumbe  = listOf(1, 2, 3, 4, 5, 6).shuffled().first()
    println("Random is " + randomNumbe)
    return when(randomNumbe) {
        2 -> R.raw.placeholder_2
        3 -> R.raw.placeholder_3
        4 -> R.raw.placeholder_4
        5 -> R.raw.placeholder_5
        6 -> R.raw.placeholder_6
        else -> R.raw.placeholder_3
    }
}
