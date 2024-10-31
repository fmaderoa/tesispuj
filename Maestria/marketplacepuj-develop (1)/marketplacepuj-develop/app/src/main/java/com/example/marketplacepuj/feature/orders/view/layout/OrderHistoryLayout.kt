package com.example.marketplacepuj.feature.orders.view.layout

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.utils.DateFormat
import com.example.marketplacepuj.core.utils.PriceFormat
import com.example.marketplacepuj.feature.cart.data.entity.DireccionEntrega
import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity
import com.example.marketplacepuj.feature.products.detail.view.theme.MarketPlacePUJTheme
import java.math.BigDecimal

@Composable
fun OrderHistoryScreen(
    orderEntities: MutableState<List<OrderEntity>>,
    onClickItem: () -> Unit = {},
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val endYPercentage = 3f
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xffffffff), Color(0xffebebeb)),
                    startY = 0f,
                    endY = screenHeight * endYPercentage
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(orderEntities.value.size) { index ->
                val product = orderEntities.value[index]
                OrderHistoryItem(product)
                if (index == orderEntities.value.size - 1) {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun OrderHistoryScreenWithDataPreview(
    @PreviewParameter(CartItemProvider::class) products: OrderEntity,
) {
    MarketPlacePUJTheme {
        val list = listOf(products, products, products)
        OrderHistoryScreen(
            orderEntities = mutableStateOf(list),
        )
    }
}

@Composable
fun OrderHistoryItem(
    orderEntity: OrderEntity,
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.historyItemColor),
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            Text(
                color = Color.White,
                text = orderEntity.orderId.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                color = Color.White,
                text = PriceFormat.apply(BigDecimal(orderEntity.precioTotal.toString())),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_box),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format(
                        pluralStringResource(
                            id = R.plurals.elements,
                            count = orderEntity.detallePedido.size
                        ), orderEntity.detallePedido.size
                    ),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(16.dp))

                orderEntity.fechaOrden?.let { fechaOrden ->
                    Image(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = DateFormat.getDateFormatted(fechaOrden),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }

        }
    }
}

@Preview(showBackground = false)
@Composable
fun CartItemPreview(
    @PreviewParameter(CartItemProvider::class) product: OrderEntity,
) {
    OrderHistoryItem(orderEntity = product)
}

class CartItemProvider : PreviewParameterProvider<OrderEntity> {
    override val values = sequenceOf(
        OrderEntity(
            detallePedido = emptyList(),
            direccionEntrega = DireccionEntrega(),
            estado = "Pendiente",
            valorNeto = 100000.0,
            impuestos = 50000.0,
            precioTotal = 100000.0,
            fechaOrden = "2023-11-14T14:39:00.000Z",
            orderId = "AQS#FRGS@!",
        )
    )
}