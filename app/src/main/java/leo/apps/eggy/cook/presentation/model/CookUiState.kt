package leo.apps.eggy.cook.presentation.model

import androidx.annotation.StringRes
import leo.apps.eggy.R
import leo.apps.eggy.base.data.model.SetupType

data class CookUiState(
    val calculatedTime: Int,
    val selectedType: SetupType?,
    val timerText: String,
    @StringRes val titleTextId: Int
) {
    companion object {
        val DEFAULT = CookUiState(
            calculatedTime = 0,
            timerText = "",
            selectedType = null,
            titleTextId = R.string.cook_eggs_medium
        )
    }
}
