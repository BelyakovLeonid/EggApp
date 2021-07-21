package leo.apps.eggy.cook.presentation.model

sealed class CookNavigationCommand {
    object PopUp : CookNavigationCommand()
}