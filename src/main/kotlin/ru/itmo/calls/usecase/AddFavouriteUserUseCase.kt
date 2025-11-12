package ru.itmo.calls.usecase

import mu.KotlinLogging
import org.springframework.dao.DuplicateKeyException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
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
    private val log = KotlinLogging.logger { }

    @Retryable(
        maxAttempts = 3,
        include = [
            DuplicateKeyException::class
        ],
        backoff = Backoff(random = true, delay = 100, maxDelay = 300)
    )
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
            log.info { "[FAVOURITES][UserId:${favouriteUser.userId}][Favourite:${favouriteUser.favouriteUserId}] Connection already exists, skipping" }
            return
        }

        favouriteUsersProvider.addFavouriteUser(favouriteUser)
    }
}