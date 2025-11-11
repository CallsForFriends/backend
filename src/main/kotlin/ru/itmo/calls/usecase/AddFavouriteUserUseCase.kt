package ru.itmo.calls.usecase

import org.springframework.stereotype.Service
import ru.itmo.calls.domain.user.FavouriteUser
import ru.itmo.calls.port.AuthProvider
import ru.itmo.calls.port.FavouriteUsersProvider
import ru.itmo.calls.port.TransactionProvider
import ru.itmo.calls.usecase.model.AddFavouriteUserCommand

@Service
class AddFavouriteUserUseCase(
    private val authProvider: AuthProvider,
    private val favouriteUsersProvider: FavouriteUsersProvider,
    private val transactionProvider: TransactionProvider,
) {
    fun add(command: AddFavouriteUserCommand) {
        val userId = authProvider.getCurrentUserId()
        val favouriteUser = FavouriteUser(
            userId = userId,
            favouriteUserId = command.favouriteUserId
        )

        transactionProvider.executeInTransaction {
            addFavouriteUserInternal(favouriteUser)
        }
    }

    private fun addFavouriteUserInternal(favouriteUser: FavouriteUser) {
        val connectionExists = favouriteUsersProvider.exists(favouriteUser)
        if (connectionExists) {
            // TODO: log
            return
        }

        favouriteUsersProvider.addFavouriteUser(favouriteUser)
    }
}