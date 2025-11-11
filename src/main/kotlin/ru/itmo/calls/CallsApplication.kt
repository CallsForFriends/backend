package ru.itmo.calls

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class CallsApplication {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<CallsApplication>(*args)
		}
	}
}
