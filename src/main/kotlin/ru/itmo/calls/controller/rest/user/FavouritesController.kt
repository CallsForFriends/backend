package ru.itmo.calls.controller.rest.user

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.calls.controller.rest.user.mapping.toCommand
import ru.itmo.calls.controller.rest.user.mapping.toResponse
import ru.itmo.calls.controller.rest.user.models.AddFavouriteUserRequest
import ru.itmo.calls.controller.rest.user.models.GetFavouriteUsersResponse
import ru.itmo.calls.controller.rest.user.models.RemoveFavouriteUserRequest
import ru.itmo.calls.usecase.AddFavouriteUserUseCase
import ru.itmo.calls.usecase.GetFavouriteUsersUseCase
import ru.itmo.calls.usecase.RemoveFavouriteUserUseCase

@RestController
@RequestMapping("/api/v1/favourites")
class FavouritesController(
    private val addFavouriteUserUseCase: AddFavouriteUserUseCase,
    private val removeFavouriteUserUseCase: RemoveFavouriteUserUseCase,
    private val getFavouriteUsersUseCase: GetFavouriteUsersUseCase
) {
    @PutMapping
    fun addFavourite(
        @RequestBody @Valid
        request: AddFavouriteUserRequest
    ) {
        addFavouriteUserUseCase.add(request.toCommand())
    }

    @DeleteMapping
    fun removeFavourite(
        @RequestBody @Valid
        request: RemoveFavouriteUserRequest
    ) {
        removeFavouriteUserUseCase.remove(request.toCommand())
    }

    @GetMapping
    fun getFavourites(): GetFavouriteUsersResponse {
        return getFavouriteUsersUseCase.get().toResponse()
    }
}
