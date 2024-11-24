package com.cornmuffin.products.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornmuffin.products.Navigator
import com.cornmuffin.products.data.products.Product
import com.cornmuffin.products.data.products.ProductsRepository
import com.cornmuffin.products.data.products.ProductsResponse
import com.cornmuffin.products.data.settings.SettingsRepository
import com.cornmuffin.products.ui.common.CannotGoBack
import com.cornmuffin.products.util.eventprocessor.EventQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository,
    val settingsRepository: SettingsRepository,
    private val navigator: Navigator,
) : CannotGoBack, ViewModel() {
    private val eventQueue = EventQueue<Event>()
    private val _stateFlow = MutableStateFlow(ProductsViewModelState())
    internal val stateFlow: StateFlow<ProductsViewModelState> = _stateFlow.asStateFlow()

    interface HasProductResponse {
        val productsResponse: ProductsResponse
    }

    sealed interface Event : EventQueue.Item {
        data class NavigateTo(val navTarget: Navigator.NavTarget) : Event
        data object ProductsUninitialized : Event
        data class LoadFinished(override val productsResponse: ProductsResponse) : Event, HasProductResponse
        data class RefreshFinished(override val productsResponse: ProductsResponse) : Event, HasProductResponse
        data object RefreshProducts : Event
        data object RetryProducts : Event

        data object SettingsUninitialized : Event {
            override fun isTopPriority(): Boolean = true
        }
    }

    init {
        viewModelScope.launch {
            eventQueue.nextEvent.collect { control(it) }
        }

        viewModelScope.launch(Dispatchers.Default) {
            eventQueue.add(Event.SettingsUninitialized)
            yield()
            eventQueue.add(Event.ProductsUninitialized)
        }
    }

    /**
     * Public interface so that Layout can prod our state with an event.
     */
    fun enqueue(event: Event) {
        viewModelScope.launch(Dispatchers.Default) {
            eventQueue.add(event)
        }
    }

    private val control: (Event) -> Unit = { event: Event ->
        when (event) {
            is Event.SettingsUninitialized -> viewModelScope.launch { getSettings() }
            is Event.ProductsUninitialized, is Event.RetryProducts -> retryProducts()
            is Event.LoadFinished -> reduceBasedOnResponse(event)
            is Event.RefreshProducts -> refreshProducts()
            is Event.NavigateTo -> navigator.navigateTo(event.navTarget)
            is Event.RefreshFinished -> reduceBasedOnResponse(event)
        }
    }

    private suspend fun getProducts() = try {
        withContext(Dispatchers.IO) { repository.getProducts() }
    } catch (e: Exception) {
        ProductsResponse.Error(e)
    }

    private suspend fun getSettings() {
        withContext(Dispatchers.IO) { settingsRepository.initialize() }
    }

    private fun reduceBasedOnResponse(event: HasProductResponse) {
        when (val response = event.productsResponse) {
            is ProductsResponse.Error -> reduceToError(response.exception.message ?: "Bummer")
            is ProductsResponse.Success -> reduceToSuccess(response.data)
        }
    }
    private fun refreshProducts() {
        reduceToRefreshing()
        viewModelScope.launch { refresh() }
    }

    private fun reduceToError(message: String) {
        _stateFlow.value = _stateFlow.value.asError(message)
    }

    private fun reduceToLoading() {
        _stateFlow.value = _stateFlow.value.asLoading()
    }

    private fun reduceToRefreshing() {
        _stateFlow.value = _stateFlow.value.asRefreshing()
    }

    private fun reduceToSuccess(products: ImmutableList<Product>) {
        _stateFlow.value = _stateFlow.value.asSuccess(products)
    }

    private suspend fun refresh() {
        withContext(Dispatchers.IO) {
            repository.flushCache()
            eventQueue.add(Event.RefreshFinished(getProducts()))
        }
    }

    private fun retryProducts() {
        reduceToLoading()
        viewModelScope.launch { enqueue(Event.LoadFinished(getProducts())) }
    }
}
