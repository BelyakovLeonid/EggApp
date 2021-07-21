package leo.apps.eggy.cook.presentation.model

import androidx.annotation.StringRes
import leo.apps.eggy.R
import leo.apps.eggy.base.data.model.SetupType

data class CookUiState(
    val progress: Float,
    val calculatedTime: Int,
    val selectedType: SetupType?,
    val timerText: String,
    val boiledTimeText: String,
    @StringRes val buttonTextId: Int,
    @StringRes val titleTextId: Int
) {
    companion object {
        val DEFAULT = CookUiState(
            progress = 0f,
            calculatedTime = 0,
            timerText = "",
            boiledTimeText = "",
            selectedType = null,
            buttonTextId = R.string.cook_start,
            titleTextId = R.string.cook_eggs_medium
        )
    }
}
