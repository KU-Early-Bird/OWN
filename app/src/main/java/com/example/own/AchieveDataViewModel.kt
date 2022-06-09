package com.example.own

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.own.Home.AchieveTableData

class AchieveDataViewModel: ViewModel() {
    var achieveData = MutableLiveData<AchieveTableData>()

    fun update(data: AchieveTableData){
        achieveData.value = data
    }
}