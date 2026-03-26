package theRok.admin.com.admin.api.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Max
import theRok.admin.com.admin.common.constants.SortDirection
import theRok.admin.com.admin.entity.UserId
import java.time.LocalDateTime

enum class UserSort {
    createdAt, userId, userName, udpatedAt
}

enum class UserComicSort {
    createdAt, comicId
}

data class UserListRequest (
    @field:Min(1)
    val page: Int = 1,
    @field:Min(1)
    @field:Max(100)
    val limit: Int = 10,
    val sort: UserSort = UserSort.createdAt,
    val order: SortDirection = SortDirection.DESC
)

// data class GerUserRequest(
//     @field:Min(1)
//     val userId: UserId,
// )

data class GetUserResponse(
    val userId: UserId,
    val email: String,
    val name: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class GetUserComicsRequest(
    @field:Min(1)
    val userId: UserId,
    @field:Min(1)
    val page: Int = 1,
    @field:Min(1)
    @field:Max(100)
    val limit: Int = 10,
    val sort: UserComicSort = UserComicSort.createdAt,
    val order: SortDirection = SortDirection.DESC
)
