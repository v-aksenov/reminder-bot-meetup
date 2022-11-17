package me.aksenov.reminderbotmeetup.storage

import me.aksenov.reminderbotmeetup.model.Reminder
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface ReminderRepository: MongoRepository<Reminder, ObjectId> {

    fun findByTimeToReminderBeforeAndProcessedIsFalse(timeToReminder: Instant): List<Reminder>
}
