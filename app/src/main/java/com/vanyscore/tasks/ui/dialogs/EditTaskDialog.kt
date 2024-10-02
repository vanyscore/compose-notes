package com.vanyscore.tasks.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vanyscore.tasks.data.Task
import java.util.Calendar
import java.util.Date

@Composable
fun EditTaskDialog(
    date: Date = Calendar.getInstance().time,
    task: Task?,
    onResult: (task: Task) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val isCreate = task == null
    // TODO: Добавить строковые ресурсы
    val dialogTitle = if (isCreate) "Создать задачу" else "Редактировать задачу"
    val successTitle = if (isCreate) "Создать" else "Применить"
    val textValue = remember {
        mutableStateOf(task?.title ?: "")
    }
    return Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(dialogTitle)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = textValue.value,
                    onValueChange = { newText ->
                        textValue.value = newText
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onResult(task?.copy(
                            title = textValue.value
                        ) ?: Task(
                            title = textValue.value,
                            isSuccess = false,
                            date = date
                        ))
                    }
                ) {
                    Text(successTitle)
                }
            }
        }
    }
}