package me.aksenov.reminderbotmeetup.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document
data class Reminder(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    val id: ObjectId = ObjectId.get(),
    @Field
    var processed: Boolean = false,
    @Field
    val timeToReminder: Instant,
    @Field
    val chatId: Long,
    @Field
    val description: String,
    @Field
    val minutes: Long,
    @Field
    val hours: Long
) {

    override fun toString(): String {
        return description
    }

    fun toResponse() = ReminderResponse(
        id = id,
        processed = processed,
        timeToReminder = timeToReminder,
        chatId = chatId,
        description = description,
        minutes = minutes,
        hours = hours
    )
}
