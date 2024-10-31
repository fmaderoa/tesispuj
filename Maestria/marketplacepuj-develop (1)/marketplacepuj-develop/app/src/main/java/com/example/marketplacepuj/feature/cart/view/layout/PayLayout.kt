package com.example.marketplacepuj.feature.cart.view.layout

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.marketplacepuj.R
import com.example.marketplacepuj.feature.products.detail.view.theme.Purple40
import com.example.marketplacepuj.feature.products.detail.view.theme.Purple80
import com.example.marketplacepuj.feature.products.detail.view.theme.PurpleGrey40

@Composable
fun PurchaseDialog(openDialogCustom: MutableState<Boolean>, onClickAccept: () -> Unit = {}) {
    Dialog(onDismissRequest = { openDialogCustom.value = false }) {
        PurchaseDialogContent(openDialogCustom = openDialogCustom, onClickAccept = onClickAccept)
    }
}

@Composable
fun PurchaseSuccessDialog(onClickAccept: () -> Unit = {}) {
    val openDialogCustom = remember { mutableStateOf(true) }
    Dialog(
        onDismissRequest = { openDialogCustom.value = false },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        PurchaseDialogContent(
            onClickAccept = onClickAccept,
            openDialogCustom = openDialogCustom,
            enableBtnCancel = false,
            dialogIcon = rememberVectorPainter(Icons.Filled.CheckCircle),
            message = stringResource(R.string.text_success_purchase)
        )
    }
}

@Composable
fun PurchaseDialogContent(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean>,
    onClickAccept: () -> Unit = {},
    enableBtnCancel: Boolean = true,
    textBtnCancel: String = stringResource(R.string.text_not_now),
    message: String = stringResource(R.string.text_buy_products),
    dialogIcon: Painter = rememberVectorPainter(Icons.Filled.Info),
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp)
    ) {
        Column(
            modifier
                .background(Color.White)
        ) {

            //.......................................................................
            Image(
                painter = dialogIcon,
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = Purple40
                ),
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth(),

                )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.text_buy_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            //.......................................................................
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(Purple80),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                if (enableBtnCancel) {
                    TextButton(onClick = {
                        openDialogCustom.value = false
                    }) {

                        Text(
                            text = textBtnCancel,
                            fontWeight = FontWeight.Bold,
                            color = PurpleGrey40,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                }

                TextButton(onClick = {
                    openDialogCustom.value = false
                    onClickAccept()
                }) {
                    Text(
                        stringResource(R.string.text_accept),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(name = "Custom Dialog")
@Composable
fun PurchaseDialogPreview() {
    PurchaseDialogContent(openDialogCustom = mutableStateOf(false))
}

@SuppressLint("UnrememberedMutableState")
@Preview(name = "Custom Success Dialog")
@Composable
fun SuccessPurchaseDialogPreview() {
    PurchaseDialogContent(
        enableBtnCancel = false,
        openDialogCustom = mutableStateOf(false),
        dialogIcon = rememberVectorPainter(Icons.Filled.CheckCircle),
        message = stringResource(R.string.text_success_purchase)
    )
}