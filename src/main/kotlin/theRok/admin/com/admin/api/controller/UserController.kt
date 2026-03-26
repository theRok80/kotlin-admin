package theRok.admin.com.admin.api.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.security.access.prepost.PreAuthorize
import theRok.admin.com.admin.entity.User
import theRok.admin.com.admin.repository.UserRepository
import theRok.admin.com.admin.api.service.UserService
import theRok.admin.com.admin.api.dto.UserListRequest
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.RequestBody
import jakarta.validation.constraints.Min
import org.springframework.web.bind.annotation.PathVariable
import theRok.admin.com.admin.api.dto.GetUserResponse
import theRok.admin.com.admin.api.dto.GetUserComicsRequest
import theRok.admin.com.admin.entity.EpisodeAccess

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping
    fun getUsers(@RequestBody @Valid requestBody: UserListRequest): Page<User> {
        return userService.getUsers(requestBody)
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable @Valid @Min(1) userId: Long): GetUserResponse {
        return userService.getUser(userId)
    }

    @GetMapping("/comics")
    fun getAcessComicList(@RequestBody @Valid requestBody: GetUserComicsRequest): Page<EpisodeAccess> {
        return userService.getAcessComicList(requestBody)
    }
}
