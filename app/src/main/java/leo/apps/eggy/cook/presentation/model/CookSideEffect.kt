package leo.apps.eggy.cook.presentation.model

sealed class CookSideEffect {
    object Finish : CookSideEffect()
    object Cancel : CookSideEffect()
}