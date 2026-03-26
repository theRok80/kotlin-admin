package theRok.admin.com.admin.repository

import org.springframework.data.jpa.repository.JpaRepository
import theRok.admin.com.admin.entity.ConsoleUserEntity
import theRok.admin.com.admin.entity.ConsoleUserIdx
import theRok.admin.com.admin.entity.ConsoleUserId
import theRok.admin.com.admin.entity.UserEmail

interface ConsoleUserRepository : JpaRepository<ConsoleUserEntity, ConsoleUserIdx> {
    fun existsByConsoleUserId(consoleUserId: ConsoleUserId): Boolean
    fun findByConsoleUserId(consoleUserId: ConsoleUserId): ConsoleUserEntity?
}
