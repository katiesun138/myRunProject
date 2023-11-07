package com.example.asn1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class HistoryViewModel(private val repository: HistoryRepository):ViewModel() {
    val allHistoryLiveData: LiveData<List<HistoryData>> = repository.allHistory.asLiveData()

    fun insert(historyData: HistoryData){
        repository.insert(historyData);
    }

//    fun getEntry(id:Long):HistoryData?{
//        return repository.getEntry(id);
//    }

    fun deleteFirst(){
        val historyList = allHistoryLiveData.value
        if (historyList != null && historyList.size > 0){
            val id = historyList.get(0).id
            repository.delete(id);
        }
    }

    fun deleteById(id: Long) {
        repository.delete(id)
    }

    fun deleteAll() {
        val historyList = allHistoryLiveData.value
        if (historyList != null && historyList.size > 0) {
            repository.deleteAll();
        }
        repository.deleteAll();
    }
}

class HistoryViewModelFactory(private val repository: HistoryRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class <T>):T{
        if(modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}