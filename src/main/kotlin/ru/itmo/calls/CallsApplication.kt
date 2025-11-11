package ru.itmo.calls

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CallsApplication {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<CallsApplication>(*args)
		}
	}
}
