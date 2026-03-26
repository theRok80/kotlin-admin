package theRok.admin.com.admin.api.service

import org.springframework.stereotype.Service
import theRok.admin.com.admin.api.dto.AuthSignupRequest
import theRok.admin.com.admin.api.dto.AuthSignupResponse
import theRok.admin.com.admin.repository.ConsoleUserRepository
import theRok.admin.com.admin.exception.UserAlreadyExistsException
import org.springframework.context.MessageSource
import java.util.Locale
import org.springframework.transaction.annotation.Transactional
import theRok.admin.com.admin.entity.ConsoleUserEntity
import org.springframework.security.crypto.password.PasswordEncoder
import theRok.admin.com.admin.api.dto.AuthSignInRequest
import theRok.admin.com.admin.api.dto.AuthSignInResponse
import theRok.admin.com.admin.exception.UserNotFoundException
import theRok.admin.com.admin.exception.InvalidPasswordException
import theRok.admin.com.admin.security.UserPermissionProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID
import org.springframework.context.ApplicationEventPublisher
import theRok.admin.com.admin.common.event.ConsoleSignInEvent

private val log = KotlinLogging.logger {}

@Service
class AuthService(
    private val consoleUserRepository: ConsoleUserRepository,
    private val messageSource: MessageSource,
    private val passwordEncoder: PasswordEncoder,
    private val userPermissionProvider: UserPermissionProvider,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun signup(request: AuthSignupRequest): AuthSignupResponse {
        if (consoleUserRepository.existsByConsoleUserId(request.consoleUserId)) {
            throw UserAlreadyExistsException(messageSource.getMessage("error.userAlreadyExists", null, Locale.getDefault()) ?: "User already exists")
        }

        val password = passwordEncoder.encode(request.password)

        consoleUserRepository.save(ConsoleUserEntity(consoleUserId = request.consoleUserId, password = password, name = request.name, email = request.email))

        return AuthSignupResponse(request.consoleUserId, request.name, request.email)
    }

    @Transactional(readOnly = true)
    fun signIn(request: AuthSignInRequest): AuthSignInResponse {
        val user = consoleUserRepository.findByConsoleUserId(request.consoleUserId)
        if (user == null) {
            throw UserNotFoundException(messageSource.getMessage("error.userNotFound", null, Locale.getDefault()) ?: "User not found")
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw InvalidPasswordException(messageSource.getMessage("error.invalidPassword", null, Locale.getDefault()) ?: "Invalid password")
        }

        val permissions = userPermissionProvider.permissionsFor(user.consoleUserId)

        val token = UUID.randomUUID().toString()

        eventPublisher.publishEvent(ConsoleSignInEvent(user.consoleUserId, token, permissions))

        return AuthSignInResponse(token)
    }
}
