package me.aksenov.reminderbotmeetup.service

import me.aksenov.reminderbotmeetup.model.Reminder
import me.aksenov.reminderbotmeetup.storage.ReminderRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ReminderService(
    private val reminderRepository: ReminderRepository
) {

    fun saveReminder(reminder: Reminder): Reminder {
        reminder.timeToReminder = getTimeToReminder(reminder.minutes, reminder.hours)
        return reminderRepository.save(reminder)
    }

    private fun getTimeToReminder(minutes: Long, hours: Long): Instant =
        if (minutes <= 0L && hours <= 0L) {
            throw IllegalArgumentException()
        } else {
            Instant.now()
                .plus(minutes, ChronoUnit.MINUTES)
                .plus(hours, ChronoUnit.HOURS)
        }

    fun getRemindersForChat(chatId: Long): List<Reminder> = reminderRepository.findByChatIdAndProcessedFalse(chatId)

    fun deleteReminder(id: ObjectId): Boolean {
        return if (reminderRepository.findById(id) == null) {
            false
        } else {
            reminderRepository.deleteById(id)
            true
        }
    }

    fun deleteReminder(reminder: Reminder): Boolean {
        val reminder = reminderRepository
            .findFirstByChatIdAndDescriptionAndMinutesAndHours(
                reminder.chatId,
                reminder.description,
                reminder.minutes,
                reminder.hours
            )
        if (reminder == null) {
            return false
        } else {
            reminderRepository.deleteById(reminder.id)
            return true
        }
    }
}
