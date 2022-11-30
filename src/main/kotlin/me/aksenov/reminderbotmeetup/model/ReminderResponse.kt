package me.aksenov.reminderbotmeetup.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import java.time.Instant

data class ReminderResponse(
    @JsonSerialize(using = ToStringSerializer::class)
    val id: ObjectId = ObjectId.get(),
    val processed: Boolean,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    val timeToReminder: Instant,
    val chatId: Long,
    val description: String,
    val minutes: Long,
    val hours: Long
)
