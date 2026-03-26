package theRok.admin.com.admin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import theRok.admin.com.admin.security.JwtAuthenticationFilter
import theRok.admin.com.admin.common.constants.PermissionConstants
import theRok.admin.com.admin.service.RedisService
import com.fasterxml.jackson.databind.ObjectMapper

@Configuration
@EnableWebSecurity
class SecurityConfig {
    /**
     * 권한 계층 정의.
     * 구문: "상위권한 > 하위권한" — 상위 권한을 가진 유저는 하위 권한도 갖는다.
     * 예) all:write 를 가진 유저는 모든 리소스의 read 도 자동으로 갖는다.
     */
    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val hierarchy = "all:write > ${PermissionConstants.entries.joinToString("\n") { "${it.name.lowercase()}:write > ${it.name.lowercase()}:read" }}"
        return RoleHierarchyImpl.fromHierarchy(hierarchy)
    }

    @Bean
    fun jwtAuthenticationFilter(
        redisService: RedisService,
        json: ObjectMapper,
    ): JwtAuthenticationFilter = JwtAuthenticationFilter(redisService, json)

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/api/auth/**",
                        "/health",
                        "/health/**",
                        "/error",
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
