package me.aksenov.reminderbotmeetup.bot

import me.aksenov.reminderbotmeetup.configuration.TelegramProperties
import me.aksenov.reminderbotmeetup.logger.Logger
import me.aksenov.reminderbotmeetup.service.ReminderService
import me.aksenov.reminderbotmeetup.utils.parseMessageToReminder
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import javax.annotation.PostConstruct

@Service
class ReminderBotService(
    private val telegramProperties: TelegramProperties,
    private val reminderService: ReminderService
) : TelegramLongPollingBot(), Logger {

    override fun getBotToken(): String = telegramProperties.token

    override fun getBotUsername(): String = telegramProperties.username

    override fun onUpdateReceived(update: Update?) {
        update?.message?.text?.let {
            val chatId = update.message.chat.id
            val reminder = parseMessageToReminder(chatId, it)
            reminderService.saveReminder(reminder)
            sendMessage("Scheduled ${reminder.description}", chatId)
            log.info("saved reminder $reminder")
        }
    }

    fun sendMessage(message: String, chatId: Long) {
        execute(SendMessage(chatId.toString(), message))
    }

    @PostConstruct
    fun start() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        log.info("bot started")
    }
}