package me.aksenov.reminderbotmeetup.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Logger {

    val log: Logger
        get() = LoggerFactory.getLogger(this::class.java)
}
