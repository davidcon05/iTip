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
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.davecon.itip.ui.theme.ITipTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davecon.itip.components.InputField
import com.davecon.itip.components.RoundIconButton
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
                text = "Tip Per Person",
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
@Composable
fun TipCalculator() {
    // We can now use a lambda to detect changes in the bill amount
    BillForm { billAmount ->
        Log.d(TAG, "Tip Calculator: Bill amount changed to $billAmount")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier = Modifier, onValChanged: (String) -> Unit = {}) {
    val billState = remember { mutableStateOf("") }
    val validAmountState = remember(billState.value) { billState.value.trim().isNotEmpty() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val tipPercentage = remember { mutableDoubleStateOf(0.0) }
    var isCustomTip by remember { mutableStateOf(false) }
    val sliderPositionState = remember { mutableFloatStateOf(0.0f) }
    val numberPersons = remember { mutableIntStateOf(1) }
    val tipPerPerson = remember { mutableDoubleStateOf(0.0) }
    val tipAmountState = remember { mutableDoubleStateOf(0.0) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
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
            if (validAmountState) {
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
                    SplitControls(numberPersons)
                }
                Row {
                    TipPercentageButtons(tipPercentage, sliderPositionState, isCustomTip) {
                        isCustomTip = it
                    }
                }
                if (isCustomTip) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Cool idea, buttons for tip percentage: 15%, 20%, custom
                        // Tap custom to reveal slider
                        CustomTipSlider(
                            sliderPositionState = sliderPositionState,
                            tipAmountState,
                            billState,
                            numberPersons,
                        )
                    }
                } else {
                    Box {}
                }
            } else {
                Box {}
            }
        }
    }
}

fun calculateTip(
    billAmount: String,
    tipPercentage: Float,
    numberPersons: Int,
): Double {
    if (billAmount.isEmpty()) return 0.0
    val bill = billAmount.toDouble()
    return (bill * tipPercentage) / numberPersons
}

@Composable
fun SplitControls(numPersons: MutableState<Int>) {
    val persons = "%3d".format(numPersons.value)
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        RoundIconButton(
            modifier = Modifier,
            icon = Icons.Outlined.RemoveCircleOutline,
            description = "Subtract person to split tip",
            onClick = {
                if (numPersons.value > 1) numPersons.value -= 1
            }
        )

        Text(persons, modifier = Modifier.align(Alignment.CenterVertically), fontSize = 16.sp)

        RoundIconButton(
            modifier = Modifier,
            icon = Icons.Outlined.RemoveCircleOutline,
            description = "Add person to split tip",
            onClick = {
                if (numPersons.value < 100) numPersons.value += 1
            }
        )
    }
}

@Composable
fun TipPercentageButtons(
    tipPercentage: MutableState<Double> = mutableDoubleStateOf(0.0),
    sliderPositionState: MutableState<Float>,
    isCustomTip: Boolean,
    onCustomTipClicked: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        // TODO: Make these buttons adjust the tip amount
        // Pressing the tip button should update the tip per person
        TipButton(tipPercentage = ("15%")) {
            tipPercentage.value = 0.15
            sliderPositionState.value = 0.15f
        }
        TipButton(tipPercentage = ("20%")) {
            tipPercentage.value = 0.20
            sliderPositionState.value = 0.20f
        }
        TipButton(
            tipPercentage = ("Custom"),
            onClick = { onCustomTipClicked(!isCustomTip) })
    }
}

@Composable
fun CustomTipSlider(
    sliderPositionState: MutableState<Float>,
    tipAmountState: MutableDoubleState,
    billState: MutableState<String>,
    numberPersons: MutableState<Int>,
) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    Text(
        sliderPositionState.value.times(100).toInt().toString().plus('%'),
        fontSize = 24.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "1",
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
            value = sliderPositionState.value,
            onValueChange = { newVal ->
                sliderPositionState.value = newVal
                tipAmountState.value =
                    calculateTip(billState.value, sliderPositionState.value, numberPersons.value)
                Log.d("Slider", "Slider finished, tip is: ${tipAmountState.value}")
            },
            onValueChangeFinished = {
                //Log.d("Slider", "CustomTipSlider: Finished...")
            },
            valueRange = 0.01f..1.0f,
            steps = 100,
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = colorResource(id = R.color.light_green),
                inactiveTrackColor = Color.LightGray
            )
        )
        Text(
            text = "100",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TipScreenPreview() {
    ITipTheme {
        TipScreen()
    }
}