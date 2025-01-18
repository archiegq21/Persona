package com.apps.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.apps.designsystem.Res
import com.apps.designsystem.arvo_bold
import com.apps.designsystem.arvo_boldItalic
import com.apps.designsystem.arvo_italic
import com.apps.designsystem.arvo_regular
import com.apps.designsystem.oswald_bold
import com.apps.designsystem.oswald_extraLight
import com.apps.designsystem.oswald_light
import com.apps.designsystem.oswald_medium
import com.apps.designsystem.oswald_regular
import com.apps.designsystem.oswald_semiBold
import org.jetbrains.compose.resources.Font

private val bodyFontFamily: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.arvo_bold, FontWeight.Bold),
        Font(Res.font.arvo_boldItalic, FontWeight.Bold, FontStyle.Italic),
        Font(Res.font.arvo_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.arvo_regular, FontWeight.Normal),
    )

private val displayFontFamily: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.oswald_bold, FontWeight.Bold),
        Font(Res.font.oswald_extraLight, FontWeight.ExtraLight),
        Font(Res.font.oswald_light, FontWeight.Light),
        Font(Res.font.oswald_medium, FontWeight.Medium),
        Font(Res.font.oswald_regular, FontWeight.Normal),
        Font(Res.font.oswald_semiBold, FontWeight.SemiBold),
    )

private val baseline = Typography()

val PersonaTypography: Typography
    @Composable
    get() = Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )

