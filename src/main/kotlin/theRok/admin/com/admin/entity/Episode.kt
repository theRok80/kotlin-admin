package theRok.admin.com.admin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

typealias EpisodeId = Long

typealias Price = Int

@Entity
@Table(name = "episodes")
class Episode(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: EpisodeId? = null,
        @Column(nullable = false, length = 150) var title: String,
        @Column(nullable = true) var description: String,
        @Column(nullable = false) var price: Price,
        @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
        var createdAt: Instant? = null,
        @Column(
                nullable = false,
                columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
        )
        var updatedAt: Instant? = null
) {
    protected constructor() : this(null, "", "", 0, null, null)
}
