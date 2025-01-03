package com.cornmuffin.products.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.cornmuffin.products.LocalSettings
import com.cornmuffin.products.Navigator
import com.cornmuffin.products.R
import com.cornmuffin.products.data.products.Product
import com.cornmuffin.products.util.roundToNearestHalf
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicatorDefaults
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.launch

@Composable
fun ProductsLayout() {
    val viewModel = hiltViewModel<ProductsViewModel>()
    val productsViewModelState by viewModel.stateFlow.collectAsState()
    val settings by viewModel.settingsRepository.settings.collectAsState()

    CompositionLocalProvider(LocalSettings provides settings) {
        when (productsViewModelState.state) {
            ProductsViewModelState.State.ERROR -> Error(productsViewModelState.errorMessage)
            ProductsViewModelState.State.REFRESHING -> Refreshing(products = productsViewModelState.products)
            ProductsViewModelState.State.LOADING -> Loading()
            ProductsViewModelState.State.SUCCESSFUL -> Products(products = productsViewModelState.products)
            ProductsViewModelState.State.UNINITIALIZED -> Loading()
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
fun Error(message: String = stringResource(R.string.no_details)) {
    val viewModel = hiltViewModel<ProductsViewModel>()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.error, message),
            )

            Button(onClick = { viewModel.enqueue(ProductsViewModel.Event.RetryProducts) }) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.retry)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
fun Loading(text: String = stringResource(R.string.loading)) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = text,
            )

            CircularProgressIndicator(
                modifier = Modifier.width(32.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Products(
    products: List<Product>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val coroutineScope = rememberCoroutineScope()
        val viewModel = hiltViewModel<ProductsViewModel>()
        var isRefreshing by remember { mutableStateOf(false) }

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                coroutineScope.launch {
                    viewModel.enqueue(ProductsViewModel.Event.RefreshProducts)
                }
            }
        )

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            colors = PullRefreshIndicatorDefaults.colors(Color.Blue, Color.Gray)
        )

        LazyColumn(modifier = Modifier.pullRefresh(pullRefreshState)) {
            items(products) {
                Product(it)
                HorizontalDivider()
            }
        }

        Button(
            onClick = {
                viewModel.enqueue(ProductsViewModel.Event.NavigateTo(Navigator.NavTarget.Settings))
            }
        ) {
            Text("Settings...")
        }
    }
}

@Composable
fun Refreshing(
    products: List<Product>,
    modifier: Modifier = Modifier,
) {
    Products(
        products,
        modifier
            .fillMaxSize()
            .background(color = Color.Green.copy(alpha = 0.1f))
    )
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
fun ProductsPreview() {
    Products(
        listOf(
            Product(
                title = "MegaFlame Blow Torch",
                description = "Once you've used this on them, they won't have much more to say to you or anyone else.",
                rating = 4.5,
                count = 123,
            ),
            Product(
                title = "Ronco Pocket Harpoon",
                description = "Gets the job done as nothing else can.",
                rating = 2.3,
                count = 3,
            ),
        ),
    )
}

@Composable
private fun Product(product: Product) {
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        product.image?.also {
            SubcomposeAsyncImage(
                model = it,
                contentDescription = null,
                loading = {
                    CircularProgressIndicator()
                },
            )
        }
        Text(
            fontWeight = FontWeight.Bold,
            text = product.title,
            color = if (LocalSettings.current.enableDebugging) Color.Blue else Color.Red
        )
        Text(product.description)
        Text(
            fontWeight = FontWeight.Bold,
            text = stringResource(
                id = R.string.rating,
                product.rating.roundToNearestHalf(),
                product.count
            ),
        )
    }
}
