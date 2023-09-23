package com.davecon.itip

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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TipCalculator() {
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
                            // TODO: dismiss keyboard
                        keyboardController?.hide()
                    },
                )
            )

            // Text FAB- numberPersons - FAB+
            // Tip
            // Cool idea, buttons for tip percentage: 10%, 15%, 20%, custom
            // Tap custom to reveal slider
            Text(
                text = "Jib black jack aft rigging heave to fore wench jolly boat gabion Shiver me timbers. Heave to hands nipperkin Chain Shot hornswaggle no prey, no pay splice the main brace topmast belay doubloon. Hempen halter loaded to the gunwalls maroon plunder warp fluke case shot no prey, no pay belaying pin hardtack.\n" +
                        "\n" +
                        "Plate Fleet gangway belay no prey, no pay Brethren of the Coast parley Privateer list avast American Main. Avast rutters crimp provost mizzen maroon black jack aye skysail strike colors. Cog tender killick transom squiffy holystone handsomely long clothes matey mizzenmast.\n",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
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





