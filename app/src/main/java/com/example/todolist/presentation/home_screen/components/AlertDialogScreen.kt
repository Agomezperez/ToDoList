package com.example.todolist.presentation.home_screen.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.domain.Todo
import com.example.todolist.presentation.MainViewModel
import com.example.todolist.presentation.common.taskTextStyle
import kotlinx.coroutines.job

@Composable
fun AlertDialogScreen(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    mainVIewModel: MainViewModel
) {
    var text: String by remember { mutableStateOf("") }

    var isFavorite: Boolean by remember {
        mutableStateOf(false)
    }

    var todo = Todo(0, text, isFavorite)

    val focusRequester = FocusRequester()
    val context: Context = LocalContext.current

    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = "Todo",
                    fontFamily = FontFamily.Serif
                )
            },
            text = {
                LaunchedEffect(
                    key1 = true,
                    block = {
                        coroutineContext.job.invokeOnCompletion {
                            focusRequester.requestFocus()
                        }
                    })

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = {
                            Text("Add task", fontFamily = FontFamily.Monospace)

                        },
                        shape = RectangleShape,
                        modifier = Modifier
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (text.isNotBlank()) {
                                    mainVIewModel.insert(todo)
                                    text = ""
                                    isFavorite = false
                                    onDismiss()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Task cannot be empty",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        ),
                        trailingIcon = {
                            IconButton(onClick = { text = "" }) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "Clear",
                                )
                            }
                        },

                        textStyle = taskTextStyle
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Favorite",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.size(8.dp))
                        Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
                    }
                }
            },

            onDismissRequest = {
                onDismiss()
                text = ""
                isFavorite = false
            },
            confirmButton = {
                Button(onClick = {
                    if (text.isNotBlank()) {
                        mainVIewModel.insert(todo)
                        text = ""
                        isFavorite = false
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Task cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text(text = "Add")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    onDismiss()
                    text = ""
                    isFavorite = false
                }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}