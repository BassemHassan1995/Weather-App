package bassem.ahoy.weather.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiEvent: Event> : ViewModel() {

    private val eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    protected fun sendEvent(event: UiEvent) = launchCoroutine { eventChannel.send(event) }

    fun launchCoroutine(eventBlock: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = eventBlock)
    }

}