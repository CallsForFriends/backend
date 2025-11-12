package ru.itmo.calls.usecase

import org.springframework.stereotype.Service
import ru.itmo.calls.domain.user.FavouriteUser
import ru.itmo.calls.port.AuthDataProvider
import ru.itmo.calls.port.FavouriteUsersProvider
import ru.itmo.calls.port.TransactionProvider
import ru.itmo.calls.usecase.model.RemoveFavouriteUserCommand

@Service
class RemoveFavouriteUserUseCase(
    private val authDataProvider: AuthDataProvider,
    private val favouriteUsersProvider: FavouriteUsersProvider,
    private val transactionProvider: TransactionProvider,
) {
    fun remove(command: RemoveFavouriteUserCommand) {
        val userId = authDataProvider.getCurrentUserId()
        val favouriteUser = FavouriteUser(
            userId = userId,
            favouriteUserId = command.favouriteUserId
        )

        transactionProvider.executeInTransaction {
            removeFavouriteUserInternal(favouriteUser)
        }
    }

    private fun removeFavouriteUserInternal(user: FavouriteUser) {
        favouriteUsersProvider.removeFavouriteUser(user)
    }
}
