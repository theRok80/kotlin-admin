package theRok.admin.com.admin.repository

import org.springframework.data.jpa.repository.JpaRepository
import theRok.admin.com.admin.entity.User
import theRok.admin.com.admin.entity.UserId
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page

interface UserRepository : JpaRepository<User, UserId> {
    fun findByEmail(email: String): User?
    override fun findAll(pageable: Pageable): Page<User>
}
