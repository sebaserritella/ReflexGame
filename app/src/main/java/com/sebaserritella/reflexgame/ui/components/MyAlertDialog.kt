package com.sebaserritella.reflexgame.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebaserritella.reflexgame.R


@Composable
fun MyAlertDialog(
    onDismissDialog: () -> Unit,
) {
    AlertDialog(modifier = Modifier.clip(shape = RoundedCornerShape(20.dp)),
        onDismissRequest = { onDismissDialog() },
        confirmButton = {
            Row(
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(shape = RoundedCornerShape(20.dp)),
                    onClick = { onDismissDialog() },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.play_again), fontSize = 14.sp)
                }
            }
        },
        title = { Text(text = stringResource(id = R.string.empty)) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.fail),
                    fontSize = 24.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        })
}