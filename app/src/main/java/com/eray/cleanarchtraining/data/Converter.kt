package com.eray.cleanarchtraining.data

import androidx.room.TypeConverter
import com.eray.cleanarchtraining.data.models.Priority

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority) : String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priorityStr: String): Priority {
        return Priority.valueOf(priorityStr)
    }

}