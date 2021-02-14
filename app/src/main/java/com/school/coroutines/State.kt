package com.school.coroutines

sealed class State {
    object Loading : State()
    data class Loaded(val content: MainActivity.Adapter.Item) : State()
}
