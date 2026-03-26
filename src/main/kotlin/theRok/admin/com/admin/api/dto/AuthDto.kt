package theRok.admin.com.admin.api.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import theRok.admin.com.admin.entity.ConsoleUserId
import theRok.admin.com.admin.entity.UserName
import theRok.admin.com.admin.entity.UserEmail

data class AuthSignupRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 20)
    val consoleUserId: ConsoleUserId,
    @field:NotBlank
    @field:Size(min = 5, max = 26)
    val password: String,
    @field:NotBlank
    @field:Size(min = 3, max = 20)
    val name: UserName,
    @field:NotBlank
    @field:Size(min = 5, max = 26)
    val email: UserEmail,
)

data class AuthSignupResponse(
    val consoleUserId: ConsoleUserId,
    val name: UserName,
    val email: UserEmail,
)

data class AuthSignInRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 20)
    val consoleUserId: ConsoleUserId,
    @field:NotBlank
    @field:Size(min = 5, max = 26)
    val password: String,
)

data class AuthSignInResponse(
    val token: String,
)
