package com.example.mymoviedatabase.presentation.ui.search

import android.graphics.drawable.Drawable

sealed class SearchFragmentErrors(
    val tvErrorVisibility: Boolean,
    val ivErrorVisibility: Boolean,
    val btErrorVisibility: Boolean,
    val drawableRes: Drawable?,
    val stringRes: String?
) {

    object HideSearchErrors : SearchFragmentErrors(false, false, false, null, null)

    class EmptyResponse(errorMsg: String?, errorImg: Drawable?) :
        SearchFragmentErrors(true, true, false, errorImg, errorMsg)

    class InternetConnection(errorMsg: String?, errorImg: Drawable?) :
        SearchFragmentErrors(true, true, true, errorImg, errorMsg)
}