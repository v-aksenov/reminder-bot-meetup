package me.aksenov.reminderbotmeetup.scheduler

import me.aksenov.reminderbotmeetup.bot.ReminderBotService
import me.aksenov.reminderbotmeetup.logger.Logger
import me.aksenov.reminderbotmeetup.storage.ReminderRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ReminderScheduler(
    private val repository: ReminderRepository,
    private val botService: ReminderBotService
) : Logger {

    @Scheduled(cron = "0 * * * * *")
    fun checkReminders() {
        val reminders =
            repository.findByTimeToReminderBeforeAndProcessedIsFalse(Instant.now())
        reminders
            .also { log.info("remind for ${it.size} tasks") }
            .forEach {
                log.info("send reminder ${it.description} to ${it.chatId}")
                botService.sendMessage(it.description, it.chatId)
                it.processed = true
                repository.save(it)
            }
    }
}
