package leo.apps.eggy.base.presentation.vibrator

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibratorManager(
    context: Context?
) {

    private val vibratorService = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

    fun makeDefaultVibration() {
        if (vibratorService?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect = VibrationEffect.createWaveform(
                    DEFAULT_VIBRATION_TIMINGS,
                    DEFAULT_VIBRATION_AMPLITUDES,
                    -1
                )
                vibratorService.vibrate(vibrationEffect)
            } else {
                vibratorService.vibrate(DEFAULT_VIBRATION_MS)
            }
        }
    }

    private companion object {
        const val DEFAULT_VIBRATION_MS = 600L
        val DEFAULT_VIBRATION_TIMINGS = longArrayOf(50, 50, 50, 50, 200, 100, 200, 100, 200)
        val DEFAULT_VIBRATION_AMPLITUDES = intArrayOf(100, 0, 100, 0, 220, 0, 220, 0, 220)
    }
}