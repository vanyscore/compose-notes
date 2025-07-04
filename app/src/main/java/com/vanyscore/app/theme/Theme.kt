package com.vanyscore.app.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.vanyscore.app.AppState

// Предположим, что вы объявили эти цвета в другом файле (Color.kt)
private val YellowColorScheme = lightColorScheme(
    primary = Amber500,
    onPrimary = White,
    primaryContainer = Amber100,
    onPrimaryContainer = DeepBrown,
    secondary = Brown500,
    onSecondary = White,
    secondaryContainer = Brown200,
    onSecondaryContainer = Color(0xFF3E2723),
    tertiary = Orange500,
    onTertiary = Color(0xFF3E2723),
    tertiaryContainer = Orange200,
    onTertiaryContainer = Color(0xFF4E342E),
    background = LightYellow,
    onBackground = DeepBrown,
    surface = White,
    onSurface = Color(0xFF4E342E),
    error = ErrorRed,
    onError = White,
    outline = MutedBrown,
    surfaceVariant = Color(0xFFEAEAEA),
    inverseOnSurface = White,
    onErrorContainer = White,
    onSurfaceVariant = DeepBrown,
    errorContainer = White,
    inversePrimary = Color(0xFFFFA000),
    inverseSurface = Color(0xFFEEEEEE),
    outlineVariant = MutedBrown,
    scrim = Color(0x99000000),
    surfaceTint = Amber500,
)

private val YellowDarkColorScheme = darkColorScheme(
    primary = Amber200,
    onPrimary = DeepBrown,
    primaryContainer = Amber700,
    onPrimaryContainer = White,
    secondary = Brown200,
    onSecondary = DeepBrown,
    secondaryContainer = Brown700,
    onSecondaryContainer = White,
    tertiary = Orange200,
    onTertiary = DeepBrown,
    tertiaryContainer = Orange700,
    onTertiaryContainer = White,
    background = DarkAmber,
    onBackground = White,
    surface = Color(0xFF2C2C2C),
    onSurface = White,
    error = ErrorRedDark,
    onError = White,
    outline = MutedBrown,
    surfaceVariant = Color(0xFF444444),
    inverseOnSurface = DeepBrown,
    onErrorContainer = White,
    onSurfaceVariant = Amber100,
    errorContainer = ErrorRedLight,
    inversePrimary = Amber300,
    inverseSurface = Color(0xFF222222),
    outlineVariant = Amber200,
    scrim = Color(0x99000000),
    surfaceTint = Amber200,
)

private val GreenLightColorScheme = lightColorScheme(
    primary = Green500,
    onPrimary = White,
    primaryContainer = Green100,
    onPrimaryContainer = Color(0xFF1B5E20),
    secondary = Teal500,
    onSecondary = White,
    secondaryContainer = Teal200,
    onSecondaryContainer = Color(0xFF004D40),
    tertiary = Lime500,
    onTertiary = Black,
    tertiaryContainer = Lime200,
    onTertiaryContainer = Black,
    background = Color(0xFFE8F5E9),
    onBackground = Color(0xFF1B5E20),
    surface = White,
    onSurface = Color(0xFF2E7D32),
    error = ErrorRed,
    onError = White,
    outline = Color(0xFF81C784),
    surfaceVariant = Color(0xFFD0F0C0),
    inverseOnSurface = White,
    onErrorContainer = White,
    onSurfaceVariant = Color(0xFF1B5E20),
    errorContainer = Color(0xFFFFCDD2),
    inversePrimary = Green700,
    inverseSurface = Color(0xFFEEEEEE),
    outlineVariant = Color(0xFFA5D6A7),
    scrim = Color(0x99000000),
    surfaceTint = Green500,
)

private val GreenDarkColorScheme = darkColorScheme(
    primary = Green200,
    onPrimary = Color(0xFF1B5E20),
    primaryContainer = Green700,
    onPrimaryContainer = White,
    secondary = Teal200,
    onSecondary = Black,
    secondaryContainer = Teal700,
    onSecondaryContainer = White,
    tertiary = Lime200,
    onTertiary = Black,
    tertiaryContainer = Lime700,
    onTertiaryContainer = White,
    background = Color(0xFF1B5E20),
    onBackground = White,
    surface = Color(0xFF263238),
    onSurface = White,
    error = ErrorRedDark,
    onError = White,
    outline = Color(0xFF4CAF50),
    surfaceVariant = Color(0xFF37474F),
    inverseOnSurface = Color(0xFF1B5E20),
    onErrorContainer = White,
    onSurfaceVariant = Color(0xFF81C784),
    errorContainer = ErrorRedLight,
    inversePrimary = Green300,
    inverseSurface = Color(0xFF212121),
    outlineVariant = Green300,
    scrim = Color(0x99000000),
    surfaceTint = Green200,
)

// 🔴 RED LIGHT THEME
private val RedLightColorScheme = lightColorScheme(
    primary = Red500,
    onPrimary = White,
    primaryContainer = Red100,
    onPrimaryContainer = Red700,
    secondary = Pink200,
    onSecondary = Black,
    secondaryContainer = Pink100,
    onSecondaryContainer = Black,
    tertiary = Orange500,
    onTertiary = White,
    tertiaryContainer = Orange200,
    onTertiaryContainer = Black,
    background = Color(0xFFFFEBEE),
    onBackground = Red700,
    surface = White,
    onSurface = Red700,
    error = ErrorRed,
    onError = White,
    outline = Red300,
    surfaceVariant = Red100,
    inverseOnSurface = White,
    onErrorContainer = White,
    onSurfaceVariant = Red700,
    errorContainer = Red100,
    inversePrimary = Red300,
    inverseSurface = Color(0xFFFAFAFA),
    outlineVariant = Red200,
    scrim = Color(0x99000000),
    surfaceTint = Red500,
)

// 🔴 RED DARK THEME
private val RedDarkColorScheme = darkColorScheme(
    primary = Red200,
    onPrimary = Black,
    primaryContainer = Red700,
    onPrimaryContainer = White,
    secondary = Pink200,
    onSecondary = Black,
    secondaryContainer = Pink700,
    onSecondaryContainer = White,
    tertiary = Orange200,
    onTertiary = Black,
    tertiaryContainer = Orange700,
    onTertiaryContainer = White,
    background = Color(0xFF311B1B),
    onBackground = White,
    surface = Color(0xFF2C1A1A),
    onSurface = White,
    error = ErrorRedDark,
    onError = White,
    outline = Red300,
    surfaceVariant = Red200,
    inverseOnSurface = Red100,
    onErrorContainer = White,
    onSurfaceVariant = Red100,
    errorContainer = Red100,
    inversePrimary = Red300,
    inverseSurface = Color(0xFF212121),
    outlineVariant = Red200,
    scrim = Color(0x99000000),
    surfaceTint = Red200,
)


// 🔵 BLUE LIGHT THEME
private val BlueLightColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue700,
    secondary = Teal500,
    onSecondary = White,
    secondaryContainer = Teal200,
    onSecondaryContainer = Black,
    tertiary = Purple200,
    onTertiary = Black,
    tertiaryContainer = Purple100,
    onTertiaryContainer = Black,
    background = Color(0xFFE3F2FD),
    onBackground = Blue700,
    surface = White,
    onSurface = Blue700,
    error = ErrorRed,
    onError = White,
    outline = Blue300,
    surfaceVariant = Blue100,
    inverseOnSurface = White,
    onErrorContainer = White,
    onSurfaceVariant = Blue700,
    errorContainer = Blue100,
    inversePrimary = Blue300,
    inverseSurface = Color(0xFFFAFAFA),
    outlineVariant = Blue200,
    scrim = Color(0x99000000),
    surfaceTint = Blue500,
)

// 🔵 BLUE DARK THEME
private val BlueDarkColorScheme = darkColorScheme(
    primary = Blue200,
    onPrimary = Black,
    primaryContainer = Blue700,
    onPrimaryContainer = White,
    secondary = Teal200,
    onSecondary = Black,
    secondaryContainer = Teal700,
    onSecondaryContainer = White,
    tertiary = Purple300,
    onTertiary = Black,
    tertiaryContainer = Purple700,
    onTertiaryContainer = White,
    background = Color(0xFF0D47A1),
    onBackground = White,
    surface = Color(0xFF102027),
    onSurface = White,
    error = ErrorRedDark,
    onError = White,
    outline = Blue300,
    surfaceVariant = Blue200,
    inverseOnSurface = Blue100,
    onErrorContainer = White,
    onSurfaceVariant = Blue100,
    errorContainer = Red100,
    inversePrimary = Blue300,
    inverseSurface = Color(0xFF212121),
    outlineVariant = Blue200,
    scrim = Color(0x99000000),
    surfaceTint = Blue200,
)


// 🟣 PURPLE LIGHT THEME
private val PurpleLightColorScheme = lightColorScheme(
    primary = Purple500,
    onPrimary = White,
    primaryContainer = Purple100,
    onPrimaryContainer = Purple700,
    secondary = Pink200,
    onSecondary = Black,
    secondaryContainer = Pink100,
    onSecondaryContainer = Black,
    tertiary = Blue200,
    onTertiary = Black,
    tertiaryContainer = Blue100,
    onTertiaryContainer = Black,
    background = Color(0xFFF3E5F5),
    onBackground = Purple700,
    surface = White,
    onSurface = Purple700,
    error = ErrorRed,
    onError = White,
    outline = Purple300,
    surfaceVariant = Purple100,
    inverseOnSurface = White,
    onErrorContainer = White,
    onSurfaceVariant = Purple700,
    errorContainer = Red100,
    inversePrimary = Purple300,
    inverseSurface = Color(0xFFFAFAFA),
    outlineVariant = Purple200,
    scrim = Color(0x99000000),
    surfaceTint = Purple500,
)

// 🟣 PURPLE DARK THEME
private val PurpleDarkColorScheme = darkColorScheme(
    primary = Purple200,
    onPrimary = Black,
    primaryContainer = Purple700,
    onPrimaryContainer = White,
    secondary = Pink200,
    onSecondary = Black,
    secondaryContainer = Pink700,
    onSecondaryContainer = White,
    tertiary = Blue300,
    onTertiary = Black,
    tertiaryContainer = Blue700,
    onTertiaryContainer = White,
    background = Color(0xFF4A148C),
    onBackground = White,
    surface = Color(0xFF1A237E),
    onSurface = White,
    error = ErrorRedDark,
    onError = White,
    outline = Purple300,
    surfaceVariant = Purple200,
    inverseOnSurface = Purple100,
    onErrorContainer = White,
    onSurfaceVariant = Purple100,
    errorContainer = Red100,
    inversePrimary = Purple300,
    inverseSurface = Color(0xFF212121),
    outlineVariant = Purple200,
    scrim = Color(0x99000000),
    surfaceTint = Purple200,
)

val lightThemes = listOf(AppTheme.YELLOW_LIGHT,
    AppTheme.GREEN_LIGHT, AppTheme.RED_LIGHT, AppTheme.BLUE_LIGHT, AppTheme.PURPLE_LIGHT
)

enum class AppTheme {
    YELLOW_LIGHT,
    YELLOW_DARK,
    GREEN_LIGHT,
    GREEN_DARK,
    RED_LIGHT,
    RED_DARK,
    BLUE_LIGHT,
    BLUE_DARK,
    PURPLE_LIGHT,
    PURPLE_DARK;

    fun toColorScheme(): ColorScheme {
        return when (this) {
            YELLOW_LIGHT -> YellowColorScheme
            YELLOW_DARK -> YellowDarkColorScheme
            GREEN_LIGHT -> GreenLightColorScheme
            GREEN_DARK -> GreenDarkColorScheme
            RED_LIGHT -> RedLightColorScheme
            RED_DARK -> RedDarkColorScheme
            BLUE_LIGHT -> BlueLightColorScheme
            BLUE_DARK -> BlueDarkColorScheme
            PURPLE_LIGHT -> PurpleLightColorScheme
            PURPLE_DARK -> PurpleDarkColorScheme
        }
    }
}

@Composable
fun ThemeProvider(
    content: @Composable () -> Unit
) {
    val themeState = AppState.source.collectAsState()
    val theme = themeState.value.theme
    val colorScheme = theme.toColorScheme()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
