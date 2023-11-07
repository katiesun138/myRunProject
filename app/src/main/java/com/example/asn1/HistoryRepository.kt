package com.example.asn1

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HistoryRepository(val dao: HistoryDataDao) {
    private val historyDataDao: HistoryDataDao = dao

    val allHistory : Flow<List<HistoryData>> = historyDataDao.getAllHistoryDisplay()

    fun insert(historyData: HistoryData){
        CoroutineScope(IO).launch {
            historyDataDao.insertActivity(historyData)
        }
    }

//    val singleEntry: HistoryData = historyDataDao.getEntry(id)
//    fun getEntry(id:Long): HistoryData{
//        val singleEntry: HistoryData = historyDataDao.getEntry(id)
//    }

    fun delete(id:Long){
        CoroutineScope(IO).launch{
            historyDataDao.deleteEntry(id);
        }
    }


    fun deleteAll(){
        CoroutineScope(IO).launch{
            historyDataDao.deleteAll();
        }
    }
}