package com.davecon.itip

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.davecon.itip.ui.theme.ITipTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davecon.itip.components.InputField
import com.davecon.itip.components.TipButton

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
                fontSize = 40.sp
            )
        }
    }
}

/**
 * This implementation allows for state hoisting the form info to the calculator composable
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TipCalculator() {
    // We can now use a lambda to detct changes in the bill amount
    BillForm() { billAmount ->
        Log.d(TAG, "Tip Calculator: Bill amount changed to $billAmount")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier = Modifier, onValChanged: (String) -> Unit = {}) {
    val billState = remember { mutableStateOf("") }
    val validAmountState = remember(billState.value) { billState.value.trim().isNotEmpty() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val tipPercentage = remember { mutableStateOf(0.0) }
    val isCustomTip = remember { mutableStateOf(false) }
    val sliderPosition = remember { mutableStateOf(0.0f) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(corner = CornerSize(12.dp))
            ),
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Text Field
            InputField(
                valueState = billState,
                labelID = "Enter Bill Amount",
                onAction = KeyboardActions(
                    onNext = {
                        if (!validAmountState) return@KeyboardActions
                        onValChanged(billState.value.trim())
                        keyboardController?.hide()
                    },
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            // validState is true when the text field is not empty
            if (true /*validAmountState*/) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Text FAB- numberPersons - FAB+
                    Text(
                        "Split",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        fontSize = 16.sp
                    )
                    SplitControls(numPersons = 99)
                }
                Row {
                    TipPercentageButtons()
                }
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                ) {
                    TipAmount(tipPerPerson = 0.0)
                }
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Text("33%", fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "0",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp)
                        )
                        Slider(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp)
                                .width(272.dp)
                                .align(Alignment.CenterVertically)
                                .semantics {
                                    contentDescription = "Custom Tip Percentage Slider"
                                },
                            value = sliderPosition.value,
                            onValueChange = { sliderPosition.value = it },
                            valueRange = 0.0f..1.0f,
                            steps = 100,
                            colors = SliderDefaults.colors(
                                thumbColor = Color.Black,
                                activeTrackColor = colorResource(id = R.color.light_green),
                                inactiveTrackColor = Color.LightGray
                            )
                        )
                        Text(text = "100",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 8.dp)
                        )
                    }
                }


                // Cool idea, buttons for tip percentage: 10%, 15%, 20%, custom
                // Tap custom to reveal slider
            } else {
                Box() {}
            }
        }
    }
}

@Composable
fun SplitControls(numPersons: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier.shadow(0.5.dp, CircleShape),
                imageVector = Icons.Outlined.RemoveCircleOutline,
                contentDescription = "Subtract person to split tip"
            )
        }
        val persons = "%3d".format(numPersons)
        Text("$persons", modifier = Modifier.align(Alignment.CenterVertically), fontSize = 16.sp)
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier.shadow(0.5.dp, CircleShape),
                imageVector = Icons.Outlined.AddCircleOutline,
                contentDescription = "Add person to split tip"
            )
        }
    }
}

@Composable
fun TipPercentageButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TipButton(tipPercentage = "15%")
        TipButton(tipPercentage = "20%")
        TipButton(tipPercentage = "Custom")
    }
}

@Composable
fun TipAmount(tipPerPerson: Double) {
    // Tip
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val tip = "%.2f".format(tipPerPerson)
        Text("$$tip", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(text = "Tip per Person", modifier = Modifier.padding(start = 8.dp), fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun TipScreenPreview() {
    ITipTheme {
        TipScreen()
    }
}





