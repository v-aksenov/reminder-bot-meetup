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
            if (it == "all") {
                getAll(chatId)
            } else if (update.message.isReply && it == "delete") {
                deleteReminder(chatId, update.message.replyToMessage.text)
            } else {
                saveReminder(chatId, it)
            }
        }
    }

    private fun deleteReminder(chatId: Long, message: String) {
        try {
            parseMessageToReminder(chatId, message).run {
                reminderService.deleteReminder(chatId, description, minutes, hours)
            }
            sendMessage("Reminder deleted", chatId)
        } catch (e: IllegalStateException) {
            sendMessage("reminder not found", chatId)
        } catch (e: NumberFormatException) {
            sendMessage("cannot parse reminder from message", chatId)
        }
    }

    private fun getAll(chatId: Long) {
        val messageText = reminderService.getRemindersForChat(chatId)
            .joinToString(prefix = "Your reminders\n", separator = "\n") { it.toString() }
        sendMessage(messageText, chatId)
    }

    private fun saveReminder(chatId: Long, message: String) {
        try {
            val reminder = parseMessageToReminder(chatId, message)
            reminderService.saveReminder(reminder)
            sendMessage("Scheduled ${reminder.description}", chatId)
            log.info("saved reminder $reminder")
        } catch (e: NumberFormatException) {
            sendMessage("cannot parse reminder from message", chatId)
        }

    }

    fun sendMessage(message: String, chatId: Long) {
        log.info("sent message [$message] to user $chatId")
        execute(SendMessage(chatId.toString(), message))
    }

    @PostConstruct
    fun start() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        log.info("bot started")
    }
}