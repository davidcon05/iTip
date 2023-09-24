package com.davecon.itip.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davecon.itip.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    // Adding the = makes this optional
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelID: String,
    enabled: Boolean = true,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Next, // Specifies the action to be performed when the user presses the IME action button
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { newValue -> valueState.value = newValue },
        label = { Text(labelID) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.AttachMoney,
                contentDescription = "Enter bill amount"
            )
        },
        enabled = enabled,
        textStyle = TextStyle(fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground),
        singleLine = isSingleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        modifier = modifier,
    )
}

@Composable
fun TipButton(
    modifier: Modifier = Modifier,
    tipPercentage: Double?,
    onClick: () -> Unit,
) {
    val percentageString = if (0.0 == tipPercentage) {
        "Custom"
    } else {
        "${(tipPercentage?.times(100))?.toInt()}%"
    }
    Button(
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.light_green)
        ),
        onClick = { onClick() }) {
        Text(text = percentageString, color = Color.Black)
    }
}

@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
) {
    IconButton( onClick = { onClick() }
    ) {
        Icon(
            modifier = Modifier.shadow(0.5.dp, CircleShape),
            imageVector = icon,
            contentDescription = description
        )
    }
}