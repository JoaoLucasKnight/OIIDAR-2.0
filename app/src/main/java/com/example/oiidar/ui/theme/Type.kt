package com.example.oiidar.ui.theme


import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.oiidar.R

val Poppins = FontFamily(
    Font(R.font.ppbold, FontWeight.Bold),
    Font(R.font.ppmedium, FontWeight.Medium),
    Font(R.font.ppregular, FontWeight.Normal),
    Font(R.font.pplight, FontWeight.Light),
    Font(R.font.ppthin, FontWeight.Thin),
)

val AnyPro = FontFamily(
    Font(R.font.anypbold, FontWeight.Bold),
    Font(R.font.anypregular, FontWeight.Normal),
)



// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = AnyPro,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 60.sp,
        letterSpacing = 0.sp,
        color = Color(0xFFF09020)
    ),

    displayMedium = TextStyle(
        fontFamily = AnyPro,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = primaryLight
    ),

    headlineSmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        color = primaryLight,
    ),
    bodyLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Light,
        fontSize = 20.sp,
        lineHeight = 30.sp
    ),

    labelLarge =  TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),

    labelMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),

    labelSmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp
    ),




    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)