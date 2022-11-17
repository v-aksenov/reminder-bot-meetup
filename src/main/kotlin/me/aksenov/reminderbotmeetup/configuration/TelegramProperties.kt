package me.aksenov.reminderbotmeetup.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("bot")
data class TelegramProperties(
    val token: String,
    val username: String
)
