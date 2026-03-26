package theRok.admin.com.admin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import org.hibernate.annotations.CreationTimestamp
import theRok.admin.com.admin.entity.UserId
import theRok.admin.com.admin.entity.EpisodeId
import theRok.admin.com.admin.entity.PurchaseId

@Entity
@Table(name = "episode_accesses")
class EpisodeAccess(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
        @Column(name = "user_id", nullable = false) var userId: UserId,
        @Column(name = "episode_id", nullable = false) var episodeId: EpisodeId,
        @Column(name = "purchase_id", nullable = false)
        var purchaseId: PurchaseId, // AccessLog 의 근거
        @Column(name = "client_ip", nullable = false, length = 45) var clientIp: String,
        @CreationTimestamp
        @Column(name = "created_at", nullable = false)
        var createdAt: Instant? = null
) {
    protected constructor() : this(null, 0, 0, 0L, "", null)
}
