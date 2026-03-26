package theRok.admin.com.admin.service

import java.time.Duration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService(
        private val redisTemplate: RedisTemplate<String, String>,
) {
    fun setValue(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    fun setValueWithTtl(key: String, value: String, ttl: Duration) {
        redisTemplate.opsForValue().set(key, value, ttl)
    }

    fun getValue(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    fun deleteValue(key: String): Boolean {
        return redisTemplate.delete(key)
    }

    fun incrementValue(key: String): Long {
        return redisTemplate.opsForValue().increment(key) ?: 0
    }

    fun setExpire(key: String, ttl: Duration) {
        redisTemplate.expire(key, ttl)
    }

    fun getValueByPrefix(prefix: String): List<String> {
        return redisTemplate.keys("${prefix}:*").toList()
    }

    fun deleteValueByPrefix(prefix: String): Long {
        return redisTemplate.delete(redisTemplate.keys("${prefix}:*"))
    }

    fun getSignInToken(token: String): String? {
        val pattern = "signIn:*:$token"
        val keys = redisTemplate.keys(pattern)
        if (keys.isEmpty()) return null
        val key = keys.first()
        return redisTemplate.opsForValue().get(key)
    }

    fun getConsoleSignInToken(token: String): String? {
        val pattern = "console:signIn:*:$token"
        val keys = redisTemplate.keys(pattern)
        if (keys.isEmpty()) return null
        val key = keys.first()
        return redisTemplate.opsForValue().get(key)
    }

}
