package ru.itmo.calls.adapter.favourites

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.itmo.calls.domain.user.FavouriteUser
import ru.itmo.calls.port.FavouriteUsersProvider
import java.time.Clock
import java.time.OffsetDateTime

@Repository
class JdbcFavouriteUsersRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val clock: Clock
) : FavouriteUsersProvider {
    companion object {
        private const val SELECT = """
            select f.user_id,
                f.favourite_user_id
            from favourite_users f
        """

        private const val SELECT_BY_USER_ID = """
            $SELECT
            where f.user_id = :userId
        """

        private const val SELECT_EXISTS = """
            select exists (
                select 1
                from favourite_users
                where user_id = :userId
                    and favourite_user_id = :favouriteUserId
            )
        """

        private const val INSERT_FAVOURITE_USER = """
            insert into favourite_users (user_id, favourite_user_id, created_at)
            values (:userId, :favouriteUserId, :createdAt)       
        """

        private const val DELETE_FAVOURITE_USER = """
            delete from favourite_users
            where user_id = :userId
                and favourite_user_id = :favouriteUserId
        """

        private val MAPPER = RowMapper { rs, _ ->
            FavouriteUser(
                userId = rs.getInt("user_id"),
                favouriteUserId = rs.getInt("favourite_user_id")
            )
        }
    }

    override fun exists(favouriteUser: FavouriteUser): Boolean {
        return jdbcTemplate.queryForObject(
            SELECT_EXISTS,
            mapOf(
                "userId" to favouriteUser.userId,
                "favouriteUserId" to favouriteUser.favouriteUserId,
            ),
            Boolean::class.java
        ) ?: false
    }

    override fun getFavouriteUsersByUserId(userId: Int): List<FavouriteUser> {
        return jdbcTemplate.query(
            SELECT_BY_USER_ID,
            mapOf("userId" to userId),
            MAPPER
        )
    }

    override fun addFavouriteUser(favouriteUser: FavouriteUser) {
        jdbcTemplate.update(
            INSERT_FAVOURITE_USER,
            mapOf(
                "userId" to favouriteUser.userId,
                "favouriteUserId" to favouriteUser.favouriteUserId,
                "createdAt" to OffsetDateTime.now(clock)
            )
        )
    }

    override fun removeFavouriteUser(favouriteUser: FavouriteUser) {
        jdbcTemplate.update(
            DELETE_FAVOURITE_USER,
            mapOf(
                "userId" to favouriteUser.userId,
                "favouriteUserId" to favouriteUser.favouriteUserId,
            )
        )
    }
}
