package com.xheghun.xchange.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.xheghun.xchange.R
import com.xheghun.xchange.data.model.Currency
import com.xheghun.xchange.ui.theme.colorBackground
import com.xheghun.xchange.ui.theme.colorBlue
import com.xheghun.xchange.ui.theme.colorGray

@Composable
fun HomeScreen(navController: NavController) {

    Column(
        Modifier
            .background(colorBackground)
            .padding(horizontal = 12.dp)

    ) {
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

        Column(
            Modifier
                .padding(vertical = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .shadow(2.dp, RoundedCornerShape(12.dp))
                .background(Color.White)
                .weight(5f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val baseCurrency = Currency()
            val exchangeCurrency = Currency("GBP")
            Text("Amount", color = colorGray)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "base currency image",
                    modifier = Modifier.clip(CircleShape).size(40.dp)
                )
                Text(text = baseCurrency.code)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "")


            }

            Text("Converted Amount", color = colorGray)

        }

        Column(Modifier.weight(3f)) {
            Text(text = "Indicative Exchange Rate", color = colorGray)
            Text(text = "1 SDG = 0.7 USD", fontWeight = FontWeight.Medium, fontSize = 18.sp)
        }
    }
}

@Composable
fun XTextField() {

}