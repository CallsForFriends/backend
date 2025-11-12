package ru.itmo.calls.usecase

import org.springframework.stereotype.Service
import ru.itmo.calls.port.AuthProvider
import ru.itmo.calls.port.FavouriteUsersProvider
import ru.itmo.calls.port.UserInfoProvider
import ru.itmo.calls.usecase.model.GetFavouriteUsersResult

@Service
class GetFavouriteUsersUseCase(
    private val authProvider: AuthProvider,
    private val favouriteUsersProvider: FavouriteUsersProvider,
    private val userInfoProvider: UserInfoProvider,
) {
    fun get(): GetFavouriteUsersResult {
        val userId = authProvider.getCurrentUserId()
        val favouriteUserIds = favouriteUsersProvider.getFavouriteUsersByUserId(userId).map { it.favouriteUserId }
        val userInfos = userInfoProvider.findUserInfoByIds(favouriteUserIds)

        return GetFavouriteUsersResult(userInfos)
    }
}