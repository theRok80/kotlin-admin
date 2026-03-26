package theRok.admin.com.admin.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Column
import jakarta.persistence.UniqueConstraint
import jakarta.persistence.Index
import java.time.Instant
import org.hibernate.annotations.CreationTimestamp
import theRok.admin.com.admin.entity.UserId
import theRok.admin.com.admin.entity.EpisodeId
import theRok.admin.com.admin.entity.Price

/** 유니크 인덱스 명을 서비스에서도 에러 구분을 위해 상수로 정의 */
const val UNIQ_PURCHASE_USER_EPISODE = "uniq_purchase_user_episode"

typealias PurchaseId = Long

@Entity
@Table(
        name = "purchases",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = UNIQ_PURCHASE_USER_EPISODE,
                                columnNames = ["user_id", "episode_id"]
                        )],
        indexes =
                [
                        Index(name = "idx_user_id", columnList = "user_id"),
                        Index(name = "idx_episode_id", columnList = "episode_id")]
)
class Purchase(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: PurchaseId? = null,
        @Column(name = "user_id", nullable = false) var userId: UserId,
        @Column(name = "episode_id", nullable = false) var episodeId: EpisodeId,
        @Column(name = "price", nullable = false) var price: Price,
        @Column(name = "client_ip", nullable = false, length = 45) var clientIp: String,
        @CreationTimestamp
        @Column(
                name = "purchased_at",
                nullable = false,
                updatable = false,
                columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP"
        )
        var purchasedAt: Instant? = null
) {
    protected constructor() : this(null, 0, 0, 0, "", null)
}
