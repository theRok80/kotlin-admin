package theRok.admin.com.admin.provider

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import theRok.admin.com.admin.config.JwtProperties

class JwtTokenProviderPermissionTest {
    private val provider =
        JwtTokenProvider(
            JwtProperties(
                secret = "01234567890123456789012345678901",
                accessTokenExpirationMinutes = 15,
                refreshTokenExpirationDays = 7,
            ),
        )

    @Test
    fun `access token roundtrips permission list`() {
        val token =
            provider.createAccessToken(
                "user-1",
                listOf("hello:read", "hello:write"),
            )
        val parsed = provider.parseAccessToken(token)
        assertEquals("user-1", parsed.subject)
        assertEquals(listOf("hello:read", "hello:write"), parsed.permissions)
    }

    @Test
    fun `reader style permissions`() {
        val token = provider.createAccessToken("reader", listOf("hello:read"))
        val parsed = provider.parseAccessToken(token)
        assertEquals(listOf("hello:read"), parsed.permissions)
    }

    @Test
    fun `empty permission list when claim absent uses empty authorities path`() {
        val token = provider.createAccessToken("x", emptyList())
        val parsed = provider.parseAccessToken(token)
        assertEquals(emptyList<String>(), parsed.permissions)
    }
}
