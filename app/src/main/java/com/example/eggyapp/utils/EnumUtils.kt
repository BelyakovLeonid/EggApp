package com.example.eggyapp.utils

import com.example.eggyapp.ui.setup.Identifiable

inline fun <reified T> findById(id: Int?): T? where T : Enum<T>, T : Identifiable {
    return enumValues<T>().find { it.id == id }
}