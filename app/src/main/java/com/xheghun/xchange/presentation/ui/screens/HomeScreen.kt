package com.xheghun.xchange.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xheghun.xchange.R
import com.xheghun.xchange.presentation.ui.theme.OpenSans
import com.xheghun.xchange.presentation.ui.theme.colorBackground
import com.xheghun.xchange.presentation.ui.theme.colorBlue
import com.xheghun.xchange.presentation.ui.theme.colorGray
import com.xheghun.xchange.presentation.ui.theme.colorGrey
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen() {
    val model = koinViewModel<HomeViewmodel>()

    val baseCurrency = model.baseCurrency.collectAsStateWithLifecycle().value
    val exchangeCurrency = model.exchangeCurrency.collectAsStateWithLifecycle().value

    val currencies =
        model.exchangeResult.collectAsStateWithLifecycle().value?.rates?.keys?.toList() ?: listOf()

    var baseOptionsVisible by remember { mutableStateOf(false) }
    var exchangeOptionsVisible by remember { mutableStateOf(false) }

    Column(
        Modifier
            .background(colorBackground)
            .padding(horizontal = 12.dp)

    ) {
        //HEADER
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Text("XChange", color = colorBlue, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Check live rates, set rate alerts, receive notifications and more.",
                color = colorGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(200.dp)
            )
        }

        //BODY INPUT
        Column(
            Modifier
                .padding(vertical = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .shadow(2.dp, RoundedCornerShape(12.dp))
                .background(Color.White)
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text("Amount", color = colorGray)

            CurrencyLayout(
                currencyCode = baseCurrency,
                value = model.baseCurrencyAmount.collectAsStateWithLifecycle().value.format(),
                onValueChanged = { newValue ->
                    model.updateBaseAmount(newValue)
                },
                onDropdownClicked = {
                    baseOptionsVisible = true
                }
            )

            DropdownMenu(
                expanded = baseOptionsVisible,
                modifier = Modifier.height(250.dp),
                onDismissRequest = { baseOptionsVisible = false },
            ) {
                currencies.filter { it != model.baseCurrency.value }.forEach { baseSymbol ->
                    DropdownMenuItem(
                        text = { Text(text = baseSymbol) },
                        onClick = {
                            baseOptionsVisible = false
                            model.updateBaseCurrency(baseSymbol)
                        }
                    )
                }
            }
            Box(Modifier.fillMaxWidth()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .height(1.dp)
                        .background(Color.LightGray)
                )

                Image(
                    modifier = Modifier
                        .clickable { model.swapCurrencies() }
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(colorBlue)
                        .padding(vertical = 12.dp, horizontal = 15.dp),
                    painter = painterResource(id = R.drawable.resource_switch),
                    contentDescription = "switch currency"
                )
            }

            Text("Converted Amount", color = colorGray)

            CurrencyLayout(
                readOnly = true,
                currencyCode = exchangeCurrency,
                value = model.exchangeCurrencyAmount.collectAsStateWithLifecycle().value.format(),
                onValueChanged = { newValue ->
                    model.updateExchangeAmount(newValue)
                },
                onDropdownClicked = { exchangeOptionsVisible = true }
            )

            DropdownMenu(
                expanded = exchangeOptionsVisible,
                modifier = Modifier.height(250.dp),
                onDismissRequest = { exchangeOptionsVisible = false }) {
                currencies.filter { it != model.exchangeCurrency.value }.forEach { exchangeSymbol ->
                    DropdownMenuItem(
                        text = { Text(text = exchangeSymbol) },
                        onClick = {
                            exchangeOptionsVisible = false
                            model.updateExchangeCurrency(exchangeSymbol)
                        })
                }
            }
        }

        //RESULT
        Column(Modifier.weight(3.5f)) {
            Text(text = "Indicative Exchange Rate", color = colorGray)
            Text(
                text = "${model.baseCurrencyAmount.collectAsStateWithLifecycle().value} $baseCurrency = ${model.exchangeTotal.collectAsStateWithLifecycle().value} $exchangeCurrency",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
    }
}


@Composable
fun CurrencyLayout(
    readOnly: Boolean = false,
    currencyCode: String,
    value: String,
    onValueChanged: (String) -> Unit,
    onDropdownClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 10.dp)
    ) {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .padding(all = 4.dp)
                .clickable { onDropdownClicked.invoke() }) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "base currency image",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
            )
            Box(Modifier.width(10.dp))
            Text(
                text = currencyCode,
                color = colorBlue,
                fontSize = 18.sp,
                style = TextStyle(fontFamily = OpenSans, fontWeight = FontWeight.Medium)
            )
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "")
        }
        Box(Modifier.width(10.dp))

        CurrencyInput(
            readOnly = readOnly,
            value = value,
            onValueChanged = onValueChanged::invoke,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CurrencyInput(
    readOnly: Boolean,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier
) {
    BasicTextField(
        value = value,
        singleLine = true,
        readOnly = readOnly,
        textStyle = TextStyle(
            fontFamily = OpenSans,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        onValueChange = onValueChanged::invoke,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                )
            } else {
                innerTextField()
            }

        },
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colorGrey)
            .padding(vertical = 15.dp, horizontal = 10.dp)
    )
}