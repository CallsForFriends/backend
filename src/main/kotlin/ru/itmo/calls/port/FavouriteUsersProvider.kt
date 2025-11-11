package ru.itmo.calls.port

import ru.itmo.calls.domain.user.FavouriteUser

interface FavouriteUsersProvider {
    fun exists(favouriteUser: FavouriteUser): Boolean
    fun getFavouriteUsersByUserId(userId: Int): List<FavouriteUser>
    fun addFavouriteUser(favouriteUser: FavouriteUser)
    fun removeFavouriteUser(favouriteUser: FavouriteUser)
}