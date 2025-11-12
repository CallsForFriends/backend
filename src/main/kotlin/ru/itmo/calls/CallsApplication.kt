package ru.itmo.calls

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableRetry
@EnableTransactionManagement
class CallsApplication {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<CallsApplication>(*args)
		}
	}
}
