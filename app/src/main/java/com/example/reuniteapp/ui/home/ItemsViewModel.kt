package com.example.reuniteapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.models.Items
import kotlinx.coroutines.launch

class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val itemsDao = AppDatabase.getDatabase(application).itemsDao()
    private val _items = MutableLiveData<List<Items>>()
    val items: LiveData<List<Items>> get() = _items

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _items.postValue(itemsDao.getItemsByReunitedStatus(false))
        }
    }

    fun addItem(item: Items) {
        viewModelScope.launch {
            itemsDao.insert(item)
            loadItems()
        }
    }

    fun getItemsByTitle(itemTitle: String): LiveData<List<Items>> {
        viewModelScope.launch {
            _items.postValue(itemsDao.getItemByTitle(itemTitle))
        }
        return _items
    }
}
