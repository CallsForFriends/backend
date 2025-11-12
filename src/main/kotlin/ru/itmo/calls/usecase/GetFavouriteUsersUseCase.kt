package ru.itmo.calls.usecase

import org.springframework.stereotype.Service
import ru.itmo.calls.port.AuthDataProvider
import ru.itmo.calls.port.FavouriteUsersProvider
import ru.itmo.calls.port.UserInfoProvider
import ru.itmo.calls.usecase.model.GetFavouriteUsersResult

@Service
class GetFavouriteUsersUseCase(
    private val authDataProvider: AuthDataProvider,
    private val favouriteUsersProvider: FavouriteUsersProvider,
    private val userInfoProvider: UserInfoProvider,
) {
    fun get(): GetFavouriteUsersResult {
        val userId = authDataProvider.getCurrentUserId()
        val favouriteUserIds = favouriteUsersProvider.getFavouriteUsersByUserId(userId).map { it.favouriteUserId }
        val userInfos = userInfoProvider.findUserInfoByIds(favouriteUserIds)

        return GetFavouriteUsersResult(userInfos)
    }
}
