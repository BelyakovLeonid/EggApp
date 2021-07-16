package leo.apps.eggy.setup.presentation.model

data class SetupUiState(
    val selectedTemperatureIndex: Int,
    val selectedSizeIndex: Int,
    val selectedTypeIndex: Int,
    val calculatedTime: Int,
    val isButtonNextEnable: Boolean
) {
    companion object {
        private const val NO_INDEX = -1

        val DEFAULT = SetupUiState(
            selectedTemperatureIndex = NO_INDEX,
            selectedSizeIndex = NO_INDEX,
            selectedTypeIndex = NO_INDEX,
            calculatedTime = 0,
            isButtonNextEnable = false
        )
    }
}