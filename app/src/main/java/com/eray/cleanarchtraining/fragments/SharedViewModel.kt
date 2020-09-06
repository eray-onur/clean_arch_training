package com.eray.cleanarchtraining.fragments

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.eray.cleanarchtraining.R
import com.eray.cleanarchtraining.data.models.Priority
import com.eray.cleanarchtraining.data.models.TodoData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    val noDataAvailable: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfNoData(todoData: List<TodoData>) {
        noDataAvailable.value = todoData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object:
    AdapterView.OnItemSelectedListener{
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            when(position) {
                0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red)) }
                1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow)) }
                2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green)) }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

    }

    fun validateData(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }

    fun parsePriority(priority: String): Priority {
        return when(priority) {
            "High Priority" -> {
                Priority.HIGH}
            "Medium Priority" -> {
                Priority.MEDIUM}
            "Low Priority" -> {
                Priority.LOW}
            else -> Priority.LOW
        }
    }


}