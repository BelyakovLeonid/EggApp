package leo.apps.eggy.base.analytics

object Analytics {

    object Constants {
        const val PARAM_ACTION = "EventAction"
    }

    object Main {
        const val EVENT_NAME = "main"
        const val BACK_ACTION = "back_click"
        const val BACK_CONFIRM_ACTION = "back_confirm_click"
    }

    object Welcome {
        const val EVENT_NAME = "welcome"
        const val SCREEN_NAME = "welcome_screen"
        const val COOK_ACTION = "cook_click"
    }

    object Setup {
        const val EVENT_NAME = "setup"
        const val SCREEN_NAME = "setup_screen"
        const val TEMPERATURE_SELECT_ACTION = "temperature_select_"
        const val SIZE_SELECT_ACTION = "size_select_"
        const val TYPE_SELECT_ACTION = "type_select_"
        const val NEXT_ACTION = "next_click"
    }

    object Cook {
        const val EVENT_NAME = "cook"
        const val SCREEN_NAME = "cook_screen"
        const val START_ACTION = "start_click"
        const val CANCEL_ACTION = "cancel_click"
        const val BACK_ACTION = "back_click"
        const val BACK_CONFIRM_ACTION = "back_confirm_click"
        const val BACK_CANCEL_ACTION = "back_cancel_click"

    }
}