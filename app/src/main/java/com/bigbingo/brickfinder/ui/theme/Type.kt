package com.bigbingo.brickfinder.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bigbingo.brickfinder.R

val HelveticaNeue = FontFamily(
    Font(R.font.helveticaneue_light, FontWeight.Light),
    Font(R.font.helveticaneue_bold, FontWeight.Bold),
    Font(R.font.helveticaneue_roman, FontWeight.Medium),
    Font(R.font.helveticaneue_medium, FontWeight.Medium),
    Font(R.font.helveticaneue_thin, FontWeight.Thin)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = HelveticaNeue,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    titleLarge = TextStyle(
        fontFamily = HelveticaNeue,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 5.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = HelveticaNeue,
        fontWeight = FontWeight.Light,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)