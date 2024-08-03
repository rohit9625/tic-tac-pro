package com.devx.tictacpro.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String)-> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    @DrawableRes icon: Int? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = {
            label?.let {
                Text(text = it)
            }
        },
        trailingIcon = {
            icon?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        singleLine = true
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String)-> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val passwordIcon = if(isPasswordVisible) painterResource(R.drawable.ic_visibility_on)
                        else painterResource(R.drawable.ic_visibility_off_24)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = {
            label?.let {
                Text(text = it)
            }
        },
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    painter = passwordIcon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        visualTransformation = if(isPasswordVisible) VisualTransformation.None
                                else PasswordVisualTransformation(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        singleLine = true
    )
}

@Composable
fun UnderlinedTextField(
    value: String,
    onValueChange: (String)-> Unit,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.onSurface
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .drawBehind {
                drawLine(
                    color = color,
                    start = Offset(0f, size.height + 8),
                    end = Offset(size.width, size.height + 8),
                    strokeWidth = 1.dp.toPx()
                )
            },
        textStyle = TextStyle(
            color = color
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}