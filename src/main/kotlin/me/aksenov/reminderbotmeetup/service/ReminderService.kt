package me.aksenov.reminderbotmeetup.service

import me.aksenov.reminderbotmeetup.model.CreateReminderRequest
import me.aksenov.reminderbotmeetup.model.Reminder
import me.aksenov.reminderbotmeetup.model.ReminderResponse
import me.aksenov.reminderbotmeetup.storage.ReminderRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ReminderService(private val reminderRepository: ReminderRepository) {

    fun saveReminder(reminder: CreateReminderRequest): ReminderResponse {
        val document = reminder.toReminderDocument(getTimeToReminder(reminder.minutes, reminder.hours))
        return reminderRepository.save(document).toResponse()
    }

    private fun getTimeToReminder(minutes: Long, hours: Long): Instant =
        if (minutes <= 0L && hours <= 0L) {
            throw IllegalArgumentException()
        } else {
            Instant.now()
                .plus(minutes, ChronoUnit.MINUTES)
                .plus(hours, ChronoUnit.HOURS)
        }

    fun getRemindersForChat(chatId: Long): List<ReminderResponse> =
        reminderRepository.findByChatIdAndProcessedFalse(chatId)
            .map(Reminder::toResponse)

    fun deleteReminder(id: ObjectId) {
        if (reminderRepository.existsById(id)) {
            reminderRepository.deleteById(id)
        } else {
            throw IllegalStateException()
        }
    }

    fun deleteReminder(chatId: Long, description: String, minutes: Long, hours: Long) {
        val reminderDocument = reminderRepository
            .findFirstByChatIdAndDescriptionAndMinutesAndHours(chatId, description, minutes, hours)
        reminderDocument?.run { reminderRepository.deleteById(id) } ?: throw IllegalStateException()
    }
}
