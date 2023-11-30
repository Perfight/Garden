package com.example.garden

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.*
import com.example.garden.database.Dependencies
import com.example.garden.database.Garden
import com.example.garden.database.MainVM
import com.google.firebase.inappmessaging.model.Button
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.init(applicationContext)
        val viewModel: MainVM = MainVM(Dependencies.gardenRepository)
        setContent {
            var openDialog by remember { mutableStateOf(false) }
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            var topBarTitle = remember { mutableStateOf("Список препаратов") }
            var checklists by remember { mutableStateOf(listOf("Loading...")) }
            var deleteID by remember {
                mutableStateOf(0)
            }
            viewModel.getLists()
            viewModel.temp_list1.observe(this) { //получение из базы данных списков по симптомам
                checklists = it
            }

            Scaffold( //организация элементов окна
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar {
                        MainTopBar(
                            title = topBarTitle.value, //название
                            scaffoldState
                        )
                    }
                },
                drawerContent = {
                    DrawerMenu(checklists, onEvent = { event -> //боковое меню
                        when (event) {
                            is DrawerEvents.OnItemClick -> {
                                topBarTitle.value = event.title
                            }
                        }
                        coroutineScope.launch {
                            scaffoldState.drawerState.close() //подгружает боковое меню
                        }
                    }, context = this@MainActivity)
                },
                floatingActionButton = {//плавающая кнопочка с переходом на активность вот так вот да
                    FloatingActionButton(
                        content = { Icon(Icons.Filled.Add, contentDescription = "Добавить") },
                        onClick = {
                            val intent = Intent(this@MainActivity, GardenInput::class.java)
                            startActivity(intent)
                            viewModel.getLists()
                        }
                    )
                },
                floatingActionButtonPosition = FabPosition.End,
                isFloatingActionButtonDocked = true,
            ) { it ->
                it.calculateBottomPadding()
                viewModel.getGardenList(
                    intent.getStringExtra("listName") ?: " "
                )
                var list by remember {
                    mutableStateOf(
                        listOf(
                            Garden(
                                "Loading...",
                                0,
                                "Loading...",
                                "Loading...",
                            )
                        )
                    )
                }
                viewModel.gardlist.observe(this) {
                    list = it //получение списка лекарств
                }
                if(openDialog){
                    AlertDialog(onDismissRequest = {
                        openDialog = false
                    },
                        title = { Text(text = "Вы действительно хотите удалить?") },
                        buttons = {
                            Button(modifier = Modifier.fillMaxWidth(),
                                onClick = { openDialog = false }
                            ) {
                                Text("Отмена", fontSize = 12.sp)
                            }
                            Button(modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Препарат было удалено.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.deleteGarden(deleteID)
                                    openDialog = false

                                }
                            ) {
                                Text("Удалить", fontSize = 12.sp)
                            }
                        })
                }
                LazyColumn {
                    items(list) {gard -> //принял отобразил список
                        Card(modifier = Modifier
                            .fillMaxWidth() //занял максимальную ширину
                            .defaultMinSize(minHeight = 50.dp)
                            .padding(3.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 5.dp, //тень под кнопочкой
                            onClick = {
                                val intent =
                                    Intent(this@MainActivity, InfoActivity::class.java)
                                intent.putExtra("id", gard.id)
                                startActivity(intent)
                            }
                        ) {
                            Box(contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),

                                ) {
                                Row {
                                    Text(text = "${gard.name}")

                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.CenterEnd
                                    ){
                                        Button(onClick = { //удаляет элемент
                                            openDialog = true
                                            deleteID = gard.id
                                        }) {
                                            Icon(Icons.TwoTone.Delete, contentDescription = "Delete")
                                        }

                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerMenu(list: List<String>, onEvent: (DrawerEvents) -> Unit, context: Context) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header()
            Body(list, onEvent, context = context)
        }
    }
}

@Composable
fun Header() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Домашняя аптечка",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun Body(list: List<String>, onEvent: (DrawerEvents) -> Unit, context: Context) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(list) { index, title ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                onClick = {
                    var intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("listName", title)
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .wrapContentWidth(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MainTopBar(title: String, scaffoldState: ScaffoldState) {
    val coroutine = rememberCoroutineScope()
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = {
                coroutine.launch {
                    scaffoldState.drawerState.open()
                }
            })
            { Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu") }
        }
    )
}

sealed class DrawerEvents {
    data class OnItemClick(val title: String, val index: Int) : DrawerEvents()
}