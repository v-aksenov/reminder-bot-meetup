package me.aksenov.reminderbotmeetup.utils

import me.aksenov.reminderbotmeetup.model.Reminder

// 1h иди погулять -> hours = 1, description = иди погулять

//Wh test

fun parseMessageToReminder(chatId: Long, message: String): Reminder {
    val minutes = message.getAmount(minuteRegex)
    val hours = message.getAmount(hourRegex)
    val description = message
        .replace(minuteRegex, "")
        .replace(hourRegex, "")
        .trim()
    return Reminder(
        minutes = minutes,
        hours = hours,
        chatId = chatId,
        description = description
    )
}

private fun String.getAmount(regex: Regex): Long =
    regex.find(this)
        ?.groupValues
        ?.firstOrNull()
        ?.replace(regex.pattern.last().toString(), "")
        ?.toLong()
        ?: 0

private val hourRegex: Regex = Regex("\\d+h")
private val minuteRegex: Regex = Regex("\\d+m")
