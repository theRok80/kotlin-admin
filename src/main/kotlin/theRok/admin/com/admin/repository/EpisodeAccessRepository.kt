package theRok.admin.com.admin.repository

import org.springframework.data.jpa.repository.JpaRepository
import theRok.admin.com.admin.entity.EpisodeAccess
import theRok.admin.com.admin.entity.UserId
import theRok.admin.com.admin.entity.EpisodeId
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page

interface EpisodeAccessRepository : JpaRepository<EpisodeAccess, UserId> {
    fun findByUserIdAndEpisodeId(userId: UserId, episodeId: EpisodeId): EpisodeAccess?
    fun findByUserId(userId: UserId, pageable: Pageable): Page<EpisodeAccess>
}
