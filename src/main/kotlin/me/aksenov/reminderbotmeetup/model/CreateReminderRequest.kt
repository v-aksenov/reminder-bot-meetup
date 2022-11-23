package me.aksenov.reminderbotmeetup.model

data class CreateReminderRequest(
    val chatId: Long,
    val description: String,
    val minutes: Long = 0,
    val hours: Long = 0
) {

    fun toReminder(): Reminder = Reminder(
        chatId = chatId,
        description = description,
        minutes = minutes,
        hours = hours
    )
}
