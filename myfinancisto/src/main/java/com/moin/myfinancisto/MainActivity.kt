package com.moin.myfinancisto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moin.myfinancisto.ui.theme.FinancistoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancistoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FinancistoApp()
                }
            }
        }
    }
}

@Composable
fun FinancistoApp() {
    val navController = rememberNavController()
    NavHost(navController, "main") {
        composable(route = "main") {
            val myviewModel: MainViewModel = viewModel()
            MainScreen(viewModel = myviewModel)
        }
        composable(route = "second") {

        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    Column {
        ItemList(itemsList = viewModel.state.value.toItemList())
        Button(onClick = { viewModel.fetchItems(context) }) {

        }
    }

}

fun List<String>.toItemList() = map {
    Item("Item 1", "Type 1", it, "01/01/2022")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FinancistoTheme {
        FinancistoApp()
    }
}
data class Item(val title: String, val type: String, val value: String, val lastUsed: String)
val items = listOf(
    Item("Item 1", "Type 1", "$10", "01/01/2022"),
    Item("Item 2", "Type 2", "$20", "01/02/2022"),
    Item("Item 3", "Type 3", "$30", "01/03/2022"),
)

@Composable
fun ItemList(itemsList: List<Item>) {
    LazyColumn {
        items(itemsList) { item ->
            Row(modifier = Modifier.padding(16.dp)) {
                //Icon
                Image(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                //Main Content
                Column(modifier = Modifier.weight(1f)) {
                    //Type of Content
                    Text(
                        text = item.type,
                        style = TextStyle(
                            fontSize = 12.sp
                        )
                    )
                    //Title
                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    //Last Used Date
                    Text(
                        text = item.lastUsed,
                        style = TextStyle(
                            fontSize = 12.sp
                        )
                    )
                }
                //Currency Value
                Text(
                    text = item.value,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

    }
}
