package theRok.admin.com.admin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@Configuration
@EnableMethodSecurity
class MethodSecurityConfig {
    /**
     * SecurityConfig에 정의한 RoleHierarchy를 @PreAuthorize 표현식에도 적용.
     * 이 빈이 없으면 메서드 보안은 계층을 무시하고 정확한 권한만 비교한다.
     */
    @Bean
    fun methodSecurityExpressionHandler(roleHierarchy: RoleHierarchy): MethodSecurityExpressionHandler =
        DefaultMethodSecurityExpressionHandler().apply {
            setRoleHierarchy(roleHierarchy)
        }
}
