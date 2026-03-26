package theRok.admin.com.admin.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Column
import java.time.LocalDateTime
import jakarta.persistence.UniqueConstraint

typealias ConsoleUserIdx = Long
typealias ConsoleUserId = String
typealias UserName = String
typealias UserEmail = String

@Entity
@Table(name = "console_user", uniqueConstraints = [UniqueConstraint(columnNames = ["userId"])])
data class ConsoleUserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val consoleUserIdx: ConsoleUserIdx = 0L,
    @Column(nullable = false, unique = true, length = 25)
    val consoleUserId: ConsoleUserId = "",
    @Column(nullable = false, length = 255)
    val password: String = "",
    @Column(nullable = false, length = 25)
    val name: UserName = "",
    @Column(nullable = false, length = 255)
    val email: UserEmail = "",
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    protected constructor() : this(0L, "", "", "", "", LocalDateTime.now(), LocalDateTime.now())
}
