package io.github.datt16.monea_devtool.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.datt16.monea_devtool.R
import io.github.datt16.monea_devtool.ui.theme.DevToolMaterialTheme

class MainMenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DevToolMaterialTheme {
                    MenuCard(nextPageId = R.id.action_mainMenuFragment_to_mainFragment)
                }
            }
        }
    }

    @Composable
    fun MenuCard(
        title: String = "センサーの管理",
        subTitle: String = "センサーの追加・削除、情報の更新が行えます",
        nextPageId: Int = -1
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
                .clickable { if (nextPageId > 0) findNavController().navigate(nextPageId) },
            elevation = 4.dp,
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Column(modifier = Modifier.fillMaxWidth())
                {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
        }
    }

    @Preview(showBackground = true, name = "Card Preview")
    @Composable
    fun PreviewFun() {
        MenuCard(
            title = "センサーの管理",
            subTitle = "センサーの追加・削除、情報の更新が行えます",
        )
    }
}