package me.aksenov.reminderbotmeetup.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.aksenov.reminderbotmeetup.bot.ReminderBotService
import me.aksenov.reminderbotmeetup.model.CreateReminderRequest
import me.aksenov.reminderbotmeetup.model.ReminderResponse
import me.aksenov.reminderbotmeetup.storage.ReminderRepository
import org.bson.types.ObjectId
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


@SpringBootTest
@AutoConfigureMockMvc
class ReminderControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val reminderRepository: ReminderRepository
): StringSpec() {

    @MockkBean(relaxed = true)
    private lateinit var reminderBotService: ReminderBotService

    override fun beforeTest(f: suspend (TestCase) -> Unit) {
        reminderRepository.deleteAll()
    }

    init {
        "create reminder" {
            val reminder = createReminder(reminderRequest)

            reminder.chatId shouldBe reminderRequest.chatId
            reminder.description shouldBe reminderRequest.description
            reminder.minutes shouldBe reminderRequest.minutes
            reminder.id shouldNotBe null
        }
        "get reminder" {
            val reminder = createReminder(reminderRequest)

            getReminder(reminderRequest.chatId) shouldContain reminder
        }
        "delete reminder" {
            val reminder = createReminder(reminderRequest)

            deleteReminder(reminder.id)

            getReminder(reminderRequest.chatId) shouldNotContain reminder
        }
    }

    private fun createReminder(request: CreateReminderRequest): ReminderResponse =
        mockMvc.post("/reminder") {
            content = objectMapper.writeValueAsString(request)
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        }
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andReturn()
            .response
            .contentAsString
            .let { objectMapper.readValue(it) }

    private fun getReminder(chatId: Long): List<ReminderResponse> =
        mockMvc.get("/reminder") {
            param("chatId", chatId.toString())
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        }
            .andDo { print() }
            .andExpect { status { isOk() } }
            .andReturn()
            .response
            .contentAsString
            .let { objectMapper.readValue(it) }

    private fun deleteReminder(reminderId: ObjectId) {
        mockMvc.delete("/reminder") {
            param("id", reminderId.toString())
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        }
            .andDo { print() }
            .andExpect { status { isOk() } }
    }

    private val reminderRequest = CreateReminderRequest(
        chatId = 123L,
        description = "test",
        minutes = 1,
        hours = 0
    )
}
