package theRok.admin.com.admin.api.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody
import theRok.admin.com.admin.api.dto.AuthSignupRequest
import theRok.admin.com.admin.api.service.AuthService
import theRok.admin.com.admin.api.dto.AuthSignupResponse
import theRok.admin.com.admin.api.dto.AuthSignInRequest
import theRok.admin.com.admin.api.dto.AuthSignInResponse

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/signUp")
    fun signup(
        @RequestBody @Valid request: AuthSignupRequest,
    ): AuthSignupResponse {
        return authService.signup(request)
    }

    @PostMapping("/signIn")
    fun signIn(
        @RequestBody @Valid request: AuthSignInRequest,
    ): AuthSignInResponse {
        return authService.signIn(request)
    }
}
