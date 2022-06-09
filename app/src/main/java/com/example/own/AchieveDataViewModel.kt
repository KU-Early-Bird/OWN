package com.example.own

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AchieveDataViewModel: ViewModel() {
    var achieveData = MutableLiveData<AchieveTableData>()

    fun update(data:AchieveTableData){
        achieveData.value = data
    }
}