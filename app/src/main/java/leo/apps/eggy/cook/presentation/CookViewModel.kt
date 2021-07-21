package leo.apps.eggy.cook.presentation

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.plus
import leo.apps.eggy.R
import leo.apps.eggy.base.data.SetupEggRepository
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.utils.toTimerString
import leo.apps.eggy.cook.presentation.model.CookSideEffect
import leo.apps.eggy.cook.presentation.model.CookUiState
import leo.apps.eggy.timer.TimerService
import javax.inject.Inject

class CookViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {

    private val mutableState = MutableStateFlow(CookUiState.DEFAULT)
    val state = mutableState.asStateFlow()

    private val mutableSideEffects = Channel<CookSideEffect>(Channel.BUFFERED)
    val sideEffects = mutableSideEffects.receiveAsFlow()

    val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            onServiceDisconnected()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            onServiceConnected(service as? TimerService.TimerBinder)
        }
    }

    private var binder: TimerService.TimerBinder? = null
    private var binderObserveScope: CoroutineScope? = null

    init {
        observeCalculatedTime()
        observeSelectedType()
    }

    fun onControlClick() {
        when (binder?.isRunning) {
            true -> binder?.stopTimer()
            else -> binder?.startTimer()
        }
    }

    fun onExitConfirm() {
        binder?.stopTimer()
    }

    fun onUnbindService(){
        onServiceDisconnected()
    }

    private fun onServiceDisconnected() {
        binder = null
        Log.d("MyTag", "onServiceDisconnected")
        binderObserveScope?.cancel()
        binderObserveScope = null
    }

    private fun onServiceConnected(binder: TimerService.TimerBinder?) {
        this.binder = binder
        binder?.setTime(setupRepository.getCalculatedTime().toLong())
        binder?.setType(setupRepository.getSelectedType())
        observeBinder()
    }

    private fun observeBinder() {
        Log.d("MyTag", "observeBinder")
        binderObserveScope?.cancel()
        binderObserveScope = viewModelScope + Job()
        binderObserveScope?.observeBinderState()
        binderObserveScope?.observeBinderCancel()
        binderObserveScope?.observeBinderFinish()
    }

    private fun CoroutineScope.observeBinderState(){
        binder?.state?.onEach { timerState ->
            mutableState.value = mutableState.value.copy(
                progress = timerState.progress,
                timerText = timerState.timerText,
                buttonTextId = if (timerState.isRunning) R.string.cook_cancel else R.string.cook_start
            )
        }?.launchIn(this)
    }

    private fun CoroutineScope.observeBinderCancel(){
        binder?.cancel?.onEach {
            mutableSideEffects.send(CookSideEffect.Cancel)
        }?.launchIn(this)
    }

    private fun CoroutineScope.observeBinderFinish(){
        binder?.finish?.onEach {
            Log.d("MyTag", "observeBinderFinish")
            mutableSideEffects.send(CookSideEffect.Finish)
        }?.launchIn(this)
    }

    private fun observeCalculatedTime() {
        setupRepository.calculatedTimeFlow
            .onEach { time ->
                mutableState.value = state.value.copy(
                    calculatedTime = time,
                    boiledTimeText = time.toTimerString()
                )
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun observeSelectedType() {
        setupRepository.selectedTypeFlow
            .onEach { type ->
                mutableState.value = state.value.copy(
                    selectedType = type,
                    titleTextId = getTitleByType(type)
                )
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    @StringRes
    private fun getTitleByType(type: SetupType?): Int = when (type) {
        SetupType.SOFT -> R.string.cook_eggs_soft
        SetupType.MEDIUM -> R.string.cook_eggs_medium
        else -> R.string.cook_eggs_hard
    }
}