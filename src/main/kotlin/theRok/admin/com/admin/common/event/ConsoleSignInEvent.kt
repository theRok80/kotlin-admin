package theRok.admin.com.admin.common.event

import theRok.admin.com.admin.entity.ConsoleUserId

data class ConsoleSignInEvent(
    val consoleUserId: ConsoleUserId,
    val token: String,
    val permissions: List<String>,
)
