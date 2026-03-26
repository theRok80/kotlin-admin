package theRok.admin.com.admin.exception

class MaintenanceException(message: String) : RuntimeException(message)

class TooManyRequestsException(message: String) : RuntimeException(message)

class UserAlreadyExistsException(message: String) : RuntimeException(message)

class UserNotFoundException(message: String) : RuntimeException(message)

class InvalidPasswordException(message: String) : RuntimeException(message)

class InvalidAuthorizationTokenException(message: String) : RuntimeException(message)

class AuthenticationException(message: String) : RuntimeException(message)
