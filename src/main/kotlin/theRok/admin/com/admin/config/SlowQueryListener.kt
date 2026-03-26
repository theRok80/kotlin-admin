package theRok.admin.com.admin.config

import io.github.oshai.kotlinlogging.KotlinLogging
import net.ttddyy.dsproxy.ExecutionInfo
import net.ttddyy.dsproxy.QueryInfo
import net.ttddyy.dsproxy.listener.QueryExecutionListener
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

private val log = KotlinLogging.logger {}

@Component
class SlowQueryListener(
    private val props: SlowQueryProperties,
) : QueryExecutionListener {
    private val blockComment = Pattern.compile("/\\*[\\s\\S]*?\\*/")

    override fun beforeQuery(execInfo: ExecutionInfo?, queryInfoList: MutableList<QueryInfo>?) {}

    override fun afterQuery(execInfo: ExecutionInfo?, queryInfoList: MutableList<QueryInfo>?) {
        if (!props.enabled || execInfo == null || queryInfoList.isNullOrEmpty()) {
            return
        }
        val elapsedMs = TimeUnit.NANOSECONDS.toMillis(execInfo.elapsedTime)
        // forceExplain이면 임계값과 관계없이(조회 쿼리만) EXPLAIN 로그. 아니면 느린 경우만.
        if (!props.forceExplain && elapsedMs < props.thresholdMs) {
            return
        }
        for (qi in queryInfoList) {
            val sql = qi.query ?: continue
            if (!isLikelyReadQuery(sql)) {
                continue
            }
            log.warn {
                "Slow read query ${elapsedMs}ms (threshold ${props.thresholdMs}ms). Run in DB for analysis: ${toExplainSql(sql)}"
            }
        }
    }

    private fun isLikelyReadQuery(sql: String): Boolean {
        val stripped = blockComment.matcher(sql).replaceAll(" ").trim()
        val upper = stripped.uppercase()
        return upper.startsWith("SELECT") || upper.startsWith("WITH") || upper.startsWith("(SELECT")
    }

    private fun toExplainSql(sql: String): String {
        val trimmed = sql.trimEnd().removeSuffix(";").trimEnd()
        return "EXPLAIN $trimmed"
    }
}
