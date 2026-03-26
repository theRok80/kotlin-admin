package theRok.admin.com.admin.exception

import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.Locale
import theRok.admin.com.admin.common.constants.LogConstants
import org.slf4j.MDC
import theRok.admin.com.admin.dto.ErrorResponseDto
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource,
) {
    private fun currentTraceId(): String = MDC.get(LogConstants.TRACE_ID) ?: ""

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValid(e: MethodArgumentNotValidException): ErrorResponseDto {
        return ErrorResponseDto(
            traceId = currentTraceId(),
            message = e.message ?: messageSource.getMessage("error.validationError", null, Locale.getDefault()) ?: "Validation error",
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadable(e: HttpMessageNotReadableException): ErrorResponseDto {
        return ErrorResponseDto(
            traceId = currentTraceId(),
            message = e.message ?: messageSource.getMessage("error.validationError", null, Locale.getDefault()) ?: "Validation error",
        )
    }

    @ExceptionHandler(MaintenanceException::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun handleMaintenanceException(exception: MaintenanceException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE.value())
            .body(
                ErrorResponseDto(
                    currentTraceId(),
                    exception.message ?: messageSource.getMessage("error.serverMaintenance", null, Locale.getDefault()) ?: "Service is under maintenance",
                ),
            )

    @ExceptionHandler(TooManyRequestsException::class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    fun handleTooManyRequestsException(exception: TooManyRequestsException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS.value())
            .body(
                ErrorResponseDto(
                    currentTraceId(),
                    exception.message ?: messageSource.getMessage("error.tooManyRequests", null, Locale.getDefault()) ?: "Too many requests",
                ),
            )

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFoundException(exception: UserNotFoundException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND.value())
            .body(ErrorResponseDto(currentTraceId(), exception.message ?: messageSource.getMessage("error.userNotFound", null, Locale.getDefault()) ?: "User not found"))

    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUserAlreadyExistsException(exception: UserAlreadyExistsException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST.value())
            .body(ErrorResponseDto(currentTraceId(), exception.message ?: messageSource.getMessage("error.userAlreadyExists", null, Locale.getDefault()) ?: "User already exists"))

    @ExceptionHandler(InvalidPasswordException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidPasswordException(exception: InvalidPasswordException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST.value())
            .body(ErrorResponseDto(currentTraceId(), exception.message ?: messageSource.getMessage("error.invalidPassword", null, Locale.getDefault()) ?: "Invalid password"))

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAccessDenied(exception: AccessDeniedException): ErrorResponseDto =
        ErrorResponseDto(
            currentTraceId(),
            exception.message
                ?: messageSource.getMessage("error.forbidden", null, Locale.getDefault()) ?: "Forbidden",
        )

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ErrorResponseDto> =
        ResponseEntity
            .internalServerError()
            .body(
                ErrorResponseDto(
                    currentTraceId(),
                    message = exception.message ?: messageSource.getMessage("error.internalServerError", null, Locale.getDefault()) ?: "Internal server error",
                ),
            )
}
