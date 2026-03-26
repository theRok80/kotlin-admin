package theRok.admin.com.admin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

typealias UserId = Long

@Entity
@Table(name = "user", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "userId")
        val id: UserId = 0L,
        @Column(name = "email", nullable = false, unique = true, length = 45) val email: String,
        @Column(name = "password", nullable = false, length = 150) val password: String,
        @Column(name = "name", length = 45) val name: String? = null,
        @Column(name = "clientIp", nullable = false, length = 15) val clientIp: String,
        @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        val createdAt: LocalDateTime = LocalDateTime.now(),
        @Column(
                nullable = false,
                columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
        )
        val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
        protected constructor() :
                this(0, "", "", null, "", LocalDateTime.now(), LocalDateTime.now())
}
