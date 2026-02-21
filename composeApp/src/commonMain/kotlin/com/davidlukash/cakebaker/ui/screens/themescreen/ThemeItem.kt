package com.davidlukash.cakebaker.ui.screens.themescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.chevron_forward
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.container.PrimaryContainer
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LazyItemScope.ThemeItem(
    theme: String,
    before: @Composable () -> Unit = {},
    after: @Composable () -> Unit = {},
) {
    PrimaryContainer(
        modifier = Modifier.fillParentMaxWidth().animateItem()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp).height(64.dp)
        ) {
            before()
            Text(
                theme,
                style = Theme.Styles.largeBodyStyle
            )
            after()
        }
    }
}