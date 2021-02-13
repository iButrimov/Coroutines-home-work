package com.school.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.Response

class ActivityViewModel : ViewModel() {
    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State>
        get() = _state

    init {
        refreshData()
    }

    fun refreshData() {

        val exHandler = CoroutineExceptionHandler { _, ex ->
            ex.printStackTrace()
            //_state.value = State.Loaded(emptyList())
        }

        viewModelScope.launch (exHandler + Dispatchers.Main.immediate) {
            try {
                val response: Response<List<MainActivity.Adapter.Item>> = Repository.getPosts()
                if(response.isSuccessful) {
                    response.body()?.let { _state.value = State.Loaded(it) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
        }



        }
    }
}
