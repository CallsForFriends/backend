package ru.itmo.calls.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.Clock

@Configuration
@Profile("!test")
class ClockConfiguration {
    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }
}
