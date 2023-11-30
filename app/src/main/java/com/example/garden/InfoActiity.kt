package com.example.garden

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.garden.database.Dependencies
import com.example.garden.database.Garden
import com.google.mlkit.vision.text.Text

class InfoActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: InfoVM = InfoVM(Dependencies.gardenRepository)
        setContent {
            val arguments = this.intent.extras
            val id = arguments?.getInt("id")
            var garden by remember {
                mutableStateOf(
                    Garden(
                        "Loading...",
                        0,
                        "Loading...",
                        "Loading..."
                    )
                )
            }
            if (id != null) {
                viewModel.getById(id)
            }
            Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show() //проверка что правильно передали
            viewModel.garden.observe(this) {
                garden = it
            }//берем данные из бд

            Scaffold(
                topBar = {
                    TopAppBar {
                        IconButton(onClick = {
                            finish()
                        }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }

            ) {
                Column {
                    ListItem("Название лекарства", garden.name)
                    ListItem("Рекомендованные препараты", garden.RecommendedDrugs)
                    ListItem("Проблема", garden.GardenProblems)
                }
            }
        }
    }
}

@Composable
fun Chapter(heading: String, description: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,//разметка
        verticalArrangement = Arrangement.Center) {
        Text(
            text = heading,
            color = Color.Gray,
            fontSize = 18.sp
        )
        Text(
            text = description,
            fontSize = 21.sp
        )
    }
}

@Composable
private fun ListItem(name: String, desc: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp)
            .padding(3.dp),
        border = BorderStroke(0.5.dp, Color.LightGray),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ) {
        Box {
            Chapter(heading = name, description = desc)
        }
    }
}