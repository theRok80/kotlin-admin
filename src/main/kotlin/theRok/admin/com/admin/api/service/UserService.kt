package theRok.admin.com.admin.api.service

import org.springframework.stereotype.Service
import theRok.admin.com.admin.repository.UserRepository
import theRok.admin.com.admin.entity.User
import org.springframework.security.access.prepost.PreAuthorize
import theRok.admin.com.admin.api.dto.UserListRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.data.domain.PageRequest
import theRok.admin.com.admin.exception.UserNotFoundException
import java.util.Locale
import org.springframework.context.MessageSource
import theRok.admin.com.admin.entity.UserId
import theRok.admin.com.admin.api.dto.GetUserResponse
import theRok.admin.com.admin.api.dto.GetUserComicsRequest
import theRok.admin.com.admin.repository.EpisodeAccessRepository
import theRok.admin.com.admin.entity.EpisodeAccess

@Service
@PreAuthorize("hasAuthority('user:read')")
class UserService(
    private val userRepository: UserRepository,
    private val messageSource: MessageSource,
    private val episodeAccessRepository: EpisodeAccessRepository,
) {
    @PreAuthorize("@auth.hasAny(authentication, 'user:read')")
    fun getUsers(request: UserListRequest): Page<User> {
        val pageable = PageRequest.of(request.page - 1, request.limit, Sort.by(Sort.Direction.valueOf(request.order.name), request.sort.name))
        return userRepository.findAll(pageable)
    }

    @PreAuthorize("@auth.hasAny(authentication, 'user:read')")
    fun getUser(userId: UserId): GetUserResponse {
        val user = userRepository.findById(userId).orElseThrow {
            UserNotFoundException(messageSource.getMessage("error.userNotFound", null, Locale.getDefault()) ?: "User not found")
        }
        return GetUserResponse(user.id, user.email, user.name, user.createdAt, user.updatedAt)
    }

    @PreAuthorize("@auth.hasAll(authentication, 'user:read', 'comic:read')")
    fun getAcessComicList(request: GetUserComicsRequest): Page<EpisodeAccess> {
        val pageable = PageRequest.of(request.page - 1, request.limit, Sort.by(Sort.Direction.valueOf(request.order.name), request.sort.name))
        return episodeAccessRepository.findByUserId(request.userId, pageable)
    }

}


