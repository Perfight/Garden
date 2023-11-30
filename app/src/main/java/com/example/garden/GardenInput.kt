package com.example.garden

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.garden.database.Dependencies
import com.example.garden.database.MainVM
import java.lang.String.format

@ExperimentalMaterial3Api
class GardenInput : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = MainVM(Dependencies.gardenRepository)
        setContent {
            val name = remember { mutableStateOf(" ") }
            val RecommendedDrugs = remember { mutableStateOf(" ") }
            val GardenProblems = remember { mutableStateOf(" ") }
            var iconClickable by remember { mutableStateOf(true) }
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
                        Spacer(Modifier.weight(1f, true))
                        if (iconClickable) {
                            IconButton(onClick = {
                                if (name.value != " "){
                                    viewModel.postQuery(format(name.value, GardenProblems.value, RecommendedDrugs.value))
                                    Toast.makeText(
                                        this@GardenInput,
                                        "Препарат был добавлен.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    iconClickable = false //кнопка сохранения больше не отображается
                                } else{
                                    Toast.makeText(
                                        this@GardenInput,
                                        "Препарат не был добавлен.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Save"
                                )
                            }

                        }
                    }
                }

            ) {
                Column {
                    EditableTextField(value = name, "Название")
                    EditableTextField(value = RecommendedDrugs, "Рекомендованные препараты")
                    EditableTextField(value = GardenProblems, "Проблема")
                }
            }
        }
    }
}



@Composable
fun EditableTextField(value: MutableState<String>, field: String) { //чтобы очень плохо не выглядело
    OutlinedTextField(
        value = value.value,
        onValueChange = { newValue ->
            value.value = newValue
        },
        label = { Text(field) },
        modifier = Modifier.fillMaxWidth(),
    )
}