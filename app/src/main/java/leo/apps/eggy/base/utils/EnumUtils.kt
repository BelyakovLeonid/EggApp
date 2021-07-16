package leo.apps.eggy.base.utils

inline fun <reified T> getByIndex(index: Int): T? where T: Enum<T> {
    return enumValues<T>().getOrNull(index)
}

inline fun <reified T> getIndexOf(value: T?): Int where T: Enum<T> {
    return enumValues<T>().indexOf(value)
}