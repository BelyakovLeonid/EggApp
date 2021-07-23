package leo.apps.eggy.cook.presentation

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import leo.apps.eggy.R
import leo.apps.eggy.base.data.SetupEggRepository
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.utils.toTimerString
import leo.apps.eggy.cook.presentation.model.CookNavigationCommand
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

    private val mutableNavigationCommands = Channel<CookNavigationCommand>(Channel.BUFFERED)
    val navigationCommands = mutableNavigationCommands.receiveAsFlow()

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

    private val updateStateMutex = Mutex()

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
        mutableNavigationCommands.trySend(CookNavigationCommand.PopUp)
    }

    fun onUnbindService(){
        onServiceDisconnected()
    }

    fun onBackPressed() {
        if(binder?.isRunning == true){
            mutableNavigationCommands.trySend(CookNavigationCommand.ShowExitDialog)
        }else{
            mutableNavigationCommands.trySend(CookNavigationCommand.PopUp)
        }
    }

    private fun onServiceDisconnected() {
        binder = null
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
        binderObserveScope?.cancel()
        binderObserveScope = viewModelScope + Job()
        binderObserveScope?.observeBinderState()
        binderObserveScope?.observeBinderCancel()
        binderObserveScope?.observeBinderFinish()
    }

    private fun CoroutineScope.observeBinderState(){
        binder?.state?.onEach { timerState ->
            updateStateMutex.withLock {
                mutableState.value = mutableState.value.copy(
                    progress = timerState.progress,
                    timerText = timerState.timerText,
                    buttonTextId = if (timerState.isRunning) R.string.cook_cancel else R.string.cook_start
                )
            }
        }?.launchIn(this)
    }

    private fun CoroutineScope.observeBinderCancel(){
        binder?.cancel?.onEach {
            mutableSideEffects.send(CookSideEffect.Cancel)
        }?.launchIn(this)
    }

    private fun CoroutineScope.observeBinderFinish(){
        binder?.finish?.onEach {
            mutableSideEffects.send(CookSideEffect.Finish)
        }?.launchIn(this)
    }

    private fun observeCalculatedTime() {
        setupRepository.calculatedTimeFlow
            .onEach { time ->
                updateStateMutex.withLock {
                    mutableState.value = state.value.copy(
                        calculatedTime = time,
                        boiledTimeText = time.toTimerString()
                    )
                }
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun observeSelectedType() {
        setupRepository.selectedTypeFlow
            .onEach { type ->
                updateStateMutex.withLock {
                    mutableState.value = state.value.copy(
                        selectedType = type,
                        titleTextId = getTitleByType(type)
                    )
                }
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    @StringRes
    private fun getTitleByType(type: SetupType?): Int = when (type) {
        SetupType.SOFT -> R.string.cook_eggs_soft
        SetupType.MEDIUM -> R.string.cook_eggs_medium
        else -> R.string.cook_eggs_hard
    }
}