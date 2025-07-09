package com.vanyscore.settings.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vanyscore.app.theme.AppTheme
import com.vanyscore.app.theme.AppThemeType

@Composable
fun ThemeDialog(onSelect: (AppTheme) -> Unit, onDismiss: () -> Unit, ) {
    return Dialog(onDismissRequest = onDismiss) {
        val themes = AppTheme.entries.toTypedArray()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Цвет")
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    themes.map { themeType ->
                        val theme = themeType.toColorScheme(AppThemeType.LIGHT)
                        Box(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(theme.primary)
                                    .clickable {
                                        onSelect(themeType)
                                        onDismiss.invoke()
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}