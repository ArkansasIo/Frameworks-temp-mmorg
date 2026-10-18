package com.arkansasio.rpgmmo.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colors=darkColorScheme(primary=Color(0xFF4FC3F7),secondary=Color(0xFF80CBC4),background=Color(0xFF071018),surface=Color(0xFF0D1B24))
@Composable fun RpgMmoTheme(content:@Composable()->Unit){MaterialTheme(colorScheme=colors,content=content)}
