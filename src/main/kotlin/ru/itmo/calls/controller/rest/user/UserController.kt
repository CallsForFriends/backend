package ru.itmo.calls.controller.rest.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.calls.adapter.api.MyItmoAdapter

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val itmoAdapter: MyItmoAdapter
) {
    @GetMapping("/{id}")
    fun find(@PathVariable id: Int): String {
        return itmoAdapter.findUserById(id)
    }
}