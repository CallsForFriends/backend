package ru.itmo.calls.controller.rest.user

import jakarta.validation.constraints.PositiveOrZero
import org.hibernate.validator.constraints.Range
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.itmo.calls.controller.rest.user.mapping.toResponse
import ru.itmo.calls.controller.rest.user.models.FindCurrentUserResponse
import ru.itmo.calls.controller.rest.user.models.FindUserByIdResponse
import ru.itmo.calls.controller.rest.user.models.FindUsersByFilterResponse
import ru.itmo.calls.usecase.FindCurrentUserUseCase
import ru.itmo.calls.usecase.FindUserByIdUseCase
import ru.itmo.calls.usecase.FindUsersByFilterUseCase
import ru.itmo.calls.usecase.model.FindUserByIdCommand
import ru.itmo.calls.usecase.model.FindUsersByFilterCommand

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val findUserByIdUseCase: FindUserByIdUseCase,
    private val findUsersByFilterUseCase: FindUsersByFilterUseCase,
    private val findCurrentUserUseCase: FindCurrentUserUseCase
) {
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): FindUserByIdResponse {
        val command = FindUserByIdCommand(id)
        return findUserByIdUseCase.find(command).toResponse()
    }

    @GetMapping("/me")
    fun findCurrentUser(): FindCurrentUserResponse {
        return findCurrentUserUseCase.find().toResponse()
    }


    @GetMapping
    fun findByFilter(
        @RequestParam(defaultValue = "0") @Range(min = 0, max = 10000)
        limit: Int,
        @RequestParam(defaultValue = "0") @PositiveOrZero
        offset: Int,
        @RequestParam
        filter: String,
    ): FindUsersByFilterResponse {
        val command = FindUsersByFilterCommand(
            limit = limit,
            offset = offset,
            filter = filter
        )

        return findUsersByFilterUseCase.find(command).toResponse(limit, offset)
    }
}