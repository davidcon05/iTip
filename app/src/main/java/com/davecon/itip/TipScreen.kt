package com.davecon.itip

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.davecon.itip.ui.theme.ITipTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davecon.itip.components.InputField

@Composable
fun TipScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        TipDisplay()
        TipCalculator()
    }

}

@Composable
fun TipDisplay(tipPerPerson: Double = 0.0) {
    val total = "%.2f".format(tipPerPerson)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp)))
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(corner = CornerSize(12.dp))
            ),
        color = colorResource(R.color.light_green),
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        }
    }
}

/**
 * This implmentation allows for state hoisting the form info to the calculator composable
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TipCalculator() {
    // We can now use a lambda to detct changes in the bill amount
    BillForm() { billAmount ->
        Log.d(TAG, "Tip Calculator: Bill amount changed to $billAmount")
    }
    // Text FAB- numberPersons - FAB+
    // Tip
    // Cool idea, buttons for tip percentage: 10%, 15%, 20%, custom
    // Tap custom to reveal slider

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier = Modifier, onValChanged: (String) -> Unit = {}) {
    val billState = remember { mutableStateOf("") }
    val textFieldState = remember(billState.value) { billState.value.trim().isNotEmpty() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(corner = CornerSize(12.dp))
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Text Field
            InputField(
                valueState = billState,
                labelID = "Enter Bill Amount",
                onAction = KeyboardActions(
                    onNext = {
                        if (!textFieldState) return@KeyboardActions
                        onValChanged(billState.value.trim())
                        keyboardController?.hide()
                    },
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipScreenPreview() {
    ITipTheme {
        TipScreen()
    }
}





