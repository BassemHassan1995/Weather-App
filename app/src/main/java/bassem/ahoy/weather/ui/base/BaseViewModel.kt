package bassem.ahoy.weather.ui.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiEvent : Event> : ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal fun sendEvent(event: UiEvent) = launchCoroutine { eventChannel.send(event) }

    fun launchCoroutine(eventBlock: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = eventBlock)
    }

    protected fun startLoading() = launchCoroutine {
        _isLoading.emit(true)
    }

    protected fun endLoading() = launchCoroutine {
        _isLoading.emit(false)
    }

}