/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.ui.themes

import androidx.annotation.StyleRes
import im.vector.R

/**
 * Class to manage Activity other possible themes.
 * Note that style for light theme is default and is declared in the Android Manifest
 */
sealed class ActivityOtherThemes(@StyleRes val dark: Int,
                                 @StyleRes val black: Int,
                                 @StyleRes val status: Int) {

    object Default : ActivityOtherThemes(
            R.style.AppTheme_Dark,
            R.style.AppTheme_Black,
            R.style.AppTheme_Status
    )

    object NoActionBarFullscreen : ActivityOtherThemes(
            R.style.AppTheme_NoActionBar_FullScreen_Dark,
            R.style.AppTheme_NoActionBar_FullScreen_Black,
            R.style.AppTheme_NoActionBar_FullScreen_Status
    )

    object Home : ActivityOtherThemes(
            R.style.HomeActivityTheme_Dark,
            R.style.HomeActivityTheme_Black,
            R.style.HomeActivityTheme_Status
    )

    object Group : ActivityOtherThemes(
            R.style.GroupAppTheme_Dark,
            R.style.GroupAppTheme_Black,
            R.style.GroupAppTheme_Status
    )

    object Picker : ActivityOtherThemes(
            R.style.CountryPickerTheme_Dark,
            R.style.CountryPickerTheme_Black,
            R.style.CountryPickerTheme_Status
    )

    object Lock : ActivityOtherThemes(
            R.style.Theme_Vector_Lock_Dark,
            R.style.Theme_Vector_Lock_Light,
            R.style.Theme_Vector_Lock_Status
    )

    object Search : ActivityOtherThemes(
            R.style.SearchesAppTheme_Dark,
            R.style.SearchesAppTheme_Black,
            R.style.SearchesAppTheme_Status
    )

    object Call : ActivityOtherThemes(
            R.style.CallActivityTheme_Dark,
            R.style.CallActivityTheme_Black,
            R.style.CallActivityTheme_Status
    )
}