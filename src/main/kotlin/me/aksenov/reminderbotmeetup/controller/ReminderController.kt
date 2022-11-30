package me.aksenov.reminderbotmeetup.controller

import me.aksenov.reminderbotmeetup.model.CreateReminderRequest
import me.aksenov.reminderbotmeetup.model.ReminderResponse
import me.aksenov.reminderbotmeetup.service.ReminderService
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reminder")
class ReminderController(private val reminderService: ReminderService) {

    @PostMapping
    fun createReminder(@RequestBody createReminderRequest: CreateReminderRequest): ReminderResponse =
        reminderService.saveReminder(createReminderRequest)

    @GetMapping
    fun getReminders(@RequestParam chatId: Long): List<ReminderResponse> =
        reminderService.getRemindersForChat(chatId)

    @DeleteMapping
    fun deleteReminder(@RequestParam id: ObjectId) {
        reminderService.deleteReminder(id)
    }

    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleException() {
    }
}
